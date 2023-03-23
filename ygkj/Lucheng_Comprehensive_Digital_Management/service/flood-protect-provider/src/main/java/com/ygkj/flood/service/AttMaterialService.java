package com.ygkj.flood.service;

import com.ygkj.flood.vo.request.AttMaterialBaseAddVo;
import com.ygkj.flood.vo.request.AttMaterialQueryVo;
import com.ygkj.flood.vo.response.AttMaterialResVo;

import java.util.List;

public interface AttMaterialService {

    List<AttMaterialResVo> selectByCondition(AttMaterialQueryVo queryVo);

    boolean addMaterials(List<AttMaterialBaseAddVo> list);
}
