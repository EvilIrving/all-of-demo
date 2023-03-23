package com.ygkj.gragh.vo.response;

import com.ygkj.gragh.model.AttBswqBase;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author xq
 * @Description
 * @Date 2021/4/21
 */
@Data
public class BswqResVo extends AttBswqBase {

    private static final long serialVersionUID = 1L;

    /**
     * 测站编码
     */
    private String code;

    /**
     * 水质采集日期
     */
    private Date time;

    /**
     * pm
     */
    private BigDecimal pm;

    /**
     * 溶解氧 对应数据库字段do
     */
    private BigDecimal do_;

    /**
     * 高锰酸盐指数
     */
    private BigDecimal permanganate;

    /**
     * 五日生化需氧量
     */
    private BigDecimal bod5;
}
