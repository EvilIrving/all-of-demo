package com.ygkj.digitization.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.ygkj.digitization.model.GmGeneral;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * 日常业务管理-日常管理
 * @author xq
 * @date 2021/02/01
 */
@DS("business")
public interface GmGeneralMapper {

    /**
     * [新增]
     * @author xq
     * @date 2021/02/01
     **/
    int insert(GmGeneral gmGeneral);

    /**
     * [刪除]
     * @author xq
     * @date 2021/02/01
     **/
    int delete(int id);

    /**
     * [更新]
     * @author xq
     * @date 2021/02/01
     **/
    int update(GmGeneral gmGeneral);

    /**
     * [查询] 根据主键 id 查询
     * @author xq
     * @date 2021/02/01
     **/
    GmGeneral load(int id);

    /**
     * [查询] 分页查询
     * @author xq
     * @date 2021/02/01
     **/
    List<GmGeneral> pageList(int offset, int pagesize);

    /**
     * [查询] 分页查询 count
     * @author xq
     * @date 2021/02/01
     **/
    int pageListCount(int offset,int pagesize);

    List<GmGeneral> selectByAdcdFillTimeTaskName(@Param("adcd")String adcd, @Param("rtStart")Date rtStart, @Param("rtEnd")Date rtEnd, @Param("taskName")String taskName);

    int virtualDeleteById(String id);

}
