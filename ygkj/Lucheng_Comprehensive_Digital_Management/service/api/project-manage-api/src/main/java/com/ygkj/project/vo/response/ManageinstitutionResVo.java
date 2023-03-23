package com.ygkj.project.vo.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ManageinstitutionResVo {

    @ApiModelProperty("名称")
    private String name;

    @ApiModelProperty("附件id,多个的会用逗号分隔,数字就通过这个判断个数")
    private String aid;
}
