package com.ygkj.project.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

@Data
public class IrsInvestProjectFinance implements Serializable {

    private static final long serialVersionUID = 1L;


    @TableId(type = IdType.UUID)
    private String id;
    /**
     * 项目代码
     */
    private String projectCode;


    /**
     * 总金额
     */
    private String totalMoney;

    /**
     * 资金来源
     */
    private String fundsSourcr;

    /**
     * 用途
     */
    private String purpose;

    /**
     * 已用金额
     */
    private String amountUserd;

    /**
     * 剩余金额
     */
    private String amountRemain;

    /**
     * 时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date time;

    /**
     * 维护人
     */
    private String maintainer;

    /**
     * 投资计划编码
     */
    private String projectBaseCode;
}
