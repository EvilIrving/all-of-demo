package com.ygkj.big.screen.vo.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import lombok.Data;

/**
 * @author xq
 * @Description
 * @Date 2021/5/25
 */
@Data
@ApiModel("海塘列表查询入参")
public class SeawallListQueryReqVo {

    @ApiModelProperty("海塘名称")
    String seawallName;

    @ApiModelProperty("行政区划编码")
    String adcd;

    @ApiModelProperty("工程等别：1级、2级、3级、4级、5级")
    String grade;

    @ApiModelProperty("100年以上、100年一遇、50年一遇、20年一遇、10年一遇、10年以下")
    String designTideStandard;

    @ApiModelProperty("排序column列名")
    String orderColumn;

    @ApiModelProperty("升序降序,false asc/true desc")
    Boolean ascOrDesc = false;
}
