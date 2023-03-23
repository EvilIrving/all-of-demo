package com.ygkj.big.screen.vo.response;

import lombok.Data;

import java.io.Serializable;

/**
 * @author winde
 * @Description TODO
 * @Date 2021/5/1
 */
@Data
public class ArealRainPointResVo implements Serializable {
    ArealRainAttrResVo attributes;
    ArealRainGeoResVo geometry;

}
