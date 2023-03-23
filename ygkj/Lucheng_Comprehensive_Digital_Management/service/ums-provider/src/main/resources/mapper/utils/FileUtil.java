package com.ygkj.utils;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;

/**
 * @author Huang.zh
 * @date 2020/9/2 10:38
 * @Description: 文件上传操作工具类
 */
public class FileUtil {
    public static final byte[] input2byte(InputStream inStream) throws IOException {
        ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
        byte[] buff = new byte[100];
        boolean var3 = false;

        int rc;
        while((rc = inStream.read(buff, 0, 100)) > 0) {
            swapStream.write(buff, 0, rc);
        }

        byte[] in2b = swapStream.toByteArray();
        inStream.close();
        return in2b;
    }

    public static String getSHA1Checksum(InputStream fis) throws Exception {
        byte[] b = createChecksum(fis);
        String result = "";

        for(int i = 0; i < b.length; ++i) {
            result = result + Integer.toString((b[i] & 255) + 256, 16).substring(1);
        }

        return result;
    }

    public static byte[] createChecksum(InputStream fis) throws Exception {
        byte[] buffer = new byte[1024];
        MessageDigest complete = MessageDigest.getInstance("SHA1");

        int numRead;
        do {
            numRead = fis.read(buffer);
            if (numRead > 0) {
                complete.update(buffer, 0, numRead);
            }
        } while(numRead != -1);

        fis.close();
        return complete.digest();
    }

    public static String getSHA1Checksum(String filename) throws Exception {
        byte[] b = createChecksum(filename);
        String result = "";

        for(int i = 0; i < b.length; ++i) {
            result = result + Integer.toString((b[i] & 255) + 256, 16).substring(1);
        }

        return result;
    }

    public static byte[] createChecksum(String filename) throws Exception {
        InputStream fis = new FileInputStream(filename);
        byte[] buffer = new byte[1024];
        MessageDigest complete = MessageDigest.getInstance("SHA1");

        int numRead;
        do {
            numRead = fis.read(buffer);
            if (numRead > 0) {
                complete.update(buffer, 0, numRead);
            }
        } while(numRead != -1);

        fis.close();
        return complete.digest();
    }

    public static String getFileExt(String fileName) {
        if (fileName == null) {
            return "";
        } else {
            String ext = "";
            int lastIndex = fileName.lastIndexOf(".");
            if (lastIndex >= 0) {
                ext = fileName.substring(lastIndex + 1).toLowerCase();
            }

            return ext;
        }
    }
}
