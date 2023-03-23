package com.ygkj.water.project.service;

import com.ygkj.project.model.AttSixchargeBase;
import com.ygkj.project.model.AttSixchargeChangeRecord;
import com.ygkj.project.vo.request.AttSixchargeBaseReqVo;
import com.ygkj.project.vo.response.AttSixchargeBaseRespVo;
import com.ygkj.vo.PageVO;

public interface AttSixchargeService {
    int save(AttSixchargeBase entity);

    int update(AttSixchargeBase entity);

    PageVO<AttSixchargeBaseRespVo> queryPage(AttSixchargeBaseReqVo vo);

    AttSixchargeBaseRespVo queryOne(AttSixchargeBaseReqVo vo);

    PageVO<AttSixchargeChangeRecord> queryChangeRecord(AttSixchargeChangeRecord record);
}
