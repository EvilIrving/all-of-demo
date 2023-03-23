package com.dcy.datamanage.biz.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dcy.datamanage.api.model.DataAssessResult;

import java.util.List;

public interface DataAssessResultMapper extends BaseMapper<DataAssessResult> {

    /**
     * 批量更新分数
     *
     * @param resultList
     */
    void replaceAll(List<DataAssessResult> resultList);
}
