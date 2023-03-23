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
    private int pageNum = 1;

    @ApiModelProperty(value = "分页数量")
    private int pageSize = 10;

    @ApiModelProperty(value = "是否查询全部记录，true：是，false：否")
    private boolean fetchAll;

    @ApiModelProperty("父文件id")
    private String parentId;

    @ApiModelProperty("主键id")
    private String id;

    /**
     * 是否允许为null 0: 允许 1:不允许
     */
    @ApiModelProperty("是否允许为null 0: 允许 1:不允许")
    private String allowNull;

    @ApiModelProperty("工程编码")
    private String projectCode;
}
