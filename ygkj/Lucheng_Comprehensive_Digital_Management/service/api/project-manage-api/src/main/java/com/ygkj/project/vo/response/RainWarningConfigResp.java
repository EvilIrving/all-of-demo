package com.ygkj.project.vo.response;

import com.ygkj.project.model.WarnUserInfo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class RainWarningConfigResp implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("报警配置信息列表")
    private List<RainWarningConfig> rainWarningConfigs;

    @ApiModelProperty("报警接收人信息")
    private List<WarnUserInfo> userInfos;
}
