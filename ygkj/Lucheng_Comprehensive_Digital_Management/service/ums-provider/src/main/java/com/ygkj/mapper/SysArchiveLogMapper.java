package com.ygkj.mapper;

import com.alibaba.fastjson.JSONObject;
import com.ygkj.auth.VO.request.SysArchiveLogReqVo;
import com.ygkj.auth.api.archivelog.model.SysArchiveLog;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @description 档案操作日志表
 * @author hucong
 * @date 2021-01-19
 */
@Repository
public interface SysArchiveLogMapper {

    /**
     * 新增
     * @author hucong
     * @date 2021/01/19
     **/
    int insert(SysArchiveLog sysArchiveLog);

    /**
     * 刪除
     * @author hucong
     * @date 2021/01/19
     **/
    int delete(int id);

    /**
     * 更新
     * @author hucong
     * @date 2021/01/19
     **/
    int update(SysArchiveLog sysArchiveLog);

    /**
     * 查询 根据主键 id 查询
     * @author hucong
     * @date 2021/01/19
     **/
    SysArchiveLog load(int id);


    /**
     * 查询 分页查询 count
     * @author hucong
     * @date 2021/01/19
     **/
    int pageListCount(int offset,int pagesize);

    /**
     * 查询全部
     * @param vo
     * @return
     */
    List<SysArchiveLog> selectAll(SysArchiveLogReqVo vo);

    /**
     * 访问量排行
     * @return
     */
    List<JSONObject> trafficFileRank();

    /**
     * 数字档案访问量统计（按年月）
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

