package com.ygkj.river.vo.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author hucong
 * @description 中小河流表
 * @date 2021-08-12
 */
@Data
@ApiModel("中小河流表")
@NoArgsConstructor
public class AttSmallMediumRiverReqVo {

    /**
     * 序号
     */
    @ApiModelProperty("序号")
    private String id;

    /**
     * 所在县区
     */
    @ApiModelProperty("所在县区")
    private String county;

    /**
     * 项目名称
     */
    @ApiModelProperty("项目名称")
    private String projectName;


    /**
     * 工程状态
     */
    @ApiModelProperty("工程状态 项目在建、已完工")
    private String projectStatus;

    @ApiModelProperty("页数")
    private Integer pageSize;


    @ApiModelProperty("第几页")
    private Integer pageNum;

    @ApiModelProperty(value = "是否包含温州市级那条记录 1:是", hidden = true)
    private String containWenzhou;
}
