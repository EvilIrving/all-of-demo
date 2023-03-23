package com.ygkj.supervision.vo.response;

import com.ygkj.supervision.model.AttSupervisionProblem;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author lxl
 */
@Data
@ApiModel("追究类型回参")
public class InvestigateResVo {

    /**
     * 单位类别
     */
    @ApiModelProperty("单位类别")
    private String unitType;

    /**
     * 单位名
     */
    @ApiModelProperty("单位名")
    private String unitName;

    /**
     * 追究问题统计值
     */
    @ApiModelProperty("追究问题统计值")
    private String countNum;


    @ApiModelProperty("追究问题列表")
    private List<AttSupervisionProblem> problems;

}
