package com.ygkj.digitization.vo.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ygkj.entity.PageEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @author xq
 * @Description
 * @Date 2021/9/22
 */
@Data
@ApiModel("水文预报查询入参")
public class HydrFcstQueryVo extends PageEntity {
    private static final long serialVersionUID = -1335222743306339721L;
    @ApiModelProperty("id")
    String id;

    @ApiModelProperty("测站名称")
    String stName;

    @ApiModelProperty("测站编码")
    String stCode;

    @ApiModelProperty("测站类型")
    String stType;

    @ApiModelProperty("发布日期，时间yyyy-MM-dd HH:mm:ss，后面的HH:mm:ss 任意传，后端不会处理")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    Date releaseDate;

    @ApiModelProperty(value = "发布日期起始时刻", hidden = true)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    Date releaseStartTm;

    @ApiModelProperty(value = "发布日期截止时刻", hidden = true)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    Date releaseEndTm;

    @ApiModelProperty("是否带上预报数据，默认为false")
    boolean withFcstVal = false;
}
