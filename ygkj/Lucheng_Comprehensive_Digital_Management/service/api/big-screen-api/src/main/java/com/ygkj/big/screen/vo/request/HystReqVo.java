package com.ygkj.big.screen.vo.request;

import com.ygkj.entity.PageEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel
public class HystReqVo extends PageEntity {

    /**
     * 行政区划编码
     */
    @ApiModelProperty("行政区划编码")
    private String areaCode;

    /**
     * 水电站类别
     */
    @ApiModelProperty("水电站类别 0-混合式  1-坝式（河坝）  2-引水式  3-坝式（坝后）")
    private String hystType;

    /**
     * 搜索关键字
     */
    @ApiModelProperty("搜索关键字")
    private String key;

}
