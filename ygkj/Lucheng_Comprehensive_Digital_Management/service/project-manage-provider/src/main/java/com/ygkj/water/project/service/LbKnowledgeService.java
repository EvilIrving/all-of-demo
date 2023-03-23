package com.ygkj.water.project.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ygkj.project.model.LbKnowledge;
import com.ygkj.project.vo.request.LbKnowledgeReqVo;
import com.ygkj.project.vo.response.LbKnowledgeRespVo;
import com.ygkj.vo.PageVO;

/**
 * @author paf
 * @DESCRIPTION:
 * @create 2021-11-12 15:25
 */
public interface LbKnowledgeService extends IService<LbKnowledge> {
    /**
     * 新增
     */
    int insert(LbKnowledge vo);


    /**
     * 更新
     */
    int update(LbKnowledge vo);

    /**
     * 分页查找
     * @param vo
     * @return
     */
    PageVO<LbKnowledgeRespVo> loadPage(LbKnowledgeReqVo vo);
}
