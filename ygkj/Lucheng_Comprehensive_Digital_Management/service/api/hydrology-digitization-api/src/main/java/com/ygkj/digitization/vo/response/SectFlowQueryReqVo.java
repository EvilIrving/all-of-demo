package com.ygkj.digitization.vo.response;

import com.ygkj.entity.PageEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author xq
 * @Description
 * @Date 2021/7/31
 */
@Data
@ApiModel("断面流量查询入参")
public class SectFlowQueryReqVo extends PageEntity {

    private static final long serialVersionUID = 1518468859422445787L;

    @ApiModelProperty("流域")
    String bas;

    @ApiModelProperty("1：红色预警；2：红色预警")
    Integer warnType;

    @ApiModelProperty("断面名称")
    String sectName;

    @ApiModelProperty("导出策略")
    Integer exportStrategy = 0;


}
