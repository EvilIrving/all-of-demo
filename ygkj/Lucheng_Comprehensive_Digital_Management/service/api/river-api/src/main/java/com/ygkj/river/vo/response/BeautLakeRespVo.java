package com.ygkj.river.vo.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@ApiModel("美丽河湖工程数量统计返回参数")
@Data
@NoArgsConstructor
public class BeautLakeRespVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("工程总数")
    private Integer projectTotalNum;

    @ApiModelProperty("项目在建(总)")
    private Integer underConstructionNum;

    @ApiModelProperty("已完工(总)")
    private Integer completeNum;

    @ApiModelProperty("美丽河湖建设数量")
    private Integer beautLakeNum;

    @ApiModelProperty("美丽河湖建设总体形象进展")
    private String beautLakeProgress;


    @ApiModelProperty("农村池塘数量")
    private Integer ruralPondNum;

    @ApiModelProperty("农村池塘总体形象进展")
    private String ruralPondNumProgress;


    @ApiModelProperty("水美乡镇数量")
    private Integer beautVillageNum;

    @ApiModelProperty("水美乡镇总体形象进展")
    private String beautVillageNumProgress;

    @ApiModelProperty("中小河流数量")
    private Integer smallMediumRiverNum;

    @ApiModelProperty("中小河流总体形象进展")
    private String smallMediumRiverNumProgress;

}
