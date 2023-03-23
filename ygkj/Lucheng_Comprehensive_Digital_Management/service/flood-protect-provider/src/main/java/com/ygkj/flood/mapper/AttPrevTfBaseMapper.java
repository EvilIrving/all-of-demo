package com.ygkj.flood.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ygkj.flood.model.AttPrevBase;
import com.ygkj.flood.model.AttPrevTfBase;
import com.ygkj.flood.vo.request.PrevTfInfoReqVo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author xq
 * @description 山洪防治对象表
 * @date 2021-06-08
 */
@Mapper
@Repository
public interface AttPrevTfBaseMapper extends BaseMapper<AttPrevTfBase> {

    /**
     * 新增
     *
     * @author xq
     * @date 2021/06/08
     **/
    int insert(AttPrevTfBase attPrevTfBase);

    /**
     * 刪除
     *
     * @author xq
     * @date 2021/06/08
     **/
    int delete(String id);

    /**
     * 更新
     *
     * @author xq
     * @date 2021/06/08
     **/
    int update(AttPrevTfBase attPrevTfBase);

    /**
     * 查询 根据主键 id 查询
     *
     * @author xq
     * @date 2021/06/08
     **/
    AttPrevTfBase load(String id);

    /**
     * 查询 分页查询
     *
     * @author xq
     * @date 2021/06/08
     **/
    List<AttPrevTfBase> pageList(int offset, int pagesize);

    /**
     * 查询 分页查询 count
     *
     * @author xq
     * @date 2021/06/08
     **/
    int pageListCount(int offset, int pagesize);


    /**
     * 根据条件查询，条件待定
     *
     * @return
     */
    List<AttPrevTfBase> selectByCondition(PrevTfInfoReqVo reqVo);
}

