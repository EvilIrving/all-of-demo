package com.ygkj.water.project.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ygkj.project.model.DataMaintainInfo;
import com.ygkj.project.model.DataMaintainTask;
import com.ygkj.project.model.ThirdPartyUnitInfo;
import com.ygkj.utils.PageUtils;
import com.ygkj.utils.StringUtils;
import com.ygkj.vo.PageVO;
import com.ygkj.water.project.mapper.DataMaintainInfoMapper;
import com.ygkj.water.project.mapper.DataMaintainTaskMapper;
import com.ygkj.water.project.service.DataMaintainTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class DataMaintainTaskServiceImpl extends ServiceImpl<DataMaintainTaskMapper, DataMaintainTask> implements DataMaintainTaskService {

    @Autowired
    private DataMaintainInfoMapper infoMapper;

    @Override
    public void sendInfoInsert(DataMaintainTask task) {
        task.setSendTime(new Date());
        baseMapper.insert(task);
    }

    @Override
    public boolean sendInfodelete(String id) {
        DataMaintainTask dataMaintainTask = new DataMaintainTask();
        dataMaintainTask.setId(id);
        dataMaintainTask.setStatus("3");
        int i = baseMapper.updateById(dataMaintainTask);
        return i > 0 ? true : false;
    }

    @Override
    public Object queryPage(DataMaintainTask task) {
        String isAdmin = infoMapper.selectAdmin(task.getHandleUserId());
        String phone = infoMapper.getPhone(task.getHandleUserId());
        task.page();
        List<DataMaintainTask> taskList = baseMapper.pageQuery(task, isAdmin, phone);
        PageVO<DataMaintainTask> pageVO = PageUtils.getPageVO(taskList);
        List<DataMaintainTask> list = pageVO.getList();
        for (DataMaintainTask task1 : list) {
            String thirdPartyUnit = task1.getThirdPartyUnit();
            if (StringUtils.isNotBlank(thirdPartyUnit)) {
                List<ThirdPartyUnitInfo> thirdPartyUnitInfos = JSONArray.parseArray(thirdPartyUnit, ThirdPartyUnitInfo.class);
                task1.setThirdPartyUnitInfos(thirdPartyUnitInfos);
            }
        }
        pageVO.setList(list);
        return pageVO;
    }

    @Override
    public void updateTask(DataMaintainTask task) {
        if ("1".equals(task.getTransManageDataType())){
            String feedbackInfo = task.getFeedbackInfo();
            if (StringUtils.isNotBlank(feedbackInfo)){
                List<ThirdPartyUnitInfo> unitInfos = JSONArray.parseArray(feedbackInfo, ThirdPartyUnitInfo.class);
                for (ThirdPartyUnitInfo unitInfo : unitInfos) {
                    if (StringUtils.isNotBlank(unitInfo.getFeedbackInfo())){
                        unitInfo.setStatus("1");
                    }else{
                        unitInfo.setStatus("0");
                    }
                }
                String thirdPartyUnitInfo = JSON.toJSONString(unitInfos);
                task.setFeedbackInfo(thirdPartyUnitInfo);
                baseMapper.updThirdPartyFeedbackInfo(task);
            }

        }else {
            if (StringUtils.isNotBlank(task.getDataMaintainFeedbackInfo())) {
                task.setDataMaintainStatus("1");
            }else{
                task.setDataMaintainStatus("0");
            }
            if (StringUtils.isNotBlank(task.getUnitFeedbackInfo())) {
                task.setUnitHandleStatus("1");
            }else{
                task.setUnitHandleStatus("0");
            }
            baseMapper.updTask(task);
        }
    }
}
