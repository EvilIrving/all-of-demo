package com.ygkj.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ygkj.entity.SysJob;
import com.ygkj.mapper.SysJobMapper;
import com.ygkj.service.ISysJobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author wgf
 * @since 2021-01-06
 */
@Service
public class SysJobServiceImpl extends ServiceImpl<SysJobMapper, SysJob> implements ISysJobService {

    @Autowired
    private SysJobMapper sysJobMapper;


    @Override
    public void removeJobRes(String id, Integer flag) {
        sysJobMapper.removeJobRes(id, flag);
    }

    @Override
    public void removeJobResAll(String id) {
        sysJobMapper.removeJobResAll(id);
    }

    @Override
    public List<String> getJobResId(String id) {
        return sysJobMapper.getJobResId(id);
    }

    @Override
    public List<String> getBangdingJobResId(String id) {
        return sysJobMapper.getBangdingJobResId(id);
    }
}
