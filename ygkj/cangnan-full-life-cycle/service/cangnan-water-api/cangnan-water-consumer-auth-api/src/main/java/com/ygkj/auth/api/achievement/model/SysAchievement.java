package com.ygkj.auth.api.achievement.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.ygkj.auth.api.file.model.SysFile;
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

    private List<SysAchievement> children;

    private boolean modify = true;

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
