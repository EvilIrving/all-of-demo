package com.ygkj.supervision.vo.response;

import com.ygkj.supervision.model.AttSupervisionMatterPerson;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author lxl
 */
@Data
@ApiModel("检查事项回参")
public class MatterResVo {

    /**
     * 检查事项，检查时间，检查人员，存在问题数量，追究类型，整改回复意见，整改附件，整改率
     */

    /**
     * 事项编码
     */
    @ApiModelProperty("事项编码")
    private String matterCode;

    /**
     * 检查事项（固定类别）
     */
    @ApiModelProperty("检查事项（固定类别）")
    private String matterType;

    /**
     * 检查时间
     */
    @ApiModelProperty("检查时间")
    private Date censorDate;

    /**
     * 检查人员
     */
    @ApiModelProperty("检查人员")
    private List<AttSupervisionMatterPerson> matterPersons;

    @ApiModelProperty("流程id")
    private String processId;

    @ApiModelProperty("存在问题数量")
    private Integer problemTotal;

    @ApiModelProperty("检查人次")
    private Integer personTotal;

    /**
     * 追究类型
     */
    @ApiModelProperty("追究类型")
    private String investigateType;

    @ApiModelProperty("已整改问题数量")
    private Integer rectificationTotal;

    @ApiModelProperty("未整改问题数量")
    private Integer unRectificationTotal;

    @ApiModelProperty("整改率")
    private Double rectifyRate;

    @ApiModelProperty("工程名称")
    private String projectName;

    @ApiModelProperty("工程编码")
    private String projectCode;

    @ApiModelProperty("发布人id")
    private String createId;

    @ApiModelProperty("发布人名称")
    private String createName;

    @ApiModelProperty("被检查人员电话")
    private String censorPhone;

    @ApiModelProperty("被检查人员姓名")
    private String censorName;

    @ApiModelProperty("事项状态")
    private String matterStatus;

    @ApiModelProperty("被检查人员id")
    private String censorPerson;

}
