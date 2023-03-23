package com.ygkj.data.management.vo.request;

import com.google.common.base.Converter;
import com.ygkj.data.management.model.SysDataManagementRole;
import com.ygkj.utils.StringUtils;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.beans.BeanUtils;

@Data
@ApiModel("数据管理系统权限新增/修改入参封装")
public class SysDataManagementRoleAddVo {

    /**
     * id
     */
    @ApiModelProperty("id，修改的时候用")
    private String id;

    /**
     * 角色名称
     */
    @ApiModelProperty("角色名称")
    private String roleName;

    /**
     * 角色简介
     */
    @ApiModelProperty("角色简介")
    private String description;

    /**
     * 部门id，关联到main_db中的系统部门表sys_dept
     */
    @ApiModelProperty("部门id")
    private String deptId;

    /**
     * 下一步操作限定的角色id，用于审批流程
     */
    @ApiModelProperty("下一个关联的角色id")
    private String nextId;

    @ApiModelProperty("查询权限1-是 0-否")
    private boolean operQuery;
    @ApiModelProperty("新增权限1-是 0-否")
    private boolean operCreate;
    @ApiModelProperty("修改权限1-是 0-否")
    private boolean operUpdate;
    @ApiModelProperty("删除权限1-是 0-否")
    private boolean operDelete;

    @ApiModelProperty("业务类型 BASIC 基础数据  REALTIME 实时数据")
    private String busType;

    public String getBusType() {
        return StringUtils.isEmpty(this.busType) ? "BASIC" : this.busType;
    }

    public SysDataManagementRole convertToRole() {
        RoleConverter converter = new RoleConverter();
        SysDataManagementRole role = converter.convert(this);
        return role;
    }

    public SysDataManagementRoleAddVo convertFor(SysDataManagementRole role) {
        RoleConverter converter = new RoleConverter();
        SysDataManagementRoleAddVo addVo = converter.reverse().convert(role);
        return addVo;
    }

    private static class RoleConverter extends Converter<SysDataManagementRoleAddVo, SysDataManagementRole> {

        @Override
        protected SysDataManagementRole doForward(SysDataManagementRoleAddVo addVo) {
            SysDataManagementRole role = new SysDataManagementRole();
            BeanUtils.copyProperties(addVo, role);
            return role;
        }

        @Override
        protected SysDataManagementRoleAddVo doBackward(SysDataManagementRole sysDataManagementRole) {
            SysDataManagementRoleAddVo addVo = new SysDataManagementRoleAddVo();
            BeanUtils.copyProperties(sysDataManagementRole, addVo);
            return addVo;
        }
    }
}
