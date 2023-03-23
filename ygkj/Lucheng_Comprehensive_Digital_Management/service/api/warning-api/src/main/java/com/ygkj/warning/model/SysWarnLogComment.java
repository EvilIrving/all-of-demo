package com.ygkj.warning.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Huangzh
 * @description sys_warn_log_comment
 * @date 2021-04-09
 */
@Data
@ApiModel
public class SysWarnLogComment implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @ApiModelProperty("id")
    private Long id;

    /**
     * 预警记录id
     */
    @ApiModelProperty("预警记录id")
    private Long warnLogId;

    /**
     * 预警人id
     */
    @ApiModelProperty("预警人id")
    private String commentUserId;

    /**
     * 评论内容
     */
    @ApiModelProperty("评论内容")
    private String commentContent;

    /**
     * 评论时间
     */
    @ApiModelProperty("评论时间")
    private Date commentTime;

    private String userName;

    public SysWarnLogComment() {
    }

}

