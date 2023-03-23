package com.ygkj.project.model;

import java.io.Serializable;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * @author Huangzh
 * @description 摄像头报警记录
 * @date 2021-09-10
 */
@Data
public class CameraWarnRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 摄像头编码
     */
    private String cameraIndexCode;

    /**
     * 区域摄像头哈希值
     */
    private Integer regionCameraHash;

    /**
     * 报警时刻
     */
    private Date warnTime;

    /**
     * 报警正文
     */
    private String content;

    /**
     * 运维人员电话
     */
    private String opsPhone;

    /**
     * 运维人员姓名
     */
    private String opsName;

    /**
     * 通知方式
     */
    private String notifyType;

    private String tableName = "camera_warn_record_" + LocalDate.now().getYear();

    public CameraWarnRecord() {
    }

}
