package com.ygkj.supervision.vo.request;

import com.ygkj.supervision.model.AttSupervisionMatterPerson;
import com.ygkj.supervision.model.AttSupervisionProblem;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author lxl
 */
@Data
@ApiModel
public class InsertProblemReqVo {

    @ApiModelProperty("检查事项类型")
    private String matterType;

    @ApiModelProperty("所属项目名称")
    private String projectName;

    @ApiModelProperty("所属项目编码")
    private String projectCode;

    @ApiModelProperty("检查时间")
    private String censorDate;

    @ApiModelProperty("检查人员")
    private List<AttSupervisionMatterPerson> matterPersons;

    @ApiModelProperty("被检查人员Id")
    private String censorPerson;

    @ApiModelProperty("被检查人员姓名")
    private String censorName;

    @ApiModelProperty("被检查人类别  0-内部  1-外部")
    private String censorType;

    @ApiModelProperty("被检查人员手机号码")
    private String censorPhone;

    @ApiModelProperty("问题列表")
    private List<AttSupervisionProblem> problemList;

    @ApiModelProperty("检查事项编码-更新使用")
    private String MatterCode;

    @ApiModelProperty("外部被检查人职务")
    private String censorJob;

    @ApiModelProperty("外部被检查人单位")
    private String censorUnit;


}
