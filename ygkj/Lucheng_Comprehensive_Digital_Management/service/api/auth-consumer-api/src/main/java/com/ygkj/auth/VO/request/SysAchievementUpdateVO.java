package com.ygkj.auth.VO.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author Huang.zh
 * @date 2020/9/15 19:03
 * @Description: 归档修改参数
 */
@Data
public class SysAchievementUpdateVO {

    @NotNull(message = "id不允许为空！")
    @ApiModelProperty("主键")
    private String id;

    @ApiModelProperty("归档名称")
    private String name;

    @ApiModelProperty("父级id")
    private String parentId;

    @ApiModelProperty("附件id，多个用逗号隔开")
    private String fileIds;

    @ApiModelProperty("工程编码")
    private String projectCode;
}
