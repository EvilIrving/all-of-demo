package com.ygkj.vo.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author: fml
 * @Date: 2020/8/24 17:59
 * @Description:
 */

@Data
public class CompanyUpdateReqVO {

    @ApiModelProperty(value = "企业id")
    @NotBlank(message = "企业id不能为空")
    private String id;

    @ApiModelProperty(value = "企业单位名称")
    private String companyName;

    @ApiModelProperty(value = "统一社会信用代码")
    private String socialCreditCode;

    @ApiModelProperty(value = "行政区划")
    private String areaCode;

    @ApiModelProperty(value = "所在地")
    private String areaAddr;

    @ApiModelProperty(value = "管理员姓名")
    private String managerName;

    @ApiModelProperty(value = "手机号")
    private String phone;

    @ApiModelProperty(value = "行业类型数据")
    private String industryCategory;

    @ApiModelProperty(value = "审核状态(1未审核、2已通过、3已驳回、4停用)")
    private String status;
}
