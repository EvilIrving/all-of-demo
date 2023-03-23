package com.ygkj.project.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author hucong
 * @description 变更管理表
 * @date 2021-07-08
 */
@Data
public class ProjChangeManagement implements Serializable {

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
     * 文件名称/人员名称
     */
    private String name;

    /**
     * 附件
     */
    private String fileUrl;

    /**
     * 上传时间
     */
    private Date uploadTime;

    /**
     * 上传人员id
     */
    private String uploadId;

    /**
     * 上传人员姓名
     */
    private String uploadName;

    /**
     * 备注
     */
    private String remark;

    /**
     * 删除标识
     */
    private String delFlag;

    /**
     * 类型 0: 合同变更 1: 人员变更
     */
    private String type;

    public ProjChangeManagement() {
    }

}