package com.ygkj.gragh.vo.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class WiuBaseResVo {

    @ApiModelProperty("取水户编码")
    private String wiuCode;

    @ApiModelProperty("取水户编码")
    private String wiuName;

    @ApiModelProperty("取水户类型")
    private String wiuType;

    @ApiModelProperty("计划总量")
    private BigDecimal watPlan;

    @ApiModelProperty("累计总量")
    private BigDecimal watUse;

    @ApiModelProperty("许可是否有用")
    private Boolean isWptValid;

    @ApiModelProperty("取用水户征信/国家标准组织机构代码")
    private String wrOrgCode;

    @ApiModelProperty("取水起始日期")
    private Date intBd;

    @ApiModelProperty("取水终止日期")
    private Date intDl;

}
