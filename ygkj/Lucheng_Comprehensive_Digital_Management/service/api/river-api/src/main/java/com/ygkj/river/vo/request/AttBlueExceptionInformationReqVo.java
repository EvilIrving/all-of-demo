package com.ygkj.river.vo.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author lxl
 * @create 2021-08-06 13:43
 * @description 配置条目筛选类别
 */
@Data
@ApiModel("配置条目筛选类别")
public class AttBlueExceptionInformationReqVo {

    @ApiModelProperty("页数")
    private Integer pageSize;

    @ApiModelProperty("第几页")
    private Integer pageNum;

    @ApiModelProperty("项目阶段 1-前期阶段 2-施工阶段 3-完工阶段")
    private Integer projectStage;


}
