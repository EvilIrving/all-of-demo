package com.ygkj.auth.api.achievement.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.ygkj.auth.api.department.model.SysDept;
import com.ygkj.auth.api.file.model.SysFile;
import com.ygkj.auth.api.user.model.SysUser;
import io.swagger.annotations.ApiModel;
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
@JsonIgnoreProperties(value = {"hibernateLazyInitializer", "handler"})
@ApiModel("归档记录实体类")
public class SysAchievement implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @ApiModelProperty("主键")
    private String id;

    /**
     * 归档名称
     */
    @ApiModelProperty("归档名称")
    private String name;

    /**
     * 父级id
     */
    @ApiModelProperty("父级id")
    private String parentId;

    /**
     * 附件ids
     */
    @ApiModelProperty("附件ids")
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

    private Double fileSize;

    private List<SysAchievement> children;

    private boolean modify = true;

    @ApiModelProperty("排序号")
    private Integer sort;
    @ApiModelProperty("部门ids")
    private String deportIds;
    @ApiModelProperty("用户ids")
    private String userIds;

    /**
     * 档案路径
     */
    @ApiModelProperty("档案路径")
    private String achiPath;

    /**
     * 管理员id
     */
    @ApiModelProperty("管理员id")
    private String adminId;

    /**
     * 管理员姓名
     */
    @ApiModelProperty("管理员姓名")
    private String adminName;

    /**
     * 状态 0:完成 1:空
     */
    @ApiModelProperty("状态 0:完成 1:空")
    private String status;

    /**
     * 是否允许为null 0: 允许 1:不允许
     */
    @ApiModelProperty("是否允许为null 0: 允许 1:不允许")
    private String allowNull;

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

    @ApiModelProperty("管理权限 0:没有 1:有")
    private String adminFlag = "0";

    @ApiModelProperty("工程编码")
    private String projectCode;

    public SysAchievement() {
    }

    public SysAchievement(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public SysAchievement(String id, String name, String parentId, String fileIds, List<SysFile> fileList
            , List<SysAchievement> children, boolean modify) {
        this.id = id;
        this.name = name;
        this.parentId = parentId;
        this.fileIds = fileIds;
        this.fileList = fileList;
        this.children = children;
        this.modify = modify;
    }

    public boolean hasChild() {
        return !children.isEmpty();
    }


}
