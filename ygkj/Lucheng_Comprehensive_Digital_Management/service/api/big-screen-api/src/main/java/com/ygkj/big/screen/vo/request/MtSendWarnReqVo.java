package com.ygkj.big.screen.vo.request;

import com.ygkj.big.screen.vo.response.MtFcstMonValueDetailResVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author xq
 * @Description
 * @Date 2021/10/11
 */
@Data
@ApiModel("山洪报警通知入参")
public class MtSendWarnReqVo {

    @ApiModelProperty("批量报警通知入参")
    List<MtFcstMonValueDetailResVo> warnList;

    @ApiModelProperty("单个报警通知入参")
    MtFcstMonValueDetailResVo warnDetail;

    @ApiModelProperty("1：过去；2：现在；3：将来")
    Integer warnType;
}
