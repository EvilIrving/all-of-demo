package com.ygkj.big.screen.vo.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@ApiModel("工程参数查询封装")
public class ProjectManageQueryVo {

    @ApiModelProperty("工程名称")
    private String projectName;

    @ApiModelProperty("行政区划编码")
    private String areaCode;

    @ApiModelProperty("工程类型:大中型水库/小型水库/水库/泵站/水闸/海塘/堤防/山塘/水电站/农村供水工程/在建工程")
    @NotBlank(message = "工程类型缺失，请指定一种工程类型！")
    private String type;
    @ApiModelProperty("工程编码，查询单条记录用")
    private String code;
    @ApiModelProperty("工程规模（仅查询水库/水电站/泵站/水闸时使用）:大（Ⅰ）型，大（Ⅱ）型，中型，小（Ⅰ）型，小（Ⅱ）型")
    private String level;
    @ApiModelProperty("堤防和海塘条件查询字段，级别：1级，2级，3级，4级，5级，其他")
    private String grade;
    @ApiModelProperty("堤防条件查询字段，堤防类型:河（江）堤，湖堤，海堤，围（圩、圈）")
    private String dikeType;
    @ApiModelProperty("堤防条件查询字段，堤防形式:土堤，砌石堤，土石混合堤，钢筋混凝土防洪墙，其他")
    private String dikeForm;
    @ApiModelProperty("页码")
    private Integer page;
    @ApiModelProperty("每页记录条数")
    private Integer rows;

    @ApiModelProperty("海塘条件查询字段，海塘防潮标准起始")
    private Integer designTideStandardStart;

    @ApiModelProperty("海塘条件查询字段，海塘防潮标准终止")
    private Integer designTideStandardEnd;

    private Boolean withWaterLevel;

}