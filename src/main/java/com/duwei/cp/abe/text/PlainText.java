package com.duwei.cp.abe.text;

import com.duwei.cp.abe.parameter.PublicKey;
import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Field;
import lombok.Data;

import java.nio.charset.StandardCharsets;

/**
 * @BelongsProject: JPBC-ABE
 * @BelongsPackage: com.duwei.jpbc.cp.abe
 * @Author: duwei
 * @Date: 2022/7/21 16:42
 * @Description: 明文
 *  明文空间 - G1
 */
@Data
public class PlainText {
    private Element messageValue;
    private String messageStr;

    public PlainText(String messageStr, PublicKey publicKey) {
        this(messageStr, publicKey.getPairingParameter().getG1());
    }

    private PlainText(String messageStr, Field G1) {
        this.messageStr = messageStr;
        this.messageValue = G1.newElementFromBytes(messageStr.getBytes(StandardCharsets.UTF_8)).getImmutable();
    }

}
