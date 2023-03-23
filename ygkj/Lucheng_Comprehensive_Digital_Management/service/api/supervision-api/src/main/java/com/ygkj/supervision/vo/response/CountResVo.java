package com.ygkj.supervision.vo.response;

import lombok.Data;

/**
 * 统计数回参
 *
 * @author lxl
 * @date 2021/5/10
 */
@Data
public class CountResVo {

    /**
     * 统计数键
     */
    private String key;

    /**
     * 统计值
     */
    private String value;

    /**
     * 统计专用 - 整改数
     */
    private String count;

    public CountResVo() {
    }

    public CountResVo(String key, String value) {
        this.key = key;
        this.value = value;
    }
}
