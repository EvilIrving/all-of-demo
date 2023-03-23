package com.ygkj.digitization.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author hucong
 * @description 水位预警信息表
 * @date 2021-07-13
 */
@Data
public class WaterWarnInformation implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    private String id;

    /**
     * 预警类型 1:时段雨量警报 2:日雨量警报 3:河道水情警报 4:水库水情警报 5:潮位警报 6:自写
     */
    private String type;

    /**
     * 预警规则
     */
    private String ruleContent;

    /**
     * 预警内容
     */
    private String warnContent;

    /**
     * 发送人数
     */
    private Integer sendPersonNum;

    /**
     * 发送人员ids
     */
    private String sendPersonIds;

    /**
     * 发送状态 0: 未发送 1: 已发送
     */
    private Integer sendStatus;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 删除标识
     */
    private String delFlag;

    /**
     * 创建人id
     */
    private String creatorId;

    /**
     * 创建人姓名
     */
    private String createName;

    /**
     * pdf链接地址
     */
    private String pdfUrl;

    /**
     * 发送类型 1:短信 2:传真
     */
    private String sendType;

    /**
     * 预警规则id
     */
    private String ruleContentId;

    /**
     * 签发人员
     */
    private String lsPerson;

    private String peopleNames;

    /**
     * 签发时间
     */
    private Date lsTime;

    /**
     * 关联站点编码集合
     */
    private String stationIds;

    /**
     * 报警模板id
     */
    private String warnTemplteId;

    public WaterWarnInformation() {
    }

}