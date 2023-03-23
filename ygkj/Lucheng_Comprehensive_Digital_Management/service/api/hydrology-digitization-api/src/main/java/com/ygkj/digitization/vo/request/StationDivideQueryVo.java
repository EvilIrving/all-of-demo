package com.ygkj.digitization.vo.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class StationDivideQueryVo {

    @ApiModelProperty("站点类型：RR水库水位站，ZZ河道水位站,PP雨量站，DD闸坝水位站，TT潮位站，ZG地下水位站，ZQ流量站")
    private String stationType;
    @ApiModelProperty("集合类型：流域/行政区划")
    private String collectionType;

}
