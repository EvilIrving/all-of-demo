package com.ygkj.water.project.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ygkj.project.model.MoRegularMonitor;
import com.ygkj.project.vo.request.MoRegularMonitorVo;
import com.ygkj.project.vo.request.ProjMonitorReqVo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * @author lxl
 * @create 2022-04-28 14:51
 * @description
 */
public interface MoRegularMonitorMapper extends BaseMapper<MoRegularMonitor> {

    /**
     * 查询列表
     * @param reqVo
     * @return
     */
    List<MoRegularMonitorVo> selectRegularMonitorList(@Param("reqVo") ProjMonitorReqVo reqVo);

    @Update("update mo_regular_monitor set del_flag = 1 where id = #{id}")
    Integer deleteByRegularId(@Param("id") String id);
}
