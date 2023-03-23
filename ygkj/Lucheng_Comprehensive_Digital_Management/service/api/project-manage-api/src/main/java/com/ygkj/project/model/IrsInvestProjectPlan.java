package com.ygkj.project.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

@Data
public class IrsInvestProjectPlan implements Serializable {

    private static final long serialVersionUID = 1L;


    @TableId(type = IdType.UUID)
    private String id;

    /**
     * 项目代码
     */
    private String projectCode;

    /**
     * 计划名称
     */
    private String planName;

    /**
     * 时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date time;

    /**
     * 上报人
     */
    private String reportPerson;

    /**
     * 投资计划编码
     */
    private String projectBaseCode;

}
