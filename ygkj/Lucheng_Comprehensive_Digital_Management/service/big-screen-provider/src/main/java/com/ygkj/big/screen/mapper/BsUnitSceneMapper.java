package com.ygkj.big.screen.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.ygkj.big.screen.model.BsUnitScene;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author xq
 * @description 大屏与组件关联表
 * @date 2021-06-28
 */
@DS("meta")
public interface BsUnitSceneMapper {

    /**
     * 新增
     *
     * @author xq
     * @date 2021/06/28
     **/
    int insert(BsUnitScene bsUnitScene);

    /**
     * 刪除
     *
     * @author xq
     * @date 2021/06/28
     **/
    int delete(@Param("sceneId") String sceneId, @Param("unitId") String unitId);

    /**
     * 更新
     *
     * @author xq
     * @date 2021/06/28
     **/
    int update(BsUnitScene bsUnitScene);

    /**
     * 查询 根据主键 id 查询
     *
     * @author xq
     * @date 2021/06/28
     **/
    BsUnitScene load(String id);

    /**
     * 查询 分页查询
     *
     * @author xq
     * @date 2021/06/28
     **/
    List<BsUnitScene> pageList(int offset, int pagesize);

    /**
     * 查询 分页查询 count
     *
     * @author xq
     * @date 2021/06/28
     **/
    int pageListCount(int offset, int pagesize);

    List<BsUnitScene> selectByCondition(@Param("sceneId") String sceneId, @Param("unitId") String unitId);

}
