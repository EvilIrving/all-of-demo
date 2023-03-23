package com.ygkj.visualization.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 浪场数据记录表
 *
 * @author huangzh 2021-02-22
 */
@Data
public class WaveField implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    private Integer id;

    /**
     * 预报时间
     */
    private Date predictTime;

    /**
     * 具体浪场数据
     */
    private String dataJson;

    public WaveField() {
    }

}

