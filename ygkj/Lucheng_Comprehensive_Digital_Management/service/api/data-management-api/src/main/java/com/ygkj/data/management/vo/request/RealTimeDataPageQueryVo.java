package com.ygkj.data.management.vo.request;

import com.ygkj.entity.PageEntity;
import com.ygkj.enums.StationType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import javax.validation.constraints.NotBlank;

@ApiModel
public class RealTimeDataPageQueryVo extends PageEntity {

    @ApiModelProperty("测站名称")
    private String stationName;

    @ApiModelProperty("测站类型")
    @NotBlank(message = "请指定一种测站类型！")
    private String stationType;

    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    public String getStationType() {
        return stationType;
    }

    public void setStationType(String stationType) {
        this.stationType = stationType;
        checkStationType();
    }

    private void checkStationType() {
        if (StringUtils.isNotBlank(stationType) && StationType.match(stationType) == null) {
            throw new IllegalArgumentException("非法参数！");
        }
    }
}
