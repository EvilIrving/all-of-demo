package com.ygkj.data.management.vo.response;

import lombok.Data;

@Data
public class TableColumnsResVo {

    private String id;

    private String columnName;

    private String columnComment;

    private Boolean gridDisplay;

    private Boolean formDisplay;
}
