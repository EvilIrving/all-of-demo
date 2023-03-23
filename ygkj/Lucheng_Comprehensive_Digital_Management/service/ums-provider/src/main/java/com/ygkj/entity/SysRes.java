package com.ygkj.entity;


import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author: lint
 * @Description：
 * @Date：2019/1/30
 */
@Data
@ApiModel(value = "com.ygkj.entity.SysRes", description = "系统资源表")
public class SysRes {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value = "ID")
    private String id;

    @ApiModelProperty(value = "资源名称")
    private String resName;

    @ApiModelProperty(value = "上级资源id")
    private String pId;

    @ApiModelProperty(value = "资源类型（1目录、2菜单、3功能）")
    private String resType;

    @ApiModelProperty(value = "菜单图标")
    private String resIcon;

    @ApiModelProperty(value = "菜单路径")
    private String resUrl;

    @ApiModelProperty(value = "排序号")
    private Integer sort;

    @ApiModelProperty(value = "权限标识")
    private String permission;

    @ApiModelProperty(value = "删除标记0-未删除 1-已删除")
    private Integer delFlag;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "1.登陆后菜单，2内部查看菜单")
    private Integer flag;

    @ApiModelProperty(value = "隐藏二级目录")
    private Boolean hideSecond;

    @ApiModelProperty(value = "组件")
    private String component;

    @ApiModelProperty(value = "重定向")
    private String redirect;

    @ApiModelProperty(value = "标识")
    private String name;

    @TableField(exist = false)
    private Integer pageNum;

    @TableField(exist = false)
    private Integer pageSize;
}
