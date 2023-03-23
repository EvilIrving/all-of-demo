package com.ygkj.flood.mapper;

import com.ygkj.flood.model.AttDictBase;
import com.ygkj.flood.vo.request.AttDictRequestVo;
import com.ygkj.flood.vo.response.AttDictResVo;

import java.util.List;

/**
 * 数据字典
 *
 * @author huangzh
 * @date 2021/01/18
 */
public interface AttDictBaseMapper {

    /**
     * [新增]
     *
     * @author huangzh
     * @date 2021/01/18
     **/
    int insert(AttDictBase attDictBase);

    /**
     * [刪除]
     *
     * @author huangzh
     * @date 2021/01/18
     **/
    int delete(String id);

    /**
     * [更新]
     *
     * @author huangzh
     * @date 2021/01/18
     **/
    int update(AttDictBase attDictBase);

    /**
     * [查询] 根据主键 id 查询
     *
     * @author huangzh
     * @date 2021/01/18
     **/
    AttDictBase load(int id);

    /**
     * [查询] 分页查询
     *
     * @author huangzh
     * @date 2021/01/18
     **/
    List<AttDictBase> pageList(int offset, int pagesize);

    /**
     * [查询] 分页查询 count
     *
     * @author huangzh
     * @date 2021/01/18
     **/
    int pageListCount(int offset, int pagesize);

    List<AttDictResVo> selectDict(AttDictRequestVo requestVo);

}

