package com.ygkj.big.screen.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.ygkj.big.screen.model.BsDrillStcase;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author xq
 * @description 方案测站关联表
 * @date 2021-07-08
 */
@DS("meta")
public interface BsDrillStcaseMapper {

    /**
     * 新增
     *
     * @author xq
     * @date 2021/07/08
     **/
    int insert(BsDrillStcase bsDrillStcase);

    /**
     * 刪除
     *
     * @author xq
     * @date 2021/07/08
     **/
    int delete(int id);

    /**
     * 更新
     *
     * @author xq
     * @date 2021/07/08
     **/
    int update(BsDrillStcase bsDrillStcase);

    /**
     * 查询 根据主键 id 查询
     *
     * @author xq
     * @date 2021/07/08
     **/
    BsDrillStcase load(int id);

    /**
     * 查询 分页查询
     *
     * @author xq
     * @date 2021/07/08
     **/
    List<BsDrillStcase> pageList(int offset, int pagesize);

    /**
     * 查询 分页查询 count
     *
     * @author xq
     * @date 2021/07/08
     **/
    int pageListCount(int offset, int pagesize);

    List<BsDrillStcase> selectByCondition(BsDrillStcase reqVo);

}

