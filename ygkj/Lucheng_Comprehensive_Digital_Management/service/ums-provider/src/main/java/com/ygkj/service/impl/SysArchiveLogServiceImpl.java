package com.ygkj.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ygkj.auth.VO.request.SysArchiveLogReqVo;
import com.ygkj.auth.api.archivelog.model.SysArchiveLog;
import com.ygkj.mapper.SysArchiveLogMapper;
import com.ygkj.service.SysArchiveLogService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @description 档案操作日志表
 * @author hucong
 * @date 2021-01-19
 */
@Service
public class SysArchiveLogServiceImpl implements SysArchiveLogService {

    @Resource
    private SysArchiveLogMapper sysArchiveLogMapper;


    @Override
    public int insert(SysArchiveLog sysArchiveLog) {
        return sysArchiveLogMapper.insert(sysArchiveLog);
    }


    @Override
    public int delete(int id) {
        int ret = sysArchiveLogMapper.delete(id);
        return ret;
    }


    @Override
    public int update(SysArchiveLog sysArchiveLog) {
        int ret = sysArchiveLogMapper.update(sysArchiveLog);
        return ret;
    }


    @Override
    public SysArchiveLog load(int id) {
        return sysArchiveLogMapper.load(id);
    }


    @Override
    public PageInfo<SysArchiveLog> pageList(SysArchiveLogReqVo vo) {
        if (!vo.isFetchAll()){
            PageHelper.startPage(vo.getPageNum(), vo.getPageSize());
        }
        List<SysArchiveLog> selectAll = sysArchiveLogMapper.selectAll(vo);
        return new PageInfo(selectAll);
    }

    @Override
    public List<JSONObject> trafficFileRank() {
        return sysArchiveLogMapper.trafficFileRank();
    }

    @Override
    public List<JSONObject> trafficFileGraph(String time) {
        return sysArchiveLogMapper.trafficFileGraph(time);
    }

    @Override
    public List<String> archiveLogTimeParameter(String time) {
        return sysArchiveLogMapper.archiveLogTimeParameter(time);
    }
}
