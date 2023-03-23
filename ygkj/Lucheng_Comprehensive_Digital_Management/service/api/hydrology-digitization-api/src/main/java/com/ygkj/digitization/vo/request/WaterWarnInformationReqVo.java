package com.ygkj.digitization.vo.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * @author hucong
 * @description 水位预警信息表
 * @date 2021-07-13
 */
@Data
@ApiModel("水位预警信息表")
public class WaterWarnInformationReqVo implements Serializable {

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

    @ApiModelProperty("页数")
    private Integer pageSize;

    @ApiModelProperty("第几页")
    private Integer pageNum;

    @ApiModelProperty("查询开始时间")
    private String startTime;

    @ApiModelProperty("查询结束时间")
    private String endTime;

    /**
     * pdf链接地址
     */
    @ApiModelProperty("pdf链接地址")
    private String pdfUrl;


    @ApiModelProperty("发送预警短信or pdf标识")
    private boolean sendSmsFlag;

    @ApiModelProperty("发送类型  1:短信 2:传真")
    private String sendType;

    /**
     * 签发人员
     */
    @ApiModelProperty("签发人员")
    private String lsPerson;

    @ApiModelProperty("发送人员姓名")
    private String peopleNames;


    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    @ApiModelProperty("签发时间")
    private Date lsTime;

    @ApiModelProperty("关联站点编码集合")
    private String stationIds;
    /**
     * 报警模板id
     */
    @ApiModelProperty("报警模板id")
    private String warnTemplteId;

    public WaterWarnInformationReqVo() {
    }

}
