package com.ygkj.digitization.vo.response;

import com.ygkj.digitization.vo.request.RuleDetailInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author hucong
 * @description 水位规则配置表
 * @date 2021-07-13
 */
@Data
@ApiModel("水位规则配置表")
public class WaterRuleConfigurationRespVo implements Serializable {

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
    @ApiModelProperty(value = "报警模板id", hidden = true)
    private String warnTemplteId;

    /**
     * 报警模板内容
     */
    @ApiModelProperty("报警模板内容")
    private String warnTemplteConent;

    /**
     * 发送方式 1:短信
     */
    @ApiModelProperty("发送方式 1:短信")
    private String sendType;

    /**
     * 关联站点ids
     */
    @ApiModelProperty("关联站点编码集合")
    private String stationIds;

    /**
     * 关联人员ids
     */
    @ApiModelProperty("关联人员ids")
    private String peopleIds;

    @ApiModelProperty("预警规则内容")
    private String ruleContent;

    @ApiModelProperty("站点数")
    private Integer stNum;

    @ApiModelProperty("发送人数")
    private Integer peopleNum;

    /**
     * 签发人员
     */
    @ApiModelProperty("签发人员")
    private String lsPerson;

    /**
     * 签发时间
     */
    @ApiModelProperty("签发时间")
    private Date lsTime;

    /**
     * 预警信息详情str
     */
    @ApiModelProperty(value = "预警信息详情str", hidden = true)
    private String ruleDetailInfoStr;

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

    @ApiModelProperty("配置规则详情list")
    List<RuleDetailInfo> ruleDetailInfoList;

    public WaterRuleConfigurationRespVo() {
    }

}