package com.ygkj.data.management.vo.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("数据管理列返回值封装")
@JsonIgnoreProperties(value = {"hibernateLazyInitializer", "handler"})
public class BusDataTableColumnResVo {

    public BusDataTableColumnResVo() {
    }

    /**
     * id
     */
    @ApiModelProperty("id")
    private String id;

    /**
     * 表id
     */
    @ApiModelProperty("表id")
    private String tableId;

    /**
     * 字段名
     */
    @ApiModelProperty("字段名")
    private String colName;

    /**
     * 字段说明
     */
    @ApiModelProperty("字段说明")
    private String colDesc;

    @ApiModelProperty("字段类型")
    private String colType;

    /**
     * 是否列表显示 0-显示 1-不显示
     */
    @ApiModelProperty("是否列表显示 0-显示 1-不显示")
    private Integer isListView;

    /**
     * 是否下拉筛选项 0-是 1-不是
     */
    @ApiModelProperty("是否下拉筛选项 0-是 1-不是")
    private Integer isDropDown;

    /**
     * 下拉选项键值对
     */
    @ApiModelProperty("下拉选项键值对")
    private String dropDownItems;

    @ApiModelProperty("文件属性，为空时就不是附件，PDF,IMG,限定文件上传类型")
    private String fileDesc;

    /**
     * 是否检索项 0-是 1-不是
     */
    @ApiModelProperty("是否检索项 0-是 1-不是")
    private Integer isSearch;

    @ApiModelProperty("检索类型 text/radio/select/checkbox")
    private String searchType;

    @ApiModelProperty("是否弹窗展示 0-是 1-不是")
    private Integer isFormDisplay;

    @ApiModelProperty("部门id")
    private Integer deptId;

    private String candidateRoleNames;

    @ApiModelProperty("列表字段排序")
    private Integer listSort;
    @ApiModelProperty("表单字段排序")
    private Integer formSort;
    @ApiModelProperty("搜索字段排序")
    private Integer searchSort;
    @ApiModelProperty("表单列宽度")
    private Integer tableWidth;
    @ApiModelProperty("是否质量考核 0-是 1-不是")
    private Integer isQualityExamine;


}
