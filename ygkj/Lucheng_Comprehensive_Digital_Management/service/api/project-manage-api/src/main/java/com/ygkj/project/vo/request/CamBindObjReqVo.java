package com.ygkj.project.vo.request;

import com.ygkj.entity.PageEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author xq
 * @Description
 * @Date 2021/8/25
 */
@Data
@ApiModel("摄像头绑定对象查询入参")
public class CamBindObjReqVo extends PageEntity {
    private static final long serialVersionUID = -2152567206053623321L;
    @ApiModelProperty("对象类型")
    String projectType;

    @ApiModelProperty("对象名称")
    String projectName;

    @ApiModelProperty("对象编码")
    String projectCode;
}
