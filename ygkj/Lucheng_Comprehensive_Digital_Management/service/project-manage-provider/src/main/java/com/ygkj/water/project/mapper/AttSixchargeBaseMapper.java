package com.ygkj.water.project.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ygkj.project.model.AttSixchargeBase;
import com.ygkj.project.model.AttSixchargeChangeRecord;
import com.ygkj.project.vo.request.AttSixchargeBaseReqVo;
import com.ygkj.project.vo.response.AttSixchargeBaseRespVo;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;


import java.util.List;

@Repository
public interface AttSixchargeBaseMapper extends BaseMapper<AttSixchargeBase> {
    List<AttSixchargeBaseRespVo> queryPage(AttSixchargeBaseReqVo vo);

    AttSixchargeBaseRespVo queryOne(AttSixchargeBaseReqVo vo);

    List<AttSixchargeChangeRecord> queryChangeRecord(AttSixchargeChangeRecord record);

    @Select("select prcd from data_maintain_people_syn")
    List<String> queryProjectSyn();
}
