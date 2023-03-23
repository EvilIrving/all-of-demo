package com.ygkj.gragh.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author hucong
 * @description 墒情实时表
 * @date 2021-06-30
 */
@Data
public class StSoliR implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * mgstcd
     */
    private String mgstcd;

    /**
     * tm
     */
    private Date tm;

    /**
     * slm10
     */
    private Double slm10;

    /**
     * slm20
     */
    private Double slm20;

    /**
     * slm30
     */
    private Double slm30;

    public StSoliR() {
    }
}
