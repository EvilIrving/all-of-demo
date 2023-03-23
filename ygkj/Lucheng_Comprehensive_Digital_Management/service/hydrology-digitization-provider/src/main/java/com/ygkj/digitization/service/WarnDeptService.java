package com.ygkj.digitization.service;

import com.ygkj.digitization.vo.request.WarnDeptReqVo;
import com.ygkj.digitization.vo.response.WarnDeptRespVo;
import com.ygkj.vo.PageVO;

import java.util.List;

/**
 * @description 预警部门表
 * @author hucong
 * @date 2021-07-26
 */
public interface WarnDeptService {

    /**
     * 新增
     */
    int insert(WarnDeptReqVo vo);

    /**
     * 删除
     */
    int delete(String id);

    /**
     * 更新
     */
    int update(WarnDeptReqVo vo);

    PageVO<WarnDeptRespVo> loadPage(WarnDeptReqVo vo);

    List<WarnDeptRespVo> findTree(WarnDeptReqVo vo);
}
