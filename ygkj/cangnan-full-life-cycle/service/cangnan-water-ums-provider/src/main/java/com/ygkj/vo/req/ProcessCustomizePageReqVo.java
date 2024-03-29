package com.ygkj.vo.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: huangzh
 * @Date: 2020/10/10 11:16
 * @Description: 流程定制分页请求参数
 */
@Data
public class ProcessCustomizePageReqVo {

    @ApiModelProperty(value = "第几页")
    private int pageNum=1;

    @ApiModelProperty(value = "分页数量")
    private int pageSize=20;

    @ApiModelProperty(value = "步骤(1企业申请，2经办人受理，3审批，4领导签批，5校对)")
    private Integer step;

}
