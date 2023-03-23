package com.ygkj.project.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author xq
 * @description 摄像头区域表
 * @date 2021-04-13
 */
@Data
public class AttCameraRegion implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 区域编号
     */
    private String indexCode;

    /**
     * 区域完整路径，含本节点，@进行分割，上级节点在前
     */
    private String regionPath;

    /**
     * 区域标识 0：本级 1：级联 2：混合，下级推送给上级的本级点（杭州本级有滨江，然后下级滨江又把自己推送上来了，滨江是混合区域节点） 入参cascadeflag与返回值对应： cascadeflag=0：返回0、1、2 cascadeflag=1：返回0、2 cascadeflag=2：返回1、2
     */
    private String cascadeType;

    /**
     * 区域类型， 国标区域：0， 雪亮工程区域：1， 司法行政区域：2， 自定义区域：9， 历史兼容版本占用普通区域:10， 历史兼容版本占用级联区域:11， 楼栋单元:12
     */
    private String catalogType;

    /**
     * 级联平台标识，多个级联编号以@分隔，本级区域默认值“0”
     */
    private String cascadeCode;

    /**
     * 用于标识区域节点是否有权限操作，true：有权限 false：无权限
     */
    private String available;

    /**
     * 创建时间，要求遵守iso8601标准，如2018-07-26t21:30:08.32208:00
     */
    private String updateTime;

    /**
     * 同级区域顺序
     */
    private String sort;

    /**
     * node_type
     */
    private String nodeType;

    /**
     * true:是叶子节点，表示该区域下面未挂区域 false:不是叶子节点，表示该区域下面挂有区域
     */
    private String leaf;

    /**
     * 区域类型，10-普通区域，11-级联区域，12-楼栋单元
     */
    private String regionCode;

    /**
     * 创建时间，要求遵守iso8601标准，如2018-07-26t21:30:08.32208:00
     */
    private String createTime;

    /**
     * 区域名称
     */
    private String name;

    /**
     * 父区域唯一标识码
     */
    private String parentIndexCode;

    /**
     * 外码(如：国际码)
     */
    private String externalIndexCode;

    /**
     * 同步时间
     */
    private Date syncTime;

    public AttCameraRegion() {
    }

}
