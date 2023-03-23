package com.ygkj.platform.management.vo.response;

import lombok.Data;

import java.io.Serializable;

/**
 * 热门关键词对象
 */
@Data
public class TopKeyWordVo implements Serializable {
    /**
     * 关键词名称
     */
    String keyWord;
    /**
     * 关键词数量
     */
    Long keyWordNum;
}
