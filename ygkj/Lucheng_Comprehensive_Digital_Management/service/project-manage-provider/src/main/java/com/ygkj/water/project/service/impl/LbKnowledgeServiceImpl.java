package com.ygkj.water.project.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.ygkj.project.model.LbKnowledge;
import com.ygkj.project.vo.request.LbKnowledgeReqVo;
import com.ygkj.project.vo.response.LbKnowledgeRespVo;
import com.ygkj.utils.PageUtils;
import com.ygkj.vo.PageVO;
import com.ygkj.water.project.mapper.LbKnowledgeMapper;
import com.ygkj.water.project.service.LbKnowledgeService;

import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author pqf
 * @DESCRIPTION:
 * @create 2021-11-12 15:28
 */
@Service
public class LbKnowledgeServiceImpl extends ServiceImpl<LbKnowledgeMapper, LbKnowledge>  implements LbKnowledgeService {

    @Override
    public int insert(LbKnowledge vo) {
        vo.setCreateTime(new Date());
        return baseMapper.insert(vo);
    }

    @Override
    public int update(LbKnowledge vo) {
        return baseMapper.updateById(vo);

    }

    @Override
    public PageVO<LbKnowledgeRespVo> loadPage(LbKnowledgeReqVo vo) {

        if (vo.getPageNum() != null && vo.getPageNum() > 0
                && vo.getPageSize() != null && vo.getPageSize() > 0) {
            PageHelper.startPage(vo.getPageNum(), vo.getPageSize());
        }
        return PageUtils.getPageVO(baseMapper.loadPage(vo));
    }
}
