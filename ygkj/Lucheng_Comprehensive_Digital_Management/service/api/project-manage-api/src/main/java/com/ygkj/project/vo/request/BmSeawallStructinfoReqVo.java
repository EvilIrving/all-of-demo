package com.ygkj.project.vo.request;

import lombok.Data;

/**
 * @author pqf
 * @DESCRIPTION:
 * @create 2021-11-12 9:19
 */
@Data
public class BmSeawallStructinfoReqVo {
    /**
     * 工程编码
     */
    private String prcd;

    /**
     * 页数
     */
    private Integer pageNum;

    /**
     * 页面大小
     */
    private Integer pageSize;
}
