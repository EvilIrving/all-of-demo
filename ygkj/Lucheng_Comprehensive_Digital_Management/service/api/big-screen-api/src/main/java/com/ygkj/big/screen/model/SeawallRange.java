package com.ygkj.big.screen.model;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;

/**
 * @author wgf 海塘保护范围线,管保区域
 * @date 2021/11/12 17:23
 * @Description:
 */
@Data
public class SeawallRange implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(type = IdType.ASSIGN_UUID)
    private String id;

    /**
     * 工程编码
     */
    private String prcd;

    /**
     * 1.保护范围线2.管保区域
     */
    private Integer type;

    /**
     * 经纬度点位集合json字符串
     */
    private String point;

    public SeawallRange() {}

}
