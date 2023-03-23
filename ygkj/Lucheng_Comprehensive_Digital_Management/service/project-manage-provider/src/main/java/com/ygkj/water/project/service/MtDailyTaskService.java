package com.ygkj.water.project.service;

import com.ygkj.project.model.MtDailyTask;
import com.ygkj.project.vo.request.MtDmDailyTaskReqVo;
import com.ygkj.vo.PageVO;

/**
 * @author lxl
 * @create 2022-04-18 18:04
 * @description
 */
public interface MtDailyTaskService {

    int insert(MtDmDailyTaskReqVo vo);

    int update(MtDmDailyTaskReqVo vo);

    int deleteByTaskId(String id);

    PageVO<MtDailyTask> loadPage(MtDmDailyTaskReqVo vo);
}
