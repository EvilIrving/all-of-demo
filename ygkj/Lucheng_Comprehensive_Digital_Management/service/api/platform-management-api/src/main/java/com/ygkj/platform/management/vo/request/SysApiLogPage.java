package com.ygkj.platform.management.vo.request;

import com.ygkj.entity.PageEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel
public class SysApiLogPage extends PageEntity {

    /**
     * 用户
     */
    @ApiModelProperty("名称")
    private String userName;

    /**
     * 操作时间
     */
    @ApiModelProperty("操作时间")
    private String requestTime;
}
