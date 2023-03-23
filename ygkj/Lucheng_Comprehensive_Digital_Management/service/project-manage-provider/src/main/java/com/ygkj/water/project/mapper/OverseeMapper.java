package com.ygkj.water.project.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ygkj.project.model.ProjectOversee;
import com.ygkj.project.model.ProjectOverseeWork;
import com.ygkj.project.vo.request.ProjectOverseeReqVo;
import com.ygkj.project.vo.response.ProjectOverseeResVo;

import java.util.List;

public interface OverseeMapper extends BaseMapper<ProjectOversee> {

    /**
     * 查询督办
     *
     * @param reqVo
     * @return
     */
    List<ProjectOverseeResVo> page(ProjectOverseeReqVo reqVo);

    /**
     * 批量保存整改人员
     *
     * @param list
     */
    void batchInsertWorker(List<ProjectOverseeWork> list);

    /**
     * 根据督办id查询督办人员
     *
     * @param list
     * @return
     */
    List<ProjectOverseeWork> listWorkerByIds(List<String> list);

    /**
     * 批量保存短信消息
     *
     * @param stringList
     */
    void batchInsertMsg(List<String> stringList);

    /**
     * 整改人查询
     *
     * @param projectOverseeWork
     * @return
     */
    List<ProjectOverseeWork> selectWork(ProjectOverseeWork projectOverseeWork);
}
