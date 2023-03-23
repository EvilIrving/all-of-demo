package com.ygkj.river.mapper;

import com.ygkj.river.model.RiverInfo;
import com.ygkj.river.vo.request.RiverInfoPageQueryVo;
import com.ygkj.river.vo.response.RiverInfoResVo;

import java.util.List;

/**
 * 河道名录表
 *
 * @author huangzh
 * @date 2021/03/02
 */
public interface RiverInfoMapper {

    /**
     * [新增]
     *
     * @author huangzh
     * @date 2021/03/02
     **/
    int insert(RiverInfo riverInfo);

    /**
     * [刪除]
     *
     * @author huangzh
     * @date 2021/03/02
     **/
    int delete(String ids);

    /**
     * [更新]
     *
     * @author huangzh
     * @date 2021/03/02
     **/
    int update(RiverInfo riverInfo);

    /**
     * [查询] 根据主键 id 查询
     *
     * @author huangzh
     * @date 2021/03/02
     **/
    RiverInfoResVo load(String id);

    /**
     * [查询] 分页查询
     *
     * @author huangzh
     * @date 2021/03/02
     **/
    List<RiverInfo> pageList(int offset, int pagesize);

    /**
     * [查询] 分页查询 count
     *
     * @author huangzh
     * @date 2021/03/02
     **/
    int pageListCount(int offset, int pagesize);


    List<RiverInfoResVo> selectByCondition(RiverInfoPageQueryVo queryVo);

    List<RiverInfoResVo> selectByRiverCodes(String riverCodes);


}

