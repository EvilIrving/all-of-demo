package com.ygkj.river.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.ygkj.river.model.RiverPatrolTaskEventProb;
import com.ygkj.river.vo.request.RptProbQueryReqVo;
import com.ygkj.river.vo.response.ComKeyValue;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * @author xq
 * @Description
 * @Date 2021/4/6
 */
@DS("business")
public interface RiverPatrolTaskEventProbMapper {
    /**
     * 新增
     *
     * @author xq
     * @date 2021/04/06
     **/
    int insert(RiverPatrolTaskEventProb riverPatrolTaskEventProb);

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
    int update(RiverPatrolTaskEventProb riverPatrolTaskEventProb);

    /**
     * 查询 根据主键 id 查询
     *
     * @author xq
     * @date 2021/04/06
     **/
    RiverPatrolTaskEventProb load(String id);

    /**
     * 查询 分页查询
     *
     * @author xq
     * @date 2021/04/06
     **/
    List<RiverPatrolTaskEventProb> pageList(int offset, int pagesize);

    /**
     * 查询 分页查询 count
     *
     * @author xq
     * @date 2021/04/06
     **/
    int pageListCount(int offset, int pagesize);

    @Select("select count(1) as value,event_id as `key` " +
            " from river_patrol_task_event_prob" +
            " where del_flag=0 and FIND_IN_SET(event_id,#{eventIds})")
    List<ComKeyValue<String, Integer>> countByEventIds(@Param("eventIds") String eventIds);

    @Select("select distinct camera_code" +
            " from river_patrol_task_event_prob" +
            " where del_flag=0 and FIND_IN_SET(event_id,#{eventIds})")
    List<String> cameraCodesOfProbByEventIds(@Param("eventIds") String eventIds);


    List<RiverPatrolTaskEventProb> selectByCondition(RptProbQueryReqVo reqVo);


}
