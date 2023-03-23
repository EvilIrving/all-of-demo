package com.ygkj.digitization.model;

import lombok.Data;

@Data
public class RelRelPs {
    /**
     * 主键
     */
    private String id;

    /**
     * 关联表主键
     */
    private String relId;

    /**
     * 工程或者测站编码
     */
    private String psCode;
}
