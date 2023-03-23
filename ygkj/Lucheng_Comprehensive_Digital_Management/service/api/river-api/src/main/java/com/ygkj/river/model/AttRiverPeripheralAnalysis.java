package com.ygkj.river.model;

import lombok.Data;

import java.io.Serializable;

/**
 * @author Huangzh
 * @description 河道周边分析关联对象表
 * @date 2021-10-11
 */
@Data
public class AttRiverPeripheralAnalysis implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    private Integer id;

    /**
     * 河段编号，关联到att_riv_seg_base表
     */
    private Integer riverSegId;

    /**
     * 河段名称
     */
    private String riverName;

    /**
     * 河道编号，用来和att_riv_shape_base表做关联
     */
    private String riverNo;

    /**
     * 距离，单位米
     */
    private Double distance;

    /**
     * 河段计算点经度
     */
    private String longitude;

    /**
     * 河段计算点纬度
     */
    private String latitude;

    /**
     * 类型：入河排污口/雨水口/取水口/工程类型/视频
     */
    private String type;

    /**
     * 具体对象的唯一编码
     */
    private String objectId;

    public AttRiverPeripheralAnalysis() {
    }

}

