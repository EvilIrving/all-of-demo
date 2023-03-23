package com.ygkj.auth.VO.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author: fml
 * @Date: 2020/8/24 17:59
 * @Description:
 */

@Data
public class CompanyAddReqVO {

    @ApiModelProperty(value = "企业单位名称")
    @NotBlank(message = "企业单位名称不能为空")
    private String companyName;

    @ApiModelProperty(value = "统一社会信用代码")
    @NotBlank(message = "统一社会信用代码不能为空")
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

}
