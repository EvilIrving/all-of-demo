package com.ygkj.big.screen.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.ygkj.big.screen.model.BsDrillAffair;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author xq
 * @description bs_drill_affair
 * @date 2021-07-10
 */
@DS("meta")
public interface BsDrillAffairMapper {

    /**
     * 新增
     *
     * @author xq
     * @date 2021/07/10
     **/
    int insert(BsDrillAffair bsDrillAffair);

    /**
     * 刪除
     *
     * @author xq
     * @date 2021/07/10
     **/
    int delete(String id);

    /**
     * 更新
     *
     * @author xq
     * @date 2021/07/10
     **/
    int update(BsDrillAffair bsDrillAffair);

    /**
     * 查询 根据主键 id 查询
     *
     * @author xq
     * @date 2021/07/10
     **/
    BsDrillAffair load(String id);

    /**
     * 查询 分页查询
     *
     * @author xq
     * @date 2021/07/10
     **/
    List<BsDrillAffair> pageList(int offset, int pagesize);

    /**
     * 查询 分页查询 count
     *
     * @author xq
     * @date 2021/07/10
     **/
    int pageListCount(int offset, int pagesize);

}
