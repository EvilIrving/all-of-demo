package com.ygkj.project.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 报警记录表
 *
 * @author huangzh 2020-12-30
 */
@Data
public class WarningRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id，统一采用雪花算法策略
     */
    private String id;

    /**
     * 报警时间
     */
    private Date warningTime;

    /**
     * 配置id
     */
    private String configId;

    /**
     * 创建人
     */
    private String createdBy;

    /**
     * 创建时间
     */
    private Date createdTime;

    /**
     * 更新人
     */
    private String updatedBy;

    /**
     * 更新时间
     */
    private Date updatedTime;

    /**
     * 删除标记
     */
    private boolean delFlag;

    public WarningRecord() {
    }

}

