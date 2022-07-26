package com.duwei.cp.abe.structure;

import com.duwei.cp.abe.attribute.Attribute;
import it.unisa.dia.gas.jpbc.Element;
import lombok.Data;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

/**
 * @BelongsProject: JPBC-ABE
 * @BelongsPackage: com.duwei.jpbc.cp.abe.structure
 * @Author: duwei
 * @Date: 2022/7/21 16:58
 * @Description: 访问树的节点
 */
@Data
@ToString
public abstract class AccessTreeNode {
    /**
     * 秘密数
     */
    private Element secretNumber;
    /**
     * 记录其父亲节点
     */
    private AccessTreeNode parent;
    /**
     * 节点的标号
     */
    private int index;
    /**
     * 孩子
     */
    private List<AccessTreeNode> children;
    //父亲节点ID，方便构建树
    private Integer parentId;


    protected AccessTreeNode(){
        children = new ArrayList<>();
    }

    public abstract byte getAccessTreeNodeType();

    public  int getChildrenSize(){
        return this.children.size();
    };





}
