package com.ygkj.entity;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.util.Date;
import java.util.List;

/**
 * @description 文档权限表
 * @author hucong
 * @date 2021-01-20
 */
@Data
public class SysAchievementJurisdiction implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    private String id;

    /**
     * 文档id
     */
    private String achievementId;

    /**
     * 部门单位ids
     */
    private String deportIds;

    /**
     * 人员ids
     */
    private String userIds;

    /**
     * 创建人
     */
    private String createId;

    /**
     * 更新人
     */
    private String updateId;
    /**
     * 权限类型 1:查看权限 2:上传权限 3:删除权限 4:下载权限 5:更新权限
     */
    private String type;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    private List<com.ygkj.auth.api.user.model.SysUser> userList;

    private List<com.ygkj.auth.api.department.model.SysDept> deptList;

    public SysAchievementJurisdiction() {
    }

}

