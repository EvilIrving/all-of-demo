package com.ygkj.gragh.utils;

import org.springframework.util.Base64Utils;
import org.springframework.util.DigestUtils;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class AesUtil {

    public final static String key = "11bfe9b7c523c945f911d12f3837ab65";

    private static String Algorithm = "AES";

    private static String AlgorithmProvider = "AES/ECB/PKCS5Padding";

    private final static String encoding = "UTF-8";

    public static String encrypt(String src, String k) throws Exception {
        SecretKey secretKey = new SecretKeySpec(k.getBytes(encoding), Algorithm);
        Cipher cipher = Cipher.getInstance(AlgorithmProvider);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] cipherBytes = cipher.doFinal(src.getBytes(Charset.forName(encoding)));
        return Base64Utils.encodeToString(cipherBytes);
    }

    public static String decrypt(String src) throws Exception {
        SecretKey secretKey = new SecretKeySpec(key.getBytes(encoding), Algorithm);
        Cipher cipher = Cipher.getInstance(AlgorithmProvider);
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] hexBytes = Base64Utils.decodeFromString(src);
        byte[] plainBytes = cipher.doFinal(hexBytes);
        return new String(plainBytes, encoding);
    }


    public static String stringToMD5(String plainText) {
        byte[] secretBytes = null;
        try {
            secretBytes = MessageDigest.getInstance("md5").digest(
                    plainText.getBytes(encoding));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("没有这个md5算法！");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String md5code = new BigInteger(1, secretBytes).toString(16);
        for (int i = 0; i < 32 - md5code.length(); i++) {
            md5code = "0" + md5code;
        }
        return md5code;
    }

    public static long getNum(int G, int P, int B) {
        long result = 1;
        for (int i = 0; i < B; i++) {
            result *= G;
        }
        return result % P;
    }

    public static String getKey(int P, int num, int B) {
        //生成一个随机数
        long result = 1;
        for (int i = 0; i < B; i++) {
            result *= num;
        }
        return stringToMD5(String.valueOf(result % P));
    }
}
