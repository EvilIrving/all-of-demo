package com.ygkj.data.management.vo.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel("数据管理系统权限出参封装")
@JsonIgnoreProperties(value = {"hibernateLazyInitializer", "handler"})
public class SysDataManagementPermissionResVo {

    /**
     * 主键
     */
    @ApiModelProperty("主键")
    private String id;

    /**
     * 权限编码
     */
    @ApiModelProperty("权限编码")
    private String code;

    /**
     * 权限名称
     */
    @ApiModelProperty("权限名称")
    private String name;

    /**
     * 表权限对应的表id
     */
    @ApiModelProperty("表权限对应的表id")
    private String tableId;

    /**
     * 行权限对应的表主键名称
     */
    @ApiModelProperty("行权限对应的表主键名称")
    private String primaryKey;

    /**
     * 行权限对应的表主键值
     */
    @ApiModelProperty("行权限对应的表主键值")
    private String primaryValue;

    /**
     * 列权限对应的列字段名
     */
    @ApiModelProperty("列权限对应的列字段名")
    private String columnName;

    @ApiModelProperty("字段id")
    private String columnId;


    /**
     * 父级菜单权限名称
     */
    @ApiModelProperty("父级菜单权限名称")
    private String pid;

    /**
     * 排序
     */
    @ApiModelProperty("排序")
    private Integer orderNum;

    /**
     * 权限类型(表权限，行权限，列权限)
     */
    @ApiModelProperty("权限类型(表权限，行权限，列权限)")
    private String type;

    @ApiModelProperty("业务类型 BASIC 基础数据  REALTIME 实时数据")
    private String busType;

    List<SysDataManagementPermissionResVo> childPermissions;
    //标记当前角色是否拥有该权限
    Boolean marked = Boolean.FALSE;
}
