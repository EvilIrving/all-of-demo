package com.ygkj.project.model;

import java.io.Serializable;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author hucong
 * @description 检查管理
 * @date 2021-07-08
 */
@Data
public class ProjInspectionManagement implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    private String id;

    /**
     * 工程编码
     */
    private String projectCode;

    /**
     * 文件名称
     */
    private String fileName;

    /**
     * 文件地址，多个文件用逗号隔开
     */
    private String fileUrl;

    /**
     * 类型 0:质量体系 1:质量行为
     */
    private String type;

    /**
     * 上传时间
     */
    private Date uploadTime;

    /**
     * 上传人姓名
     */
    private String uploadName;

    /**
     * 上传人姓名
     */
    private String uploadId;

    /**
     * 备注
     */
    private String remark;

    /**
     * 审核状态 3:已驳回 2:未上报 1:待审核 0:已通过
     */
    private String status;

    /**
     * 删除标识
     */
    private String delFlag;

    /**
     * 驳回意见
     */
    private String dismissedSuggestion;

    public ProjInspectionManagement() {
    }

}