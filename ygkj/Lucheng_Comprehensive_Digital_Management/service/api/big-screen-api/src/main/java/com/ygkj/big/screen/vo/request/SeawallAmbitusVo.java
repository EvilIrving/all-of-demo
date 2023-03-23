package com.ygkj.big.screen.vo.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author wgf
 * @date 2021/11/11 19:27
 * @Description: 海塘极简模式周边数据
 */
@Data
@ApiModel("海塘极简模式周边数据查询参数封装")
public class SeawallAmbitusVo {

    @ApiModelProperty("查询的类型：1.保护范围，2.救援仓库,3救援队伍，4.避灾点，5.社会经济要素")
    private String type;

    @ApiModelProperty("海塘编码")
    private String seawallCode;

    @ApiModelProperty("半径距离（km）")
    private Double radius;

    @ApiModelProperty("社会要素,关键字搜索")
    private String keyWord;
}
