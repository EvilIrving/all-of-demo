package com.ygkj.project.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author hucong
 * @description 项目划分表
 * @date 2021-07-08
 */
@Data
public class ProjectDivision implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    private String id;

    /**
     * 父id，当为父单位工程时，传-1
     */
    private String pid;

    /**
     * 单位工程名称
     */
    private String name;

    /**
     * 项目工程名称
     */
    private String projectCode;

    /**
     * 删除标识
     */
    private String delFlag;

    /**
     * 上传人id
     */
    private String uploadId;

    /**
     * 上传人姓名
     */
    private String uploadName;

    /**
     * 创建时间
     */
    private Date createTime;

    public ProjectDivision() {
    }

}