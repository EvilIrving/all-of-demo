package com.ygkj.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.ygkj.entity.SysRes;
import com.ygkj.mapper.SysResMapper;
import com.ygkj.service.ISysResService;
import com.ygkj.utils.PageUtils;
import com.ygkj.vo.resp.PageVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author  wgf
 * @date  2021/1/6 14:06
 */
@Service
public class SysResServiceImpl extends ServiceImpl<SysResMapper, SysRes> implements ISysResService {

    @Autowired
    SysResMapper sysResMapper;

    @Override
    public PageVO<SysRes> listPage(SysRes entity) {
        PageHelper.startPage(entity.getPageNum(), entity.getPageSize());
        List<SysRes> sysRes = sysResMapper.listPage(entity);
        PageVO<SysRes> pageVO = PageUtils.getPageVO(sysRes);
        return pageVO;
    }

    @Override
    public List<SysRes> getPidSysRes(SysRes entity) {
        return sysResMapper.getPidSysRes(entity);
    }

    @Override
    public List<SysRes> getBandingList(String jobId, String p_id, int flag) {
        return sysResMapper.getBandingList(jobId, p_id, flag);
    }

    @Override
    public List<String> permissionList(String id, String type) {
        return sysResMapper.permissionList(id, type);
    }
}
