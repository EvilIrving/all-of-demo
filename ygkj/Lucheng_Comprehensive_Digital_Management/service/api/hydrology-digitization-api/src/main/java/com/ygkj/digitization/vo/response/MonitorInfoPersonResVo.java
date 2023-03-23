package com.ygkj.digitization.vo.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

/**
 * @author xq
 * @Description 排班信息人人员信息实体类
 * @Date 2021/1/14
 */
@Data
//@Builder
public class MonitorInfoPersonResVo {

    @ApiModelProperty("用户id")
    String userId;

    @ApiModelProperty("用户名称")
    String username;
}
