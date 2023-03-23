package com.ygkj.big.screen.vo.request;

import com.ygkj.entity.PageEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @author xq
 * @Description
 * @Date 2021/5/26
 */
@Data
@ApiModel("水灾害防御-温瑞平水闸列表查询入参")
public class WrpWagaQueryReqVo extends PageEntity {

    @ApiModelProperty("水闸编码")
    String wagaCode;

    @ApiModelProperty("水闸名称")
    String wagaName;

    @ApiModelProperty("行政区划")
    String areaCode;

    @ApiModelProperty("流域")
    String bas;

    @ApiModelProperty("工程规模")
    String engScal;

    @ApiModelProperty("是否筛选开闸的工程")
    Boolean filterOpenGate;

    @ApiModelProperty("小时数")
    Integer hours;

    @ApiModelProperty("复盘时刻")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    Date dataTime;

    @ApiModelProperty("排序column列名")
    String orderColumn;

    @ApiModelProperty("升序降序,false asc/true desc")
    Boolean ascOrDesc = false;
}
