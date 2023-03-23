package com.ygkj.data.management.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.ygkj.data.management.vo.response.SysDataManagementRoleResVo;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(value = {"hibernateLazyInitializer", "handler"})
public class SysUser {

    private String id;

    private String username;

    private String phone;

    private String sex;

    private String deptId;

    private String deptName;

    private String roleName;

    private Integer roleNum;

    private List<SysDataManagementRoleResVo> roles;
}
