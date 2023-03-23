package com.ygkj.river.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.ygkj.river.model.RiverPatrolTask;
import com.ygkj.river.vo.request.RptTaskQueryReqVo;
import com.ygkj.river.vo.response.RptTaskResVo;

import java.util.List;

/**
 * @author xq
 * @Description
 * @Date 2021/4/6
 */
@DS("business")
public interface RiverPatrolTaskMapper {
    /**
     * 新增
     *
     * @author xq
     * @date 2021/04/06
     **/
    int insert(RiverPatrolTask riverPatrolTask);

    /**
     * 刪除
     *
     * @author xq
     * @date 2021/04/06
     **/
    int delete(String id);

    /**
     * 更新
     *
     * @author xq
     * @date 2021/04/06
     **/
    int update(RiverPatrolTask riverPatrolTask);

    /**
     * 查询 根据主键 id 查询
     *
     * @author xq
     * @date 2021/04/06
     **/
    RiverPatrolTask load(String id);

    RptTaskResVo loadTaskResVo(String id);

    /**
     * 查询 分页查询
     *
     * @author xq
     * @date 2021/04/06
     **/
    List<RiverPatrolTask> pageList(int offset, int pagesize);

    /**
     * 查询 分页查询 count
     *
     * @author xq
     * @date 2021/04/06
     **/
    int pageListCount(int offset, int pagesize);


    List<RptTaskResVo> selectByCondition(RptTaskQueryReqVo reqVo);

    List<RptTaskResVo> selectByTaskIds(String taskIds);
}
