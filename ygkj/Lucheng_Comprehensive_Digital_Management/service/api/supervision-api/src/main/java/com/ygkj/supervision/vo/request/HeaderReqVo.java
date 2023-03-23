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
public class HeaderReqVo extends PageEntity {

    @ApiModelProperty("表头编码 - 查询单条记录使用")
    private String formCode;

    @ApiModelProperty("表头名称")
    private String formName;

    @ApiModelProperty("所属部门")
    private String branchName;

    @ApiModelProperty("所属部门编码")
    private String branchCode;

    @ApiModelProperty("检查事项类型")
    private String matterType;

    @ApiModelProperty("内容名称 --5/11 暂定查询字段内容")
    private String contentName;


}
