package com.ygkj.data.management.vo.request;

import com.ygkj.utils.StringUtils;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class SysDataManagementPermissionQueryVo {

    private List<String> ids;

    private String type;

    private String tableId;

    @ApiModelProperty("业务类型 BASIC 基础数据  REALTIME 实时数据")
    private String busType;

    public String getBusType() {
        return StringUtils.isEmpty(this.busType) ? "BASIC" : this.busType;
    }

}
