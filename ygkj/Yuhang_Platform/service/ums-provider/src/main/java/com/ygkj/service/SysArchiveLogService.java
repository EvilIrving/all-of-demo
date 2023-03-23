package com.ygkj.service;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.ygkj.auth.VO.request.SysArchiveLogReqVo;
import com.ygkj.auth.api.archivelog.model.SysArchiveLog;

import java.util.List;

/**
 * @description 档案操作日志表
 * @author hucong
 * @date 2021-01-19
 */
public interface SysArchiveLogService {

    /**
     * 新增
     */
    public int insert(SysArchiveLog sysArchiveLog);

    /**
     * 删除
     */
    public int delete(int id);

    /**
     * 更新
     */
    public int update(SysArchiveLog sysArchiveLog);

    /**
     * 根据主键 id 查询
     */
    public SysArchiveLog load(int id);

    /**
     * 分页查询
     */
    public PageInfo<SysArchiveLog> pageList(SysArchiveLogReqVo vo);

    /**
     * 访问量排行
     * @return
     */
    List<JSONObject> trafficFileRank();

    /**
     * 数字档案访问量曲线图接口
     * @return
     */
    List<JSONObject> trafficFileGraph(String time);

    /**
     * 日志时间参数查询
     * @param time
     * @return
     */
    List<String> archiveLogTimeParameter(String time);
}
