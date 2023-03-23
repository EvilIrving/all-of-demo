package com.ygkj.river.service;

import com.ygkj.river.vo.request.RiverInfoAddVo;
import com.ygkj.river.vo.request.RiverInfoPageQueryVo;
import com.ygkj.vo.PageVO;

public interface RiverService {

    PageVO riverPageQuery(RiverInfoPageQueryVo queryVo);

    boolean insert(RiverInfoAddVo addVo);

    boolean update(RiverInfoAddVo addVo);

    boolean delete(String ids);
}
