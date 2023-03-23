package com.ygkj.auth.VO.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author Huang.zh
 * @date 2020/9/16 9:26
 * @Description: 归档文件查询参数封装
 */
@Data
@ApiModel("归档文件查询参数")
public class AchievementFilePageQueryVO {

    @ApiModelProperty("归档目录下的文件id")
    private String achievementFileIds;

    @ApiModelProperty("文件名称")
    private String fileName;

    @ApiModelProperty(value = "第几页")
    private int pageNum=1;

    @ApiModelProperty(value = "分页数量")
    private int pageSize=10;
}
