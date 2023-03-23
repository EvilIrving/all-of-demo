package com.ygkj.project.vo.request;

import com.ygkj.project.model.RegularReview;
import lombok.Data;

import java.util.List;

/**
 * @author wgf
 * @date 2022/4/25 10:43
 * @Description:
 */
@Data
public class RegularReviewVo extends RegularReview {

    private List<EvaluationItemDataVo> list;
}
