package com.ygkj.data.management.vo.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(value = {"hibernateLazyInitializer", "handler"})
public class DataTableFormResVo {

    private String id;

    private String dataName;

    private String dataTable;

    private Integer tableRows;

    private Integer tableRuleCount;

    private Integer persistenceCount;

    private Double persistenceRate;

    private Integer questionCount;

    private Integer messageCount;
}
