package com.ygkj.river.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author lxl
 */
@Data
@ApiModel("河道蓝线消息信息发送人记录表")
public class AttBlueMsgInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @ApiModelProperty("主键")
    private String id;

    /**
     * 巡查id
     */
    @ApiModelProperty("巡查id")
    private String inspectionId;

    /**
     * 关联用户表id
     */
    @ApiModelProperty("关联用户表id")
    private String userId;

    /**
     * 手机号
     */
    @ApiModelProperty("手机号")
    private String phone;

    @ApiModelProperty("通知内容")
    private String content;


    /**
     * 名字
     */
    @ApiModelProperty("名字")
    private String userName;

    @ApiModelProperty("阶段 0-复核阶段  1-移交执法大队阶段 ")
    private Integer stage;

    @ApiModelProperty("创建时间")
    private Date createTime;

    public AttBlueMsgInfo() {
    }

}
