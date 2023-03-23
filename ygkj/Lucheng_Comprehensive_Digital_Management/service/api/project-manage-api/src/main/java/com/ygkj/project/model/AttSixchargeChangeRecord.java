package com.ygkj.project.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class AttSixchargeChangeRecord extends PageEntity implements Serializable {

    private static final long serialVersionUID = 1L;


    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 工程编码
     */
    private String projectCode;

    /**
     * 责任人类型
     */
    private String type;

    /**
     * 变更前责任人姓名
     */
    private String chargeNameBefore;

    /**
     * 变更前责任人手机号
     */
    private String chargePhoneBefore;

    /**
     * 变更前责任人职务
     */
    private String chargeJobBefore;

    /**
     * 变更前责任人部门
     */
    private String chargeDeptBefore;

    /**
     * 变更后责任人姓名
     */
    private String chargeNameAfter;

    /**
     * 变更后责任人手机号
     */
    private String chargePhoneAfter;

    /**
     * 变更后责任人职务
     */
    private String chargeJobAfter;

    /**
     * 变更后责任人部门
     */
    private String chargeDeptAfter;

    /**
     * 变更时间
     */
    private Date changeTime;

    @TableField(exist = false)
    private String keyWord;

    @TableField(exist = false)
    private String projectName;
}
