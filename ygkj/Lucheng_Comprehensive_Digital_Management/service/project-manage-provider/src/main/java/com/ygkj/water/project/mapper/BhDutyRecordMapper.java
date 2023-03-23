package com.ygkj.water.project.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ygkj.project.model.BhDutyRecord;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author wgf
 * @date 2022/4/26 14:04
 * @Description:
 */
@DS("exchange")
@Repository
public interface BhDutyRecordMapper extends BaseMapper<BhDutyRecord> {

    List<BhDutyRecord> getList(@Param("bhDutyRecord") BhDutyRecord bhDutyRecord);
}
