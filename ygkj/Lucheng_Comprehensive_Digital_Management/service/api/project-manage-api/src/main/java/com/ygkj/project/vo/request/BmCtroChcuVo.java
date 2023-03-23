package com.ygkj.project.vo.request;

import lombok.Data;

/**
 * @author wgf
 * @date 2022/3/23 11:23
 * @Description:
 */
@Data
public class BmCtroChcuVo {

    private String id;

    /**
     * 曲线索引唯一标识
     */
    private String curcd;

    /**
     * X
     */
    private Float x;
    /**
     * Y
     */
    private Float y;
}
