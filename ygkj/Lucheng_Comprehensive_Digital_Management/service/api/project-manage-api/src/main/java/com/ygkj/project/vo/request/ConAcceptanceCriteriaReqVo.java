package com.ygkj.project.vo.request;

import com.ygkj.entity.PageEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ConAcceptanceCriteriaReqVo extends PageEntity {
    @ApiModelProperty("主键")
    private String id;


    @ApiModelProperty("验收事项")
    private String items;


    @ApiModelProperty("验收要求")
    private String requirement;


    @ApiModelProperty("标准分")
    private String standardScore;


    @ApiModelProperty("赋分标准")
    private String scoreCriteria;


    @ApiModelProperty("得分")
    private String score;


    @ApiModelProperty("扣分原因")
    private String reason;


    @ApiModelProperty("删除标记 0未删除 1已删除")
    private int delFlag;

    @ApiModelProperty("部门id")
    private String deptId;

}
