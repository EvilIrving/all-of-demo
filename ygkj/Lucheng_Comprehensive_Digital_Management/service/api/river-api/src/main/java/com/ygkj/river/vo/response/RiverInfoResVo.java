package com.ygkj.river.vo.response;

import lombok.Data;

@Data
public class RiverInfoResVo {

    private String id;

    /**
     * 河道名称
     */
    private String name;

    /**
     * 河流类型
     */
    private String riverType;

    /**
     * 河流等级
     */
    private String riverGrade;

    /**
     * 所属流域
     */
    private String basin;

    /**
     * 河流长度
     */
    private Double riverLength;

    /**
     * 河源位置
     */
    private String resourceLocation;

    /**
     * 河口位置
     */
    private String estuaryLocation;

    /**
     * 河长级别
     */
    private String riverManagerGrade;

    /**
     * 河长姓名
     */
    private String riverManagerName;

    /**
     * 河长职务
     */
    private String riverManagerPhone;

    /**
     * 河长职务
     */
    private String riverManagerJob;

    /**
     * 所属单位
     */
    private String department;

    /**
     * 所在行政区划代码
     */
    private String distrinctAdcd;

    /**
     * 河道行政级别（省级/市级/县级/乡级）
     */
    private String policy;
}
