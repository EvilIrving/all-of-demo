package com.ygkj.utils;

public final class Base64 {
    private static final char[] BASE64CODE = new char[]{'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '+', '/'};
    private static final byte[] BASE64DECODE = new byte[]{-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 62, -1, 63, -1, 63, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, -1, -1, -1, 0, -1, -1, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, -1, -1, -1, -1, -1, -1, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, -1, -1, -1, -1, -1};
    private static final int HEX_255 = 255;
    private static final int HEX_16515072 = 16515072;
    private static final int HEX_258048 = 258048;
    private static final int HEX_4032 = 4032;
    private static final int HEX_63 = 63;
    private static final int HEX_16711680 = 16711680;
    private static final int HEX_65280 = 65280;
    private static final int NUMBER_TWO = 2;
    private static final int NUMBER_THREE = 3;
    private static final int NUMBER_FOUR = 4;
    private static final int NUMBER_SIX = 6;
    private static final int NUMBER_EIGHT = 8;
    private static final int NUMBER_TWELVE = 12;
    private static final int NUMBER_SIXTEEN = 16;
    private static final int NUMBER_EIGHTEEN = 18;

    private Base64() {
    }

    public static String encode(byte[] b) {
        int code = 0;
        StringBuffer sb = new StringBuffer((b.length - 1) / 3 << 6);

        for(int i = 0; i < b.length; ++i) {
            code |= b[i] << 16 - i % 3 * 8 & 255 << 16 - i % 3 * 8;
            if (i % 3 == 2 || i == b.length - 1) {
                sb.append(BASE64CODE[(code & 16515072) >>> 18]);
                sb.append(BASE64CODE[(code & 258048) >>> 12]);
                sb.append(BASE64CODE[(code & 4032) >>> 6]);
                sb.append(BASE64CODE[code & 63]);
                code = 0;
            }
        }

        if (b.length % 3 > 0) {
            sb.setCharAt(sb.length() - 1, '=');
        }

        if (b.length % 3 == 1) {
            sb.setCharAt(sb.length() - 2, '=');
        }

        return sb.toString();
    }

    public static byte[] decode(String code) {
        if (code == null) {
            return null;
        } else {
            int len = code.length();
            if (len % 4 != 0) {
                throw new IllegalArgumentException("Base64 string length must be 4*n");
            } else if (code.length() == 0) {
                return new byte[0];
            } else {
                int pad = 0;
                if (code.charAt(len - 1) == '=') {
                    ++pad;
                }

                if (code.charAt(len - 2) == '=') {
                    ++pad;
                }

                int retLen = len / 4 * 3 - pad;
                byte[] ret = new byte[retLen];

                for(int i = 0; i < len; i += 4) {
                    int j = i / 4 * 3;
                    char ch1 = code.charAt(i);
                    char ch2 = code.charAt(i + 1);
                    char ch3 = code.charAt(i + 2);
                    char ch4 = code.charAt(i + 3);
                    int tmp = BASE64DECODE[ch1] << 18 | BASE64DECODE[ch2] << 12 | BASE64DECODE[ch3] << 6 | BASE64DECODE[ch4];
                    ret[j] = (byte)((tmp & 16711680) >> 16);
                    if (i < len - 4) {
                        ret[j + 1] = (byte)((tmp & '\uff00') >> 8);
                        ret[j + 2] = (byte)(tmp & 255);
                    } else {
                        if (j + 1 < retLen) {
                            ret[j + 1] = (byte)((tmp & '\uff00') >> 8);
                        }

                        if (j + 2 < retLen) {
                            ret[j + 2] = (byte)(tmp & 255);
                        }
                    }
                }

                return ret;
            }
        }
    }
}
