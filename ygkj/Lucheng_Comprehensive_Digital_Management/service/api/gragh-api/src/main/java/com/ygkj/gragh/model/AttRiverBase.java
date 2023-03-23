package com.ygkj.gragh.model;

import com.alibaba.fastjson.JSONArray;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Map;

/**
 * @author Huangzh
 * @description 河道基础信息表
 * @date 2020-12-11
 */
@Data
public class AttRiverBase implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    private String id;

    /**
     * 河道名称
     */
    private String riverName;

    /**
     * 河道等级
     */
    private String riverLevel;

    /**
     * 河道长度
     */
    private BigDecimal riverLength;

    /**
     * 河道水域面积
     */
    private BigDecimal riverArea;

    /**
     * 所属流域
     */
//    private String watershed;

    private String basin;

    /**
     * 所在平原
     */
    private String mountainPlains;

    /**
     * 所在行政区划名称
     */
    private String distrinctName;

    /**
     * 所在行政区划代码
     */
    private String distrinctAdcd;

    /**
     * 水域年份
     */
    private String cxsd;

    /**
     * 起点
     */
    private String upperBoundPosition;

    /**
     * 止点
     */
    private String lowerBoundPosition;

    /**
     * 上界集水面积
     */
    private String upperDrainageArea;

    /**
     * 下界集水面积
     */
    private String lowerDrainageArea;

    /**
     * 支干流
     */
    private String branchType;

    /**
     * 河段数量
     */
    private Integer riverSegmentAmount;

    /**
     * 定位中心点经度
     */
    private Integer centerLongitude;

    /**
     * 定位中心点纬度
     */
    private Integer centerLatitude;

    /**
     * 起点坐标
     */
    private String startCoordinates;

    /**
     * 中点坐标
     */
    private String endCoordinates;

    /**
     * 几何图形
     */
    private String geom;

    /**
     * 中点坐标
     */
    private String centerCoordinates;

    /**
     * 河道排序字段
     */
    private Integer sort;

    /**
     * 河道行政级别（市级/县级）
     */
    private String policy;

    /**
     * 行政区划代码
     */
    private String areaCode;

    /**
     * 政府负责人用户id，关联到用户体系
     */
    private String govermentChargeId;

    /**
     * 水行政部门负责人用户id，关联到用户体系
     */
    private String waterDepartmentChargeId;

    /**
     * 主管部门负责人id，关联到用户体系
     */
    private String competentDepartmentChargeId;

    /**
     * 管理单位责任人id，关联到用户体系
     */
    private String manageDepartmentChargeId;

    /**
     * 技术责任人id，关联到用户体系
     */
    private String technologyChargeId;
    /**
     * 巡查责任人id，关联到用户体系
     */
    private String patrolChargeId;

    private JSONArray charges;

    public AttRiverBase() {
    }

}

