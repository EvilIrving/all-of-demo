package com.ygkj.water.project.service;


import com.ygkj.project.model.ProjTestCompletion;
import com.ygkj.project.vo.request.ProjTestCompletionInsVo;
import com.ygkj.project.vo.request.ProjTestCompletionReqVo;
import com.ygkj.project.vo.response.ProjTestCompletionRespVo;
import com.ygkj.vo.PageVO;

/**
 * @author hucong
 * @description 工程监督-检测完成情况表
 * @date 2021-07-09
 */
public interface ProjTestCompletionService {

    /**
     * 新增
     */
    int insert(ProjTestCompletionInsVo vo);

    /**
     * 删除
     */
    int delete(String id);

    /**
     * 更新
     */
    int update(ProjTestCompletionInsVo vo);

    /**
     * 分页查找
     *
     * @param vo
     * @return
     */
    PageVO<ProjTestCompletionRespVo> loadPage(ProjTestCompletionReqVo vo);
}
