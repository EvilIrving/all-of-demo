package com.ygkj.utils.loginUtils;

import lombok.SneakyThrows;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class Md5Utils {
    private static ThreadLocal<MessageDigest> MESSAGE_DIGEST_LOCAL = new ThreadLocal<MessageDigest>() {
        @Override
        protected MessageDigest initialValue() {
            try {
                return MessageDigest.getInstance("MD5");
            } catch (NoSuchAlgorithmException e) {
                return null;
            }
        }
    };

    private static final int HEX_VALUE_COUNT = 16;

    public static String getMD5(byte[] bytes) throws NoSuchAlgorithmException {

        MessageDigest messageDigest = MESSAGE_DIGEST_LOCAL.get();
        if (messageDigest != null) {
            return new BigInteger(1, messageDigest.digest(bytes)).toString(HEX_VALUE_COUNT);
        }

        throw new NoSuchAlgorithmException("MessageDigest get MD5 instance error");
    }

    public static String getMD5(String value, String encode) {
        try {
            return getMD5(value.getBytes(encode));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    @SneakyThrows
    private static String irsMd5(final String src, final String charset) {
        MessageDigest md5;
        StringBuilder hexValue = new StringBuilder(32);
        md5 = MessageDigest.getInstance("MD5");
        byte[] byteArray;
        byteArray = src.getBytes(charset);
        byte[] md5Bytes = md5.digest(byteArray);
        for (byte md5Byte : md5Bytes) {
            int val = ((int) md5Byte) & 0xff;
            if (val < 16) {
                hexValue.append("0");
            }
            hexValue.append(Integer.toHexString(val));
        }
        return hexValue.toString();
    }

    /**
     * Md 5 string.
     *
     * @param src the src
     * @return the string
     */
    public static String irsMd5(final String src) {
        return irsMd5(src, "UTF-8");
    }

}
