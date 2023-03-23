package com.ygkj.project.model;

import lombok.Data;

/**
 * @author wgf
 * @date 2022/5/13 17:26
 * @Description: 波高实体
 */
@Data
public class WaveHeight {

    /**
     * 波长
     */
    private double length;

    /**
     * 1%波高
     */
    private double h1;

    /**
     * 2%波高
     */
    private double h2;

    /**
     * 13%波高
     */
    private double h13;
}
