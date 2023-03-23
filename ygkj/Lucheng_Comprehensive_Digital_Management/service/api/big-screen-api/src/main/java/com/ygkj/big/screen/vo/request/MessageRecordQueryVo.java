package com.ygkj.big.screen.vo.request;

import com.ygkj.entity.PageEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class MessageRecordQueryVo extends PageEntity {

    @ApiModelProperty("内容关键字")
    private String content;

    @ApiModelProperty("预警类型：暴雨/山洪/水库...")
    private String sendType;

    @ApiModelProperty("已发送，未发送")
    private String sendStatus;
    @ApiModelProperty("开始时间")
    private String start;
    @ApiModelProperty("结束时间")
    private String end;
}
