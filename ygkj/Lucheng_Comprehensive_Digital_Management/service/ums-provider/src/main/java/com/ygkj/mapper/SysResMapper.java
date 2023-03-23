package com.ygkj.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ygkj.entity.SysRes;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 系统资源
 * @author  wgf
 * @date  2021/1/6 13:57
 */
@Repository
public interface SysResMapper extends BaseMapper<SysRes> {

    /**
     * 根据pid查询子集
     * @param
     * @param entity
     * @return
     */
    List<SysRes> listPage(@Param("sysRes") SysRes entity);

    List<SysRes> getPidSysRes(@Param("sysRes") SysRes entity);

    @Select("SELECT b.* FROM (SELECT distinct RES_ID FROM sys_job_res WHERE find_in_set(JOB_ID,#{jobId})) a LEFT JOIN sys_res b ON a.RES_ID = b.id WHERE b.P_ID = #{pId} AND FLAG =  #{flag} AND b.DEL_FLAG = 0 ORDER BY SORT ASC")
    List<SysRes> getBandingList(@Param("jobId") String jobId, @Param("pId")String pId, @Param("flag")int flag);

    /**
     * 获取xx角色绑定的资源类型是xx类型的所有资源权限标识
     * @param id 角色id
     * @param type 资源类型
     * @return
     */
    @Select("SELECT distinct b.PERMISSION FROM sys_job_res a LEFT JOIN sys_res b ON a.RES_ID = b.ID WHERE find_in_set(a.JOB_ID,#{id}) AND b.DEL_FLAG = 0 AND b.RES_TYPE = #{type}")
    List<String> permissionList(@Param("id") String id, @Param("type") String type);
}
