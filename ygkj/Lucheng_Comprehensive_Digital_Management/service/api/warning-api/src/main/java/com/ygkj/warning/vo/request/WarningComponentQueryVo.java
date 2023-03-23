package com.ygkj.warning.vo.request;

import com.ygkj.entity.PageEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel
@Data
public class WarningComponentQueryVo extends PageEntity {

    @ApiModelProperty("消息类型：提醒/预警")
    private String messageType;
    @ApiModelProperty("开始时间")
    private String startTime;
    @ApiModelProperty("结束时间")
    private String endTime;
    @ApiModelProperty("内容关键字，用于模糊搜索")
    private String content;
}
