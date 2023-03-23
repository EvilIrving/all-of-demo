package com.ygkj.big.screen.vo.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author xq
 * @Description
 * @Date 2021/7/6
 */
@Data
@ApiModel("山洪演练入参")
public class MountainTorrentDrillReqVo implements Serializable {
    private static final long serialVersionUID = 668476232364586470L;

    @ApiModelProperty("降水预设值")
    List<MountainTorrentReqVo> presetList;

    @ApiModelProperty("数据时刻，等效于endTime,格式为:\"yyyy-MM-dd HH:mm:ss\" 数据时刻，不传为当前时刻")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    Date dataTime;

    /**
     * 降雨查询起始截止时刻，就是 dataTime往前推peroid时长的时刻，不得在startTime之前
     */
    Date startTime;

    /**
     * 指定的测站编码，多个测站编码用逗号隔开，用于查询这个测站对山洪监测点的影响
     */
    String stcds;
}
