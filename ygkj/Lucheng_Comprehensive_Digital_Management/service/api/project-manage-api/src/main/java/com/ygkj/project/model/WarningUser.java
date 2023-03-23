package com.ygkj.project.model;

import com.baomidou.mybatisplus.annotation.TableId;
import com.ygkj.gragh.model.PageEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author wgf
 * @date 2021/10/21 17:43
 * @Description:
 */
@Data
public class WarningUser extends PageEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "id")
    /**
     * id
     */
    @ApiModelProperty(value = "id")
    private String id;

    /**
     * 绑定的部门id
     */
    @ApiModelProperty(value = "绑定的部门id")
    private String deptid;

    /**
     * 类型1.浙政钉同步的部门，2.自定义的部门表
     */
    @ApiModelProperty(value = "类型1.浙政钉同步的部门，2.自定义的部门表")
    private Integer type;

    /**
     * 姓名
     */
    @ApiModelProperty(value = "姓名")
    private String name;

    /**
     * 手机号
     */
    @ApiModelProperty(value = "手机号")
    private String phone;

    /**
     * 职务
     */
    @ApiModelProperty(value = "职务")
    private String duty;

    /**
     * 新增时间
     */
    @ApiModelProperty(value = "新增时间")
    private Date tm;

    public WarningUser() {}
}
