package com.ygkj.service;

import com.ygkj.vo.req.RolePermissionOperationReqVO;

import java.util.List;

/**
 * @ClassName: RolePermissionService
 * TODO:类文件简单描述
 * @Author: Huang.zh
 * @CreateDate: 2019/9/19 11:39
 * @UpdateUser: Huang.zh
 * @UpdateDate: 2019/9/19 11:39
 * @Version: 0.0.1
 */
public interface RolePermissionService {

    int removeByRoleId(String roleId);

    List<String> getPermissionIdsByRoles(List<String> roleIds);

    void addRolePermission(RolePermissionOperationReqVO vo);

    int removeByPermissionId(String permissionId);

    List<String> getRoleIds(String permissionId);

    List<String> getPermissionIdsByRoleId(String roleId);

}
