package com.ygkj.big.screen.vo.response;

import com.ygkj.utils.StringUtils;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @author xq
 * @Description
 * @Date 2021/7/13
 */
@Data
public class CurTyphoonReqVo {

    @ApiModelProperty("台风编码")
    private String code;

    @ApiModelProperty("经度")
    private Double lng;

    @ApiModelProperty("纬度")
    private Double lat;

    @ApiModelProperty("东南")
    private Double se;

    @ApiModelProperty("西南")
    private Double sw;

    @ApiModelProperty("东北")
    private Double ne;

    @ApiModelProperty("西北")
    private Double nw;

    @ApiModelProperty("时刻")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date time;

    @ApiModelProperty("风力等级")
    private Integer power;

    public boolean isAllNull() {
        return StringUtils.isEmpty(code)
                && lng == null
                && lat == null
                && se == null
                && sw == null
                && nw == null
                && time == null;
    }
}
