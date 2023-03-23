package com.dcy.datamanage.biz.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dcy.datamanage.api.model.DataAssessFrequency;

import java.util.List;

public interface DataAssessFrequencyMapper extends BaseMapper<DataAssessFrequency> {

    /**
     * 批量替换
     *
     * @param list
     */
    void batchReplace(List<DataAssessFrequency> list);
}
