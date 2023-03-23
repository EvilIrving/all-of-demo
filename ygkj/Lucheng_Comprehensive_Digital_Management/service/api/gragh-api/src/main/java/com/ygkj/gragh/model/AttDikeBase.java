package com.ygkj.gragh.model;

import java.io.Serializable;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author Huangzh
 * @description 堤防基础信息表
 * @date 2020-12-09
 */
@Data
public class AttDikeBase implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 堤防代码
     */
    private String dikeCode;

    /**
     * 堤防名称
     */
    private String dikeName;

    /**
     * 起点经度
     */
    private BigDecimal startLong;

    /**
     * 起点维度
     */
    private BigDecimal startLat;

    /**
     * 终点经度
     */
    private BigDecimal endLong;

    /**
     * 终点维度
     */
    private BigDecimal endLat;

    /**
     * 堤防中心点经度
     */
    private BigDecimal dikestartlongitude;

    /**
     * 堤防中心点维度
     */
    private BigDecimal dikestartlatitude;

    /**
     * 起点所在位置
     */
    private String startLoc;

    /**
     * 终点所在位置
     */
    private String endLoc;

    /**
     * 堤防级别
     */
    private String dikeGrad;

    /**
     * 堤防类型
     */
    private String dikeType;

    /**
     * 堤防形式
     */
    private String dikePatt;

    /**
     * 堤防长度
     */
    private Integer dikeLen;

    /**
     * 堤防起点桩号
     */
    private String dikeStartNum;

    /**
     * 堤防终点桩号
     */
    private String dikeEndNum;

    /**
     * 高程系统
     */
    private String elevSys;

    /**
     * 起点顶点高程
     */
    private BigDecimal startDikeTopEl;

    /**
     * 终点顶点高程
     */
    private BigDecimal endDikeTopEl;

    /**
     * 堤顶最小宽度
     */
    private BigDecimal dikeTopWidMin;

    /**
     * 堤顶最大宽度
     */
    private BigDecimal dikeTopWidMax;

    /**
     * 堤防最低高程
     */
    private BigDecimal dikeLowEl;

    /**
     * 工程任务
     */
    private String endTask;

    /**
     * 工程建设情况
     */
    private String endStar;

    /**
     * 开工时间
     */
    private Date startDate;

    /**
     * 竣工验收时间
     */
    private Date compDate;

    /**
     * 归口管理部门
     */
    private String admDep;

    /**
     * 河流岸别
     */
    private String rvBank;

    /**
     * 堤防跨界情况
     */
    private String dikeCorBoun;

    /**
     * 规划防洪(潮)
     */
    private String planFlSta;

    /**
     * 达到规划防洪（潮）标准的长度 (m)
     */
    private BigDecimal flStaLen;

    /**
     * 设计水（高潮）位 (m)
     */
    private BigDecimal desStag;

    /**
     * 堤防高度(m)
     */
    private BigDecimal dikeHigMax;

    /**
     * 堤防高度(m)
     */
    private BigDecimal dikeHigMin;

    /**
     * 一般堤高
     */
    private String avgcrestelevation;

    /**
     * 堤防结构
     */
    private String dikestruct;

    /**
     * 空间坐标
     */
    private String geom;

    /**
     * 完工时间（非加固）
     */
    private String finshTime;

    /**
     * 参照水位站警戒水位(米)
     */
    private BigDecimal warnWaterLevel;

    /**
     * 参照水位站危急水位(米)
     */
    private BigDecimal dangerWaterLevel;

    /**
     * 薄弱堤段名称
     */
    private String weakDikeName;

    /**
     * 薄弱堤段起始经度
     */
    private BigDecimal weakDikeStartLongitude;

    /**
     * 薄弱堤段起始纬度(度)
     */
    private BigDecimal weakDikeStartLatitude;

    /**
     * 薄弱堤段终止经度(度)
     */
    private BigDecimal weakDikeEndLongitude;

    /**
     * 薄弱堤段终止纬度(度)
     */
    private BigDecimal weakDikeEndLatitude;

    /**
     * 薄弱情况描述
     */
    private String weakSituationDescription;

    /**
     * 水闸数量(孔)
     */
    private Integer sluiceCount;

    /**
     * 泵站数量(处)
     */
    private Integer pumpCount;

    /**
     * 参照水位站编码
     */
    private String relativeStationCode;

    /**
     * 参照水位站名称
     */
    private String relativeStationName;

    /**
     * 所在河道等级
     */
    private String riverLevel;

    /**
     * 是否五水共治任务
     */
    private String isWsgz;

    /**
     * 是否竣工验收（非加固）
     */
    private String isComplete;

    /**
     * 保护范围（平方公里）
     */
    private BigDecimal protectionArea;

    /**
     * 中间堤顶高程
     */
    private BigDecimal middleElevation;

    /**
     * 涵管涵洞数量
     */
    private String culvertCount;

    /**
     * 其他穿堤建筑物数量
     */
    private String otherCrossBuildCount;

    /**
     * 道口数量
     */
    private String levelCrossingCount;

    /**
     * 其他涉堤建筑物数量
     */
    private String otherBuildingCount;

    /**
     * 备注
     */
    private String note;

    /**
     * 记录生效时间
     */
    private Date effDate;

    /**
     * 记录失效时间
     */
    private Date exprDate;

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
     * 线
     */
    private String points;

    /**
     * 技术责任人id，关联到用户体系
     */
    private String technologyChargeId;
    /**
     * 巡查责任人id，关联到用户体系
     */
    private String patrolChargeId;

    /**
     * 堤防线
     */
    private String coorpot;

    public AttDikeBase() {
    }

}
