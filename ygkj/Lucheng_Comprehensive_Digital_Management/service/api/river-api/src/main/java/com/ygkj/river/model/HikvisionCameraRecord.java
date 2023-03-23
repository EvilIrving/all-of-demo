package com.ygkj.river.model;

import java.io.Serializable;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author xq
 * @description hikvision_camera_record
 * @date 2021-04-08
 */
@Data
public class HikvisionCameraRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * camera_index_code
     */
    private String cameraIndexCode;

    /**
     * camera_name
     */
    private String cameraName;

    /**
     * device_index_code
     */
    private String deviceIndexCode;

    /**
     * longitude
     */
    private String longitude;

    /**
     * latitude
     */
    private String latitude;

    /**
     * altitude
     */
    private String altitude;

    /**
     * camera_type
     */
    private String cameraType;

    /**
     * camera_type_name
     */
    private String cameraTypeName;

    /**
     * channel_type
     */
    private String channelType;

    /**
     * channel_type_name
     */
    private String channelTypeName;

    /**
     * region_index_code
     */
    private String regionIndexCode;

    /**
     * sync_time
     */
    private Date syncTime;

    /**
     * create_time
     */
    private Date createTime;

    /**
     * creator_id
     */
    private Integer creatorId;

    /**
     * update_time
     */
    private Date updateTime;

    /**
     * updator_id
     */
    private Integer updatorId;

    /**
     * del_flag
     */
    private boolean delFlag;

    /**
     * source
     */
    private Integer source;

    /**
     * preview_url
     */
    private String previewUrl;

    /**
     * status
     */
    private Integer status;

    /**
     * area_code
     */
    private String areaCode;

    /**
     * 工程编码
     */
    private String projectCode;

    /**
     * 离线次数
     */
    private Integer outLineNumber;

    public HikvisionCameraRecord() {
    }

}
