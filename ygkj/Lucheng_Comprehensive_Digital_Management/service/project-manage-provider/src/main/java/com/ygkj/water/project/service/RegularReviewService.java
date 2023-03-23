package com.ygkj.water.project.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ygkj.project.model.RegularReview;
import com.ygkj.project.vo.request.EvaluationItemDataVo;
import com.ygkj.project.vo.request.RegularReviewVo;
import com.ygkj.vo.PageVO;

import java.util.List;

/**
 * @author wgf
 * @date 2022/4/24 10:36
 * @Description:
 */
public interface RegularReviewService extends IService<RegularReview> {

    PageVO<RegularReview> getList(RegularReview regularReview);

    List<EvaluationItemDataVo> getEdListById(String id);

    PageVO<RegularReview> yearList(RegularReview regularReview);
}
