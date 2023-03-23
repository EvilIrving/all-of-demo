package com.ygkj.gateway.utils;

import java.util.regex.Pattern;

/**
 * @Author: GuanYZ
 * @Description: xss过滤工具
 * @Date: Created in 2020/4/23.
 */
public class XssCleanRuleUtils {
    private final static String[] fbsArr = { "\\", "$", "(", ")", "*", "+", ".", "[", "]", "?", "^", "{", "}", "|" };
    private final static Pattern[] scriptPatterns = {
            Pattern.compile("<script>(.*?)</script>", Pattern.CASE_INSENSITIVE),
            Pattern.compile("src[\r\n]*=[\r\n]*\\'(.*?)\\'", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
            Pattern.compile("</script>", Pattern.CASE_INSENSITIVE),
            Pattern.compile("<script(.*?)>", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
            Pattern.compile("eval\\((.*?)\\)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
            Pattern.compile("expression\\((.*?)\\)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
            Pattern.compile("javascript:", Pattern.CASE_INSENSITIVE),
            Pattern.compile("vbscript:", Pattern.CASE_INSENSITIVE),
            Pattern.compile("onload(.*?)=", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL)
    };
    public static String xssClean(String value) {
        if (value != null) {
            value = value.replaceAll("\0|\n|\r", "");
            for (Pattern pattern : scriptPatterns) {
                value = pattern.matcher(value).replaceAll("");
            }
            for (String fbs : fbsArr) {
                if (value.contains(fbs)) {
                    value = value.replace(fbs, "\\" + fbs);
                }
            }
        }
        return value;

    }
}