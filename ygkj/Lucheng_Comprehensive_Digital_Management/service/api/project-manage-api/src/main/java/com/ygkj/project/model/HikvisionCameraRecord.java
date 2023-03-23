package com.ygkj.project.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author xq
 * @description 摄像头数据表
 * @date 2021-04-13
 */
@Data
@ApiModel("摄像头数据表")
public class HikvisionCameraRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 摄像头编码
     */
    @ApiModelProperty("摄像头编码")
    private String cameraIndexCode;

    /**
     * 摄像头名称
     */
    @ApiModelProperty("摄像头名称")
    private String cameraName;

    /**
     * device_index_code
     */
    @ApiModelProperty("device_index_code")
    private String deviceIndexCode;

    /**
     * 经度
     */
    @ApiModelProperty("经度")
    private String longitude;

    /**
     * 纬度
     */
    @ApiModelProperty("纬度")
    private String latitude;

    /**
     * 海拔
     */
    @ApiModelProperty("海拔")
    private String altitude;

    /**
     * camera_type
     */
    @ApiModelProperty("camera_type")
    private String cameraType;

    /**
     * camera_type_name
     */
    @ApiModelProperty("camera_type_name")
    private String cameraTypeName;

    /**
     * channel_type
     */
    @ApiModelProperty("channel_type")
    private String channelType;

    /**
     * channel_type_name
     */
    @ApiModelProperty("channel_type_name")
    private String channelTypeName;

    /**
     * 区域编码
     */
    @ApiModelProperty("区域编码")
    private String regionIndexCode;

    /**
     * 同步时间
     */
    @ApiModelProperty("同步时间")
    private Date syncTime;

    /**
     * create_time
     */
    @ApiModelProperty("create_time")
    private Date createTime;

    /**
     * creator_id
     */
    @ApiModelProperty("creator_id")
    private Integer creatorId;

    /**
     * update_time
     */
    @ApiModelProperty("update_time")
    private Date updateTime;

    /**
     * updator_id
     */
    @ApiModelProperty("updator_id")
    private Integer updatorId;

    /**
     * 是否删除 0-未删除 1-已删除
     */
    @ApiModelProperty("是否删除 0-未删除 1-已删除")
    private Integer delFlag;

    /**
     * 数据源默认为1
     */
    @ApiModelProperty("数据源默认为1")
    private Integer source;

    /**
     * preview_url
     */
    @ApiModelProperty("preview_url")
    private String previewUrl;

    /**
     * 在线状态:0为下线，1为在线
     */
    @ApiModelProperty("在线状态:0为下线，1为在线")
    private Integer status;

    @ApiModelProperty("状态初次收集时刻")
    private Date statusFirstCollectTime;

    /**
     * 所属行政区划
     */
    @ApiModelProperty("所属行政区划")
    private String areaCode;

    /**
     * 工程编码
     */
    @ApiModelProperty("工程编码")
    private String projectCode;

    /**
     * 离线次数
     */
    @ApiModelProperty("离线次数")
    private Integer outLineNumber;

    /**
     * 所属流域
     */
    @ApiModelProperty("所属流域")
    private String basin;

    /**
     * 监控点国标编号
     */
    @ApiModelProperty("监控点国标编号")
    private String gbIndexCode;

    /**
     * 运维人员id
     */
    @ApiModelProperty("运维人员id")
    private String opsId;

    @ApiModelProperty("区域树和摄像头的哈希值")
    private Integer regionCameraHash;

    @ApiModelProperty("删除时刻")
    private Date delTime;

    @ApiModelProperty("区域树和摄像头的名称，用逗号分隔")
    private String regionCameraChain;

    @ApiModelProperty("1：海康平台摄像头，2，温瑞平摄像头")
    private Integer cameraSource;

    @ApiModelProperty("是否报警，1，报警，0，不报警")
    private Integer isWarnNotify;

    @ApiModelProperty("预警规则，1，每小时，2，每日，3，每周，4，每月")
    private Integer warnNotifyRule;

    @ApiModelProperty("预警规则设置时间")
    private Date warnSetTime;

    public HikvisionCameraRecord() {
    }

}

