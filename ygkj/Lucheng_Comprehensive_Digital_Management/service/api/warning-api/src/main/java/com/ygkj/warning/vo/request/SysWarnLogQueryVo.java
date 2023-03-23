package com.ygkj.warning.vo.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * sys_warn_log
 *
 * @author huangzh 2021-03-04
 */
@Data
@ApiModel("入库异常记录入参")
public class SysWarnLogQueryVo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @ApiModelProperty("主键id")
    private Long id;

    /**
     * 接收人id
     */
    @ApiModelProperty("接收人id")
    private String receiveUserId;

    @ApiModelProperty("接收人姓名")
    private String receiveUserName;

    /**
     * 接收人手机号
     */
    @ApiModelProperty("接收人手机号")
    private String receivePhone;

    /**
     * 消息内容
     */
    @ApiModelProperty("消息内容")
    private String msg;

    /**
     * 消息确认状态 1已确认 0未审核 -1无需确认
     */
    @ApiModelProperty("消息确认状态 1已确认 0未审核 -1无需确认")
    private String checked;

    /**
     * 消息下一个接收人的id集合
     */
    @ApiModelProperty("消息下一个接收人的id集合")
    private String nextReceiveIds;

    /**
     * 消息类型：100短信 010钉钉 001站内 110短信加钉钉 111短信加钉钉加站内信 200感知检测
     */
    @ApiModelProperty("消息类型：100短信 010钉钉 001站内 110短信加钉钉 111短信加钉钉加站内信 200感知检测")
    private String msgType;

    /**
     * check_time
     */
    @ApiModelProperty("检查时间")
    private Date checkTime;

    @ApiModelProperty("页码，最小为1")
    int pageNum = 1;
    @ApiModelProperty("页面大小，最小为1")
    int pageSize = 10;

    /**
     * 查询开始时间
     */
    @ApiModelProperty("查询开始时间")
    private String queryStartTime;

    /**
     * 查询结束时间
     */
    @ApiModelProperty("查询结束时间")
    private String queryEndTime;

    public SysWarnLogQueryVo() {
    }

}

