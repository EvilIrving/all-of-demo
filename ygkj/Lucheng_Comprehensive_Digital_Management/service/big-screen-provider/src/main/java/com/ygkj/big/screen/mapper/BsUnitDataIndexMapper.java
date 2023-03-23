package com.ygkj.big.screen.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.ygkj.big.screen.model.BsUnitDataIndex;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * @author xq
 * @description 组件数据表
 * @date 2021-07-09
 */
@DS("meta")
public interface BsUnitDataIndexMapper {

    /**
     * 新增
     *
     * @author xq
     * @date 2021/07/09
     **/
    int insert(BsUnitDataIndex bsUnitDataIndex);

    /**
     * 刪除
     *
     * @author xq
     * @date 2021/07/09
     **/
    int delete(String dataId);

    /**
     * 更新
     *
     * @author xq
     * @date 2021/07/09
     **/
    int update(BsUnitDataIndex bsUnitDataIndex);

    /**
     * 查询 根据主键 id 查询
     *
     * @author xq
     * @date 2021/07/09
     **/
    BsUnitDataIndex load(String dataId);

    /**
     * 查询 分页查询
     *
     * @author xq
     * @date 2021/07/09
     **/
    List<BsUnitDataIndex> pageList(int offset, int pagesize);

    /**
     * 查询 分页查询 count
     *
     * @author xq
     * @date 2021/07/09
     **/
    int pageListCount(int offset, int pagesize);

    String selectIdByCondition(BsUnitDataIndex reqVo);

    List<BsUnitDataIndex> selectByCondition(BsUnitDataIndex reqVo);

    BsUnitDataIndex selectByUniqueIndex(@Param("caseId") String caseId, @Param("unitName") String unitName,
                                        @Param("dataApi") String dataApi, @Param("dataParam") String dataParam,
                                        @Param("dataType") Integer dataType, @Param("timeStamp") Date timeStamp);

}
