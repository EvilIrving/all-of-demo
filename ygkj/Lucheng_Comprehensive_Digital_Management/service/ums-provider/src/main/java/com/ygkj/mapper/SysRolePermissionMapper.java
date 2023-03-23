package com.ygkj.mapper;

import com.ygkj.entity.SysRolePermission;

import java.util.List;

public interface SysRolePermissionMapper {
    int deleteByPrimaryKey(String id);

    int insert(SysRolePermission record);

    int insertSelective(SysRolePermission record);

    SysRolePermission selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(SysRolePermission record);

    int updateByPrimaryKey(SysRolePermission record);

    int removeByRoleId(String roleId);

    List<String> getPermissionIdsByRoles(List<String> roleIds);

    int batchRolePermission(List<SysRolePermission> list);

    int removeByPermissionId(String permissionId);

    List<String> getRoleIds(String permissionId);

    List<String> getPermissionIdsByRoleId(String roleId);

}