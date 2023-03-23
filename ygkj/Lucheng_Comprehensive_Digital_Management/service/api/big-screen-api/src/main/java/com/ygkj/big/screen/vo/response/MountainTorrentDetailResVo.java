package com.ygkj.big.screen.vo.response;

import com.ygkj.big.screen.model.AttPrevTfBase;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author xq
 * @Description
 * @Date 2021/7/5
 */
@Data
@ApiModel("山洪防治对象详情")
public class MountainTorrentDetailResVo extends AttPrevTfBase {
    private static final long serialVersionUID = 7534179510746787914L;

    @ApiModelProperty("指标列表")
    List<MountainTorrentIndicatorResVo> indicatorList;
}
