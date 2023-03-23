package com.ygkj.water.project.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ygkj.project.model.DataMaintainTask;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DataMaintainTaskMapper extends BaseMapper<DataMaintainTask> {
    @Update("update data_maintain_task set handle_user_id = #{obj.handleUserId},status =  1,unit_feedback_info = #{obj.unitFeedbackInfo}," +
            "data_maintain_feedback_info = #{obj.dataMaintainFeedbackInfo} , unit_handle_status = #{obj.unitHandleStatus}," +
            " data_maintain_status = #{obj.dataMaintainStatus}" +
            "   where id = #{obj.id}")
    void updTask(@Param("obj") DataMaintainTask task);

    List<DataMaintainTask> pageQuery(@Param("obj") DataMaintainTask task, @Param("isAdmin") String isAdmin,@Param("phone")String phone);

    @Update("update data_maintain_task set third_party_unit = #{obj.feedbackInfo}")
    void updThirdPartyFeedbackInfo(@Param("obj")DataMaintainTask task);
}
