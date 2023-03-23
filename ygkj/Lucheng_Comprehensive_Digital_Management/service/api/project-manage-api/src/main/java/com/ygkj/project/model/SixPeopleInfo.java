package com.ygkj.project.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class SixPeopleInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 责任单位名称
     */
    private String unitName;

    /**
     * 责任单位负责人
     */
    private String unitDirector;

    /**
     * 责任单位负责人职务
     */
    private String unitJob;

    /**
     * 责任单位负责人联系方式
     */
    private String unitDirectorPhone;

    /**
     * 责任人类型
     */
    private String unitType;

}
