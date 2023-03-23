package com.ygkj.river.vo.request;

import com.ygkj.entity.PageEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author xq
 * @Description
 * @Date 2021/8/7
 */
@Data
@ApiModel("河段查询入参")
public class RiverQueryReqVo extends PageEntity {

    private static final long serialVersionUID = 5559503406983245050L;

    @ApiModelProperty("河段名称")
    String riverName;

    @ApiModelProperty("行政区划编码")
    String areaCode;

    @ApiModelProperty("河道编码")
    String riverNo;

    @ApiModelProperty("河道等级2:省级;3:市级;4:县级;5:镇级;6:村级")
    Integer level;

    @ApiModelProperty("河段等级2:省级;3:市级;4:县级;5:镇级;6:村级")
    Integer areaLevel;

    @ApiModelProperty("1绿码、2黄码、3红码")
    Integer healthState = 1;

    @ApiModelProperty("是否带河道路径")
    boolean withPathStr = false;

    @ApiModelProperty("是否带河长信息")
    boolean withChiefInfo = false;
}
