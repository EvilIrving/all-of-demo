package com.ygkj.service.impl;

import com.ygkj.entity.SysUserRole;
import com.ygkj.exception.BusinessException;
import com.ygkj.exception.code.BaseResponseCode;
import com.ygkj.mapper.SysUserRoleMapper;
import com.ygkj.service.UserRoleService;
import com.ygkj.vo.req.UserRoleOperationReqVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * @ClassName: UserRoleServiceImpl
 * TODO:类文件简单描述
 * @Author: Huang.zh
 * @CreateDate: 2019/9/19 11:42
 * @UpdateUser: Huang.zh
 * @UpdateDate: 2019/9/19 11:42
 * @Version: 0.0.1
 */
@Service
public class UserRoleServiceImpl implements UserRoleService {
    @Autowired
    private SysUserRoleMapper sysUserRoleMapper;

    @Override
    public int removeByRoleId(String roleId) {
        return sysUserRoleMapper.removeByRoleId(roleId);
    }

    @Override
    public List<String> getRoleIdsByUserId(String userId) {
        return sysUserRoleMapper.getRoleIdsByUserId(userId);
    }



    @Transactional(rollbackFor = Exception.class)
    @Override
    public void addUserRoleInfo(UserRoleOperationReqVO vo) {
        if(vo.getRoleIds()==null||vo.getRoleIds().isEmpty()){
            return;
        }
        Date createTime=new Date();
        List<SysUserRole> list=new ArrayList<>();
        for (String roleId:vo.getRoleIds()){
            SysUserRole sysUserRole=new SysUserRole();
            sysUserRole.setId(UUID.randomUUID().toString());
            sysUserRole.setCreateTime(createTime);
            sysUserRole.setUserId(vo.getUserId());
            sysUserRole.setRoleId(roleId);
            list.add(sysUserRole);
        }
        sysUserRoleMapper.removeByUserId(vo.getUserId());
        int count=sysUserRoleMapper.batchUserRole(list);
        if (count==0){
            throw new BusinessException(BaseResponseCode.OPERATION_ERRO);
        }
    }

    @Override
    public int removeByUserId(String userId) {

        return sysUserRoleMapper.removeByUserId(userId);
    }

    @Override
    public List<String> getUserIdsByRoleIds(List<String> roleIds) {

        return sysUserRoleMapper.getUserIdsByRoleIds(roleIds);
    }
}
