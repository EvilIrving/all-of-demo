package com.ygkj.big.screen.vo.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author hucong
 * @version 1.0
 * @date 2021/6/16 20:13
 */
@Data
@ApiModel("整改完成回参")
public class RectificationCompletedResp implements Serializable {
    @ApiModelProperty("完成时间")
    private String completeDate;
    @ApiModelProperty("整改成果描述")
    private String recCompleteDescribe;
    @ApiModelProperty("首轮审核人")
    private String firstCheckPerson;
    @ApiModelProperty("最终审核人")
    private String finalCheckPerson;
    @ApiModelProperty("相关图片List")
    private List<FileDetail> imageList;
}
