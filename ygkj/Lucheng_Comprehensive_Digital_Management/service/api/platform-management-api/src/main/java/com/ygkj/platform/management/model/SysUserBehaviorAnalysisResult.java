package com.ygkj.platform.management.model;

import lombok.Data;

import java.io.Serializable;

/**
 * @author Huangzh
 * @description 用户行为关联分析结果
 * @date 2021-09-24
 */
@Data
public class SysUserBehaviorAnalysisResult implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户id
     */
    private String userId;

    /**
     * 用户名称
     */
    private String username;

    /**
     * 近一个月登录次数
     */
    private Integer loginCount;

    /**
     * 负责数据条数
     */
    private Integer dataNumber;

    /**
     * 常用功能
     */
    private String commonFunction;

    /**
     * 高频搜索词
     */
    private String commonSearchWord;

    public SysUserBehaviorAnalysisResult() {
    }

}

