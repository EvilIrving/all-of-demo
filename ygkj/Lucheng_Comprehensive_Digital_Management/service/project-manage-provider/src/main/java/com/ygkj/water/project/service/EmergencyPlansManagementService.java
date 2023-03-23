package com.ygkj.water.project.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ygkj.project.model.EmergencyPlansManagement;
import com.ygkj.vo.PageVO;

import java.util.List;

/**
 * @author wgf
 * @date 2022/4/27 15:29
 * @Description: 预案管理
 */
public interface EmergencyPlansManagementService extends IService<EmergencyPlansManagement> {

    PageVO<EmergencyPlansManagement> getList(EmergencyPlansManagement emergencyPlansManagement);

    List<JSONObject> planManagement(List<String> codeList, int year);
}
