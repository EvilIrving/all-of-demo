package com.ygkj.project.vo.request;

import com.ygkj.entity.PageEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;


@Data
@ApiModel("山塘工程入参数")
public class MnpdProjectReq extends PageEntity implements Serializable {

    private static final long serialVersionUID = 4508326224134135036L;

    @ApiModelProperty("山塘名称")
    private String mnpdName;

    @ApiModelProperty("山塘类别: 屋顶山塘 、普通山塘、高坝山塘")
    private String mnpdCate;

    @ApiModelProperty("行政区划编码")
    private String areaCode;

    @ApiModelProperty("导出策略:0,大屏闸站组件详表导出")
    Integer exportStrategy = 0;

}
