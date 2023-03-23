package com.ygkj.water.project.mapper;

import com.ygkj.project.model.ProjectDivision;
import com.ygkj.project.vo.request.ProjectDivisionInsVo;
import com.ygkj.project.vo.request.ProjectDivisionReqVo;
import com.ygkj.project.vo.response.ProjectDivisionRespVo;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author hucong
 * @description 项目划分表
 * @date 2021-07-08
 */
public interface ProjectDivisionMapper {

    /**
     * 新增
     *
     * @author hucong
     * @date 2021/07/08
     **/
    int insert(ProjectDivision entity);

    /**
     * 刪除
     *
     * @author hucong
     * @date 2021/07/08
     **/
    int delete(String id);

    /**
     * 更新
     *
     * @author hucong
     * @date 2021/07/08
     **/
    int update(ProjectDivision entity);

    List<ProjectDivisionRespVo> loadPage(ProjectDivisionReqVo vo);

}
