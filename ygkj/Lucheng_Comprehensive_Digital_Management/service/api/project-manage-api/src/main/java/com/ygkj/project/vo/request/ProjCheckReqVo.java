package com.ygkj.project.vo.request;

import com.ygkj.entity.PageEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author lxl
 * @create 2022-04-26 10:11
 * @description 工程检查请求类
 */
@Data
@ApiModel
public class ProjCheckReqVo extends PageEntity {

    @ApiModelProperty("工程编号")
    private String pid;

    @ApiModelProperty("模糊筛选名称")
    private String name;

    @ApiModelProperty("时间筛选 格式 yyyy-MM-dd 日期截止")
    private String date;

    @ApiModelProperty("单条查询 id")
    private String id;




}
