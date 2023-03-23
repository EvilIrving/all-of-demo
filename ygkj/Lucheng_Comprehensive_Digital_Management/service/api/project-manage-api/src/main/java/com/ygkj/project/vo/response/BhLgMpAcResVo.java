package com.ygkj.project.vo.response;

import com.ygkj.project.model.BhLgMpAc;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class BhLgMpAcResVo extends BhLgMpAc {

    @ApiModelProperty("带队检查人名称")
    private String checkName;

    @ApiModelProperty("参加人员名称")
    private String joinName;
}
