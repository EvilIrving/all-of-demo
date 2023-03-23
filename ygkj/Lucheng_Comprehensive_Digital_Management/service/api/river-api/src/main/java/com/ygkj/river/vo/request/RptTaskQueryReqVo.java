package com.ygkj.river.vo.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author xq
 * @Description
 * @Date 2021/4/6
 */
@ApiModel("任务巡河-任务查询入参")
@Data
public class RptTaskQueryReqVo {
    /**
     * 行政区划
     */
    @ApiModelProperty("行政区划")
    String adcd;
    /**
     * 河道等级
     */
    @ApiModelProperty("河道等级")
    String riverLevel;

    /**
     * 所属流域
     */
    @ApiModelProperty("所属流域")
    String riverBas;

    /**
     * 任务名称
     */
    @ApiModelProperty("任务名称")
    String taskName;
    /**
     * 页码，最小为1
     */
    @ApiModelProperty("页码，最小为1,仅分页接口有效")
    int pageNum = 1;
    /**
     * 页面大小，最小为1
     */
    @ApiModelProperty("页面大小，最小为1,仅分页接口有效")
    int pageSize = 10;
}
