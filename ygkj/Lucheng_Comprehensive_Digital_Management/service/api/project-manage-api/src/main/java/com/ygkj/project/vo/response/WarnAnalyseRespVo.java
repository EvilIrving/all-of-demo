package com.ygkj.project.vo.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.ygkj.project.model.WarnUserInfo;
import com.ygkj.project.model.WarningRecordNew;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

@Data
@JsonIgnoreProperties(value = {"handler"})
public class WarnAnalyseRespVo extends WarningRecordNew {

    @ApiModelProperty("报警类型")
    private String warningType;

    @ApiModelProperty("报警方式")
    private String pattren;

    @ApiModelProperty("阈值")
    private String threshold;

    @ApiModelProperty("流量阈值")
    private String flowThreshold;

    @ApiModelProperty("平均蓄水量")
    private String avgCapacity;

    @ApiModelProperty("测站名")
    private String stName;

    @ApiModelProperty("测站类型")
    private String stType;

    @ApiModelProperty("汛限水位")
    private Double limitLevel;

    @ApiModelProperty("防洪高水位")
    private Double floodLevel;

    @ApiModelProperty("保证水位")
    private Double guaranteeLevel;

    @ApiModelProperty("警戒水位")
    private Double warningLevel;

    @ApiModelProperty("蓝色警戒水位")
    private Double blueLevel;

    @ApiModelProperty("黄色警戒水位")
    private Double yellowLevel;

    @ApiModelProperty("橙色警戒水位")
    private Double orangeLevel;

    @ApiModelProperty("红色警戒水位")
    private Double redLevel;

    private String cid;

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
