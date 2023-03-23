package com.ygkj.water.project.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ygkj.project.model.LbKnowledge;
import com.ygkj.project.vo.request.LbKnowledgeReqVo;
import com.ygkj.project.vo.response.LbKnowledgeRespVo;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author paf
 * @DESCRIPTION:
 * @create 2021-11-12 15:29
 */
@Repository
public interface LbKnowledgeMapper extends BaseMapper<LbKnowledge> {
    List<LbKnowledgeRespVo> loadPage(LbKnowledgeReqVo vo);
}
