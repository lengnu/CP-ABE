package com.duwei.cp.abe.polynomial;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Field;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;
import lombok.Data;

import java.util.Arrays;
import java.util.List;


/**
 * @BelongsProject: JPBC-ABE
 * @BelongsPackage: com.duwei.jpbc.cp.abe.polynomial
 * @Author: duwei
 * @Date: 2022/7/22 9:43
 * @Description: 多项式表示
 */
@Data
public class Polynomial {
    /**
     * 多项式阶数
     */
    private int degree;
    /**
     * 系数，从低位到高位
     */
    private Element[] coefficients;

    /**
     * 在整数环R上运算
     */
    private Field z_r;

    public Polynomial(int degree,Element s0,Field z_r) {
        this.degree = degree;
        this.z_r = z_r;
        coefficients = new Element[degree + 1];
        coefficients[0] = s0;
        for (int i = 1; i <= degree; i++) {
            coefficients[i] = z_r.newRandomElement().getImmutable();
        }
    }

    public Polynomial(int degree, Element[] coefficients,Field z_r) {
        this.degree = degree;
        this.z_r = z_r;
        this.coefficients = coefficients;
    }

    /**
     * 获取多项式代入x的值
     * @param x
     * @return
     */
    public Element getValue(Element x){
        //初始化为0
        Element result = z_r.newZeroElement();
        Element temp = z_r.newOneElement();
        for (Element coefficient : coefficients){
            result.add(coefficient.mul(temp));
            temp.mul(x);
        }
        return result.getImmutable();
    }


    public static Element lagrangeCoefficient(Element i, List<Element> s,Element x,Field zr){
        Element result = zr.newOneElement();
        for (Element element : s) {
            if (!i.equals(element)){
                result.mul(x.sub(element).div(i.sub(element)));
            }
        }
        return result.getImmutable();
    }

}
