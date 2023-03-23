package com.ygkj.soft.database.service.impl;

import com.ygkj.soft.database.service.BusApiApplyService;
import com.ygkj.soft.database.api.VO.ApiApplyQueryVo;
import com.ygkj.soft.database.api.VO.ApiApplyUpdateVo;
import com.ygkj.soft.database.api.model.ApiApplyStatusEnum;
import com.ygkj.soft.database.api.model.BusApi;
import com.ygkj.soft.database.api.model.BusApiApply;
import com.ygkj.soft.database.dao.BusApiApplyDao;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ygkj.com.assistant.pager.EasyuiPager;
import ygkj.com.assistant.pager.Pager;

import java.util.Date;
import java.util.UUID;


@Service("busApiApplyService")
public class BusApiApplyServiceImpl implements BusApiApplyService {

    @Autowired
    private BusApiApplyDao busApiApplyDao;

    @Override
    public boolean insert(ApiApplyUpdateVo updateVo) throws Exception {
        BusApiApply apply = new BusApiApply();
        BeanUtils.copyProperties(updateVo,apply);

        if(StringUtils.isEmpty(apply.getApiId())){
            throw new Exception("接口ID不能为空");
        }

        apply.setId(UUID.randomUUID().toString());
        apply.setCreateTime(new Date());
        return busApiApplyDao.add(apply)>0?true:false;
    }

    @Override
    public boolean delete(String id) {
        return busApiApplyDao.delete(id)>0?true:false;
    }

    @Override
    public boolean update(ApiApplyUpdateVo updateVo) {
        BusApiApply apply = new BusApiApply();
        BeanUtils.copyProperties(updateVo,apply);
        apply.setUpdateTime(new Date());
        return busApiApplyDao.add(apply)>0?true:false;
    }

    @Override
    public boolean updateState(String applyId, String remark, ApiApplyStatusEnum status) {
        return busApiApplyDao.updateState(applyId,remark,status)>0?true:false;
    }

    @Override
    public BusApiApply load(String id) {
        return busApiApplyDao.findById(id);
    }

    @Override
    public Pager queryMyAuditTodo(ApiApplyQueryVo queryVo, EasyuiPager easyuiPager) throws Exception {
        return busApiApplyDao.queryMyAuditTodo(queryVo,easyuiPager);
    }

    @Override
    public Pager queryAuditRecordsFromMe(ApiApplyQueryVo queryVo, EasyuiPager easyuiPager) throws Exception {
        return busApiApplyDao.queryAuditRecordsFromMe(queryVo,easyuiPager);
    }
}
