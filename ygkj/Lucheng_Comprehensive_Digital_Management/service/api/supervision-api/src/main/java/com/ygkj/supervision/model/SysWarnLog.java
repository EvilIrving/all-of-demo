package com.ygkj.supervision.model;

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
public class SysWarnLog implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    private Long id;

    /**
     * 接收人id
     */
    @ApiModelProperty("接收人id，多个用逗号隔开")
    private String receiveUserId;

    /**
     * 接收人手机号
     */
    @ApiModelProperty("接收人手机号，多个用逗号隔开")
    private String receivePhone;

    /**
     * 消息内容
     */
    @ApiModelProperty("消息内容")
    private String msg;

    /**
     * 消息确认状态 1已确认 0未审核 -1无需确认
     */
    private String checked;

    /**
     * 消息下一个接收人的id集合
     */
    private String nextReceiveIds;

    /**
     * 消息类型：100短信010钉钉001站内110短信加钉钉111短信加钉钉加站内信
     */
    @ApiModelProperty("消息类型：100短信010钉钉001站内110短信加钉钉111短信加钉钉加站内信")
    private String msgType;

    /**
     * check_time
     */
    private Date checkTime;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 报警id
     */
    private Long warnObjId;

    /**
     * 报警组id
     */
    private Long groupId;

    private Date warningTime;

    private boolean read;

    public SysWarnLog() {
    }

}

