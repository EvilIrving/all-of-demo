package com.ygkj.project.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

@Data
@ApiModel("合同管理物业合同")
public class BmPropertyContract  implements Serializable {

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
    private String contractClientNm;

    /**
     * 物业管理单位
     */
    private String contractUnit;

    /**
     * 物业管理单位名称
     */
    private String contractUnitNm;

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
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone="GMT+8")
    private Date starttm;

    /**
     * 终止年份
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone="GMT+8")
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
