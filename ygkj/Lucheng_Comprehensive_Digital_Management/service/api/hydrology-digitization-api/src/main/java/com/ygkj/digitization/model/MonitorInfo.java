package com.ygkj.digitization.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author xq
 * @Description 排班管理表，一条id或name字段值包含多个id或name值，并用逗号（，）分割。务必保证id和name数据的顺序。
 * @Date 2021/1/14
 */
public class MonitorInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 表主键，自动增长
     */
    private Integer id;

    /**
     * 排班日期
     */
    private Date monitorDate;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getMonitorDate() {
        return monitorDate;
    }

    public void setMonitorDate(Date monitorDate) {
        this.monitorDate = monitorDate;
    }

    public String getDbldId() {
        return dbldId;
    }

    public void setDbldId(String dbldId) {
        this.dbldId = dbldId;
    }

    public String getDbldName() {
        return dbldName;
    }

    public void setDbldName(String dbldName) {
        this.dbldName = dbldName;
    }

    public String getZbryId() {
        return zbryId;
    }

    public void setZbryId(String zbryId) {
        this.zbryId = zbryId;
    }

    public String getZbryName() {
        return zbryName;
    }

    public void setZbryName(String zbryName) {
        this.zbryName = zbryName;
    }

    public String getFbryId() {
        return fbryId;
    }

    public void setFbryId(String fbryId) {
        this.fbryId = fbryId;
    }

    public String getFbryName() {
        return fbryName;
    }

    public void setFbryName(String fbryName) {
        this.fbryName = fbryName;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getCreateId() {
        return createId;
    }

    public void setCreateId(String createId) {
        this.createId = createId;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getUpdateId() {
        return updateId;
    }

    public void setUpdateId(String updateId) {
        this.updateId = updateId;
    }

    public Integer getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(Integer delFlag) {
        this.delFlag = delFlag;
    }

    /**
     * 带班领导id
     */
    private String dbldId;

    /**
     * 带班领导name
     */
    private String dbldName;

    /**
     * 值班人员id
     */
    private String zbryId;

    /**
     * 值班人员name
     */
    private String zbryName;

    /**
     * 副班人员id
     */
    private String fbryId;

    /**
     * 副班人员name
     */
    private String fbryName;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 创建人
     */
    private String createId;

    /**
     * 修改时间
     */
    private Date updateTime;

    /**
     * 修改人
     */
    private String updateId;

    /**
     * 是否删除 0-未删除 1-已删除
     */
    private Integer delFlag;
}
