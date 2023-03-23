package com.ygkj.auth.VO.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author Huang.zh
 * @date 2020/9/15 17:55
 * @Description:
 */
@Data
@ApiModel("归档新增参数")
public class SysAchievementAddVO {
    @ApiModelProperty("归档名称")
    private String name;
    @ApiModelProperty("文件id")
    private String fileIds;
    @ApiModelProperty("父级id")
    private String parentId;
    @ApiModelProperty(value = "工程编码")
    private String projectCode;
}
