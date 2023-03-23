package com.ygkj.lcsyn.model;

import lombok.Data;

import java.util.Date;

@Data
public class BhLgPrjHideDanger {

    /**
     * id
     */
    private Integer id;

    /**
     * 简称
     */
    private String abbreviation;

    /**
     * 部位
     */
    private String position;

    /**
     * 描述及说明
     */
    private String description;

    /**
     * 发现时间
     */
    private Date foundtime;

    /**
     * 发现方式
     */
    private String foundmethod;

    /**
     * 照片
     */
    private String picaid;

    /**
     * 处置方式
     */
    private String disposalmethod;

    /**
     * 计划治理完成时间
     */
    private Date planCompTm;

    /**
     * 治理责任人
     */
    private String personliable;

    /**
     * 实际完成治理时间
     */
    private Date actlCompTm;

    /**
     * 治理后照片
     */
    private String afteraid;

    /**
     * 工程编号
     */
    private String pid;

    /**
     * 治理负责人电话
     */
    private String phone;

    /**
     * position_id
     */
    private Integer positionId;

    /**
     * 是否是设备隐患处理
     */
    private int isDevicePatrol;

    /**
     * uploaddanger上报状态
     */
    private int isUploadSuccess;

    /**
     * uploaddangeruploaddangerdealsituation报错
     */
    private String uploadErrormsg;

    /**
     * uploaddanger返回编码
     */
    private String uploadKey;

    /**
     * uploaddangerdealsituation上报状态
     */
    private int isUploadSuccessDeal;

    /**
     * 治理说明
     */
    private String manageDesc;

    /**
     * 是否上报
     */
    private int isUpload;

    /**
     * 上报时间
     */
    private Date uploadTm;
}
