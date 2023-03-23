package com.ygkj.service.impl;

import com.github.pagehelper.PageHelper;
import com.ygkj.entity.SysLog;
import com.ygkj.mapper.SysLogMapper;
import com.ygkj.service.LogService;
import com.ygkj.utils.PageUtils;
import com.ygkj.vo.req.SysLogPageReqVO;
import com.ygkj.vo.resp.PageVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @ClassName: LogServiceImpl
 * TODO:类文件简单描述
 * @Author: Huang.zh
 * @CreateDate: 2019/9/23 16:18
 * @UpdateUser: Huang.zh
 * @UpdateDate: 2019/9/23 16:18
 * @Version: 0.0.1
 */
@Service
public class LogServiceImpl implements LogService {
    @Autowired
    private SysLogMapper sysLogMapper;

    @Override
    public PageVO<SysLog> pageInfo(SysLogPageReqVO vo) {

        PageHelper.startPage(vo.getPageNum(),vo.getPageSize());
        List<SysLog> sysLogs = sysLogMapper.selectAll(vo);
        return PageUtils.getPageVO(sysLogs);
    }

    @Override
    public void deleted(List<String> logIds) {
        sysLogMapper.batchDeletedLog(logIds);
    }
}
