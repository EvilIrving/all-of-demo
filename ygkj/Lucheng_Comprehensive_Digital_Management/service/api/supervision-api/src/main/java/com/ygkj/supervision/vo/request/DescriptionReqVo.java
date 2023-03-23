package com.ygkj.supervision.vo.request;


import com.ygkj.entity.PageEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author lxl
 * @date 2021/05/11
 */
@Data
@ApiModel
public class DescriptionReqVo extends PageEntity {

    @ApiModelProperty("问题描述编码 --查询单条使用")
    private String descriptionCode;

    @ApiModelProperty("问题描述所属文件编码")
    private String fileCode;

    @ApiModelProperty("检查事项编码")
    private String matterType;

    /**
     * 问题描述
     */
    @ApiModelProperty("问题描述")
    private String problemDescription;
}
