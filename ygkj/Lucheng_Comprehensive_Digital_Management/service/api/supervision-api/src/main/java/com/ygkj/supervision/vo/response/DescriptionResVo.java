package com.ygkj.supervision.vo.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @author lxl
 * @date 2021/05/11
 */
@Data
public class DescriptionResVo {


    /**
     * 问题描述编码
     */
    @ApiModelProperty("问题描述编码")
    private String descriptionCode;

    /**
     * 问题描述名称
     */
    @ApiModelProperty("问题描述名称")
    private String descriptionName;

    /**
     * 上级编码
     */
    @ApiModelProperty("上级编码")
    private String parentCode;

    @ApiModelProperty("问题描述所属文件编码")
    private String fileCode;

    /**
     * 问题责任主体
     */
    @ApiModelProperty("问题责任主体")
    private String dutyBody;

    /**
     * 问题分类 包含多个 用，隔开
     */
    @ApiModelProperty("问题分类 包含多个 用，隔开")
    private String problemType;

    /**
     * 备注
     */
    @ApiModelProperty("备注")
    private String formMark;

    /**
     * 相关法律法规
     */
    @ApiModelProperty("相关法律法规")
    private String relateLaw;

    /**
     * 法律法规标准
     */
    @ApiModelProperty("法律法规标准")
    private String lawStandard;

    /**
     * 问题描述
     */
    @ApiModelProperty("问题描述")
    private String problemDescription;

    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("子节点数据")
    private List<DescriptionResVo> childList = Arrays.asList();

}
