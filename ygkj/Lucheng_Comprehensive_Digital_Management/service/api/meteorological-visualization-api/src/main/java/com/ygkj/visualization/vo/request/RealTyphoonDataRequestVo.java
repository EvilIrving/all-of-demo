package com.ygkj.visualization.vo.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("台风查询参数封装")
@Data
public class RealTyphoonDataRequestVo {

    @ApiModelProperty("台风名称")
    private String name;

    @ApiModelProperty("台风编号")
    private String code;

    @ApiModelProperty("台风编号,多个台风编号用逗号隔开")
    private String codes;

    @ApiModelProperty("起始时间（年月，格式yyyy-MM）")
    private String begin;

    @ApiModelProperty("结束时间（年月，格式yyyy-MM）")
    private String end;

    @ApiModelProperty("起始时间（年月，格式yyyy-MM-dd HH:mm:ss）")
    private String startTime;

    @ApiModelProperty("结束时间（年月，格式yyyy-MM-dd HH:mm:ss）")
    private String endTime;

    @ApiModelProperty("登陆最小等级")
    private String minLandingLevel;

    @ApiModelProperty("登陆最大等级")
    private String maxLandingLevel;

    @ApiModelProperty("最小风力极值")
    private Double minWindExtremum;

    @ApiModelProperty("最大风力极值")
    private Double maxWindExtremum;

    @ApiModelProperty("是否影响温州默认为true")
    private Boolean affectedArea = Boolean.TRUE;

    @ApiModelProperty("年份")
    private Integer year;

    @ApiModelProperty("是否影响温州默认为null,1 代表全部，2代表影响温州，3代表不影响温州")
    private Byte affectedWz;

    public Boolean judgeAffectedWz() {
        if (affectedWz != null) {
            switch (affectedWz) {
                case 1:
                    affectedArea = null;
                    break;
                case 2:
                    affectedArea = Boolean.TRUE;
                    break;
                case 3:
                    affectedArea = Boolean.FALSE;
                    break;
                default:
                    break;
            }
        }
        return affectedArea;
    }
}
