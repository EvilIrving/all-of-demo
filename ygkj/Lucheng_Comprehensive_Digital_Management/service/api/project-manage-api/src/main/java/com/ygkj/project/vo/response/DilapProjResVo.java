package com.ygkj.project.vo.response;

import com.ygkj.gragh.model.AttMnpdBase;
import com.ygkj.gragh.model.AttResBase;
import com.ygkj.gragh.model.AttSeawallBase;
import com.ygkj.gragh.model.AttWagaBase;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author xq
 * @Description
 * @Date 2021/5/14
 */
@Data
@ApiModel("病险工程列表回参")
public class DilapProjResVo implements Serializable {

    private static final long serialVersionUID = 1043542848002851466L;

    /**
     * 工程名称
     */
    @ApiModelProperty("工程名称")
    private String projectName;

    /**
     * 工程编码
     */
    @ApiModelProperty("工程编码")
    private String projectCode;

    /**
     * 工程类型：水库/海塘/水闸/山塘
     */
    @ApiModelProperty("工程类型：水库/海塘/水闸/山塘")
    private String projectType;

    /**
     * 最后一次安全鉴定时间
     */
    @ApiModelProperty("最后一次安全鉴定时间")
    private String lastCheckTime;

    /**
     * 最后一次安全鉴定结果
     */
    @ApiModelProperty("最后一次安全鉴定结果")
    private String lastCheckResult;

    /**
     * 经度
     */
    @ApiModelProperty("经度")
    String longitude;

    /**
     * 纬度
     */
    @ApiModelProperty("纬度")
    String latitude;

    /**
     * 所在乡镇编码
     */
    @ApiModelProperty("所在乡镇编码")
    String projLocCode;

    /**
     * 所在乡镇
     */
    @ApiModelProperty("所在乡镇")
    String projLocName;

    /**
     * 行政区划编码
     */
    @ApiModelProperty("行政区划编码")
    String adcd;

    /**
     * 行政区划
     */
    @ApiModelProperty("行政区划")
    String adnm;

    /**
     * 流域
     */
    @ApiModelProperty("流域")
    String bas;

    /**
     * 工程等别
     */
    @ApiModelProperty("工程等别")
    String engScal;

    /**
     * 工程规模
     */
    @ApiModelProperty("工程规模")
    String engGrad;

    /**
     * 坝型
     */
    @ApiModelProperty("坝型")
    String damType;

    /**
     * 总库容
     */
    @ApiModelProperty("总库容")
    BigDecimal totCap;

    /**
     * 汛限水位
     */
    @ApiModelProperty("汛限水位")
    BigDecimal limitLevel;

    /**
     * 防洪高水位
     */
    @ApiModelProperty("防洪高水位")
    BigDecimal floodLevel;

    /**
     * 实时水位
     */
    @ApiModelProperty("实时水位")
    private Double waterLevel;

    /**
     * 水位测量时间
     */
    @ApiModelProperty("水位测量时间")
    private Date dataTime;

    /**
     * 水文监测站代码
     */
    @ApiModelProperty("水文监测站代码")
    private String stCode;

    /**
     * 水文监测站名称
     */
    @ApiModelProperty("水文监测站名称")
    private String stName;

    /**
     * 水文监测站类型
     */
    @ApiModelProperty("水文监测站类型")
    private String stType;

    /**
     * 水文监测站经度
     */
    @ApiModelProperty("水文监测站经度")
    private BigDecimal stLong;

    /**
     * 水文监测站纬度
     */
    @ApiModelProperty("水文监测站纬度")
    private BigDecimal stLat;

    /**
     * 海塘详情
     */
    @ApiModelProperty("海塘详情")
    private AttSeawallBase seawallInfo;

    /**
     * 水库详情
     */
    @ApiModelProperty("水库详情")
    private AttResBase rsvrInfo;

    /**
     * 山塘详情
     */
    @ApiModelProperty("山塘详情")
    private AttMnpdBase mnpdInfo;

    /**
     * 水闸详情
     */
    @ApiModelProperty("水闸详情")
    private AttWagaBase wagaInfo;

}
