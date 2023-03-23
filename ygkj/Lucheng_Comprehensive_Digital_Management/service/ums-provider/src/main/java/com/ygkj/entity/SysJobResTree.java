package com.ygkj.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author wgf
 * @date 2021/3/15 17:51
 * @Description:
 */
@Data
public class SysJobResTree {

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

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "隐藏二级目录")
    private Boolean hideSecond;

    @ApiModelProperty(value = "组件")
    private String component;

    @ApiModelProperty(value = "重定向")
    private String redirect;

    @ApiModelProperty(value = "标识")
    private String name;

    private List<SysJobResTree> sysJobResTrees;

}
