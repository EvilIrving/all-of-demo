package com.ygkj.project.vo.response;

import com.ygkj.project.model.HikvisionCameraRecord;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author xq
 * @Description
 * @Date 2021/4/14
 */
@Data
public class HikCameraResVo extends HikvisionCameraRecord {

    private static final long serialVersionUID = 8810783812414694628L;
    @ApiModelProperty("所属工程名称")
    private String projectName;

    @ApiModelProperty("所属工程类型")
    private String projectType;

    @ApiModelProperty("工程等别")
    private String engGrad;

    @ApiModelProperty("工程级别")
    private String engScal;

    @ApiModelProperty("是否标注地点")
    private boolean locateMarked = false;

    @ApiModelProperty("离线时长，单位时")
    private Double offlineTime = 0.0d;

    @ApiModelProperty("在线时长，单位时")
    private Double onlineTime = 0.0d;

    @ApiModelProperty("最新在线离线状态持续时长，单位时")
    private Integer statusDuration = 0;

    @ApiModelProperty("行政区划名称")
    private String adnm;

    /**
     * 运维人员名称
     */
    private String opsName;

    /**
     * 运维人员电话
     */
    private String opsPhone;

    /**
     * 通知方式：站内信，浙政钉，短信
     */
    private String notifyType;

    @Override
    public String toString() {
        return "HikCameraResVo{" +
                "projectName='" + projectName + '\'' +
                ", projectType='" + projectType + '\'' +
                ", engGrad='" + engGrad + '\'' +
                ", engScal='" + engScal + '\'' +
                ", locateMarked=" + locateMarked + '\'' +
                ", areaCode=" + super.getAreaCode() +
                '}';
    }
}
