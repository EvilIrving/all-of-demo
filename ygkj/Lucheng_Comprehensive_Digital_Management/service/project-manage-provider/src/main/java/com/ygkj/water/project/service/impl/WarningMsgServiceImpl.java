package com.ygkj.water.project.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ygkj.project.model.WarningInfo;
import com.ygkj.project.model.WarningMsg;
import com.ygkj.water.project.mapper.WarningMsgMapper;
import com.ygkj.water.project.service.WarningMsgService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author lxl
 * @create 2022-06-13 14:35
 * @description
 */
@Service
public class WarningMsgServiceImpl extends ServiceImpl<WarningMsgMapper, WarningMsg> implements WarningMsgService {



    @Override
    public List<WarningInfo> getListByCondition(WarningInfo warningInfo) {

        warningInfo.page();
        List<WarningInfo> list = baseMapper.getListByCondition(warningInfo);
        return list;
    }

    @Override
    public Integer getTotalAlarmNum(String phone) {
        
        return baseMapper.getTotalAlarmNum(phone);
    }

    @Override
    public Integer getTotalRemindNum(String phone) {
        return baseMapper.getTotalRemindNum(phone);
    }


}
