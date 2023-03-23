package com.ygkj.big.screen.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 摄像头
 */
@Data
public class HikvisionCameraRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("camera_index_code")
    private String cameraIndexCode;


    @ApiModelProperty("camera_name")
    private String cameraName;


    @ApiModelProperty("device_index_code")
    private String deviceIndexCode;


    @ApiModelProperty("longitude")
    private String longitude;


    @ApiModelProperty("latitude")
    private String latitude;


    @ApiModelProperty("altitude")
    private String altitude;


    @ApiModelProperty("camera_type")
    private String cameraType;


    @ApiModelProperty("camera_type_name")
    private String cameraTypeName;


    @ApiModelProperty("channel_type")
    private String channelType;


    @ApiModelProperty("channel_type_name")
    private String channelTypeName;


    @ApiModelProperty("region_index_code")
    private String regionIndexCode;


    @ApiModelProperty("sync_time")
    private String syncTime;


    @ApiModelProperty("create_time")
    private String createTime;


    @ApiModelProperty("creator_id")
    private Integer creatorId;


    @ApiModelProperty("upString_time")
    private String upStringTime;


    @ApiModelProperty("updator_id")
    private Integer updatorId;


    @ApiModelProperty("del_flag")
    private int delFlag;


    @ApiModelProperty("source")
    private Integer source;


    @ApiModelProperty("preview_url")
    private String previewUrl;


    @ApiModelProperty("status")
    private Integer status;


    @ApiModelProperty("area_code")
    private String areaCode;


    @ApiModelProperty("工程编码")
    private String projectCode;


    @ApiModelProperty("离线次数")
    private Integer outLineNumber;
}
