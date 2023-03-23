package com.ygkj.water.project.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ygkj.project.model.RegularReview;
import com.ygkj.project.vo.request.EvaluationItemDataVo;
import com.ygkj.project.vo.request.RegularReviewVo;
import com.ygkj.utils.PageUtils;
import com.ygkj.vo.PageVO;
import com.ygkj.water.project.mapper.RegularReviewMapper;
import com.ygkj.water.project.service.RegularReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author wgf
 * @date 2022/4/24 10:41
 * @Description:
 */
@Service
public class RegularReviewServiceImpl extends ServiceImpl<RegularReviewMapper, RegularReview> implements RegularReviewService {

    @Autowired
    private RegularReviewMapper regularReviewMapper;

    @Override
    public PageVO<RegularReview> getList(RegularReview regularReview) {
        regularReview.page();
        List<RegularReview> list = regularReviewMapper.getList(regularReview);
        return PageUtils.getPageVO(list);
    }

    @Override
    public List<EvaluationItemDataVo> getEdListById(String id) {
        return regularReviewMapper. getEdListById(id);
    }

    @Override
    public PageVO<RegularReview> yearList(RegularReview regularReview) {
        regularReview.page();
        List<RegularReview> list = regularReviewMapper.yearList(regularReview);
        return PageUtils.getPageVO(list);
    }

}
