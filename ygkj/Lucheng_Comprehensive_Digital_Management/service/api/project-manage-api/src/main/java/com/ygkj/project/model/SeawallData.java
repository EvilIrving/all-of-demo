package com.ygkj.project.model;

import lombok.Data;

import java.io.Serializable;

/**
 * @author wgf
 * @date 2021/11/9 15:55
 * @Description:
 */
@Data
public class SeawallData implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 工程编码
     */
    private String prcd;

    /**
     * 工程名称
     */
    private String rsnm;

    /**
     * 行政区划代码
     */
    private String adcd;

    /**
     * 安全鉴定结论（最近一次）
     */
    private String safetyidentification;

    /**
     * 安全鉴定时间（最近一次）
     */
    private String safetyidentificationtm;

    /**
     * 除险加固完成时间（最新一次）
     */
    private String reinforcementtm;

    /**
     * 应开展而未开展安全鉴定（是/否）
     */
    private String oversafetyidentification;

    /**
     * 应开展而而开展除险加固的（是/否）
     */
    private String overreinforcement;

    /**
     * 是否正在进行除险加固（是/否）
     */
    private String inforcement;

    /**
     * 是否划界确权（是/否）
     */
    private String delimitation;

    /**
     * 工程除险加固状态
     */
    private String reinforcementstatus;

    /**
     * 安全鉴定是否逾期(0-未鉴定，1-逾期，2-没有竣工时间， 3-没有除险加固，4-正常)
     */
    private String safetyevaluationovertime;

    /**
     * 有无安全责任人
     */
    private String havesafeuser;
}
