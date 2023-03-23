package com.ygkj.river.mapper;

import com.ygkj.river.model.AttLkBase;
import com.ygkj.river.vo.request.LakeInfoPageQueryVo;
import com.ygkj.river.vo.response.LakeInfoResVo;

import java.util.List;

/**
 * 湖泊基础信息表
 *
 * @author huangzh
 * @date 2021/03/02
 */
public interface AttLkBaseMapper {

    /**
     * [新增]
     *
     * @author huangzh
     * @date 2021/03/02
     **/
    int insert(AttLkBase attLkBase);

    /**
     * [刪除]
     *
     * @author huangzh
     * @date 2021/03/02
     **/
    int delete(String code);

    /**
     * [更新]
     *
     * @author huangzh
     * @date 2021/03/02
     **/
    int update(AttLkBase attLkBase);

    /**
     * [查询] 根据主键 id 查询
     *
     * @author huangzh
     * @date 2021/03/02
     **/
    AttLkBase load(int id);

    /**
     * [查询] 分页查询
     *
     * @author huangzh
     * @date 2021/03/02
     **/
    List<AttLkBase> pageList(int offset, int pagesize);

    /**
     * [查询] 分页查询 count
     *
     * @author huangzh
     * @date 2021/03/02
     **/
    int pageListCount(int offset, int pagesize);

    List<LakeInfoResVo> selectByCondition(LakeInfoPageQueryVo queryVo);

}

