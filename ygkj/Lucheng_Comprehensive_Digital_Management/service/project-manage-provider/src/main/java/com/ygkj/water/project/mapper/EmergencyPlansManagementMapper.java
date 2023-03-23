package com.ygkj.water.project.mapper;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ygkj.project.model.EmergencyPlansManagement;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author wgf
 * @date 2022/4/27 15:32
 * @Description:
 */
@Repository
public interface EmergencyPlansManagementMapper extends BaseMapper<EmergencyPlansManagement> {

    List<EmergencyPlansManagement> getList(@Param("emergencyPlansManagement") EmergencyPlansManagement emergencyPlansManagement);

    List<JSONObject> planManagement(@Param("list")List<String> codeList, @Param("year")int year);
}
