package com.duwei.cp.abe.util;

import java.nio.charset.Charset;

/**
 * @BelongsProject: CP-ABE
 * @BelongsPackage: com.duwei.cp.abe.util
 * @Author: duwei
 * @Date: 2022/7/26 10:54
 * @Description: byte和str转换
 */
public class ConvertUtils {
    /**
     * 比特数组去掉前后的0转字符串
     * @param bytes
     * @return
     */
    public static String byteToStr(byte[] bytes){
        int startIndex = 0;
        int endIndex = bytes.length;
        for (int i = 0; i < bytes.length; i++) {
            if (bytes[i] != 0){
                startIndex = i;
                break;
            }
        }

        for (int i = bytes.length - 1; i >= 0; i--) {
            if (bytes[i] != 0){
                endIndex = i;
                break;
            }
        }
        return new String(bytes,startIndex,endIndex - startIndex + 1, Charset.defaultCharset());
    }
}
