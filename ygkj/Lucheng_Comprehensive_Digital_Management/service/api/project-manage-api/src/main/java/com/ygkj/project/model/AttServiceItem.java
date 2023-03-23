package com.ygkj.project.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @author lxl
 * @create 2022-04-12 10:37
 * @description
 */
@Data
public class AttServiceItem {


    private static final long serialVersionUID = 1L;

    /**
     * 服务事项主键
     */
    private String id;

    /**
     * 所属工程id
     */
    private String projectId;

    /**
     * 工程名称（冗余字段，减少联表）
     */
    private String projectName;

    /**
     * 工程类型
     */
    private String projectType;

    /**
     * 物业化公司id
     */
    private String companyId;

    /**
     * 物业公司名称（冗余字段，减少联表）
     */
    private String companyName;

    /**
     * 服务事项
     */
    private String projectItem;

    /**
     * 服务开始时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date serviceStartTime;

    /**
     * 服务结束时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date serviceEndTime;

    /**
     * 删除标记 0未删除 1已删除
     */
    private boolean delFlag;

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
     * 部门id
     */
    private String deptId;

    public AttServiceItem() {
    }

}
