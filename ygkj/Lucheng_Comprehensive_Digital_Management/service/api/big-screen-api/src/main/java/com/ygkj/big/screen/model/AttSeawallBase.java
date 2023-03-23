package com.ygkj.big.screen.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author fml
 * @date 2021-03-29 15:20
 * @description 海塘基础信息表
 */
@Data
@ApiModel("海塘基础信息表 ")
public class AttSeawallBase implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("海塘编码")
    private String seawallCode;


    @ApiModelProperty("海塘名称")
    private String seawallName;


    @ApiModelProperty("海塘型式")
    private String seawallType;


    @ApiModelProperty("海塘级别")
    private String seawallLevel;


    @ApiModelProperty("代表潮位站代 码")
    private String tideStationCode;


    @ApiModelProperty("工程竣工验收 情况")
    private String projectCompletion;


    @ApiModelProperty("开工时间（非加 固）")
    private String startDate;


    @ApiModelProperty("竣工验收时间 （非加固）")
    private String completionDate;


    @ApiModelProperty("海塘长度")
    private Double seawallLength;


    @ApiModelProperty("起点位置")
    private String startLocation;


    @ApiModelProperty("起点桩号")
    private Integer startPileNo;


    @ApiModelProperty("海堤（段）起点 纬度")
    private Double seawallStartLatitude;


    @ApiModelProperty("海堤（段）起点 经度")
    private Double seawallStartLongitude;


    @ApiModelProperty("终点位置")
    private String finalLocation;


    @ApiModelProperty("终点桩号")
    private Integer finalPileNo;


    @ApiModelProperty("海堤（段）终点 纬度")
    private Double seawallFinalLatitude;


    @ApiModelProperty("海堤（段）终点 经度")
    private Double seawallFinalLongitude;


    @ApiModelProperty("起点堤顶高程")
    private Double startCrestAltitude;


    @ApiModelProperty("终点堤顶高程")
    private Double finalCrestAltitude;


    @ApiModelProperty("设计潮位")
    private Double designTiddeStandard;


    @ApiModelProperty("实际防潮标准")
    private String preventTideStandard;


    @ApiModelProperty("水闸数量")
    private Integer sluiceCount;


    @ApiModelProperty("保护人口")
    private String protectionPopulation;


    @ApiModelProperty("保护重点设施")
    private String protectionKeyFacility;


    @ApiModelProperty("代表潮位站名 称")
    private String tideStationName;


    @ApiModelProperty("参照潮位站蓝 色警戒潮位(米)")
    private Double tideStationBlueLevel;


    @ApiModelProperty("参照潮位站黄 色警戒潮位(米)")
    private Double tideStationYellowLevel;


    @ApiModelProperty("参照潮位站橙 色警戒潮位(米)")
    private Double tideStationOrangeLevel;


    @ApiModelProperty("参照潮位站红 色警戒潮位(米)")
    private Double tideStationRedLevel;


    @ApiModelProperty("完工时间（非加 固）")
    private String finishTime;


    @ApiModelProperty("达到规划防潮 标准的长度")
    private Double reachStandardLength;


    @ApiModelProperty("设计防潮标准")
    private Integer designTideStandard;


    @ApiModelProperty("是否竣工验收 （非加固）")
    private String isComplete;


    @ApiModelProperty("海塘平均高程 （米）")
    private Double averageAltitude;


    @ApiModelProperty("保护面积（平方 公里）")
    private Double protectionArea;


    @ApiModelProperty("所在村名称")
    private String village;


    @ApiModelProperty("更新时间")
    private String modifyTime;


    @ApiModelProperty("经度")
    private String longitude;


    @ApiModelProperty("纬度")
    private String latitude;


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


    @ApiModelProperty("所属岸段")
    private String coastalCode;

    @ApiModelProperty("塘顶宽度（最小值m）")
    private String minWidth;

    @ApiModelProperty("塘顶宽度（最大值m）")
    private String maxWidth;

    @ApiModelProperty("最低高程")
    private Double lowestElevation;

    @ApiModelProperty("海塘高度（最小值m）")
    private String minHeight;

    @ApiModelProperty("海塘高度（最大值m）")
    private String maxHeight;

    @ApiModelProperty("海塘类型")
    private String seawallPattern;

    @ApiModelProperty("水准基面")
    private String levelDatum;

    @ApiModelProperty("海塘堤防线")
    private String coorpot;
}
