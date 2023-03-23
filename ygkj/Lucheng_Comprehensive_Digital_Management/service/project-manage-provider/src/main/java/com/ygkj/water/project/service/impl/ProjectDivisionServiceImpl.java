package com.ygkj.water.project.service.impl;

import com.github.pagehelper.PageHelper;
import com.ygkj.jwt.JwtTokenUtil;
import com.ygkj.project.model.ProjectDivision;
import com.ygkj.project.vo.request.ProjectDivisionInsVo;
import com.ygkj.project.vo.request.ProjectDivisionReqVo;
import com.ygkj.project.vo.response.ProjectDivisionRespVo;
import com.ygkj.service.GenerateDistributionIDService;
import com.ygkj.utils.PageUtils;
import com.ygkj.vo.PageVO;
import com.ygkj.water.project.mapper.ProjectDivisionMapper;
import com.ygkj.water.project.mapper.UserMapper;
import com.ygkj.water.project.service.ProjectDivisionService;
import com.ygkj.water.project.utils.CommonUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * @author hucong
 * @description 项目划分表
 * @date 2021-07-08
 */
@Service
public class ProjectDivisionServiceImpl implements ProjectDivisionService {

    @Resource
    private ProjectDivisionMapper projectDivisionMapper;
    @Resource
    private GenerateDistributionIDService idService;
    @Resource
    private UserMapper userMapper;

    @Override
    public int insert(ProjectDivisionInsVo vo) {
        ProjectDivision projectDivision = new ProjectDivision();
        BeanUtils.copyProperties(vo, projectDivision);
        projectDivision.setUploadId(JwtTokenUtil.getCurrentUserId().orElse(""));
        projectDivision.setUploadName(JwtTokenUtil.getCurrentUserName().orElse(""));
        projectDivision.setId(idService.nextID());
        projectDivision.setCreateTime(new Date());
        return projectDivisionMapper.insert(projectDivision);
    }

    @Override
    public int delete(String id) {
        return projectDivisionMapper.delete(id);
    }

    @Override
    public int update(ProjectDivisionInsVo vo) {
        ProjectDivision projectDivision = new ProjectDivision();
        BeanUtils.copyProperties(vo, projectDivision);
        return projectDivisionMapper.update(projectDivision);
    }

    @Override
    public PageVO<ProjectDivisionRespVo> loadPage(ProjectDivisionReqVo vo) {
        if (!CommonUtil.isConstructionManagementPerson(userMapper)) {
            vo.setUploadId(JwtTokenUtil.getCurrentUserId().orElse(""));
        }
        if (vo.getPageNum() != null && vo.getPageNum() > 0
                && vo.getPageSize() != null && vo.getPageSize() > 0)
            PageHelper.startPage(vo.getPageNum(), vo.getPageSize());
        List<ProjectDivisionRespVo> list = projectDivisionMapper.loadPage(vo);
        if (!CollectionUtils.isEmpty(list)) {
            for (ProjectDivisionRespVo projectDivisionRespVo : list) {
                ProjectDivisionReqVo vo2 = new ProjectDivisionReqVo();
                vo2.setPid(projectDivisionRespVo.getId());
                projectDivisionRespVo.setChildren(projectDivisionMapper.loadPage(vo2));
            }
        }
        return PageUtils.getPageVO(list);
    }

    @Override
    public List<ProjectDivisionRespVo> loadList(ProjectDivisionReqVo vo) {
        return projectDivisionMapper.loadPage(vo);
    }
}
