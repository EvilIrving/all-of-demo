package com.ygkj.lcsyn.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;


@Data
public class BmPrcdAccprcd {

    @TableId(type = IdType.INPUT)
    /**
     * id
     */
    private String id;

    /**
     * 主工程编码
     */
    private String prcd;

    /**
     * 附属工程编码
     */
    private String accPrcd;

}
