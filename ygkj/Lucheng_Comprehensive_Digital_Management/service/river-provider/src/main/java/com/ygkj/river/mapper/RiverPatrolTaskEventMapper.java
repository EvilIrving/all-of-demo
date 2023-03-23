package com.ygkj.river.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.ygkj.river.model.RiverPatrolTaskEvent;
import com.ygkj.river.vo.request.RptEventQueryReqVo;
import com.ygkj.river.vo.response.RptEventResVo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author xq
 * @description 任务巡河事件表
 * @date 2021-04-06
 */
@DS("business")
public interface RiverPatrolTaskEventMapper {

    /**
     * 新增
     *
     * @author xq
     * @date 2021/04/06
     **/
    int insert(RiverPatrolTaskEvent riverPatrolTaskEvent);

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
    int update(RiverPatrolTaskEvent riverPatrolTaskEvent);

    /**
     * 查询 根据主键 id 查询
     *
     * @author xq
     * @date 2021/04/06
     **/
    RiverPatrolTaskEvent load(String id);

    /**
     * 查询 分页查询
     *
     * @author xq
     * @date 2021/04/06
     **/
    List<RiverPatrolTaskEvent> pageList(int offset, int pagesize);

    /**
     * 查询 分页查询 count
     *
     * @author xq
     * @date 2021/04/06
     **/
    int pageListCount(int offset, int pagesize);

    List<RptEventResVo> selectByCondition(RptEventQueryReqVo reqVo);

    /**
     * 导出查询  ||因为反射无法获取父类属性 所以重新写一个查询mapper
     *
     * @param reqVo
     * @return
     */
    List<RiverPatrolTaskEvent> selectForExport(RptEventQueryReqVo reqVo);
}
