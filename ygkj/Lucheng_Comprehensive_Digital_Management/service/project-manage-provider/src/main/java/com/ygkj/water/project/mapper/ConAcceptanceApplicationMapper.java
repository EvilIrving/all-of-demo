package com.ygkj.water.project.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ygkj.project.model.ConAcceptanceApplication;
import com.ygkj.project.vo.request.ConAcceptanceApplicationReqVo;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConAcceptanceApplicationMapper extends BaseMapper<ConAcceptanceApplication> {
    List<ConAcceptanceApplication> loadPage1(ConAcceptanceApplicationReqVo vo);

    List<ConAcceptanceApplication> loadPage2(ConAcceptanceApplicationReqVo vo);
}
