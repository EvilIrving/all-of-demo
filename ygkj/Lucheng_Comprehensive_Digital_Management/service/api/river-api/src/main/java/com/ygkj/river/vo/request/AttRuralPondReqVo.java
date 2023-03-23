package com.ygkj.river.vo.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@ApiModel("农村池塘表")
@NoArgsConstructor
public class AttRuralPondReqVo implements Serializable {

    private static final long serialVersionUID = 1L;


    /**
     * 主键id
     */
    @ApiModelProperty("主键id")
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
    @ApiModelProperty("工程状态 项目在建、已完工、前期阶段")
    private String projectStatus;

    @ApiModelProperty("页数")
    private Integer pageSize;


    @ApiModelProperty("第几页")
    private Integer pageNum;

    @ApiModelProperty(value = "是否包含温州市级那条记录 1:是", hidden = true)
    private String containWenzhou;

}
