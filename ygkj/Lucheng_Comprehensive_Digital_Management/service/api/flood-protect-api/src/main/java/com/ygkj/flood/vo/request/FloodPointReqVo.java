package com.ygkj.flood.vo.request;

import com.ygkj.entity.PageEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @author xq
 * @Description
 * @Date 2021/5/10
 */
@Data
@ApiModel("积水点查询-入参")
public class FloodPointReqVo extends PageEntity {
    private static final long serialVersionUID = -1891264667254889225L;
    @ApiModelProperty("积水点名称，多个名称用半角逗号隔开")
    String name;

    @ApiModelProperty("积水点名称关键字，用于搜索")
    String name4Search;

    @ApiModelProperty("往前推天数")
    Integer recentDays;

    @ApiModelProperty("行政区划编码")
    String areaCode;

    @ApiModelProperty("数据时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    Date dataTime;

    @ApiModelProperty("是否有积水")
    Boolean ponding;

    @ApiModelProperty("排序column列名")
    String orderColumn;

    @ApiModelProperty("升序降序,false asc/true desc")
    Boolean ascOrDesc = false;

    @ApiModelProperty("导出策略:0,大屏灌区组件详表导出")
    Integer exportStrategy = 0;
}
