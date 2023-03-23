package com.ygkj.utils;

import org.springframework.util.Base64Utils;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.Charset;

public class EncryptUtil {

    public final static String DEFAULT_KEY = "11bfe9b7c523c945f911d12f3837ab65";

    private static String Algorithm = "AES";

    private static String AlgorithmProvider = "AES/ECB/PKCS5Padding";

    private final static String encoding = "UTF-8";

    public static String encrypt(String src, String key) throws Exception {
        SecretKey secretKey = new SecretKeySpec(key.getBytes(encoding), Algorithm);
        Cipher cipher = Cipher.getInstance(AlgorithmProvider);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] cipherBytes = cipher.doFinal(src.getBytes(Charset.forName(encoding)));
        return Base64Utils.encodeToString(cipherBytes);
    }

    public static String decrypt(String src,String key) throws Exception {
        key = StringUtils.isBlank(key)?DEFAULT_KEY:key;
        SecretKey secretKey = new SecretKeySpec(key.getBytes(encoding), Algorithm);
        Cipher cipher = Cipher.getInstance(AlgorithmProvider);
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] hexBytes = Base64Utils.decodeFromString(src);
        byte[] plainBytes = cipher.doFinal(hexBytes);
        return new String(plainBytes, encoding);
    }

}
