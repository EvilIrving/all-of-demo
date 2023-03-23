package com.ygkj.gragh.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author Huangzh
 * @description hikvision_camera_record
 * @date 2020-12-10
 */
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

    private String projectCode;

    private String technologyChargeId;

    private Integer outLineNumber;


    public HikvisionCameraRecord() {
    }

    public Integer getOutLineNumber() {
        return outLineNumber;
    }

    public void setOutLineNumber(Integer outLineNumber) {
        this.outLineNumber = outLineNumber;
    }

    public String getTechnologyChargeId() {
        return technologyChargeId;
    }

    public void setTechnologyChargeId(String technologyChargeId) {
        this.technologyChargeId = technologyChargeId;
    }

    public String getProjectCode() {
        return projectCode;
    }

    public void setProjectCode(String projectCode) {
        this.projectCode = projectCode;
    }

    public String getCameraIndexCode() {
        return cameraIndexCode;
    }

    public void setCameraIndexCode(String cameraIndexCode) {
        this.cameraIndexCode = cameraIndexCode;
    }

    public String getCameraName() {
        return cameraName;
    }

    public void setCameraName(String cameraName) {
        this.cameraName = cameraName;
    }

    public String getDeviceIndexCode() {
        return deviceIndexCode;
    }

    public void setDeviceIndexCode(String deviceIndexCode) {
        this.deviceIndexCode = deviceIndexCode;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getAltitude() {
        return altitude;
    }

    public void setAltitude(String altitude) {
        this.altitude = altitude;
    }

    public String getCameraType() {
        return cameraType;
    }

    public void setCameraType(String cameraType) {
        this.cameraType = cameraType;
    }

    public String getCameraTypeName() {
        return cameraTypeName;
    }

    public void setCameraTypeName(String cameraTypeName) {
        this.cameraTypeName = cameraTypeName;
    }

    public String getChannelType() {
        return channelType;
    }

    public void setChannelType(String channelType) {
        this.channelType = channelType;
    }

    public String getChannelTypeName() {
        return channelTypeName;
    }

    public void setChannelTypeName(String channelTypeName) {
        this.channelTypeName = channelTypeName;
    }

    public String getRegionIndexCode() {
        return regionIndexCode;
    }

    public void setRegionIndexCode(String regionIndexCode) {
        this.regionIndexCode = regionIndexCode;
    }

    public Date getSyncTime() {
        return syncTime;
    }

    public void setSyncTime(Date syncTime) {
        this.syncTime = syncTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Integer creatorId) {
        this.creatorId = creatorId;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getUpdatorId() {
        return updatorId;
    }

    public void setUpdatorId(Integer updatorId) {
        this.updatorId = updatorId;
    }

    public boolean getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(boolean delFlag) {
        this.delFlag = delFlag;
    }

    public Integer getSource() {
        return source;
    }

    public void setSource(Integer source) {
        this.source = source;
    }

    public String getPreviewUrl() {
        return previewUrl;
    }

    public void setPreviewUrl(String previewUrl) {
        this.previewUrl = previewUrl;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

}

