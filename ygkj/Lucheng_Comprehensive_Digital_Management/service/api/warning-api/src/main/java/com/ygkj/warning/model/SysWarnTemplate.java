package com.ygkj.warning.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 预警组对预警模板
 *
 * @author huangzh 2021-03-04
 */
@Data
public class SysWarnTemplate implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    private Long id;

    /**
     * 模板名称
     */
    private String name;

    /**
     * 模板内容
     */
    private String content;

    /**
     * 预警组id
     */
    private Integer groupId;

    /**
     * 审核人id
     */
    private String checkUserId;

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
     * 是否删除 0-未删除 1-已删除
     */
    private boolean delFlag;

    public SysWarnTemplate() {
    }

}

