package com.ygkj.supervision.vo.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ygkj.supervision.model.AttSupervisionProject;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author lxl
 */
@Data
@ApiModel("工程项目回参")
public class ProjectResVo extends AttSupervisionProject implements Serializable {


    @ApiModelProperty("检查事项数")
    private String matterTotal;

    @ApiModelProperty("整改数量")
    private String rectifyTotal;

    @ApiModelProperty("存在问题数量")
    private String problemTotal;


    public ProjectResVo() {
    }
}
