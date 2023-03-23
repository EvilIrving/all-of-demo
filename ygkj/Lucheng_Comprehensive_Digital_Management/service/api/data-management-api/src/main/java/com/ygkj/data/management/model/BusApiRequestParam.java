package com.ygkj.data.management.model;

import lombok.Data;

import java.io.Serializable;

/**
 * api请求参数
 *
 * @author huang.zh 2021-03-17
 */
@Data
public class BusApiRequestParam implements Serializable {

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

    /**
     * 是否必填
     */
    private boolean isRequired;

    /**
     * 参数示例
     */
    private String paramExample;

    private String apiId;

    public BusApiRequestParam() {
    }

    public BusApiRequestParam(String id, String paramName, String paramComment, String paramType, boolean isRequired, String paramExample, String apiId) {
        this.id = id;
        this.paramName = paramName;
        this.paramComment = paramComment;
        this.paramType = paramType;
        this.isRequired = isRequired;
        this.paramExample = paramExample;
        this.apiId = apiId;
    }
}
