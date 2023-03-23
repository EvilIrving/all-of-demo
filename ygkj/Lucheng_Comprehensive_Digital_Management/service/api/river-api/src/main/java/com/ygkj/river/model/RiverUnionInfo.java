package com.ygkj.river.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 河道关联项目表
 *
 * @author huangzh 2021-03-02
 */
@Data
public class RiverUnionInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    private String id;

    /**
     * 名称
     */
    private String name;

    /**
     * 类型：河道河景/雨水口/入河排污口...
     */
    private String type;

    /**
     * 行政区划名称
     */
    private String areaName;

    /**
     * 行政区划名称
     */
    private String areaCode;

    /**
     * 所属流域
     */
    private String basin;

    /**
     * 经度
     */
    private String longitude;

    /**
     * 纬度
     */
    private String latitude;

    /**
     * 描述
     */
    private String description;

    /**
     * 附件id
     */
    private String fileIds;

    private String riverId;

    private String riverName;

    private String browseUrl;

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

    public RiverUnionInfo() {
    }

}

