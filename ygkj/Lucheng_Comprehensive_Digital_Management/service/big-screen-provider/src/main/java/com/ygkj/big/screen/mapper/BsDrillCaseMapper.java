package com.ygkj.big.screen.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.ygkj.big.screen.model.BsDrillCase;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author xq
 * @description 演练方案表
 * @date 2021-07-08
 */
@DS("meta")
public interface BsDrillCaseMapper {

    /**
     * 新增
     *
     * @author xq
     * @date 2021/07/08
     **/
    int insert(BsDrillCase bsDrillCase);

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
    int update(BsDrillCase bsDrillCase);

    /**
     * 查询 根据主键 id 查询
     *
     * @author xq
     * @date 2021/07/08
     **/
    BsDrillCase load(String id);

    /**
     * 查询 分页查询
     *
     * @author xq
     * @date 2021/07/08
     **/
    List<BsDrillCase> pageList(int offset, int pagesize);

    /**
     * 查询 分页查询 count
     *
     * @author xq
     * @date 2021/07/08
     **/
    int pageListCount(int offset, int pagesize);

    BsDrillCase loadByAffairId(String affairId);

    String loadIdByAffairId(String affairId);

    List<BsDrillCase> selectByCondition(BsDrillCase bsDrillCase);

}
