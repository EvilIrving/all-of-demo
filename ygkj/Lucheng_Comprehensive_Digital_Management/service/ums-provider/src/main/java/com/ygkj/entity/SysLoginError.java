package com.ygkj.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author lyf
 * @since 2021-03-23
 */
@Data
@TableName("sys_login_error")
@ApiModel(value="SysLoginError对象", description="")
public class SysLoginError extends Model {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId("ID")
    private String id;

    @ApiModelProperty(value = "登录的ip地址")
    @TableField("IP_ADDR")
    private String ipAddr;

    @ApiModelProperty(value = "用户id")
    @TableField("USER_ID")
    private String userId;

    @ApiModelProperty(value = "上次登录错误时间")
    @TableField("LAST_ERR_TIME")
    private Date lastErrTime;

    @ApiModelProperty(value = "当前连续失败次数")
    @TableField("CONT_NUM")
    private Integer contNum;

    @ApiModelProperty(value = "总共失败次数")
    @TableField("TOTAL_NUM")
    private Integer totalNum;

    @ApiModelProperty(value = "用户解锁时间")
    @TableField("UNLOCK_TIME")
    private Date unlockTime;

    @Override
    public String toString() {
        return "SysLoginError{" +
            "id=" + id +
            ", ipAddr=" + ipAddr +
            ", userId=" + userId +
            ", lastErrTime=" + lastErrTime +
            ", contNum=" + contNum +
            ", totalNum=" + totalNum +
            ", unlockTime=" + unlockTime +
        "}";
    }
}
