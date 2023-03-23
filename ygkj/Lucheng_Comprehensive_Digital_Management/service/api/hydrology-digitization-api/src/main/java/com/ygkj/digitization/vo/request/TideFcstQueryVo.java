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
@ApiModel("风暴潮预报入参")
public class TideFcstQueryVo extends PageEntity {

    private static final long serialVersionUID = -5412083420088978408L;

    @ApiModelProperty("发布日期，时间yyyy-MM-dd HH:mm:ss，后面的HH:mm:ss 任意传，后端不会处理")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    Date releaseDate;

    @ApiModelProperty(value = "预报起始时刻")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    Date releaseStartTm;

    @ApiModelProperty(value = "预报截止时刻")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    Date releaseEndTm;

    @ApiModelProperty("发布类型1：手动预报，2自动预报")
    Integer releaseType;

    @ApiModelProperty("年份")
    Integer year;
}
