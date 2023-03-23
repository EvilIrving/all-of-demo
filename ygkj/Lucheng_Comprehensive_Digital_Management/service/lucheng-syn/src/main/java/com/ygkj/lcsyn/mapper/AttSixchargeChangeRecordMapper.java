package com.ygkj.lcsyn.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ygkj.lcsyn.model.AttSixchargeChangeRecord;
import org.springframework.stereotype.Repository;

@Repository
@DS("basic")
public interface AttSixchargeChangeRecordMapper extends BaseMapper<AttSixchargeChangeRecord> {
}
