package com.ygkj.big.screen.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author xq
 * @description 测站数据表
 * @date 2021-07-08
 */
@Data
public class BsDrillStdata implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 演练数据id
     */
    private String id;

    /**
     * 方案测站关联id
     */
    private String stcaseId;

    /**
     * 方案id
     */
    private String caseId;

    /**
     * 时序序号
     */
    private Integer timeIndex;

    /**
     * 时序序号
     */
    private Date timeStamp;

    /**
     * 测站数据，是一个json字符串，字段各个组件自己去实现
     */
    private String stData;

    /**
     * 备用数据类型
     */
    private Integer dataType;

    public BsDrillStdata() {
    }

}
