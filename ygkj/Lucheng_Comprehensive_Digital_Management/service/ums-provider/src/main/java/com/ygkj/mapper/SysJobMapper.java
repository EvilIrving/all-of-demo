package com.ygkj.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ygkj.entity.SysJob;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author wgf
 * @since 2021-01-06
 */
@Repository
public interface SysJobMapper extends BaseMapper<SysJob> {

    /**
     * 删除角色绑定的资源数据 分菜单
     * @param id
     * @param flag
     */
    @Delete("delete from sys_job_res WHERE JOB_ID = #{id} AND fLAG = #{flag}")
    void removeJobRes(@Param("id") String id, @Param("flag") Integer flag);

    /**
     * 查询角色绑定的资源id
     * @param id
     * @return
     */
    @Select("SELECT distinct RES_ID FROM sys_job_res WHERE find_in_set(JOB_ID , #{id})")
    List<String> getJobResId(@Param("id")String id);

    /**
     * 删除角色绑定的资源数据 删除全部菜单
     * @param id
     */
    @Delete("delete from sys_job_res WHERE JOB_ID = #{id}")
    void removeJobResAll(String id);

    /**
     * 查询角色绑定的资源id，如果绑定的资源有子集id，该id要去除（前端回显需要这个格式）
     * @param id
     * @return
     */
    @Select("SELECT b.ID FROM (SELECT RES_ID FROM sys_job_res WHERE JOB_ID = #{id}) a LEFT JOIN sys_res b ON a.RES_ID = b.id " +
            "WHERE ID NOT IN (SELECT b.P_ID FROM (SELECT RES_ID FROM sys_job_res WHERE JOB_ID = #{id}) a LEFT JOIN sys_res b " +
            "ON a.RES_ID = b.id where b.RES_TYPE!=3)")
    List<String> getBangdingJobResId(@Param("id")String id);

    @Select("SELECT * FROM sys_job WHERE find_in_set(ID , #{id})")
    List<SysJob> getJobByIds(@Param("id")String id);

    @Select("SELECT * FROM sys_job WHERE JOB_NAME like concat('%',#{jobName},'%')")
    List<SysJob> getJobByName(@Param("jobName")String jobName);
}
