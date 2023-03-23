package com.ygkj.water.project.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ygkj.project.model.EmergencyPlansManagement;
import com.ygkj.utils.PageUtils;
import com.ygkj.vo.PageVO;
import com.ygkj.water.project.mapper.EmergencyPlansManagementMapper;
import com.ygkj.water.project.service.EmergencyPlansManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author wgf
 * @date 2022/4/27 15:30
 * @Description:
 */
@Service
public class EmergencyPlansManagementServiceImpl extends ServiceImpl<EmergencyPlansManagementMapper, EmergencyPlansManagement> implements EmergencyPlansManagementService {

    @Autowired
    private EmergencyPlansManagementMapper emergencyPlansManagementMapper;

    @Override
    public PageVO<EmergencyPlansManagement> getList(EmergencyPlansManagement emergencyPlansManagement) {
        emergencyPlansManagement.page();
        List<EmergencyPlansManagement> list = emergencyPlansManagementMapper.getList(emergencyPlansManagement);
        return PageUtils.getPageVO(list);
    }

    @Override
    public List<JSONObject> planManagement(List<String> codeList, int year) {
        return emergencyPlansManagementMapper.planManagement(codeList,year);
    }
}
