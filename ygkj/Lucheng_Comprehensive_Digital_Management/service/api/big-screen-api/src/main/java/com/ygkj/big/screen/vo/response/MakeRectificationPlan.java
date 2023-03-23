package com.ygkj.big.screen.vo.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author hucong
 * @version 1.0
 * @date 2021/6/16 17:52
 */
@ApiModel("制定整改计划")
@Data
public class MakeRectificationPlan implements Serializable {

    @ApiModelProperty("计划完成期限")
    private String plannedCompleteDate;
    @ApiModelProperty("计划描述")
    private String recPlanDescribe;
    private String id;
    @ApiModelProperty("计划修编预案方案")
    private String planRevisionPlan;
    @ApiModelProperty("整改情况")
    private String recCondition;
    @ApiModelProperty("管控措施")
    private String recSuperviseMeasures;
    @ApiModelProperty("落实情况")
    private String recImplementSituation;
    @ApiModelProperty("相关图片List")
    private List<FileDetail> imageList;
}
