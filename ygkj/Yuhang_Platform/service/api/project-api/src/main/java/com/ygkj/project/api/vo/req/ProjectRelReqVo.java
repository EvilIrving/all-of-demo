package com.ygkj.project.api.vo.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@Data
public class ProjectRelReqVo {

    @ApiModelProperty("工程编码")
    private String prcd;

    @ApiModelProperty("相关工程或站点类型 1水闸 2泵站 3闸站 4水位 5雨量 6摄像头,多个用逗号分隔")
    private String relTypes;

    @ApiModelProperty(hidden = true)
    List<Integer> relTypeList;

    public void setRelTypes(String relTypes) {
        this.relTypes = relTypes;
        if (relTypes != null && relTypes.length() > 0) {
            List<String> list = Arrays.asList(relTypes.split(","));
            relTypeList = new ArrayList(list.size());
            for (String relType : list) {
                relTypeList.add(Integer.valueOf(relType));
            }
        }
    }

}
