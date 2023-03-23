package com.ygkj.water.project.mapper;

import com.ygkj.project.model.ProjTestCompletion;
import com.ygkj.project.vo.request.ProjTestCompletionReqVo;
import com.ygkj.project.vo.response.ProjTestCompletionRespVo;

import java.util.List;

public interface ProjTestCompletionMapper {

    /**
     * 新增
     *
     * @author hucong
     * @date 2021/07/09
     **/
    int insert(ProjTestCompletion projTestCompletion);

    /**
     * 刪除
     *
     * @author hucong
     * @date 2021/07/09
     **/
    int delete(String id);

    /**
     * 更新
     *
     * @author hucong
     * @date 2021/07/09
     **/
    int update(ProjTestCompletion projTestCompletion);

    List<ProjTestCompletionRespVo> loadPage(ProjTestCompletionReqVo vo);
}
