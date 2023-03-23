package com.ygkj.river.mapper;

import com.ygkj.river.model.AttRivShapeBase;
import com.ygkj.river.vo.response.RiverCoordinateResVo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author xq
 * @description 河道水质测站表
 * @date 2021-08-04
 */

public interface AttRivShapeBaseMapper {

    /**
     * 新增
     *
     * @author xq
     * @date 2021/08/04
     **/
    int insert(AttRivShapeBase attRivShapeBase);

    /**
     * 刪除
     *
     * @author xq
     * @date 2021/08/04
     **/
    int delete(int id);

    /**
     * 更新
     *
     * @author xq
     * @date 2021/08/04
     **/
    int update(AttRivShapeBase attRivShapeBase);

    /**
     * 查询 根据主键 id 查询
     *
     * @author xq
     * @date 2021/08/04
     **/
    AttRivShapeBase load(int id);

    /**
     * 查询 分页查询
     *
     * @author xq
     * @date 2021/08/04
     **/
    List<AttRivShapeBase> pageList(int offset, int pagesize);

    /**
     * 查询 分页查询 count
     *
     * @author xq
     * @date 2021/08/04
     **/
    int pageListCount(int offset, int pagesize);

    List<RiverCoordinateResVo> selectByCondition(String riverNo);

    AttRivShapeBase findByRiverNo(@Param("riverNo") String riverNo);
}
