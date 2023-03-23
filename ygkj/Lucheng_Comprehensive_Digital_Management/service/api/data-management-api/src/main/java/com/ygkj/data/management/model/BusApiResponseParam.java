package com.ygkj.data.management.model;

import lombok.Data;

import java.io.Serializable;

/**
 * api响应参数
 *
 * @author huang.zh 2021-03-17
 */
@Data
public class BusApiResponseParam implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    private String id;

    /**
     * 参数名称
     */
    private String paramName;

    /**
     * varchar(255)
     */
    private String paramComment;

    /**
     * 参数类型
     */
    private String paramType;

    private String apiId;

    public BusApiResponseParam() {
    }

    public BusApiResponseParam(String id, String paramName, String paramComment, String paramType, String apiId) {
        this.id = id;
        this.paramName = paramName;
        this.paramComment = paramComment;
        this.paramType = paramType;
        this.apiId = apiId;
    }
}

