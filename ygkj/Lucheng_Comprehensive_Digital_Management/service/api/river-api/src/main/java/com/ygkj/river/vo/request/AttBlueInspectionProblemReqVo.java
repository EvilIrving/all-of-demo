package com.ygkj.river.vo.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author lxl
 * @create 2021-08-13 9:44
 * @description 巡查问题请求类
 */
@Data
@ApiModel
public class AttBlueInspectionProblemReqVo {

    /**
     * 巡查id
     */
    @ApiModelProperty("巡查id")
    private String inspectionId;

    /**
     * 状态（1正常、2异常）
     */
    @ApiModelProperty("状态（1正常、2异常）")
    private String status;

    /**
     * 是否整改（0-无需整改  1-未整改  2-已整改）
     */
    @ApiModelProperty("是否整改（0-无需整改  1-未整改  2-已整改）")
    private String rectify;

    /**
     * 本次巡查异常条目
     */
    @ApiModelProperty("本次巡查异常条目")
    private String entryName;

}
