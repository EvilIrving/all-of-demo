package com.ygkj.project.vo.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@ApiModel("提防列表返回参数")
public class DikeProjectResp implements Serializable {

    @ApiModelProperty("提防名称")
    private String dikeName;

    @ApiModelProperty("工程名称")
    private String projectName;

    @ApiModelProperty("所在市")
    private String cityAdnm;

    @ApiModelProperty("所在县")
    private String countryAdnm;

    @ApiModelProperty("所在河流")
    private String bas;

    @ApiModelProperty("河道级别")
    private String riverLevel;

    @ApiModelProperty("岸别")
    private String rvBank;

    @ApiModelProperty("堤防类型")
    private String dikeType;

    @ApiModelProperty("堤防长(m)")
    private String dikeLen;

    @ApiModelProperty("标绘长(m)")
    private String flStaLen;

    @ApiModelProperty("防洪标准(年)")
    private String planFlSta;

    @ApiModelProperty("堤防规模")
    private String dikeGrad;

    @ApiModelProperty("管理层级")
    private String managementLevel;

    @ApiModelProperty("经度")
    private String longitude;

    @ApiModelProperty("纬度")
    private String latitude;


    /**
     * 堤防代码
     */
    @ApiModelProperty("堤防代码")
    private String dikeCode;

    /**
     * 起点经度
     */
    @ApiModelProperty("起点经度")
    private BigDecimal startLong;

    /**
     * 起点维度
     */
    @ApiModelProperty("起点维度")
    private BigDecimal startLat;

    /**
     * 终点经度
     */
    @ApiModelProperty("终点经度")
    private BigDecimal endLong;

    /**
     * 终点维度
     */
    @ApiModelProperty("终点维度")
    private BigDecimal endLat;

    /**
     * 堤防中心点经度
     */
    @ApiModelProperty("堤防中心点经度")
    private BigDecimal dikestartlongitude;

    /**
     * 堤防中心点维度
     */
    @ApiModelProperty("堤防中心点维度")
    private BigDecimal dikestartlatitude;

    /**
     * 起点所在位置
     */
    @ApiModelProperty("起点所在位置")
    private String startLoc;

    /**
     * 终点所在位置
     */
    @ApiModelProperty("终点所在位置")
    private String endLoc;

    /**
     * 堤防形式
     */
    @ApiModelProperty("堤防形式")
    private String dikePatt;

    /**
     * 堤防起点桩号
     */
    @ApiModelProperty("堤防起点桩号")
    private String dikeStartNum;

    /**
     * 堤防终点桩号
     */
    @ApiModelProperty("堤防终点桩号")
    private String dikeEndNum;

    /**
     * 高程系统
     */
    @ApiModelProperty("高程系统")
    private String elevSys;

    /**
     * 起点顶点高程
     */
    @ApiModelProperty("起点顶点高程")
    private BigDecimal startDikeTopEl;

    /**
     * 终点顶点高程
     */
    @ApiModelProperty("终点顶点高程")
    private BigDecimal endDikeTopEl;

    /**
     * 堤顶最小宽度
     */
    @ApiModelProperty("堤顶最小宽度")
    private BigDecimal dikeTopWidMin;

    /**
     * 堤顶最大宽度
     */
    @ApiModelProperty("堤顶最大宽度")
    private BigDecimal dikeTopWidMax;

    /**
     * 堤防最低高程
     */
    @ApiModelProperty("堤防最低高程")
    private BigDecimal dikeLowEl;

    /**
     * 工程任务
     */
    @ApiModelProperty("工程任务")
    private String endTask;

    /**
     * 工程建设情况
     */
    @ApiModelProperty("工程建设情况")
    private String endStar;

    /**
     * 开工时间
     */
    @ApiModelProperty("开工时间")
    private Date startDate;

    /**
     * 竣工验收时间
     */
    @ApiModelProperty("竣工验收时间")
    private Date compDate;

    /**
     * 归口管理部门
     */
    @ApiModelProperty("归口管理部门")
    private String admDep;

    /**
     * 堤防跨界情况
     */
    @ApiModelProperty("堤防跨界情况")
    private String dikeCorBoun;

    /**
     * 设计水（高潮）位 (m)
     */
    @ApiModelProperty("设计水（高潮）位 (m)")
    private BigDecimal desStag;

    /**
     * 堤防高度(m)
     */
    @ApiModelProperty("堤防高度(m)")
    private BigDecimal dikeHigMax;

    /**
     * 堤防高度(m)
     */
    @ApiModelProperty("堤防高度(m)")
    private BigDecimal dikeHigMin;

    /**
     * 一般堤高
     */
    @ApiModelProperty("一般堤高")
    private String avgcrestelevation;

    /**
     * 堤防结构
     */
    @ApiModelProperty("堤防结构")
    private String dikestruct;

    /**
     * 空间坐标
     */
    @ApiModelProperty("空间坐标")
    private String geom;

    /**
     * 完工时间（非加固）
     */
    @ApiModelProperty("完工时间（非加固）")
    private String finshTime;

    /**
     * 参照水位站警戒水位(米)
     */
    @ApiModelProperty("参照水位站警戒水位(米)")
    private BigDecimal warnWaterLevel;

    /**
     * 参照水位站危急水位(米)
     */
    @ApiModelProperty("参照水位站危急水位(米)")
    private BigDecimal dangerWaterLevel;

    /**
     * 薄弱堤段名称
     */
    @ApiModelProperty("薄弱堤段名称")
    private String weakDikeName;

    /**
     * 薄弱堤段起始经度
     */
    @ApiModelProperty("薄弱堤段起始经度")
    private BigDecimal weakDikeStartLongitude;

    /**
     * 薄弱堤段起始纬度(度)
     */
    @ApiModelProperty("薄弱堤段起始纬度(度)")
    private BigDecimal weakDikeStartLatitude;

    /**
     * 薄弱堤段终止经度(度)
     */
    @ApiModelProperty("薄弱堤段终止经度(度)")
    private BigDecimal weakDikeEndLongitude;

    /**
     * 薄弱堤段终止纬度(度)
     */
    @ApiModelProperty("薄弱堤段终止纬度(度)")
    private BigDecimal weakDikeEndLatitude;

    /**
     * 薄弱情况描述
     */
    @ApiModelProperty("薄弱情况描述")
    private String weakSituationDescription;

    /**
     * 水闸数量(孔)
     */
    @ApiModelProperty("水闸数量(孔)")
    private Integer sluiceCount;

    /**
     * 泵站数量(处)
     */
    @ApiModelProperty("泵站数量(处)")
    private Integer pumpCount;

    /**
     * 参照水位站编码
     */
    @ApiModelProperty("参照水位站编码")
    private String relativeStationCode;

    /**
     * 参照水位站名称
     */
    @ApiModelProperty("参照水位站名称")
    private String relativeStationName;

    /**
     * 是否五水共治任务
     */
    @ApiModelProperty("是否五水共治任务")
    private String isWsgz;

    /**
     * 是否竣工验收（非加固）
     */
    @ApiModelProperty("是否竣工验收（非加固）")
    private String isComplete;

    /**
     * 保护范围（平方公里）
     */
    @ApiModelProperty("保护范围（平方公里）")
    private BigDecimal protectionArea;

    /**
     * 中间堤顶高程
     */
    @ApiModelProperty("中间堤顶高程")
    private BigDecimal middleElevation;

    /**
     * 涵管涵洞数量
     */
    @ApiModelProperty("涵管涵洞数量")
    private String culvertCount;

    /**
     * 其他穿堤建筑物数量
     */
    @ApiModelProperty("其他穿堤建筑物数量")
    private String otherCrossBuildCount;

    /**
     * 道口数量
     */
    @ApiModelProperty("道口数量")
    private String levelCrossingCount;

    /**
     * 其他涉堤建筑物数量
     */
    @ApiModelProperty("其他涉堤建筑物数量")
    private String otherBuildingCount;

    /**
     * 备注
     */
    @ApiModelProperty("备注")
    private String note;

    /**
     * 记录生效时间
     */
    @ApiModelProperty("记录生效时间")
    private Date effDate;

    /**
     * 记录失效时间
     */
    @ApiModelProperty("记录失效时间")
    private Date exprDate;

    /**
     * 行政区划代码
     */
    @ApiModelProperty("行政区划代码")
    private String areaCode;

    /**
     * 政府负责人用户id，关联到用户体系
     */
    @ApiModelProperty("政府负责人用户id，关联到用户体系")
    private String govermentChargeId;

    /**
     * 水行政部门负责人用户id，关联到用户体系
     */
    @ApiModelProperty("水行政部门负责人用户id，关联到用户体系")
    private String waterDepartmentChargeId;

    /**
     * 主管部门负责人id，关联到用户体系
     */
    @ApiModelProperty("主管部门负责人id，关联到用户体系")
    private String competentDepartmentChargeId;

    /**
     * 管理单位责任人id，关联到用户体系
     */
    @ApiModelProperty("管理单位责任人id，关联到用户体系")
    private String manageDepartmentChargeId;

    /**
     * points
     */
    @ApiModelProperty("points")
    private String points;

    /**
     * 技术负责人
     */
    @ApiModelProperty("技术负责人")
    private String technologyChargeId;

    /**
     * 巡查责任人
     */
    @ApiModelProperty("巡查责任人")
    private String patrolChargeId;

    /**
     * 堤防线
     */
    private String coorpot;

}
