package com.ygkj.project.model;


import lombok.Data;

import java.io.Serializable;

@Data
public class BmProject  implements Serializable {

    private static final long serialVersionUID = 1L;


    /**
     * 工程编码
     */
    private String prcd;

    /**
     * 水利厅内部编码
     */
    private String projectcode;

    /**
     * 水管理平台编码
     */
    private String unicd;

    /**
     * 普查码
     */
    private String recordcode;

    /**
     * 注册登记码
     */
    private String recordid;

    /**
     * 工程名称
     */
    private String name;

    /**
     * 行政区划代码
     */
    private String adcd;

    /**
     * 工程所在地
     */
    private String fief;

    /**
     * 管辖层级：1、2、3、4，省、市、县、乡镇
     */
    private Integer superviseLevel;

    /**
     * 工程类别
     */
    private String projectCategory;

    /**
     * 所在流域
     */
    private String basin;

    /**
     * 工程规模
     */
    private String projectScale;

    /**
     * 工程等别
     */
    private String prlv;

    /**
     * 管理单位编码
     */
    private String mUniCd;

    /**
     * 主管单位编码
     */
    private String orcpCd;

    /**
     * 经度
     */
    private String lgtd;

    /**
     * 纬度
     */
    private String lttd;

    /**
     * 缩放级别
     */
    private String zoom;

    /**
     * 创标状态
     */
    private String standard;

    /**
     * 上级工程编码
     */
    private String parentProject;

    /**
     * 工程简介
     */
    private String summary;


    /**
     * 工程形象图
     */
    private String projectSample;
}
