package com.ygkj.lcsyn.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class BmPropertyContract implements Serializable {

    private static final long serialVersionUID = 1L;


    /**
     * id
     */
    private String id;

    /**
     * 物业合同名称
     */
    private String contractName;

    /**
     * 委托方单位编码
     */
    private String contractClient;
    /**
     * 委托方单位名称
     */
    private String contractClientNM;

    /**
     * 物业管理单位
     */
    private String contractUnit;
    /**
     * 物业管理单位名称
     */
    private String contractUnitNM;

    /**
     * 物业委托管理内容
     */
    private String contractContent;

    /**
     * 行政区划代码
     */
    private String adcd;

    /**
     * 起始年份
     */
    private Date starttm;

    /**
     * 终止年份
     */
    private Date endtm;

    /**
     * 专项委托资质
     */
    private String qualification;

    /**
     * 总合同经费
     */
    private String funds;

    /**
     * 合同事项 合同事项 1.运行操作 2. 维修养护 3 工程检查
     */
    private String contractMatter;

    /**
     * 合同文件
     */
    private String contractFile;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 创建人
     */
    private String creatorId;

    /**
     * 修改时间
     */
    private Date updateTime;

    /**
     * 修改人
     */
    private String updatorId;

    /**
     * 是否删除 0-未删除 1-已删除
     */
    private int delFlag;

    /**
     * 数据来源 1-运管 2-系统新增
     */
    private String sourceType;

    /**
     * 工程编码
     */
    private String prcd;
}
