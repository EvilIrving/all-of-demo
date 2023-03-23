package com.ygkj.warning.vo.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(value = {"hibernateLazyInitializer", "handler"})
public class SysWarnGroupDetailResVo {

    private Long groupId;

    private String name;

    private List<SysWarnTemplateResVo> templates;
}
