package com.ygkj.data.management.model;

import com.ygkj.utils.StringUtils;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Huangzh
 * @description 数据管理系统权限表
 * @date 2021-06-24
 */
@Data
@ApiModel("数据管理系统权限")
public class SysDataManagementPermission implements Serializable {

    private static final long serialVersionUID = 1L;

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

    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    private Date createTime;

    /**
     * 创建人
     */
    @ApiModelProperty("创建人")
    private String createId;

    /**
     * 修改时间
     */
    @ApiModelProperty("修改时间")
    private Date updateTime;

    /**
     * 修改人
     */
    @ApiModelProperty("修改人")
    private String updateId;

    /**
     * 是否删除 0-未删除
     */
    @ApiModelProperty("是否删除 0-未删除")
    private boolean delFlag;

    @ApiModelProperty("业务类型 BASIC 基础数据  REALTIME 实时数据")
    private String busType;

    public String getBusType() {
        return StringUtils.isEmpty(this.busType) ? "BASIC" : this.busType;
    }

    public SysDataManagementPermission() {
    }

}

