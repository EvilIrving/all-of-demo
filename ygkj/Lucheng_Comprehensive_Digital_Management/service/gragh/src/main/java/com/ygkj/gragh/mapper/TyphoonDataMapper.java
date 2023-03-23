package com.ygkj.gragh.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ygkj.gragh.model.TyphoonData;
import com.ygkj.gragh.vo.request.TyphoonReqVo;

import java.util.List;

@DS("business")
public interface TyphoonDataMapper extends BaseMapper<TyphoonData> {

    /**
     * 查询历史台风列表
     *
     * @param typhoonReqVo
     * @return
     */
    List<TyphoonData> listHistory(TyphoonReqVo typhoonReqVo);

    TyphoonData load(Integer id);
}
