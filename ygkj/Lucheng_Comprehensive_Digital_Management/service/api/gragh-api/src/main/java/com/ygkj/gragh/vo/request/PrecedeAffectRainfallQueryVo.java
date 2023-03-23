package com.ygkj.gragh.vo.request;

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
 * @Date 2021/9/24
 */
@Data
@ApiModel("前期影响雨量")
public class PrecedeAffectRainfallQueryVo extends PageEntity {

    private static final long serialVersionUID = 3255620316359544179L;

    @ApiModelProperty("行政区划编码")
    String areaCode;

    @ApiModelProperty("起始日期 yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    Date startDate;

    @ApiModelProperty("截止日期 yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    Date endDate;

    @ApiModelProperty("导出策略")
    Integer exportStrategy = 0;
}
