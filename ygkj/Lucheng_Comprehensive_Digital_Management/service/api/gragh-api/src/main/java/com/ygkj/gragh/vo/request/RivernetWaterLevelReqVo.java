package com.ygkj.gragh.vo.request;

import com.ygkj.entity.PageEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @author xq
 * @Description
 * @Date 2021/5/27
 */
@Data
@ApiModel("河网查询入参")
public class RivernetWaterLevelReqVo extends PageEntity {

    private static final long serialVersionUID = -1137615122378665550L;

    @ApiModelProperty("河网名称")
    String rnName;

    @ApiModelProperty("行政区划编码")
    String adcd;

    @ApiModelProperty("流域")
    String bas;

    @ApiModelProperty("主测站名称")
    String mainStName;

    @ApiModelProperty("复盘时刻：格式是\"yyyy-MM-dd HH:mm:ss\"，不传则为最新值")
    String dataTime;

    @ApiModelProperty("是否查询计算实时库容")
    boolean waterCapacity;

    @ApiModelProperty("警戒与实时库容插值")
    boolean warnStorage;

    @ApiModelProperty("排序column列名")
    String orderColumn;

    @ApiModelProperty("升序降序,false asc/true desc")
    Boolean ascOrDesc = false;

    @ApiModelProperty("导出策略：0：海塘组件列表导出")
    private Integer exportStrategy = 0;

    @ApiModelProperty(value = "排序sql", hidden = true)
    private String orderSql;
}
