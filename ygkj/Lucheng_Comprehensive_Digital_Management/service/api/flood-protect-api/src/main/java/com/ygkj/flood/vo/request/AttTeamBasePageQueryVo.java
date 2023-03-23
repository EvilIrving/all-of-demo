package com.ygkj.flood.vo.request;

import com.ygkj.entity.PageEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class AttTeamBasePageQueryVo extends PageEntity {

    private static final long serialVersionUID = 2304961528213694515L;

    @ApiModelProperty("行政区划编码")
    private String areaCode;

    @ApiModelProperty("队伍类型id")
    private String typeId;

    @ApiModelProperty("队伍名称")
    private String name;

    @ApiModelProperty("单位性质")
    private String unitNature;


    @ApiModelProperty("抢险专业")
    private String rescueSpecialty;


    @ApiModelProperty("id，查询单条用")
    private String id;

    @ApiModelProperty("排序column列名")
    String orderColumn;

    @ApiModelProperty("升序降序,false asc/true desc")
    Boolean ascOrDesc =false;

    @ApiModelProperty("导出策略：0，大屏组件导出")
    Integer exportStrategy=0;

}
