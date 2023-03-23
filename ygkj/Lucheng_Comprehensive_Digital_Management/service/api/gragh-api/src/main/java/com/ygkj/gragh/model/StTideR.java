package com.ygkj.gragh.model;

import java.io.Serializable;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author Huangzh
 * @description 潮汐水情表
 * @date 2021-04-14
 */
@Data
public class StTideR implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 合并测站编码
     */
    private String mgstcd;

    /**
     * 测站编码
     */
    private String stcd;

    /**
     * 检测时间
     */
    private Date tm;

    /**
     * 潮位
     */
    private Double tdz;

    /**
     * tag
     */
    private boolean tag;

    /**
     * 上报时间
     */
    private Date uploadTime;

    public StTideR() {
    }

}
