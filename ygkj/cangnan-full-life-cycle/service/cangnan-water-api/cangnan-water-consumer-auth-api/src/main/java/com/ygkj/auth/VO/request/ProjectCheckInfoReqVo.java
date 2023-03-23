package com.ygkj.auth.VO.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: huangzh
 * @Date: 2020/9/18 16:28
 * @Description:
 */
@Data
public class ProjectCheckInfoReqVo {

    @ApiModelProperty("项目id")
    private String id;

    @ApiModelProperty("批复或行政许可文号")
    private String licenseNo;

    @ApiModelProperty("批复日期(yyyy-MM-dd)")
    private String replyDate;

    @ApiModelProperty("批复（核发）机关")
    private String replyAgency;

    @ApiModelProperty("处(科)长意见")
    private String directorOpinion;

    @ApiModelProperty("签字(姓名)")
    private String sign;

    @ApiModelProperty("签字日期(yyyy-MM-dd)")
    private String signDate;

    @ApiModelProperty("经办意见")
    private String assigneeOpinion;

    @ApiModelProperty("领导意见")
    private String leaderOpinion;

    @ApiModelProperty("经办人")
    private String assignee;
}
