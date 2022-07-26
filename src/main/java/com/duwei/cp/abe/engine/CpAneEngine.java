package com.duwei.cp.abe.engine;

import com.duwei.cp.abe.attribute.Attribute;
import com.duwei.cp.abe.parameter.*;
import com.duwei.cp.abe.polynomial.Polynomial;
import com.duwei.cp.abe.structure.*;
import com.duwei.cp.abe.text.CipherText;
import com.duwei.cp.abe.text.PlainText;
import com.duwei.cp.abe.util.ConvertUtils;
import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Field;
import it.unisa.dia.gas.jpbc.Pairing;

import java.util.*;

/**
 * @BelongsProject: JPBC-ABE
 * @BelongsPackage: com.duwei.jpbc.cp.abe
 * @Author: duwei
 * @Date: 2022/7/21 16:30
 * @Description: 算法引擎
 */
public class CpAneEngine {

    public UserPrivateKey keyGen(MasterPrivateKey masterPrivateKey, List<Attribute> attributes) {
        return UserPrivateKey.build(masterPrivateKey, attributes);
    }


    /**
     * 基于公共参数和访问树结构加密消息
     *
     * @param pk
     * @param plainText
     * @param accessTree
     * @return
     */
    public CipherText encrypt(PublicKey pk, PlainText plainText, AccessTree accessTree) {
        AccessTreeNode root = accessTree.getRoot();
        //根节点的秘密数
        Element s = getRandomElementInZr(pk);
        root.setSecretNumber(s);

        CipherText cipherText = new CipherText();
        //1.设置密文第一部分


        Element c_ware = (plainText.getMessageValue().mul(pk.getEgg_a().powZn(s).getImmutable())).getImmutable();
        cipherText.setC_wave(c_ware);

        //2.设置密文第二部分
        Element c = pk.getH().powZn(s).getImmutable();
        cipherText.setC(c);

        //3.递归设置子节点
        compute(root, pk, cipherText);

        //设置访问树
        cipherText.setAccessTree(accessTree);
        return cipherText;
    }

    /**
     * 在Z_r上选取随机元素
     *
     * @param publicKey
     * @return
     */
    private Element getRandomElementInZr(PublicKey publicKey) {
        return publicKey.getPairingParameter().getZr().newRandomElement().getImmutable();
    }

    private void compute(AccessTreeNode node, PublicKey publicKey, CipherText cipherText) {
        Field z_r = publicKey.getPairingParameter().getZr();
        Element secretNumber = node.getSecretNumber();
        int childrenSize = node.getChildrenSize();
        if (node.getAccessTreeNodeType() == AccessTreeNodeType.INNER_NODE) {
            //节点选择的多项式
            Polynomial polynomial = new Polynomial(((InnerAccessTreeNode)node).getThreshold() - 1, secretNumber, z_r);
            for (AccessTreeNode child : node.getChildren()) {
                int index = child.getIndex();
                Element childSecret = polynomial.getValue(z_r.newElement(index).getImmutable());
                child.setParent(node);
                child.setSecretNumber(childSecret);
                //递归去设置子节点
                compute(child, publicKey, cipherText);
            }
        }

        //节点是叶节点
        if (node.getAccessTreeNodeType() == AccessTreeNodeType.LEAF_NODE) {
            LeafAccessTreeNode leafNode = (LeafAccessTreeNode) node;
            //属性
            Attribute attribute = leafNode.getAttribute();
            //属性值
            Element attributeValue = attribute.getAttributeValue();
            Element c_y = (publicKey.getPairingParameter().getGenerator().powZn(leafNode.getSecretNumber())).getImmutable();
            Element c_y_pie = (publicKey.hash(
                    attributeValue.powZn(leafNode.getSecretNumber())
            ).getImmutable());
            cipherText.putCy(attribute, c_y);
            cipherText.putCyPie(attribute, c_y_pie);
        }
    }

    public String decryptToStr(PublicKey publicKey, UserPrivateKey userPrivateKey, CipherText cipherText){
        Element decrypt = decrypt(publicKey, userPrivateKey, cipherText);
        if (decrypt != null){
            return new String(ConvertUtils.byteToStr(decrypt.toBytes()));
        }
        return null;
    }


    public Element decrypt(PublicKey publicKey, UserPrivateKey userPrivateKey, CipherText cipherText) {
        Element decryptNode = decryptNode(publicKey, userPrivateKey, cipherText, cipherText.getAccessTree().getRoot(), userPrivateKey.getUserAttributes());
        if (decryptNode != null) {
            Element D = userPrivateKey.getD();
            Element C = cipherText.getC();
            Element c_wave = cipherText.getC_wave();
            Pairing pairing = publicKey.getPairingParameter().getPairing();
            return c_wave.div(pairing.pairing(C, D).div(decryptNode));
        }
        return null;
    }


    //解密到G1上
    public Element decryptNode(PublicKey publicKey, UserPrivateKey userPrivateKey, CipherText cipherText, AccessTreeNode x, List<Attribute> attributes) {
        //叶子节点
        if (x.getAccessTreeNodeType() == AccessTreeNodeType.LEAF_NODE) {
            LeafAccessTreeNode leafNode = ((LeafAccessTreeNode) x);
            Attribute attribute = leafNode.getAttribute();
            if (attributes.contains(attribute)) {
                Element cy = cipherText.getCy(attribute);
                Element cyPie = cipherText.getCyPie(attribute);
                Element dj = userPrivateKey.getDj(attribute);
                Element djPie = userPrivateKey.getDjPie(attribute);
                Pairing pairing = userPrivateKey.getPairingParameter().getPairing();
                return pairing.pairing(dj, cy).div(pairing.pairing(djPie, cyPie)).getImmutable();
            } else {
                return null;
            }
        } else {
            //内部节点
            InnerAccessTreeNode innerNode = ((InnerAccessTreeNode) x);
            int threshold = innerNode.getThreshold();
            int satisfyCount = 0;

            //重建
            Map<Element, Element> indexFzMap = new HashMap<>();
            for (AccessTreeNode child : innerNode.getChildren()) {
                Element decryptNode = decryptNode(publicKey, userPrivateKey, cipherText, child, attributes);
                if (decryptNode != null) {
                    satisfyCount++;
                    Element index = publicKey.getPairingParameter().getZr().newElement(child.getIndex()).getImmutable();
                    indexFzMap.put(index, decryptNode);
                }
            }
            if (satisfyCount < threshold) {
                return null;
            }

            Element result = publicKey.getPairingParameter().getG1().newOneElement();
            Element zero = publicKey.getPairingParameter().getZr().newZeroElement().getImmutable();
            List<Element> Sx = new ArrayList<>(indexFzMap.keySet());
            for (Map.Entry<Element, Element> entry : indexFzMap.entrySet()) {
                Element curIndex = entry.getKey();
                Element curFz = entry.getValue();
                Element powZn = Polynomial.lagrangeCoefficient(curIndex, Sx, zero, publicKey.getPairingParameter().getZr());
                result.mul((curFz.powZn(powZn)));
            }
            return result.getImmutable();
        }
    }
}
