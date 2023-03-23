package com.ygkj.river.vo.response;

import lombok.Data;

@Data
public class RiverUnionInfoResVo {

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
    private String areaCode;

    /**
     * 行政区划名称
     */
    private String areaName;

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

    private String riverName;

    private String browseUrl;
}
