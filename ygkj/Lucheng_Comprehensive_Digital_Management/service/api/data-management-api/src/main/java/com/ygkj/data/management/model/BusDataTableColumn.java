package com.ygkj.data.management.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.ygkj.data.management.dto.BusDataRuleDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author wd
 * @description 数据表字段值
 * @date 2021-06-25
 */
@Data
@ApiModel("数据表字段值")
@JsonIgnoreProperties(value = {"hibernateLazyInitializer", "handler"})
public class BusDataTableColumn implements Serializable {

    private static final long serialVersionUID = 1L;

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
     * 字段类型
     */
    @ApiModelProperty("字段类型,int、tinyint、float、double、decimal为数值类型,date、datetime为时间,text、varchar为文本类型")
    private String colType;

    /**
     * 字段长度
     */
    @ApiModelProperty("字段长度")
    private String colLength;

    /**
     * 小数位长度
     */
    @ApiModelProperty("小数位长度")
    private Integer colDecimalLength;

    /**
     * 字段说明
     */
    @ApiModelProperty("字段说明")
    private String colDesc;

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

    /**
     * 是否检索项 0-是 1-不是
     */
    @ApiModelProperty("是否检索项 0-是 1-不是")
    private Integer isSearch;

    @ApiModelProperty("检索类型 text/radio/select/checkbox")
    private String searchType;

    @ApiModelProperty("是否弹窗展示 0-是 1-不是")
    private Integer isFormDisplay;

    @ApiModelProperty("是否隐藏，与form表单一起使用 0-隐藏 1-不隐藏")
    private Integer isHidden;

    @ApiModelProperty("部门id")
    private Integer deptId;

    @ApiModelProperty("部门名称")
    private String departmentName;

    @ApiModelProperty("角色名称")
    private String roleName;

    @ApiModelProperty("文件属性，为空时就不是附件，PDF,IMG,限定文件上传类型")
    private String fileDesc;

    /**
     * 删除标记 0未删除 1已删除
     */
    @ApiModelProperty("删除标记 0未删除 1已删除")
    private boolean delFlag;

    @ApiModelProperty("是否必填")
    private boolean required;
    @ApiModelProperty("是否已配置正则校验规则")
    private Boolean hasRule = Boolean.FALSE;
    @ApiModelProperty("正则校验规则")
    private List<BusDataRuleDto> rules;

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

    public BusDataTableColumn() {
    }

}