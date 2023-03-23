package com.ygkj.flood.service;


import com.ygkj.flood.vo.request.AttDictAddVo;
import com.ygkj.flood.vo.request.AttDictRequestVo;
import com.ygkj.flood.vo.response.AttDictResVo;

import java.util.List;

public interface AttDictService {

    boolean insert(AttDictAddVo attDictAddVo);

    List<AttDictResVo> selectDict(AttDictRequestVo attDictRequestVo);
}
