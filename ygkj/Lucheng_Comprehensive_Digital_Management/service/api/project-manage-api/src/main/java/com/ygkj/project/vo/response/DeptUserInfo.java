package com.ygkj.project.vo.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@JsonIgnoreProperties(value = {"hibernateLazyInitializer", "handler"})
public class DeptUserInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("部门/公司id")
    private String id;

    @ApiModelProperty("部门/公司名")
    private String deptName;

    @ApiModelProperty("用户信息")
    private List<SysUserSimpleResVo> userInfos;
}
