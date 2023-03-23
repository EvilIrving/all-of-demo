package com.ygkj.utils;

import lombok.Data;

/**
 * @author Huang.zh
 * @date 2020/9/2 10:42
 * @Description:
 */
@Data
public class PutRet {
    private long fsize;
    private String key;
    private String hash;
    private int width;
    private int height;
}
