package com.ygkj.water.project.mapper;

import com.ygkj.gragh.model.AttHydrDiagBase;
import com.ygkj.project.vo.request.HydrDiagReqVo;

import java.util.List;

/**
 * @author xq
 * @description 水文概化图整编数据表
 * @date 2021-06-02
 */
public interface AttHydrDiagBaseMapper {

    /**
     * 新增
     *
     * @author xq
     * @date 2021/06/02
     **/
    int insert(AttHydrDiagBase attHydrDiagBase);

    /**
     * 刪除
     *
     * @author xq
     * @date 2021/06/02
     **/
    int delete(int id);

    /**
     * 更新
     *
     * @author xq
     * @date 2021/06/02
     **/
    int update(AttHydrDiagBase attHydrDiagBase);

    /**
     * 查询 根据主键 id 查询
     *
     * @author xq
     * @date 2021/06/02
     **/
    AttHydrDiagBase load(int id);

    /**
     * 查询 分页查询
     *
     * @author xq
     * @date 2021/06/02
     **/
    List<AttHydrDiagBase> pageList(int offset, int pagesize);

    /**
     * 查询 分页查询 count
     *
     * @author xq
     * @date 2021/06/02
     **/
    int pageListCount(int offset, int pagesize);

    /**
     * 根据条件查询
     *
     * @return
     */
    List<AttHydrDiagBase> selectByCondition(HydrDiagReqVo reqVo);

}
