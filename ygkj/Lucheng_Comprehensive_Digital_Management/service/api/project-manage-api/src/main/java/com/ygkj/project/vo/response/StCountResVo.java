package com.ygkj.project.vo.response;

import lombok.Data;

/**
 * 测站统计响应
 */
@Data
public class StCountResVo {

    /**
     * 统计类型
     */
    String countKey;

    /**
     * 统计数值
     */
    Long countNum;

}
