package com.ygkj.river.mapper;

import com.ygkj.river.model.RiverUnionInfo;
import com.ygkj.river.vo.request.RiverUnionInfoPageQueryVo;
import com.ygkj.river.vo.response.RiverUnionInfoResVo;

import java.util.List;

/**
 * 河道关联项目表
 *
 * @author huangzh
 * @date 2021/03/02
 */
public interface RiverUnionInfoMapper {

    /**
     * [新增]
     *
     * @author huangzh
     * @date 2021/03/02
     **/
    int insert(RiverUnionInfo riverUnionInfo);

    /**
     * [刪除]
     *
     * @author huangzh
     * @date 2021/03/02
     **/
    int delete(String id);

    /**
     * [更新]
     *
     * @author huangzh
     * @date 2021/03/02
     **/
    int update(RiverUnionInfo riverUnionInfo);

    /**
     * [查询] 根据主键 id 查询
     *
     * @author huangzh
     * @date 2021/03/02
     **/
    RiverUnionInfo load(int id);

    /**
     * [查询] 分页查询
     *
     * @author huangzh
     * @date 2021/03/02
     **/
    List<RiverUnionInfo> pageList(int offset, int pagesize);

    /**
     * [查询] 分页查询 count
     *
     * @author huangzh
     * @date 2021/03/02
     **/
    int pageListCount(int offset, int pagesize);

    List<RiverUnionInfoResVo> selectByCondition(RiverUnionInfoPageQueryVo queryVo);

}

