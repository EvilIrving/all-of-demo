package com.ygkj.digitization.vo.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author hucong
 * @description 水位规则配置表
 * @date 2021-07-13
 */
@Data
@ApiModel("水位规则配置表")
public class WaterRuleConfigurationReqVo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @ApiModelProperty("主键id")
    private String id;

    /**
     * 预警类型 1:时段雨量警报 2:日雨量警报 3:河道水情警报 4:水库水情警报 5:潮位警报
     */
    @ApiModelProperty("预警类型 1:时段雨量警报 2:日雨量警报 3:河道水情警报 4:水库水情警报 5:潮位警报")
    private String type;

    /**
     * 报警模板id
     */
    @ApiModelProperty("报警模板id")
    private String warnTemplteId;

    /**
     * 发送方式 1:短信 2:传真
     */
    @ApiModelProperty("发送方式 1:短信 2:传真")
    private String sendType;

    /**
     * 关联站点编码集合
     */
    @ApiModelProperty("关联站点编码集合")
    private String stationIds;

    /**
     * 关联人员ids
     */
    @ApiModelProperty("关联人员ids")
    private String peopleIds;

    @ApiModelProperty("页数")
    private Integer pageSize;

    @ApiModelProperty("第几页")
    private Integer pageNum;

    @ApiModelProperty("预警规则内容")
    private String ruleContent;

    @ApiModelProperty("预警规则详情")
    private List<RuleDetailInfo> ruleDetailInfos;

    /**
     * 签发人员
     */
    @ApiModelProperty("签发人员")
    private String lsPerson;

    /**
     * 签发时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    @ApiModelProperty("签发时间")
    private Date lsTime;

    /**
     * 关联测站名称集合
     */
    @ApiModelProperty("关联测站名称集合")
    private String stationNames;

    /**
     * 关联人员姓名集合
     */
    @ApiModelProperty("关联人员姓名集合")
    private String peopleNames;

    public WaterRuleConfigurationReqVo() {
    }

}
