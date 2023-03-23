package com.ygkj.big.screen.vo.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @author xq
 * @Description
 * @Date 2021/5/25
 */
@Data
@ApiModel("潮位列表查询入参")
public class TideListQueryReqVo {

    @ApiModelProperty("测站名称")
    String stName;

    @ApiModelProperty("行政区划编码")
    String adcd;

    @ApiParam("流域")
    String bas;

    @ApiParam("复盘时刻：格式是\"yyyy-MM-dd HH:mm:ss\"，不传则为最新值")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    Date dataTime;

    @ApiModelProperty("排序column列名")
    String orderColumn;

    @ApiModelProperty("升序降序,false asc/true desc")
    Boolean ascOrDesc = false;
}
