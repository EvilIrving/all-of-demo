package com.ygkj.water.project.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ygkj.project.model.RegularReview;
import com.ygkj.project.vo.request.EvaluationItemDataVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author wgf
 * @date 2022/4/24 10:42
 * @Description:
 */
@Repository
public interface RegularReviewMapper extends BaseMapper<RegularReview> {

    List<RegularReview> getList(@Param("regularReview") RegularReview regularReview);

    List<EvaluationItemDataVo> getEdListById(@Param("id") String id);

    List<RegularReview> yearList(@Param("regularReview")RegularReview regularReview);
}
