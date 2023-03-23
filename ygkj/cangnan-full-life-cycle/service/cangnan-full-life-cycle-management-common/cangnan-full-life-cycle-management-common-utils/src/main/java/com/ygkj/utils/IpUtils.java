package com.ygkj.utils;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * ip工具类
 */
public class IpUtils {
    public static void main(String[] args) {
        System.out.println(getIP());
    }

    //获取当前主机IP
    public static String getIP(){
        try {
            InetAddress addr = InetAddress.getLocalHost();
            addr.getHostAddress();
            return addr.getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return "";
    }

}
