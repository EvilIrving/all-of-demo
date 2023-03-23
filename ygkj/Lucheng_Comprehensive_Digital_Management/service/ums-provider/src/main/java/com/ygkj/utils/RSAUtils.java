package com.ygkj.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import java.security.*;


/**
 * @author wd
 * @修改时间：2018年6月26日 下午3:30:12
 * @修改备注：
 *
 */
public class RSAUtils {
    private static final Logger logger = LoggerFactory.getLogger(RSAUtils.class);
    private static final String KEY = "ygkj0123456789";
    /**
     * 编码格式
     */
    private static final String ENCODING = "UTF-8";
    /**
     * 签名算法
     */
    public static final String SIGN_ALGORITHMS = "SHA1PRNG";
    /**
     * KeyPair is a simple holder for a key pair.
     */
    private static final KeyPair KEY_PAIR = initKey();
    /**
     * 初始化方法，产生key pair，提供provider和random
     * @return KeyPair instance
     */
    private static KeyPair initKey() {

        try {
            //添加provider
            Provider provider = new org.bouncycastle.jce.provider.BouncyCastleProvider();
            Security.addProvider(provider);
            //产生用于安全加密的随机数
            SecureRandom random = SecureRandom.getInstance(SIGN_ALGORITHMS);
            random.setSeed(KEY.getBytes(ENCODING));


            KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA", provider);
            generator.initialize(1024, random);
            return generator.generateKeyPair();
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
    }
    /**
     * 产生public key
     * @return public key字符串
     */
    public static String generateBase64PublicKey() {
        PublicKey publicKey = KEY_PAIR.getPublic();
        //encodeBase64(): Encodes binary data using the base64
        //algorithm but does not chunk the output.
        //getEncoded():返回key的原始编码形式
        return new String(Base64.encode(publicKey.getEncoded()));
    }

    /**
     * 直接返回公匙
     * @return
     * @exception
     * @author wd
     * @time 2019年4月17日 下午5:13:03
     */
    public static PublicKey getPublicKey() {
        return KEY_PAIR.getPublic();
    }


    /**
     * 直接返回私匙
     * @return
     * @exception
     * @author wd
     * @time 2019年4月17日 下午5:13:13
     */
    public static PrivateKey getPrivateKey() {
        return KEY_PAIR.getPrivate();
    }

    /**
     * 解密数据
     * @param string 需要解密的字符串
     * @return  破解之后的字符串
     */
    public static String decryptBase64(String string) {
        //decodeBase64():将Base64数据解码为"八位字节”数据
        return new String(decrypt(Base64.decode(string)));
    }

    private static byte[] decrypt(byte[] byteArray) {
        try {
            Provider provider = new org.bouncycastle.jce.provider.BouncyCastleProvider();
            Security.addProvider(provider);
            //Cipher: 提供加密和解密功能的实例
            //transformation: "algorithm/mode/padding"
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding", provider);
            PrivateKey privateKey = KEY_PAIR.getPrivate();
            //初始化  
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            //doFinal(): 加密或者解密数据  
            byte[] plainText = cipher.doFinal(byteArray);
            return plainText;
        } catch(Exception e) {
            logger.error("RSA解密出错",e);
            throw new RuntimeException(e);
        }
    }
}
