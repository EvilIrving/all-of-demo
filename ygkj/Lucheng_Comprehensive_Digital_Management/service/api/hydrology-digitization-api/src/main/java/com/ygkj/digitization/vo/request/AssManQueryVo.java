package com.ygkj.digitization.vo.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author xq
 * @Description 日常业务管理-任务下达书市级界面查询参数
 * @Date 2021/2/1
 */
@Data
@ApiModel("日常业务管理-任务下达书市级界面查询参数")
public class AssManQueryVo {
    @ApiModelProperty("项目名称")
    String itemName;

    @ApiModelProperty("年份")
    Integer year;

    @ApiModelProperty("行政区划编码")
    String adcd;

    @ApiModelProperty("任务状态")
    Integer taskState;


    @ApiModelProperty("页码")
    int pageNum;
    @ApiModelProperty("页大小")
    int pageSize;

    @ApiModelProperty("文件名称")
    private String fileName;

}
