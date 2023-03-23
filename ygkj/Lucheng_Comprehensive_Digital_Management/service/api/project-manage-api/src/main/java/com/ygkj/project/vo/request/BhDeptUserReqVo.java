package com.ygkj.project.vo.request;

import com.ygkj.entity.PageEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author lxl
 * @create 2022-04-28 13:57
 * @description 标化部门人员筛选类
 */
@Data
public class BhDeptUserReqVo extends PageEntity {

    @ApiModelProperty("模糊筛选名称")
    private String name;

    @ApiModelProperty("部门id筛选")
    private String deptId;

    @ApiModelProperty("角色id筛选")
    private String roleId;
}
