package com.ygkj.digitization.model;

import java.io.Serializable;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author hucong
 * @description 水位预警模板表
 * @date 2021-07-13
 */
@Data
public class WaterWarnTemplate implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    private String id;

    /**
     * 模板名称
     */
    private String name;

    /**
     * 模板内容
     */
    private String content;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 删除标识
     */
    private String delFlag;

    /**
     * 创建人id
     */
    private String creatorId;

    /**
     * 创建人姓名
     */
    private String createName;

    public WaterWarnTemplate() {
    }

}
