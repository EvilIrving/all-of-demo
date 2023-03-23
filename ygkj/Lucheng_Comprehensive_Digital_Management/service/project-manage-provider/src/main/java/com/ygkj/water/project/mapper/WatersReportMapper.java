package com.ygkj.water.project.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ygkj.project.model.WatersReport;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author wgf
 * @date 2022/5/23 11:37
 * @Description:
 */
@Repository
public interface WatersReportMapper extends BaseMapper<WatersReport> {

    List<WatersReport> listPage(@Param("watersReport") WatersReport watersReport);
}
