package com.ygkj.project.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author xq
 * @description 摄像头在线记录表
 * @date 2021-04-13
 */
@Data
public class AttCameraOnline implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 资源唯一编码
     */
    private String indexCode;

    /**
     * 设备型号
     */
    private String deviceType;

    /**
     * 区域编码
     */
    private String regionIndexCode;

    /**
     * 采集时间
     */
    private Date collectTime;

    /**
     * 设备唯一编码
     */
    private String deviceIndexCode;

    /**
     * ip地址，监控点无此值
     */
    private String port;

    /**
     * 端口，监控点无此值
     */
    private Integer ip;

    /**
     * 区域名字
     */
    private String regionName;

    /**
     * 在线状态，0离线，1在线
     */
    private Integer online;

    /**
     * 设备名称
     */
    private String cn;

    /**
     * 协议类型， 海康私有协议： hiksdk_net， gb/t28181： gb_reg， ehome协议： ehome_reg， 大华私有协议： dhsdk_net， onvif协议： onvif_net， 萤石协议： ezviz_net， 级联： cascade
     */
    private String treatyType;

    /**
     * 厂商，hikvision-海康，dahua-大华
     */
    private String manufacturer;

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

    /**
     * 部门id
     */
    private String deptId;

    /**
     * 同步日期
     */
    private Date syncDate;

    /**
     * 区域树和摄像头的哈希值
     */
    private Integer regionCameraHash;

    public AttCameraOnline() {
    }

}
