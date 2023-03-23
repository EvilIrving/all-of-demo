package com.ygkj.big.screen.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.ygkj.big.screen.model.BsUnitData;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author xq
 * @description 组件数据表
 * @date 2021-07-08
 */
@DS("meta")
public interface BsUnitDataMapper {

    /**
     * 新增
     *
     * @author xq
     * @date 2021/07/08
     **/
    int insert(BsUnitData bsUnitData);

    /**
     * 刪除
     *
     * @author xq
     * @date 2021/07/08
     **/
    int delete(String dataId);

    /**
     * 更新
     *
     * @author xq
     * @date 2021/07/08
     **/
    int update(BsUnitData bsUnitData);

    /**
     * 查询 根据主键 id 查询
     *
     * @author xq
     * @date 2021/07/08
     **/
    BsUnitData load(String dataId);

    /**
     * 查询 分页查询
     *
     * @author xq
     * @date 2021/07/08
     **/
    List<BsUnitData> pageList(int offset, int pagesize);

    /**
     * 查询 分页查询 count
     *
     * @author xq
     * @date 2021/07/08
     **/
    int pageListCount(int offset, int pagesize);

    @Select("select data_value from  bs_unit_data WHERE data_id = #{dataId}")
    String loadDataValue(String dataId);

}
