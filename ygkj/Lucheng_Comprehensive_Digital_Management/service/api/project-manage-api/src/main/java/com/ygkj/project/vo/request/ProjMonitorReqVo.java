package com.ygkj.project.vo.request;

import com.ygkj.entity.PageEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author lxl
 * @create 2022-04-28 14:28
 * @description
 */
@Data
public class ProjMonitorReqVo extends PageEntity {

    @ApiModelProperty("模糊筛选名称")
    private String name;


}
