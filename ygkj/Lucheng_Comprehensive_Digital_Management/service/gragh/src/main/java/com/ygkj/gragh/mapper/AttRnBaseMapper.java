package com.ygkj.gragh.mapper;

import com.ygkj.gragh.model.AttRnBase;
import com.ygkj.gragh.vo.request.RivernetWaterLevelReqVo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author xq
 * @description 河网表
 * @date 2021-05-27
 */
@Mapper
@Repository
public interface AttRnBaseMapper {

    /**
     * 新增
     *
     * @author xq
     * @date 2021/05/27
     **/
    int insert(AttRnBase attRnBase);

    /**
     * 刪除
     *
     * @author xq
     * @date 2021/05/27
     **/
    int delete(String rnName);

    /**
     * 更新
     *
     * @author xq
     * @date 2021/05/27
     **/
    int update(AttRnBase attRnBase);

    /**
     * 查询 根据主键 id 查询
     *
     * @author xq
     * @date 2021/05/27
     **/
    AttRnBase load(String rnName);

    /**
     * 查询 分页查询
     *
     * @author xq
     * @date 2021/05/27
     **/
    List<AttRnBase> pageList(int offset, int pagesize);

    /**
     * 查询 分页查询 count
     *
     * @author xq
     * @date 2021/05/27
     **/
    int pageListCount(int offset, int pagesize);

    /**
     * 根据条件查询河网
     *
     * @param reqVo
     * @return
     */
    List<AttRnBase> selectByCondition(RivernetWaterLevelReqVo reqVo);

}
