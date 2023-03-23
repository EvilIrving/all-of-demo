package com.ygkj.project.vo.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.ygkj.project.model.WarnUserInfo;
import com.ygkj.project.model.WarningConfig;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

@Data
@JsonIgnoreProperties(value = {"hibernateLazyInitializer", "handler"})
public class RainWarningConfig extends WarningConfig {

    @ApiModelProperty("报警配置id")
    private String configId;

    @ApiModelProperty("降雨历时")
    private String rainfallPeriod;

    @ApiModelProperty("阈值")
    private Double threshold;

    @ApiModelProperty("测站代码")
    private String stCode;

    @ApiModelProperty("站点名称")
    private String stName;

    @ApiModelProperty("汛限水位")
    private Double limitLevel;

    @ApiModelProperty("防洪高水位")
    private Double floodLevel;

    @ApiModelProperty("警戒水位")
    private Double warningLevel;

    @ApiModelProperty("保证水位")
    private Double guaranteeLevel;

    @ApiModelProperty("蓝色警戒水位")
    private Double blueLevel;

    @ApiModelProperty("黄色警戒水位")
    private Double yellowLevel;

    @ApiModelProperty("橙色警戒水位")
    private Double orangeLevel;

    @ApiModelProperty("红色警戒水位")
    private Double redLevel;

    @ApiModelProperty("报警条件")
    private String warnCondition;

    @ApiModelProperty("流量阈值m³/s")
    private Double flowThreshold;

    @ApiModelProperty("平均蓄水量m³")
    private Double avgCapacity;

    @ApiModelProperty("报警接收人信息")
    private List<WarnUserInfo> userInfos;

    @ApiModelProperty(value = "报警接收人信息", hidden = true)
    private String userNames;

    public void setUserInfos(List<WarnUserInfo> userInfos) {
        this.userInfos = userInfos;
        this.userNames = userInfos.stream().filter(user -> !StringUtils.isEmpty((user.getUserName())))
                .map(user -> user.getUserName()).collect(Collectors.joining(","));
    }

}
