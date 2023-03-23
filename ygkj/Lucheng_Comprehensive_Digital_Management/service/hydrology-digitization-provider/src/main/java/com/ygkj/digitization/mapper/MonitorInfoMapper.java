package com.ygkj.digitization.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.ygkj.digitization.model.MonitorInfo;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * @author xq
 * @Description 排班管理持久化层
 * @Date 2021/1/14
 */
@DS("business")
public interface MonitorInfoMapper {
    /**
     * [新增]
     * @author xq
     * @date 2021/01/14
     **/
    int insert(MonitorInfo monitorInfo);

    /**
     * [刪除]
     * @author xq
     * @date 2021/01/14
     **/
    int delete(int id);

    /**
     * [更新]
     * @author xq
     * @date 2021/01/14
     **/
    int update(MonitorInfo monitorInfo);

    /**
     * [查询] 根据主键 id 查询
     * @author xq
     * @date 2021/01/14
     **/
    MonitorInfo load(int id);

    /**
     * [查询] 分页查询
     * @author xq
     * @date 2021/01/14
     **/
    List<MonitorInfo> pageList(int offset, int pagesize);

    /**
     * [查询] 分页查询 count
     * @author xq
     * @date 2021/01/14
     **/
    int pageListCount(int offset,int pagesize);

    MonitorInfo selectByMonitorDate(Date monitorDate);

    List<MonitorInfo> selectByMonitorDateStartEnd(@Param("startDate") Date startDate,@Param("endDate") Date endDate);

    int deleteById(Integer infoId);

    Integer checkRecordExistByDate(@Param("monitorDate") Date monitorDate);
}
