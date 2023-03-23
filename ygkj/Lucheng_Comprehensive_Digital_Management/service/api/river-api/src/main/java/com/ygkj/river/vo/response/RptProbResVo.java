package com.ygkj.river.vo.response;

import com.ygkj.river.model.RiverInfo;
import com.ygkj.river.model.RiverPatrolTaskEventProb;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author xq
 * @Description
 * @Date 2021/4/7
 */
@Data
@ApiModel("任务巡河-问题回参")
public class RptProbResVo extends RiverPatrolTaskEventProb {
    /**
     * 河长级别
     */
    @ApiModelProperty("河长级别")
    private String riverManagerGrade;

    /**
     * 河长姓名
     */
    @ApiModelProperty("河长姓名")
    private String riverManagerName;

    /**
     * 河长职务
     */
    @ApiModelProperty("河长职务")
    private String riverManagerPhone;
}
