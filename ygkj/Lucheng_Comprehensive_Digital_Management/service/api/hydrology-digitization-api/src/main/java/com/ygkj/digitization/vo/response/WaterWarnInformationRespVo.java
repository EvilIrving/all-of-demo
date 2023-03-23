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
 * @description 水位预警信息表
 * @date 2021-07-13
 */
@Data
@ApiModel("水位预警信息表")
public class WaterWarnInformationRespVo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @ApiModelProperty("主键id")
    private String id;

    /**
     * 预警类型 1:时段雨量警报 2:日雨量警报 3:河道水情警报 4:水库水情警报 5:潮位警报 6:自写
     */
    @ApiModelProperty("预警类型 1:时段雨量警报 2:日雨量警报 3:河道水情警报 4:水库水情警报 5:潮位警报 6:自写")
    private String type;

    /**
     * 预警规则内容
     */
    @ApiModelProperty("预警规则内容")
    private String ruleContent;

    @ApiModelProperty("预警规则id")
    private String ruleContentId;

    /**
     * 预警内容
     */
    @ApiModelProperty("预警内容")
    private String warnContent;

    /**
     * 发送人数
     */
    @ApiModelProperty("发送人数")
    private Integer sendPersonNum;

    /**
     * 发送人员ids
     */
    @ApiModelProperty("发送人员ids")
    private String sendPersonIds;

    /**
     * 发送状态 0: 未发送 1: 已发送
     */
    @ApiModelProperty("发送状态 0: 未发送 1: 已发送")
    private Integer sendStatus;

    @ApiModelProperty("时间")
    private Date createTime;

    /**
     * pdf链接地址
     */
    @ApiModelProperty("pdf链接地址")
    private String pdfUrl;

    @ApiModelProperty("发送类型  1:短信 2:传真")
    private String sendType;

    /**
     * 签发人员
     */
    @ApiModelProperty("签发人员")
    private String lsPerson;


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

    @ApiModelProperty("关联站点编码集合")
    private String stationIds;

    /**
     * 报警模板id
     */
    @ApiModelProperty("报警模板id")
    private String warnTemplteId;

    @ApiModelProperty("报警模板内容")
    private String warnTemplteContent;

    public WaterWarnInformationRespVo() {
    }

}
