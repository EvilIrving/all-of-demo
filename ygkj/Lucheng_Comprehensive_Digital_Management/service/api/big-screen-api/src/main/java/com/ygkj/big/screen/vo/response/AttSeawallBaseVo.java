package com.ygkj.big.screen.vo.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author  wgf
 * @date  2021/10/19 13:59
 */
@Data
@ApiModel("海塘报警 ")
public class AttSeawallBaseVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("海塘编码")
    private String seawallCode;


    @ApiModelProperty("海塘名称")
    private String seawallName;


    @ApiModelProperty("海塘型式")
    private String seawallType;


    @ApiModelProperty("海塘级别")
    private String seawallLevel;


    @ApiModelProperty("代表潮位站代码")
    private String tideStationCode;


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


    @ApiModelProperty("设计潮位")
    private Double designTiddeStandard;

    @ApiModelProperty("实际防潮标准")
    private String preventTideStandard;

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


    @ApiModelProperty("经度")
    private String longitude;


    @ApiModelProperty("纬度")
    private String latitude;


    @ApiModelProperty("行政区划代码")
    private String areaCode;


    @ApiModelProperty("所属流域")
    private String bas;


    @ApiModelProperty("所属岸段")
    private String coastalCode;

    @ApiModelProperty("报警状态：1.正常，2.超蓝色3.超黄色,4.超橙色，5.超红色   没有其他情况或者没有数据默认正常")
    private Integer status = 1;

    @ApiModelProperty("监测时间")
    private String tm;

    @ApiModelProperty("监测数据")
    private Double tdz;

    @ApiModelProperty("海塘堤防线")
    private String coorpot;
}
