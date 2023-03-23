package com.ygkj.river.service;

import com.ygkj.river.vo.request.RiverUnionInfoAddVo;
import com.ygkj.river.vo.request.RiverUnionInfoPageQueryVo;
import com.ygkj.vo.PageVO;

public interface RiverUnionInfoService {

    PageVO riverUnionInfos(RiverUnionInfoPageQueryVo queryVo);

    boolean insert(RiverUnionInfoAddVo addVo);

    boolean update(RiverUnionInfoAddVo addVo);

    boolean delete(String id);
}
