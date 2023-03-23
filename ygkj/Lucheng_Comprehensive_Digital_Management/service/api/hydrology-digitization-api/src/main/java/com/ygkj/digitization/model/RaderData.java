package com.ygkj.digitization.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Huangzh
 * @description 实时雷达监测数据
 * @date 2021-10-11
 */
@Data
public class RaderData implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 图片名称
     */
    private String name;

    /**
     * 图片地址
     */
    private String url;

    /**
     * 时间
     */
    private Date monitorTime;

    public RaderData() {
    }

}
