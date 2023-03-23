package com.ygkj.water.model;

import lombok.Data;

import java.io.File;

@Data
public class PtUpload {

    /**
     * 文件
     */
    private File file;

    /**
     * 文件二进制数组
     */
    private byte[] bytes;

    /**
     * 系统名称
     */
    private String appName;

    /**
     * 相对路径
     */
    private String path;

    /**
     * 创建人id
     */
    private String createId;
}
