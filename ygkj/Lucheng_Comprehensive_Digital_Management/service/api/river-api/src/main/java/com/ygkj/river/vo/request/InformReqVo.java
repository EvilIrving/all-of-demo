package com.ygkj.river.vo.request;

import com.ygkj.entity.PageEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author lxl
 * @create 2022-07-12 9:52
 * @description
 */
@Data
@ApiModel("项目举报请求类")
public class InformReqVo extends PageEntity {


    @ApiModelProperty("举报内容")
    private String informContent;


    @ApiModelProperty("工程id")
    @NotNull(message = "工程编码不能为空")
    private String projectId;


    @ApiModelProperty("主键")
    private String id;


}
