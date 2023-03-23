package com.ygkj.project.vo.response;

import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@ApiModel("字典出参")
public class DictResVo {

    private String dictCode;

    private String dictValue;
}
