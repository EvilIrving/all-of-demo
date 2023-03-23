package com.ygkj.project.vo.request;

import com.ygkj.entity.PageEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel("提防工程入参数")
public class DikeProjectReq extends PageEntity implements Serializable {

    private static final long serialVersionUID = 5039290395810167916L;

    @ApiModelProperty("提防名称")
    private String dikeName;

    @ApiModelProperty("提防类型 1:1级 2:2级 3:3级 4:4级 5:5级")
    private String dikeDrad;

    @ApiModelProperty("行政区划编码")
    private String areaCode;

    @ApiModelProperty("导出策略:0,大屏灌区组件详表导出")
    Integer exportStrategy = 0;

}
