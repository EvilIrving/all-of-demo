package com.ygkj.lcsyn.model;

import lombok.Data;

import java.util.Date;

@Data
public class AttTideFcstBase {

    /**
     * 预报id
     */
    private String id;

    /**
     * 预报发布时刻
     */
    private Date releaseTime;

    /**
     * 发布类型1：手动预报，2自动预报
     */
    private int releaseType;

    /**
     * 附件名称
     */
    private String attachName;

    /**
     * 附件url
     */
    private String attachUrl;

    /**
     * 自动预报起始时刻
     */
    private String fcstStart;

    /**
     * 自动预报结束时刻
     */
    private String fcstEnd;
}
