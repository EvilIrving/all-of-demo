package com.ygkj.project.vo.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@ApiModel("泵站列表返回参数")
public class PustProjectResp implements Serializable {

    private static final long serialVersionUID = -980832839601825132L;
    @ApiModelProperty("泵站名称")
    private String pustName;

    @ApiModelProperty("工程名称")
    private String projectName;

    @ApiModelProperty("所在市")
    private String cityAdnm;

    @ApiModelProperty("所在县")
    private String countryAdnm;

    @ApiModelProperty("泵站规模")
    private String engScal;

    @ApiModelProperty("所在河流")
    private String bas;

    @ApiModelProperty("装机流量(m³/s)")
    private String insFlow;

    @ApiModelProperty("装机功率(kW)")
    private String insPow;

    @ApiModelProperty("管理层级")
    private String managementLevel;

    @ApiModelProperty("经度")
    private String longitude;

    @ApiModelProperty("纬度")
    private String latitude;


    /**
     * 泵站代码
     */
    @ApiModelProperty("泵站代码")
    private String pustCode;

    /**
     * 泵站经度
     */
    @ApiModelProperty("泵站经度")
    private BigDecimal pustLong;

    /**
     * 泵站维度
     */
    @ApiModelProperty("泵站维度")
    private BigDecimal pustLat;

    /**
     * 泵站水闸位置
     */
    @ApiModelProperty("泵站水闸位置")
    private String pustLoc;

    /**
     * 泵站类型
     */
    @ApiModelProperty("泵站类型")
    private String pustType;

    /**
     * 水泵数量
     */
    @ApiModelProperty("水泵数量")
    private Integer pumpNum;

    /**
     * 设计扬程
     */
    @ApiModelProperty("设计扬程")
    private BigDecimal desHead;

    /**
     * 工程任务
     */
    @ApiModelProperty("工程任务")
    private String engTask;

    /**
     * 工程等别
     */
    @ApiModelProperty("工程等别")
    private String engGrad;

    /**
     * 主要建筑物级别
     */
    @ApiModelProperty("主要建筑物级别")
    private String mainBuildGrad;

    /**
     * 工程建设情况
     */
    @ApiModelProperty("工程建设情况")
    private String engStart;

    /**
     * 开工时间
     */
    @ApiModelProperty("开工时间")
    private Date startDate;

    /**
     * 建成时间
     */
    @ApiModelProperty("建成时间")
    private Date builtdate;

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
     * 是否通过标准化管理创建
     */
    @ApiModelProperty("是否通过标准化管理创建")
    private String staMan;

    /**
     * 主要构筑物
     */
    @ApiModelProperty("主要构筑物")
    private String mainbuilding;

    /**
     * 是否竣工验收 （非加固）
     */
    @ApiModelProperty("是否竣工验收 （非加固）")
    private String isComplete;

    /**
     * 是否为闸站工程
     */
    @ApiModelProperty("是否为闸站工程")
    private String isGateStationProject;

    /**
     * 泵站参照水位站编码
     */
    @ApiModelProperty("泵站参照水位站编码")
    private String pumpReferenceWlStationCode;

    /**
     * 泵站参照水位站名称
     */
    @ApiModelProperty("泵站参照水位站名称")
    private String pumpReferenceWlStationName;

    /**
     * 泵站参照水位站警戒水位
     */
    @ApiModelProperty("泵站参照水位站警戒水位")
    private BigDecimal pumpReferenceWlStationWarningLevel;

    /**
     * 泵站参照水位站危急水位
     */
    @ApiModelProperty("泵站参照水位站危急水位")
    private BigDecimal pumpReferenceWlStationCriticalLevel;

    /**
     * 内河常水位(米)
     */
    @ApiModelProperty("内河常水位(米)")
    private BigDecimal riverWaterLevel;

    /**
     * 电动机台数(台)
     */
    @ApiModelProperty("电动机台数(台)")
    private Integer motorNumber;

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
     * 设计标准
     */
    @ApiModelProperty("设计标准")
    private String designStandard;



    /**
     * 机组1开关情况
     */
    @ApiModelProperty("机组1开关情况")
    private String num1;

    /**
     * 机组2开关情况
     */
    @ApiModelProperty("机组2开关情况")
    private String num2;

    /**
     * 机组3开关情况
     */
    @ApiModelProperty("机组3开关情况")
    private String num3;

    /**
     * 机组4开关情况
     */
    @ApiModelProperty("机组4开关情况")
    private String num4;

    /**
     * 机组5开关情况
     */
    @ApiModelProperty("机组5开关情况")
    private String num5;

    /**
     * 进水口水位
     */
    @ApiModelProperty("进水口水位")
    private Double jskZ;

    /**
     * 进水池水位
     */
    @ApiModelProperty("进水池水位")
    private Double jscZ;

    /**
     * 出水池水位
     */
    @ApiModelProperty("出水池水位")
    private Double cscZ;

    @ApiModelProperty("流量")
    private String sum;

    @ApiModelProperty("泵站是否开启")
    private Boolean pustOpened;

    public static boolean isPustOpened(PustProjectResp resp){
        if(!resp.getPustOpened()){
            return false;
        }else
            return true;

    }
}
