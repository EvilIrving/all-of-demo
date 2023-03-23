package com.ygkj.mapper;

import com.ygkj.entity.SysLog;
import com.ygkj.platform.management.model.SysLoginLog;
import com.ygkj.platform.management.vo.request.SysLoginLogPage;
import com.ygkj.vo.req.SysLogPageReqVO;

import java.util.List;

public interface SysLogMapper {
    int deleteByPrimaryKey(String id);

    int insert(SysLog record);

    int insertSelective(SysLog record);

    SysLog selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(SysLog record);

    int updateByPrimaryKey(SysLog record);

    List<SysLog> selectAll(SysLogPageReqVO vo);

    void batchDeletedLog(List<String> logIds);

    List<SysLoginLog> loadLoginLogs(SysLoginLogPage pageVo);
}