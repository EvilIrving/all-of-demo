package com.ygkj.big.screen.model;

import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author fml
 * @date 2021-03-29 15:22
 * @description 水电站基础信息表
 */
@Data
@ApiModel("水电站基础信息表 ")
public class AttHystBase implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("水电站代码")
    @TableId(value = "hyst_code")
    private String hystCode;


    @ApiModelProperty("水电站名称")
    private String hystName;


    @ApiModelProperty("配套水库名称")
    private String sresName;


    @ApiModelProperty("配套水库库容")
    private Double sresCap;


    @ApiModelProperty("水库编码")
    private String reservoirCode;


    @ApiModelProperty("水电站经度")
    private Double hystLong;


    @ApiModelProperty("水电站纬度")
    private Double hystLat;


    @ApiModelProperty("水电站所在位置")
    private String hystLoc;


    @ApiModelProperty("水电站类型")
    private String hystType;


    @ApiModelProperty("总装机容量")
    private Integer totInsCap;


    @ApiModelProperty("保证出力")
    private Integer firmPow;


    @ApiModelProperty("额定水头")
    private Double ratHead;


    @ApiModelProperty("工程等别")
    private String engGrad;


    @ApiModelProperty("工程规模")
    private String engScal;


    @ApiModelProperty("主要建筑物级别")
    private String mainBuildGrad;


    @ApiModelProperty("工程建设情况")
    private String engStat;


    @ApiModelProperty("开工时间")
    private String startDate;


    @ApiModelProperty("竣工验收时间")
    private String compDate;


    @ApiModelProperty("归口管理部门")
    private String amdDep;


    @ApiModelProperty("机组台数(台)")
    private Integer generatorsCount;


    @ApiModelProperty("多年平均发电量 (万千瓦时)")
    private Double yearsAveragePowerGeneration;


    @ApiModelProperty("是否利用水库发电")
    private String isUseResvoirWater;


    @ApiModelProperty("本流域集雨面积 (平方千米)")
    private Double localWaterCollectionArea;


    @ApiModelProperty("水库安全技术认定日期")
    private String reservoirSafetyIdentificationDate;


    @ApiModelProperty("水库安全技术认定结论")
    private String reservoirSafetyIdentificationConclusion;


    @ApiModelProperty("水轮机主要型式")
    private String hydraulicTurbineMainType;


    @ApiModelProperty("机组布置主要方式")
    private String generatorsArrangementMainWay;


    @ApiModelProperty("完工时间")
    private String completeTime;


    @ApiModelProperty("大坝类型按材料分")
    private String damTypeByMaterials;


    @ApiModelProperty("最大坝高(米)")
    private Double damMaxHeight;


    @ApiModelProperty("备注")
    private String note;


    @ApiModelProperty("记录生效时间")
    private String effDate;


    @ApiModelProperty("记录失效时间")
    private String exprDate;


    @ApiModelProperty("行政区划代码")
    private String areaCode;


    @ApiModelProperty("装机流量")
    private Integer insCap;


    @ApiModelProperty("政府负责人用户id，关联到用户体系")
    private String govermentChargeId;


    @ApiModelProperty("水行政部门负责人用户id，关联到用户体系")
    private String waterDepartmentChargeId;


    @ApiModelProperty("主管部门负责人id，关联到用户体系")
    private String competentDepartmentChargeId;


    @ApiModelProperty("管理单位责任人id，关联到用户体系")
    private String manageDepartmentChargeId;


    @ApiModelProperty("技术负责人")
    private String technologyChargeId;


    @ApiModelProperty("巡查责任人")
    private String patrolChargeId;


    @ApiModelProperty("删除标记 0未删除 1已删除")
    private int delFlag;


    @ApiModelProperty("所属流域")
    private String bas;


    @ApiModelProperty("设计标准(年)")
    private String designStandard;


    @ApiModelProperty("机组台数")
    private Integer unitCount;


    @ApiModelProperty("多年平均发电量(万千瓦时)")
    private Double avgOutput;


    @ApiModelProperty("是否划界")
    private String isDeli;


    @ApiModelProperty("是否确权")
    private String isConfRight;


    @ApiModelProperty("机组装机容量")
    private String uintInsCap;


    @ApiModelProperty("所在河流")
    private String riverName;


    @ApiModelProperty("流域级别")
    private String basinLevel;


    @ApiModelProperty("水电站所在位置")
    private String hystSite;


    @ApiModelProperty("数源行政区划编码")
    private String adcd;
}
