package com.ygkj.big.screen.vo.response;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author winde
 * @Description TODO
 * @Date 2021/5/1
 */
@Data
public class ArealRainResVo implements Serializable {
    List<ArealRainPointResVo> features;
}
