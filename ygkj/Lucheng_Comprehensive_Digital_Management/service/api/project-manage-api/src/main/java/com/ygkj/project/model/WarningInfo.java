package com.ygkj.project.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ygkj.gragh.model.PageEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author wgf
 * @date 2021/10/22 15:00
 * @Description: 预警记录表
 */
@Data
public class WarningInfo  extends PageEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.ASSIGN_UUID)
    /**
     * id
     */
    @ApiModelProperty(value = "id")
    private String id;

    /**
     * 预警规则（1.安全鉴定超期，2.汛前检查超期，3.年度总结超期，4.巡查信息异常，5.工程隐患，6.海塘实时报警，7.海塘预报预警，8.时段雨量报警，9.河道水情报警，10.水库水情报警，11.潮位报警）
     */
    @ApiModelProperty(value = "预警规则（1.安全鉴定超期，2.汛前检查超期，3.年度总结超期，4.巡查信息异常，5.工程隐患，" +
            "6.海塘实时报警，7.海塘预报预警，8.时段雨量报警，9.河道水情报警，10.水库水情报警，11.潮位报警,12测站关联报警，13测站限值报警）")
    private Integer warningrule;

    @ApiModelProperty("规则配置（1.报警2.提醒）")
    private Integer ruletype;

    /**
     * 发送人数
     */
    @ApiModelProperty(value = "发送人数")
    private Integer sendcount;

    /**
     * 发送人员的id
     */
    @ApiModelProperty(value = "发送人员的id，多个逗号拼接")
    private String sendcode;

    /**
     * 报警内容
     */
    @ApiModelProperty(value = "报警内容")
    private String warningcontent;

    /**
     * 报警时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "报警时间")
    private Date warningtime;

    /**
     * 消息状态 1.已转发2.未转发
     */
    @ApiModelProperty(value = "消息状态 1.已转发2.未转发")
    private Integer status;

    @TableField(exist = false)
    private List<WarningUser> users;

    @TableField(exist = false)
    private String startTime;

    @TableField(exist = false)
    private String endTime;

    /**
     * 当前登录用户电话号码
     */
    @TableField(exist = false)
    private String phone;

    /**
     * 阅读状态
     */
    @TableField(exist = false)
    private String readStatus;

    public WarningInfo() {}
}
