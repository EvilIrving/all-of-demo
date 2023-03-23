package com.ygkj.big.screen.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.ygkj.big.screen.model.BsInfluentRel;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author xq
 * @description 组件数据影响关联表
 * @date 2021-07-08
 */
@DS("meta")
public interface BsInfluentRelMapper {

    /**
     * 新增
     *
     * @author xq
     * @date 2021/07/08
     **/
    int insert(BsInfluentRel bsInfluentRel);

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
    int update(BsInfluentRel bsInfluentRel);

    /**
     * 查询 根据主键 id 查询
     *
     * @author xq
     * @date 2021/07/08
     **/
    BsInfluentRel load(int id);

    /**
     * 查询 分页查询
     *
     * @author xq
     * @date 2021/07/08
     **/
    List<BsInfluentRel> pageList(int offset, int pagesize);

    /**
     * 查询 分页查询 count
     *
     * @author xq
     * @date 2021/07/08
     **/
    int pageListCount(int offset, int pagesize);

}
