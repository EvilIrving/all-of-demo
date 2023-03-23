package com.ygkj.river.mapper;

import com.ygkj.river.model.AttRiverOutletBase;
import com.ygkj.river.vo.request.RiverOutletQueryVo;
import com.ygkj.river.vo.response.RiverOutletResVo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author xq
 * @description 入河排污口基本信息表（温州市第三次水资源调查评价报告附表 11-2-1）
 * @date 2021-10-09
 */
public interface AttRiverOutletBaseMapper {

    /**
     * 新增
     *
     * @author xq
     * @date 2021/10/09
     **/
    int insert(AttRiverOutletBase attRiverOutletBase);

    /**
     * 刪除
     *
     * @author xq
     * @date 2021/10/09
     **/
    int delete(int id);

    /**
     * 更新
     *
     * @author xq
     * @date 2021/10/09
     **/
    int update(AttRiverOutletBase attRiverOutletBase);

    /**
     * 查询 根据主键 id 查询
     *
     * @author xq
     * @date 2021/10/09
     **/
    AttRiverOutletBase load(int id);

    /**
     * 查询 分页查询
     *
     * @author xq
     * @date 2021/10/09
     **/
    List<AttRiverOutletBase> pageList(int offset, int pagesize);

    /**
     * 查询 分页查询 count
     *
     * @author xq
     * @date 2021/10/09
     **/
    int pageListCount(int offset, int pagesize);

    /**
     * 查询 根据条件查询
     *
     * @author xq
     * @date 2021/10/09
     **/
    List<RiverOutletResVo> selectByCondition(RiverOutletQueryVo queryVo);

}

