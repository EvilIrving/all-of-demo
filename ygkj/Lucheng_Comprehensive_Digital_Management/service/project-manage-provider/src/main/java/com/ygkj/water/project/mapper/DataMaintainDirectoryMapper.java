package com.ygkj.water.project.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ygkj.auth.api.achievement.model.SysAchievement;
import com.ygkj.project.model.DataMaintainDirectory;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DataMaintainDirectoryMapper extends BaseMapper<DataMaintainDirectory> {

    @Select("SELECT getChildDirectoryId(#{id})")
    String getDirectoryIdsString(@Param("id") String id);

    @Select(" select * from data_maintain_directory where pid IS not NULL and pid !='0'")
    List<DataMaintainDirectory> selectAll();

    @Update("update data_maintain_info set directory_name = #{replace} where directory_id = #{dirId}")
    void updInfo(@Param("dirId") String dirId,@Param("replace") String replace);

    @Update("update data_maintain_task set directory_name = #{replace} where directory_id = #{dirId}")
    void updateTask(@Param("dirId")String dirId,@Param("replace") String replace);

    @Select("SELECT * from data_maintain_directory where directory_name = #{directoryName}")
    DataMaintainDirectory queryByName(@Param("directoryName") String directoryName);

    @Select("SELECT * from data_maintain_directory where pid = #{pid} and directory_name = #{projectType}")
    DataMaintainDirectory queryByPidAndName(@Param("pid") String pid, @Param("projectType") String projectType);
}
