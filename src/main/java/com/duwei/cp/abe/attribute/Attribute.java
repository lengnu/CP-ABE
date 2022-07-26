package com.duwei.cp.abe.attribute;

import com.duwei.cp.abe.parameter.PublicKey;
import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Field;
import lombok.Data;

import javax.swing.*;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * @BelongsProject: JPBC-ABE
 * @BelongsPackage: com.duwei.jpbc.cp.abe.attribute
 * @Author: duwei
 * @Date: 2022/7/22 9:04
 * @Description: 用户的属性描述
 * 属性空间 G0
 */
@Data
public class Attribute {
    /**
     * 用Element元素来标识用户属性
     */
    private Element attributeValue;
    private String attributeName;

    public Attribute(String attributeName, PublicKey publicKey){
        this(attributeName,publicKey.getPairingParameter().getG0());
    }

    public Attribute(String attributeName, Field G0){
        this.attributeName = attributeName;
        this.attributeValue = G0.newElementFromBytes(attributeName.getBytes(StandardCharsets.UTF_8)).getImmutable();
    }

    @Override
    public String toString(){
        return attributeName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Attribute)) {
            return false;
        }
        Attribute attribute1 = (Attribute) o;
        return Objects.equals(attributeValue, attribute1.attributeValue) && Objects.equals(attributeName, attribute1.attributeName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(attributeValue, attributeName);
    }
}
