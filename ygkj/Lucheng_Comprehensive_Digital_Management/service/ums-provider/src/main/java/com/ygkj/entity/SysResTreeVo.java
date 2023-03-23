package com.ygkj.entity;

import lombok.Data;

import java.util.List;

/**
 * @author  wgf
 * @date  2021/1/6 10:02
 */
@Data
public class SysResTreeVo {

    private static final long serialVersionUID = 1L;

    /**
     * 父级
     */
    private SysRes sysRes;

    /**
     * 子集
     */
    private List<SysResTreeVo> subset;

}
