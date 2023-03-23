package com.ygkj.digitization.vo.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@Data
@ApiModel("关联基本信息入参")
public class RelBaseReqVo {

    @ApiModelProperty("主键")
    private String id;

    @ApiModelProperty("关联名称")
    private String relName;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("关联类型 1旱情关联 2水位关联")
    private Integer type;

    @ApiModelProperty(value = "部门id", hidden = true)
    private String deptId;

    @ApiModelProperty(value = "关联水库编码或者关联测站的编码")
    private String codes;

    public void setCodes(String codes) {
        this.codes = codes;
        list = Arrays.asList(codes.split(","));
    }

    @ApiModelProperty(value = "关联水库编码或者关联测站的编码", hidden = true)
    private List<String> list;

}
