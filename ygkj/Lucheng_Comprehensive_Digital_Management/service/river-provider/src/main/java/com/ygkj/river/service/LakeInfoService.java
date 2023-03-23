package com.ygkj.river.service;

import com.ygkj.river.vo.request.LakeInfoAddVo;
import com.ygkj.river.vo.request.LakeInfoPageQueryVo;
import com.ygkj.vo.PageVO;

public interface LakeInfoService {

    PageVO lakes(LakeInfoPageQueryVo queryVo);

    boolean insert(LakeInfoAddVo addVo);

    boolean update(LakeInfoAddVo addVo);

    boolean delete(String code);
}
