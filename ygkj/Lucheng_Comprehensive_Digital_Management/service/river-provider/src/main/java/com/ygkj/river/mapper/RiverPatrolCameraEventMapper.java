package com.ygkj.river.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.ygkj.river.model.RiverPatrolCameraEvent;
import com.ygkj.river.vo.request.RpcEventQueryReqVo;
import com.ygkj.river.vo.response.RpcEventResVo;

import java.util.List;

/**
 * @author xq
 * @Description
 * @Date 2021/4/7
 */
@DS("business")
public interface RiverPatrolCameraEventMapper {
    /**
     * 新增
     *
     * @author xq
     * @date 2021/04/07
     **/
    int insert(RiverPatrolCameraEvent riverPatrolCameraEvent);

    /**
     * 刪除
     *
     * @author xq
     * @date 2021/04/07
     **/
    int delete(String id);

    /**
     * 更新
     *
     * @author xq
     * @date 2021/04/07
     **/
    int update(RiverPatrolCameraEvent riverPatrolCameraEvent);

    /**
     * 查询 根据主键 id 查询
     *
     * @author xq
     * @date 2021/04/07
     **/
    RiverPatrolCameraEvent load(String id);

    /**
     * 查询 分页查询
     *
     * @author xq
     * @date 2021/04/07
     **/
    List<RiverPatrolCameraEvent> pageList(int offset, int pagesize);

    /**
     * 查询 分页查询 count
     *
     * @author xq
     * @date 2021/04/07
     **/
    int pageListCount(int offset, int pagesize);

    List<RpcEventResVo> selectByCondition(RpcEventQueryReqVo reqVo);

    /**
     * 查询 视频巡河导出列表 ||因为无法反射获取父类属性
     *
     * @param reqVo
     * @return
     */
    List<RiverPatrolCameraEvent> selectForExport(RpcEventQueryReqVo reqVo);
}
