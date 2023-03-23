package com.ygkj.lcsyn.utils;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Base64;
import java.util.Map;
import java.util.Set;

public class EncryUtil {

    /**
     * AES(ECB)加密
     *
     * @param plainText 待加密的明文字符串
     * @param secretKey 加密密钥
     * @return java.lang.String AES加密数据
     **/
    public static String aesEncrypt(String plainText, String secretKey) throws Exception {

        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        byte[] raw = secretKey.getBytes();
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec);

        byte[] encrypted = cipher.doFinal(plainText.getBytes("UTF-8"));
        Base64.Encoder encoder = Base64.getEncoder();
        // 此处使用BASE64做转码
        return encoder.encodeToString(encrypted);
    }

    /**
     * @param signMap 签名参数
     * @return
     * @Description 生成数据签名
     **/
    public static String generateSign(Map<String, Object> signMap) throws Exception {
        //对key进行排序
        Set<String> set = signMap.keySet();
        Object[] keyArray = set.toArray();
        Arrays.sort(keyArray);

        StringBuilder builder = new StringBuilder();
        boolean first = true;
        for (Object key : keyArray) {
            // a=aa&bb=bb&c=cc
            if (first) {
                first = false;
            } else {
                builder.append("&");
            }
            builder.append(key.toString().toLowerCase() + "=" + signMap.get(key));
        }

        String context = builder.toString();
        //hash摘要算法
        return sha(context);
    }

    public static String sha(String inputStr) throws Exception {
        MessageDigest sha = null;

        try {
            sha = MessageDigest.getInstance("SHA");
        } catch (Exception var7) {
            System.out.println(var7.toString());
            var7.printStackTrace();
            return "";
        }

        byte[] byteArray = inputStr.getBytes("UTF-8");
        byte[] md5Bytes = sha.digest(byteArray);
        StringBuffer hexValue = new StringBuffer();

        for(int i = 0; i < md5Bytes.length; ++i) {
            int val = md5Bytes[i] & 255;
            if (val < 16) {
                hexValue.append("0");
            }

            hexValue.append(Integer.toHexString(val));
        }

        return hexValue.toString();
    }
}
