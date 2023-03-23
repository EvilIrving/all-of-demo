package com.ygkj.project.vo.request;

import com.ygkj.entity.PageEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
@ApiModel("工程参数查询封装")
public class ProjectManageQueryVo extends PageEntity {
    // -------------------------------必须字段
    @ApiModelProperty("工程类型:大中型水库/小型水库/水库/泵站/水闸/海塘/堤防/山塘/水电站/农村供水工程/在建工程/农饮水/灌区/闸站")
    @NotBlank(message = "工程类型缺失，请指定一种工程类型！")
    private String type;

    //-------------------------------公共字段
    @ApiModelProperty("工程名称")
    private String projectName;

    @ApiModelProperty("行政区划编码")
    private String areaCode;

    @ApiModelProperty("流域")
    private String bas;

    @ApiModelProperty("工程编码，查询单条记录用")
    private String code;

    @ApiModelProperty("工程编码，查询多条编码记录用")
    private List<String> codes;

    @ApiModelProperty("工程规模（仅查询水库/水电站/泵站/水闸/灌区/闸站时使用）:大（Ⅰ）型，大（Ⅱ）型，中型，小（Ⅰ）型，小（Ⅱ）型")
    private String level;

    @ApiModelProperty("工程规模（仅查询水库/水电站/泵站/水闸/灌区/闸站时使用）:大（Ⅰ）型：1，大（Ⅱ）型：2，中型：3，小（Ⅰ）型：4，小（Ⅱ）型：5")
    private String levels;

    @ApiModelProperty("堤防和海塘条件查询字段，级别：1级，2级，3级，4级，5级，其他")
    private String grade;

    //--------------------------------堤防字段------------------------------------------
    @ApiModelProperty("堤防条件查询字段，堤防类型:河（江）堤，湖堤，海堤，围（圩、圈）")
    private String dikeType;

    @ApiModelProperty("堤防条件查询字段，堤防形式:土堤，砌石堤，土石混合堤，钢筋混凝土防洪墙，其他")
    private String dikeForm;

    //----------------------------------海塘字段-------------------------------------------
    @ApiModelProperty("海塘条件查询字段，海塘防潮标准起始")
    private Integer designTideStandardStart;

    @ApiModelProperty("海塘条件查询字段，海塘防潮标准终止")
    private Integer designTideStandardEnd;

    @ApiModelProperty("海塘条件计算字段，是否计算海塘实时报警")
    private boolean seawallMonitorWarn;

    @ApiModelProperty("水闸条件查询字段，是否温瑞平水闸 是为true 否为false 不置可否 为null")
    private Boolean wrpWaga;

    @ApiModelProperty("水库水闸是否加上水位工程，默认为false")
    private Boolean withWaterLevel = false;

    @ApiModelProperty("是否是病险工程：仅查询 水库/海塘/水闸/山塘")
    private Boolean dilapProj;

    @ApiModelProperty("安全鉴定结论：仅查询 水库/海塘/水闸/山塘")
    private String safetyConclusion;

    @ApiModelProperty("是否查询水库实时水位")
    private Boolean resWaterLevel = false;

    @ApiModelProperty("山塘条件查询字段，山塘类别：全部、高坝山塘、屋顶山塘、普通山塘、其他")
    private String mnpdCate;

    @ApiModelProperty("农饮水条件查询字段，类型:城市、乡镇、单村、联村")
    private String engType;

    @ApiModelProperty("排序子句")
    private String orderSql;

}