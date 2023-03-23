package com.ygkj.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author xq
 * @Description
 * @Date 2021/5/18
 */
public class StringUtils extends org.springframework.util.StringUtils {

    public static String JOIN_DELIMITER=",";

    public static String EMPTY="";

    public static final String SPACE = " ";

    public static final String LF = "\n";

    public static final String CR = "\r";

    private static Pattern snakePattern = Pattern.compile("_(\\w)");

    /**
     * 蛇形转驼峰
     * @param str
     * @return
     */
    public static String snake2Camel(String str) {
        str = str.toLowerCase();
        Matcher matcher = snakePattern.matcher(str);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(sb, matcher.group(1).toUpperCase());
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    private static Pattern camelPattern = Pattern.compile("[A-Z]");

    /**
     * 驼峰转蛇形
     * @param str
     * @return
     */
    public static String camel2Snake(String str) {
        Matcher matcher = camelPattern.matcher(str);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(sb, "_" + matcher.group(0).toLowerCase());
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    public static boolean isBlank(CharSequence cs) {
        int strLen;
        if (cs != null && (strLen = cs.length()) != 0) {
            for(int i = 0; i < strLen; ++i) {
                if (!Character.isWhitespace(cs.charAt(i))) {
                    return false;
                }
            }

            return true;
        } else {
            return true;
        }
    }

    public static boolean isNotBlank(CharSequence cs) {
        return !isBlank(cs);
    }

}
