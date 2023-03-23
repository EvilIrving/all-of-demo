package com.ygkj.mapper;

import com.ygkj.auth.VO.request.SysPermissionPageQueryVo;
import com.ygkj.entity.SysPermission;

import java.util.List;
import java.util.Map;

public interface SysPermissionMapper {
    int deleteByPrimaryKey(String id);

    int insert(SysPermission record);

    int insertSelective(SysPermission record);

    SysPermission selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(SysPermission record);

    int updateByPrimaryKey(SysPermission record);

    List<SysPermission> selectInfoByIds (List<String> list,String system);

    List<SysPermission> selectAll();

    List<SysPermission> selectSystemAll(String system);

    List<SysPermission> selectChild(String pid);

    List<Map<String,String>> polymerizationRoles(String userId);

    List<SysPermission> selectByCondition(SysPermissionPageQueryVo queryVo);
}