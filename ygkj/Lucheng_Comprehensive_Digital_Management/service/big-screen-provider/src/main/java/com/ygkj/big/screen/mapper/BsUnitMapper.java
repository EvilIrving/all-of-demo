package com.ygkj.big.screen.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.ygkj.big.screen.model.BsUnit;
import com.ygkj.big.screen.vo.request.BsUnitQueryReqVo;
import com.ygkj.big.screen.vo.response.ChartResVo;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author xq
 * @description 大屏组件表
 * @date 2021-06-28
 */
@DS("meta")
public interface BsUnitMapper {

    /**
     * 新增
     *
     * @author xq
     * @date 2021/06/28
     **/
    int insert(BsUnit bsUnit);

    /**
     * 刪除
     *
     * @author xq
     * @date 2021/06/28
     **/
    int delete(String id);

    /**
     * 更新
     *
     * @author xq
     * @date 2021/06/28
     **/
    int update(BsUnit bsUnit);

    /**
     * 查询 根据主键 id 查询
     *
     * @author xq
     * @date 2021/06/28
     **/
    BsUnit load(int id);

    /**
     * 查询 分页查询
     *
     * @author xq
     * @date 2021/06/28
     **/
    List<BsUnit> pageList(int offset, int pagesize);

    /**
     * 查询 分页查询 count
     *
     * @author xq
     * @date 2021/06/28
     **/
    int pageListCount(int offset, int pagesize);

    @Select("select count(1) as `value`, bs_name as `key` from bs_unit GROUP BY bs_name")
    List<ChartResVo<String, Long>> countUnitNumByBsName();

    List<BsUnit> selectByCondition(BsUnitQueryReqVo reqVo);

    //    @Select("select id,unit_name from lc_meta_db.bs_unit where find_in_set(unit_name,#{unitNames}) and bs_name=#{bsName}")
//    @ResultType(BsUnit.class)
    List<BsUnit> listIdByName(String unitNames);

}
