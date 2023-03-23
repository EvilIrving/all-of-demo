package com.ygkj.gragh.mapper;

import com.ygkj.gragh.model.AttRiverBase;
import com.ygkj.gragh.vo.request.RiverQueryVo;

import java.util.List;

/**
 * @author Huangzh
 * @description 河道基础信息表
 * @date 2020-12-11
 */
public interface AttRiverBaseMapper {

    /**
     * 新增
     *
     * @author Huangzh
     * @date 2020/12/11
     **/
    int insert(AttRiverBase attRiverBase);

    /**
     * 刪除
     *
     * @author Huangzh
     * @date 2020/12/11
     **/
    int delete(int id);

    /**
     * 更新
     *
     * @author Huangzh
     * @date 2020/12/11
     **/
    int update(AttRiverBase attRiverBase);

    /**
     * 查询 根据主键 id 查询
     *
     * @author Huangzh
     * @date 2020/12/11
     **/
    AttRiverBase load(int id);

    /**
     * 查询 分页查询
     *
     * @author Huangzh
     * @date 2020/12/11
     **/
    List<AttRiverBase> pageList(int offset, int pagesize);

    /**
     * 查询 分页查询 count
     *
     * @author Huangzh
     * @date 2020/12/11
     **/
    int pageListCount(int offset, int pagesize);

    List<AttRiverBase> selectByCondition(RiverQueryVo riverQueryVo);

}

