package com.ygkj.data.management.vo.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("表字段配置更新入参封装")
public class BusDataTableColumnConfigAddVo {

    @ApiModelProperty("id")
    private String id;

    @ApiModelProperty("是否列表显示 0-显示 1-不显示")
    private Integer isListView;

    /**
     * 是否下拉筛选项 0-是 1-不是
     */
/*    @ApiModelProperty("是否下拉筛选项 0-是 1-不是")
    private Integer isDropDown;*/

    /**
     * 下拉选项键值对
     */
    @ApiModelProperty("下拉选项键值对")
    private String dropDownItems;

    /**
     * 是否检索项 0-是 1-不是
     */
    @ApiModelProperty("是否检索项 0-是 1-不是")
    private Integer isSearch;

    @ApiModelProperty("检索类型 text/radio/select/checkbox")
    private String searchType;

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

    @ApiModelProperty("是否弹窗展示 0-是 1-不是")
    private Integer isFormDisplay;

    @ApiModelProperty("文件属性，为空时就不是附件，PDF,IMG,限定文件上传类型")
    private String fileDesc;

    @ApiModelProperty("字段说明")
    private String colDesc;

}
