package com.duwei.cp.abe.text;

import com.duwei.cp.abe.attribute.Attribute;
import com.duwei.cp.abe.structure.AccessTree;
import it.unisa.dia.gas.jpbc.Element;
import lombok.Data;
import lombok.ToString;

import java.util.HashMap;
import java.util.Map;

/**
 * @BelongsProject: JPBC-ABE
 * @BelongsPackage: com.duwei.jpbc.cp.abe
 * @Author: duwei
 * @Date: 2022/7/21 16:41
 * @Description: 密文
 */
@Data
@ToString
public class CipherText {
    //g1
    private Element c_wave;
    //g0
    private Element c;
    //g0
    private Map<Attribute,Element> c_y_map;
    //g0
    private Map<Attribute,Element> c_y_pie_map;
    //访问树
    private AccessTree accessTree;

    public void putCy(Attribute attribute,Element cy){
        c_y_map.put(attribute,cy);
    }

    public void putCyPie(Attribute attribute,Element cy_pie){
        c_y_pie_map.put(attribute,cy_pie);
    }

    public Element getCy(Attribute attribute){
        return c_y_map.get(attribute);
    }

    public Element getCyPie(Attribute attribute){
        return c_y_pie_map.get(attribute);
    }

    public CipherText() {
        c_y_map = new HashMap<>();
        c_y_pie_map = new HashMap<>();
    }
}
