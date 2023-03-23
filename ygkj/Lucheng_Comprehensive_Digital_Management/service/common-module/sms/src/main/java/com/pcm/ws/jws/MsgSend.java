package com.pcm.ws.jws;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Security;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;

public class MsgSend {

    private static String userCode = "lcqslj";
    private static String password = "lcqslj@2021";
    private static WsItfTask wsItfTaskImplPort;
    static {
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
        WsItfTask_Service wsItfTask_service = new WsItfTask_Service();
        wsItfTaskImplPort = wsItfTask_service.getWsItfTaskImplPort();
    }

    public static void select(String id){
        String token = getToken();
        GetSubmitResult getSubmitResult = new GetSubmitResult();
        getSubmitResult.submitKey = id;
        GetSubmitResultResponse submitResult = wsItfTaskImplPort.getSubmitResult(getSubmitResult, token);
        System.out.println(submitResult.getReturn().getResult());

    }

    public static String getToken(){
        //先获取token
        String unix = String.valueOf(System.currentTimeMillis());
        String u = encodeUserCode(userCode, unix.substring(0, 8));
        String p = encodePassword(userCode, password, unix);
        InterfaceResult authorization = wsItfTaskImplPort.authorization(u, p, Long.valueOf(unix));
        TokenObj tokenObj = (TokenObj) authorization.getResult();
        String token = tokenObj.token;
        return token;
    }

    public static SubmitTaskResponse sendMsg(String phone, String context) {
        String token = getToken();

        SubmitTask submitTask = new SubmitTask();
        List<ItfSendDetail> detailList = new ArrayList<>();
        ItfSendDetail detail = new ItfSendDetail();
        detail.setContent(context);
        detail.setReceiveNumber(phone);
        detailList.add(detail);
        submitTask.details = detailList;
        ItfSmsOption itfSmsOption = new ItfSmsOption();
        itfSmsOption.setHasSms(true);
        ItfOptions itfOptions = new ItfOptions();
        itfOptions.setItfSmsOption(itfSmsOption);
        submitTask.setItfOptions(itfOptions);
        SubmitTaskResponse submitTaskResponse = wsItfTaskImplPort.submitTask(submitTask, token);
        return submitTaskResponse;
    }


    private static String md5(String str) {
        byte[] digest = null;
        try {
            MessageDigest md5 = MessageDigest.getInstance("md5");
            digest = md5.digest(str.getBytes("utf-8"));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        //16是表示转换为16进制数
        String md5Str = new BigInteger(1, digest).toString(16);
        return md5Str;
    }

    private static String encodePassword(String userCode, String password, String unix) {
        String md5 = md5(password);
        md5 = md5(userCode + md5);
        md5 = md5(md5 + unix);
        return md5;
    }

    private static Key generateKey(String key) throws Exception {
        DESKeySpec dks = new DESKeySpec(key.getBytes("UTF-8"));
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        return keyFactory.generateSecret(dks);
    }


    private static String encodeUserCode(String userCode, String key) {
        try {
            Key secretKey = generateKey(key);
            Cipher cipher = Cipher.getInstance("DES/ECB/PKCS7Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            byte[] bytes = cipher.doFinal(userCode.getBytes(StandardCharsets.UTF_8));
            return (new String(Base64.getEncoder().encode(bytes)));
        } catch (Exception e) {
            e.printStackTrace();
            return userCode;
        }
    }


}
