package com.ygkj.big.screen.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author fml
 * @date 2021-03-29 15:16
 * @description 堤防基础信息表
 */
@Data
@ApiModel("堤防基础信息表 ")
public class AttDikeBase implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("堤防代码")
    private String dikeCode;


    @ApiModelProperty("堤防名称")
    private String dikeName;


    @ApiModelProperty("起点经度")
    private Double startLong;


    @ApiModelProperty("起点维度")
    private Double startLat;


    @ApiModelProperty("终点经度")
    private Double endLong;


    @ApiModelProperty("终点维度")
    private Double endLat;


    @ApiModelProperty("堤防中心点经度")
    private Double dikestartlongitude;


    @ApiModelProperty("堤防中心点维度")
    private Double dikestartlatitude;


    @ApiModelProperty("起点所在位置")
    private String startLoc;


    @ApiModelProperty("终点所在位置")
    private String endLoc;


    @ApiModelProperty("堤防级别")
    private String dikeGrad;


    @ApiModelProperty("堤防类型")
    private String dikeType;


    @ApiModelProperty("堤防形式")
    private String dikePatt;


    @ApiModelProperty("堤防长度")
    private Integer dikeLen;


    @ApiModelProperty("堤防起点桩号")
    private String dikeStartNum;


    @ApiModelProperty("堤防终点桩号")
    private String dikeEndNum;


    @ApiModelProperty("高程系统")
    private String elevSys;


    @ApiModelProperty("起点顶点高程")
    private Double startDikeTopEl;


    @ApiModelProperty("终点顶点高程")
    private Double endDikeTopEl;


    @ApiModelProperty("堤顶最小宽度")
    private Double dikeTopWidMin;


    @ApiModelProperty("堤顶最大宽度")
    private Double dikeTopWidMax;


    @ApiModelProperty("堤防最低高程")
    private Double dikeLowEl;


    @ApiModelProperty("工程任务")
    private String endTask;


    @ApiModelProperty("工程建设情况")
    private String endStar;


    @ApiModelProperty("开工时间")
    private String startDate;


    @ApiModelProperty("竣工验收时间")
    private String compDate;


    @ApiModelProperty("归口管理部门")
    private String admDep;


    @ApiModelProperty("河流岸别")
    private String rvBank;


    @ApiModelProperty("堤防跨界情况")
    private String dikeCorBoun;


    @ApiModelProperty("规划防洪(潮)")
    private String planFlSta;


    @ApiModelProperty("达到规划防洪（潮）标准的长度 (m)")
    private Double flStaLen;


    @ApiModelProperty("设计水（高潮）位 (m)")
    private Double desStag;


    @ApiModelProperty("堤防高度(m)")
    private Double dikeHigMax;


    @ApiModelProperty("堤防高度(m)")
    private Double dikeHigMin;


    @ApiModelProperty("一般堤高")
    private String avgcrestelevation;


    @ApiModelProperty("堤防结构")
    private String dikestruct;


    @ApiModelProperty("空间坐标")
    private String geom;


    @ApiModelProperty("完工时间（非加固）")
    private String finshTime;


    @ApiModelProperty("参照水位站警戒水位(米)")
    private Double warnWaterLevel;


    @ApiModelProperty("参照水位站危急水位(米)")
    private Double dangerWaterLevel;


    @ApiModelProperty("薄弱堤段名称")
    private String weakDikeName;


    @ApiModelProperty("薄弱堤段起始经度")
    private Double weakDikeStartLongitude;


    @ApiModelProperty("薄弱堤段起始纬度(度)")
    private Double weakDikeStartLatitude;


    @ApiModelProperty("薄弱堤段终止经度(度)")
    private Double weakDikeEndLongitude;


    @ApiModelProperty("薄弱堤段终止纬度(度)")
    private Double weakDikeEndLatitude;


    @ApiModelProperty("薄弱情况描述")
    private String weakSituationDescription;


    @ApiModelProperty("水闸数量(孔)")
    private Integer sluiceCount;


    @ApiModelProperty("泵站数量(处)")
    private Integer pumpCount;


    @ApiModelProperty("参照水位站编码")
    private String relativeStationCode;


    @ApiModelProperty("参照水位站名称")
    private String relativeStationName;


    @ApiModelProperty("所在河道等级")
    private String riverLevel;


    @ApiModelProperty("是否五水共治任务")
    private String isWsgz;


    @ApiModelProperty("是否竣工验收（非加固）")
    private String isComplete;


    @ApiModelProperty("保护范围（平方公里）")
    private Double protectionArea;


    @ApiModelProperty("中间堤顶高程")
    private Double middleElevation;


    @ApiModelProperty("涵管涵洞数量")
    private String culvertCount;


    @ApiModelProperty("其他穿堤建筑物数量")
    private String otherCrossBuildCount;


    @ApiModelProperty("道口数量")
    private String levelCrossingCount;


    @ApiModelProperty("其他涉堤建筑物数量")
    private String otherBuildingCount;


    @ApiModelProperty("备注")
    private String note;


    @ApiModelProperty("记录生效时间")
    private String effDate;


    @ApiModelProperty("记录失效时间")
    private String exprDate;


    @ApiModelProperty("行政区划代码")
    private String areaCode;


    @ApiModelProperty("政府负责人用户id，关联到用户体系")
    private String govermentChargeId;


    @ApiModelProperty("水行政部门负责人用户id，关联到用户体系")
    private String waterDepartmentChargeId;


    @ApiModelProperty("主管部门负责人id，关联到用户体系")
    private String competentDepartmentChargeId;


    @ApiModelProperty("管理单位责任人id，关联到用户体系")
    private String manageDepartmentChargeId;


    @ApiModelProperty("points")
    private String points;


    @ApiModelProperty("技术负责人")
    private String technologyChargeId;


    @ApiModelProperty("巡查责任人")
    private String patrolChargeId;


    @ApiModelProperty("删除标记 0未删除 1已删除")
    private int delFlag;


    @ApiModelProperty("所属流域")
    private String bas;
}
