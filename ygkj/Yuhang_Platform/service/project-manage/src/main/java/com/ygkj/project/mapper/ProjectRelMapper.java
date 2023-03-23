package com.ygkj.project.mapper;

import com.ygkj.project.api.vo.req.ProjectRelReqVo;
import com.ygkj.project.api.vo.res.ProjectRelResVo;
import com.ygkj.project.api.vo.res.RelCountResVo;

import java.util.List;


public interface ProjectRelMapper {

    /**
     * 相关工程、站点
     *
     * @param relReqVo
     * @return
     */
    List<ProjectRelResVo> listRel(ProjectRelReqVo relReqVo);

    /**
     * 各个类型的工程数量
     *
     * @param relReqVo
     * @return
     */
    List<RelCountResVo> countRel(ProjectRelReqVo relReqVo);
}
