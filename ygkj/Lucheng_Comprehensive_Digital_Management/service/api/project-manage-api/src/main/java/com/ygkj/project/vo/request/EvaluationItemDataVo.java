package com.ygkj.project.vo.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author wgf
 * @date 2022/4/25 10:47
 * @Description:
 */
@Data
public class EvaluationItemDataVo {

    /**
     * 模板数据id
     */
    @ApiModelProperty(value = "模板数据id")
    private String id;

    /**
     * 验收事项
     */
    @ApiModelProperty(value = "验收事项")
    private String acceptanceItem;

    /**
     * 验收要求
     */
    @ApiModelProperty(value = "验收要求")
    private String acceptanceRequirements;

    /**
     * 标准分
     */
    @ApiModelProperty(value = "标准分")
    private String standardScore;

    /**
     * 考评得分数据id
     */
    @ApiModelProperty(value = "考评得分数据id")
    private String kpId;

    /**
     * 得分
     */
    @ApiModelProperty(value = "得分")
    private String score;

    /**
     * 扣分原则
     */
    @ApiModelProperty(value = "扣分原则")
    private String markPrinciple;
}
