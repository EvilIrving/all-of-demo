package com.ygkj.project.mapper;

import com.ygkj.project.api.vo.model.BmReach;
import com.ygkj.project.api.vo.req.BmReachReqVo;
import com.ygkj.project.api.vo.res.BmReachRespVo;

import java.util.List;

/**
 * @author hucong
 * @description 河道基本信息表
 * @date 2022-04-27
 */
public interface BmReachMapper {

    /**
     * 新增
     *
     * @author hucong
     * @date 2022/04/27
     **/
    int insert(BmReach bmReach);

    /**
     * 刪除
     *
     * @author hucong
     * @date 2022/04/27
     **/
    int delete(String id);

    /**
     * 更新
     *
     * @author hucong
     * @date 2022/04/27
     **/
    int update(BmReach bmReach);

    /**
     * 数据list查找
     * @param vo
     * @return
     */
    List<BmReachRespVo> loadPage(BmReachReqVo vo);

}