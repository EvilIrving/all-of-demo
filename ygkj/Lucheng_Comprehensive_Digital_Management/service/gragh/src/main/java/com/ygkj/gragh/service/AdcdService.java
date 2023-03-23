package com.ygkj.gragh.service;

import com.ygkj.gragh.model.AttAdcdBase;
import com.ygkj.gragh.vo.request.AdcdQueryVo;

import java.util.List;

public interface AdcdService {


    String orderSql = "ORDER BY FIELD(CONCAT(SUBSTR( `adcd` from 1 for 6),'000000'),'330383000000','330327000000','330329000000','330326000000','330328000000','330324000000','330381000000','330382000000','330322000000','330304000000','330303000000','330302000000','330301000000') desc";


    List<AttAdcdBase> adcd(AdcdQueryVo adcdQueryVo);

    List<AttAdcdBase> selectAdnmByAdcdList(List<String> adcdList);
}
