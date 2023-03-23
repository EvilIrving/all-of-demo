package com.ygkj.gragh.model;

import java.io.Serializable;

import com.ygkj.gragh.vo.response.BswqResVo;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author xq
 * @description 交界断面水质测站表
 * @date 2021-04-21
 */
@Data
public class AttBswqBase implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 编码
     */
    private String code;

    /**
     * 站点名称
     */
    private String name;

    /**
     * 纬度
     */
    private BigDecimal latitude;

    /**
     * 经度
     */
    private BigDecimal longitude;

    /**
     * 行政区划编码
     */
    private String adcd;

    /**
     * PM限值
     */
    private String pmLimit;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 创建人
     */
    private String createId;

    /**
     * 修改时间
     */
    private Date updateTime;

    /**
     * 修改人
     */
    private String updateId;

    /**
     * 是否删除 0-未删除 1-已删除
     */
    private Integer delFlag;

    /**
     * dept_id
     */
    private String deptId;

    private BswqResVo waterQuality;

    public AttBswqBase() {
    }

}
