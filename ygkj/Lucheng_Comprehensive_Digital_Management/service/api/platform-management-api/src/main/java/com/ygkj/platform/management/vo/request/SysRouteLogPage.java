package com.ygkj.platform.management.vo.request;

import com.ygkj.entity.PageEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@ApiModel
public class SysRouteLogPage extends PageEntity {

    @ApiModelProperty("用户名称")
    private String userName;
    @ApiModelProperty("访问时间：yyyy-MM-dd")
    private String routeTime;
    @ApiModelProperty("访问类型：快捷/浙政钉/微信")
    private String interviewMethod;
    @ApiModelProperty("开始时间：yyyy-MM-dd")
    private String start;
    @ApiModelProperty("结束时间：yyyy-MM-dd")
    private String end;

}
