package com.ygkj.data.management.vo.request;

import com.ygkj.entity.PageEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
@ApiModel("审批历史记录查询参数封装")
public class BusColumnHistoryQueryVo extends PageEntity {

    @ApiModelProperty("数据表id")
    private String tableId;

    @ApiModelProperty("列名")
    private String columnName;

    @ApiModelProperty("主键值")
    private String primary;

    @ApiModelProperty("提交人")
    private String subimtUser;

    @ApiModelProperty("开始时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;

    @ApiModelProperty("结束时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;

    @ApiModelProperty(value = "关键字")
    private String keyWords;

    // 状态
    @ApiModelProperty(hidden = true)
    private String status;
}
