package com.ygkj.digitization.model;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author hucong
 * @description 水位规则配置表
 * @date 2021-07-13
 */
@Data
public class WaterRuleConfiguration implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    private String id;

    /**
     * 预警类型 1:时段雨量警报 2:日雨量警报 3:河道水情警报 4:水库水情警报 5:潮位警报
     */
    private String type;

    /**
     * 报警模板id
     */
    private String warnTemplteId;

    /**
     * 发送方式 1:短信
     */
    private String sendType;

    /**
     * 关联站点编码集合
     */
    private String stationIds;

    /**
     * 关联人员ids
     */
    private String peopleIds;

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
     * 预警规则内容
     */
    private String ruleContent;

    /**
     * 预警信息详情str
     */
    private String ruleDetailInfoStr;

    /**
     * 签发人员
     */
    private String lsPerson;

    /**
     * 关联测站名称集合
     */
    private String stationNames;

    /**
     * 关联人员姓名集合
     */
    private String peopleNames;

    /**
     * 签发时间
     */
    private Date lsTime;

    public WaterRuleConfiguration() {
    }

}
