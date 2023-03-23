package com.ygkj.data.management.vo.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.ygkj.data.management.model.BusDataTableColumn;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel("数据管理列配置返回值封装")
@JsonIgnoreProperties(value = {"hibernateLazyInitializer", "handler"})
public class BusDataTableColumnConfigResVo {

    /**
     * 表单项配置
     */
    @ApiModelProperty("表单项配置")
    private List<BusDataTableColumn> formConfig;

    /**
     * 查询项配置
     */
    @ApiModelProperty("查询项配置")
    private List<BusDataTableColumn> queryConfig;

    /**
     * 列表项配置
     */
    @ApiModelProperty("列表项配置")
    private List<BusDataTableColumn> listConfig;

    @ApiModelProperty("主键名称")
    private String primaryKey;

    /**
     * 概要字段，用于概要一条行记录得字段，如水库名称、测站名称
     */
    private String outlineColumn;

    @ApiModelProperty("表操作权限")
    private SysDataTableOperResVo sysDataTableOperResVo;

    @ApiModelProperty("责任部门id")
    private String dutyDepartmentId;

    @ApiModelProperty("责任部门名称")
    private String dutyDepartment;

    public BusDataTableColumnConfigResVo() {
    }
}
