package com.ygkj.water.project.service.impl;

import com.ygkj.project.vo.request.ProjectManageQueryVo;
import com.ygkj.project.vo.response.BmAccBuildResVo;
import com.ygkj.utils.Builder;
import com.ygkj.water.project.mapper.ProjectMapper;
import com.ygkj.project.model.BmAccBuild;
import com.ygkj.water.project.service.BmAccBuildService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author pqf
 * @DESCRIPTION:
 * @create 2021-11-11 13:55
 */
@Service
public class BmAccBuildServiceImpl implements BmAccBuildService {

    @Resource
    private ProjectMapper projectMapper;
    @Override
    public List<BmAccBuildResVo> selectBmAccBuildByCondition(String type,String prcd) {
        return projectMapper.selectBmAccBuildByCondition(Builder
                .of(ProjectManageQueryVo::new)
                .with(ProjectManageQueryVo::setType, type)
                .with(ProjectManageQueryVo::setCode, prcd)
                .build());
    }
}
