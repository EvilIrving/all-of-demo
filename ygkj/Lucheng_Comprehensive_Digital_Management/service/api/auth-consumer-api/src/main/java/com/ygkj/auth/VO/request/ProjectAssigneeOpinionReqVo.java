package com.ygkj.auth.VO.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author: fml
 * @Date: 2020/9/18 10:58
 * @Description:
 */
@Data
public class ProjectAssigneeOpinionReqVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("项目id")
    private String id;

    @ApiModelProperty("意见或理由")
    private String opinioin;

    @ApiModelProperty("经办人")
    private String assignee;
}
