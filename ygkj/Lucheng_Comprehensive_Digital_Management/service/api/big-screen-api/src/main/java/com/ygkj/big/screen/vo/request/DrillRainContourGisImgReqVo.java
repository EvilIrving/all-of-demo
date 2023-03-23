package com.ygkj.big.screen.vo.request;

import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * @author xq
 * @Description
 * @Date 2021/6/23
 */
@Data
@ApiModel("")
public class DrillRainContourGisImgReqVo {
//    String param;
//    String rainfall_newsest_1_rcurl;
//    String rainfall_1_rcurl;
//    String rainfall_3_rcurl;
//    String rainfall_6_rcurl;
//    String rainfall_24_rcurl;
//    String rainfall_72_rcurl;

    String areaName;
    String type;
    String endTime;
    String url;

}
