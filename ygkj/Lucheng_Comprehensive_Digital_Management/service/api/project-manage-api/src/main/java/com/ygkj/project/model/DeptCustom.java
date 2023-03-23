package com.ygkj.project.model;




import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @description 预警自定义部门表
 * @author wgf
 * @date 2021-10-21
 */
@Data
public class DeptCustom implements Serializable  {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @ApiModelProperty(value = "ID")
    @TableId("id")
    private String id;

    /**
     * 部门名称
     */
    @ApiModelProperty(value = "部门名称")
    private String deptname;

    /**
     * 子集部门id
     */
    @ApiModelProperty(value = "子集部门id")
    private String pid;

    /**
     * 新增时间
     */
    @ApiModelProperty(value = "新增时间")
    private Date tm;

    public DeptCustom() {}
}