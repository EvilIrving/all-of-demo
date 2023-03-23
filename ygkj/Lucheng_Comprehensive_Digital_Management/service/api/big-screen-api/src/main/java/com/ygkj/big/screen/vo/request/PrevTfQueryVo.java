package com.ygkj.big.screen.vo.request;

import lombok.Data;

/**
 * @author xq
 * @Description
 * @Date 2021/10/11
 */
@Data
public class PrevTfQueryVo {

    /**
     * true 必须要有测站编码，false 必须没有测站编码
     */
    Boolean filterHasStcd;
}
