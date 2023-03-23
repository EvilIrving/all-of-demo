package com.ygkj.river.vo.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author lxl
 * @create 2021-08-05 21:28
 * @description 蓝线工程 条件筛选请求类
 */
@Data
@ApiModel("水域调查数据-人工水道水面线数据表")
public class AttBlueProjectReqVo {

    @ApiModelProperty("页数")
    private Integer pageSize;

    @ApiModelProperty("第几页")
    private Integer pageNum;

    @ApiModelProperty("行政区划编码")
    private String areaCode;

    @ApiModelProperty("项目阶段 1-前期阶段 2-施工阶段 3-完工阶段")
    private Integer projectStage;

    /**
     * 监管状态 1-新项目 2-监管中 3-复核中 4-移交执法大队
     */
    @ApiModelProperty("监管状态 1-新项目 2-监管中 3-复核中 4-移交执法大队")
    private Integer regulatoryStatus;

    @ApiModelProperty("模糊搜索")
    private String key;

    @ApiModelProperty("河道等级 1、2、3、4、5级")
    private String riverLevel;

    @ApiModelProperty("所属流域")
    private String basin;

    @ApiModelProperty("筛选所属河道")
    private String riverId;

    @ApiModelProperty("是否携带监管信息")
    private boolean withInspection = false;

    @ApiModelProperty("年份筛选")
    private Integer year;

}
