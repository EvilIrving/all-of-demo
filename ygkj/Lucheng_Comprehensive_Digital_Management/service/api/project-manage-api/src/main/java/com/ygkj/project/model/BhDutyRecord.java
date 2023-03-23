package com.ygkj.project.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ygkj.gragh.model.PageEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

@Data
public class BhDutyRecord extends PageEntity implements Serializable {

    /**
     * duty_date
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty("值班时间")
    private Date dutyDate;

    /**
     * pid
     */
    @ApiModelProperty("工程编码")
    private String pid;

    /**
     * leader
     */
    @ApiModelProperty("值班负责人id")
    private String leader;

    /**
     * mgt_day
     */
    @ApiModelProperty("值班员id")
    private String mgtDay;

    /**
     * mgt_night
     */
    private String mgtNight;

    /**
     * bq_a
     */
    private String bqA;

    /**
     * bq_b
     */
    private String bqB;

    /**
     * bf_a
     */
    private String bfA;

    /**
     * bf_b
     */
    private String bfB;

    /**
     * tm_a
     */
    private String tmA;

    /**
     * tm_b
     */
    private String tmB;

    /**
     * dh_a
     */
    private String dhA;

    /**
     * dh_b
     */
    private String dhB;

    /**
     * cd_a
     */
    private String cdA;

    /**
     * cd_b
     */
    private String cdB;

    /**
     * resume
     */
    private String resume;

    /**
     * week
     */
    private String week;

    /**
     * id
     */
    @TableId(value = "id")
    @ApiModelProperty("id")
    private String id;

    /**
     * fs_a
     */
    private String fsA;

    /**
     * fs_b
     */
    private String fsB;

    /**
     * 中层领导
     */
    private String midLeader;

    /**
     * 巡查人员联系电话
     */
    private String mgtNightPhone;

    /**
     * 巡查人员编号
     */
    private String mgtNightId;

    /**
     * 巡查类型
     */
    @ApiModelProperty("巡查类型1.日常巡查 2.汛前检查 3.年度检查 4.特别检查")
    private String patrolType;

    /**
     * affair
     */
    private String affair;

    /**
     * 白班管理人员
     */
    private String manager;

    /**
     * 晚班管理人员
     */
    private String managerN;

    /**
     * 运行负责
     */
    private Integer runQin;

    /**
     * 值班长
     */
    private Integer dutyCap;

    /**
     * 带班领导（晚班）
     */
    private Integer leaderN;

    /**
     * 运行负责（晚班）
     */
    private Integer runQinN;

    /**
     * 值班长（晚班）
     */
    private Integer dutyCapN;

    /**
     * 带班领导（中班）
     */
    private Integer leaderA;

    /**
     * 运行负责（中班）
     */
    private Integer runQinA;

    /**
     * 值班长（中班）
     */
    private Integer dutyCapA;

    /**
     * 管理值班（中班）
     */
    private String managerA;

    /**
     * 值班员（中班）
     */
    private String mgtAfternoon;

    /**
     * 设备巡查人员
     */
    @ApiModelProperty("设备巡查人员id")
    private String devicePatrolUid;

    /**
     * 物业巡查员
     */
    private Integer propertyPatrolUid;

    /**
     * 值班电话
     */
    @ApiModelProperty("值班电话")
    private String dutyPhone;

    @ApiModelProperty("数据来源 1-标化 2-本地新增")
    private int sourceType;

    @TableField(exist = false)
    @ApiModelProperty("工程名称")
    private String pjnm;

    @TableField(exist = false)
    @ApiModelProperty("值班时间年月")
    private String timeYm;

    @TableField(exist = false)
    @ApiModelProperty("值班负责人名称")
    private String leaderName;

    @TableField(exist = false)
    @ApiModelProperty("值班员名称")
    private String mgtDayName;

    @TableField(exist = false)
    @ApiModelProperty("水工巡查人员名称")
    private String mgtNightName;

    @TableField(exist = false)
    @ApiModelProperty("设备巡查人员名称")
    private String devicePatrolName;

    @TableField(exist = false)
    @ApiModelProperty("设备巡查人员联系方式")
    private String devicePatrolPhone;
}
