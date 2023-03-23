package com.ygkj.gragh.vo.response;

import com.ygkj.gragh.model.AttStBase;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author xq
 * @Description
 * @Date 2021/5/29
 */
@Data
@ApiModel("河网复盘回参")
public class TideReplayResVo extends AttStBase {
    @ApiModelProperty("是否超警")
    boolean overWarn = false;

    @ApiModelProperty("超警时长")
    Integer overWarnMinutes;

    @ApiModelProperty("最高水位")
    Double highestRealTide;

    @ApiModelProperty("最高水位")
    Date highestRealTideTm;

    @ApiModelProperty("最低水位")
    Double lowestRealTide;

    @ApiModelProperty("最低水位")
    Date lowestRealTideTm;

    @ApiModelProperty("最高增水")
    Double highestSubTide;

    @ApiModelProperty("最高增水时刻")
    Date highestSubTideTm;

    @ApiModelProperty("天文潮位列表")
    List<StPrrwtResVo> astroTide;

    @ApiModelProperty("实时潮位列表")
    List<StPrrwtResVo> realTide;

    @ApiModelProperty("实时潮位列表")
    List<StPrrwtResVo> subTide;
}
