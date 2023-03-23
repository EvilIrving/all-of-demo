package com.ygkj.project.vo.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class RuleStReqVo {

    @ApiModelProperty("测站类型 1水位 2雨量 3流量")
    private Integer stType;

    @ApiModelProperty("测站名称")
    private String stName;

    @ApiModelProperty(hidden = true)
    private List<String> stTypeList;

    public void setStType(Integer stType){
        this.stType = stType;
        stTypeList = new ArrayList<>();
        switch (stType){
            case 1:
                stTypeList.add("ZZ");
                stTypeList.add("RR");
                stTypeList.add("TT");
                stTypeList.add("DD");
                break;
            case 2:
                stTypeList.add("PP");
                break;
            case 3:
                stTypeList.add("ZQ");
                stTypeList.add("DD");
        }
    }
}
