package com.ygkj.auth.api.monitor.model;

import lombok.Data;

import java.io.Serializable;

/**
 * @author: fml
 * @Date: 2020/10/23 11:06
 * @Description:
 */
@Data
public class BusProjectTag implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    private String id;

    /**
     * 标签名
     */
    private String tag;
}
