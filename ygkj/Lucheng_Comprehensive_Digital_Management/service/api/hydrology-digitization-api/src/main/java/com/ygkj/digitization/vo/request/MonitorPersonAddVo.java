package com.ygkj.digitization.vo.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author xq
 * @Description 新建值班人员接收参数
 * @Date 2021/1/14
 */
@Data
@ApiModel("新建值班人员接收参数")
public class MonitorPersonAddVo {
    @ApiModelProperty("用户ID集合")
    List<String> userIdList;

    @ApiModelProperty("人员类型：1：副班人员，2：值班人员，3：带班领导")
    Integer personType;
}
