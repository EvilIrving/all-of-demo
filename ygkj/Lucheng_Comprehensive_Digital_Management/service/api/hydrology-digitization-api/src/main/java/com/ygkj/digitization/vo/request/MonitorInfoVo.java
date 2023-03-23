package com.ygkj.digitization.vo.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

/**
 * @author xq
 * @Description 排班信息接受参数
 * @Date 2021/1/14
 */
@Data
@ApiModel("排班信息")
public class MonitorInfoVo {

    @ApiModelProperty("排班信息id，当id不为空的时候，更新，否则插入")
    Integer id;

    /**
     * 排班日期，精确到日
     */
    @ApiModelProperty("排班日期，精确到日,unix timestamp")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(
            pattern = "yyyy-MM-dd",
            timezone = "GMT+8"
    )
    Date monitorDate;
    /**
     * 排班领导id集合
     */
    @ApiModelProperty("排班领导id集合")
    List<String> dbldIdList;
    /**
     * 值班人员id集合
     */
    @ApiModelProperty("值班人员id集合")
    List<String> zbryIdList;
    /**
     * 副班人员id集合
     */
    @ApiModelProperty("副班人员id集合")
    List<String> fbryIdList;
}
