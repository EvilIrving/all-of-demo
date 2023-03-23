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
public class FileReqVo extends PageEntity {


    /**
     * 表单名称
     */
    @ApiModelProperty("表单名称")
    private String formName;

    /**
     * 表单编码
     */
    @ApiModelProperty("表单编码")
    private String formCode;

    /**
     * 文件名称
     */
    @ApiModelProperty("文件名称")
    private String fileName;

    /**
     * 文件编码
     */
    @ApiModelProperty("文件编码")
    private String fileCode;

    @ApiModelProperty("部门名称")
    private String branchName;

    @ApiModelProperty("检查事项")
    private String matterType;


    @ApiModelProperty("开始时间")
    private String start;

    @ApiModelProperty("结束时间")
    private String end;

}
