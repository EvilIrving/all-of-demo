package com.ygkj.service.impl;

import com.ygkj.entity.SysRolePermission;
import com.ygkj.exception.BusinessException;
import com.ygkj.exception.code.BaseResponseCode;
import com.ygkj.mapper.SysRolePermissionMapper;
import com.ygkj.service.RolePermissionService;
import com.ygkj.vo.req.RolePermissionOperationReqVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * @ClassName: RolePermissionServiceImpl
 * TODO:类文件简单描述
 * @Author: Huang.zh
 * @CreateDate: 2019/9/19 11:41
 * @UpdateUser: Huang.zh
 * @UpdateDate: 2019/9/19 11:41
 * @Version: 0.0.1
 */
@Service
public class RolePermissionServiceImpl implements RolePermissionService {
    @Autowired
    private SysRolePermissionMapper sysRolePermissionMapper;

    @Override
    public int removeByRoleId(String roleId) {
        return sysRolePermissionMapper.removeByRoleId(roleId);
    }

    @Override
    public List<String> getPermissionIdsByRoles(List<String> roleIds) {

        return sysRolePermissionMapper.getPermissionIdsByRoles(roleIds);
    }

    @Override
    public void addRolePermission(RolePermissionOperationReqVO vo) {

        Date createTime = new Date();
        List<SysRolePermission> list = new ArrayList<>();
        for (String permissionId : vo.getPermissionIds()) {
            SysRolePermission sysRolePermission = new SysRolePermission();
            sysRolePermission.setId(UUID.randomUUID().toString());
            sysRolePermission.setCreateTime(createTime);
            sysRolePermission.setPermissionId(permissionId);
            sysRolePermission.setRoleId(vo.getRoleId());
            list.add(sysRolePermission);
        }
        sysRolePermissionMapper.removeByRoleId(vo.getRoleId());
        int count = sysRolePermissionMapper.batchRolePermission(list);
        if (count == 0) {
            throw new BusinessException(BaseResponseCode.OPERATION_ERRO);
        }
    }

    @Override
    public int removeByPermissionId(String permissionId) {

        return sysRolePermissionMapper.removeByPermissionId(permissionId);
    }

    @Override
    public List<String> getRoleIds(String permissionId) {

        return sysRolePermissionMapper.getRoleIds(permissionId);
    }

    @Override
    public List<String> getPermissionIdsByRoleId(String roleId) {

        return sysRolePermissionMapper.getPermissionIdsByRoleId(roleId);
    }
}
