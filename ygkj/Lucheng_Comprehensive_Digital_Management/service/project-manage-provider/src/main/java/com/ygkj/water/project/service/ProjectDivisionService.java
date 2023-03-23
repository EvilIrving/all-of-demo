package com.ygkj.water.project.service;

import com.ygkj.project.model.ProjectDivision;
import com.ygkj.project.vo.request.ProjectDivisionInsVo;
import com.ygkj.project.vo.request.ProjectDivisionReqVo;
import com.ygkj.project.vo.response.ProjectDivisionRespVo;
import com.ygkj.vo.PageVO;

import java.util.List;

/**
 * @author hucong
 * @description 项目划分表
 * @date 2021-07-08
 */
public interface ProjectDivisionService {

    /**
     * 新增
     */
    int insert(ProjectDivisionInsVo vo);

    /**
     * 删除
     */
    int delete(String id);

    /**
     * 更新
     */
    int update(ProjectDivisionInsVo vo);

    /**
     * 分页查询
     *
     * @param vo
     * @return
     */
    PageVO<ProjectDivisionRespVo> loadPage(ProjectDivisionReqVo vo);

    List<ProjectDivisionRespVo> loadList(ProjectDivisionReqVo vo);
}
