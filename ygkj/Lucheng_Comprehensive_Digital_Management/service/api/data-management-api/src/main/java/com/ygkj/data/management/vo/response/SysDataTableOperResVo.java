package com.ygkj.data.management.vo.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("数据管理系统权限返回参数封装")
@JsonIgnoreProperties(value = {"hibernateLazyInitializer", "handler"})
public class SysDataTableOperResVo {

    @ApiModelProperty("查询权限1-是 0-否")
    private Boolean operQuery;
    @ApiModelProperty("新增权限1-是 0-否")
    private Boolean operCreate;
    @ApiModelProperty("修改权限1-是 0-否")
    private Boolean operUpdate;
    @ApiModelProperty("删除权限1-是 0-否")
    private Boolean operDelete;
}
