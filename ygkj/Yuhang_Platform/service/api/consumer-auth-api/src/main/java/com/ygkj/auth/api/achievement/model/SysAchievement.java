package com.ygkj.auth.api.achievement.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.ygkj.auth.api.department.model.SysDept;
import com.ygkj.auth.api.file.model.SysFile;
import com.ygkj.auth.api.user.model.SysUser;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author Huang.zh
 * @date 2020/9/15 16:18
 * @Description: 归档记录实体类
 */
@Data
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler" })
public class SysAchievement implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    private String id;

    /**
     * 归档名称
     */
    private String name;

    /**
     * 父级id
     */
    private String parentId;

    /**
     * 附件id
     */
    private String fileIds;

    /**
     * 是否删除(1已删除；0未删除)
     */
    private boolean deleted;

    /**
     * 创建人
     */
    private String createId;

    /**
     * 更新人
     */
    private String updateId;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * update_time
     */
    private Date updateTime;

    private List<SysFile> fileList;

    private String fileStr;

    private List<SysAchievement> children;

    private boolean modify = true;

    private Integer sort;
    private String deportIds;
    private String userIds;

    private List<SysUser> userList;
    private List<SysDept> deptList;
    List<SysAchievementJurisdiction> jurisdictionListction;

    @ApiModelProperty("标签ids")
    private String labelIds;

    @ApiModelProperty("查看权限 0:没有 1:有")
    private String lookFlag = "0";

    @ApiModelProperty("上次权限 0:没有 1:有")
    private String uploadFlag = "0";

    @ApiModelProperty("删除权限 0:没有 1:有")
    private String deleteFlag = "0";

    @ApiModelProperty("删除权限 0:没有 1:有")
    private String downloadFlag = "0";

    public SysAchievement() {
    }

    public SysAchievement(String id,String name) {
        this.id = id;
        this.name = name;
    }

    public SysAchievement(String id, String name, String parentId, String fileIds, List<SysFile> fileList
            , List<SysAchievement> children,boolean modify) {
        this.id = id;
        this.name = name;
        this.parentId = parentId;
        this.fileIds = fileIds;
        this.fileList = fileList;
        this.children = children;
        this.modify = modify;
    }

    public boolean hasChild(){
        return !children.isEmpty();
    }



}
