package com.ygkj.project.controller;


import com.ygkj.project.api.vo.req.ProjectRelReqVo;
import com.ygkj.project.api.vo.res.ProjectRelResVo;
import com.ygkj.project.api.vo.res.RelCountResVo;
import com.ygkj.project.service.ProjectRelService;
import com.ygkj.water.model.CommonResult;
import com.ygkj.water.utils.vo.PageVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("projectRel")
@Api(tags = "工程关联")
public class ProjectRelController {

    @Autowired
    private ProjectRelService projectRelService;

    @ApiOperation(value = "工程关联的工程或站点",response = ProjectRelResVo.class)
    @PostMapping("listRange")
    public CommonResult<PageVO<ProjectRelResVo>> listRel(ProjectRelReqVo relReqVo){
        return CommonResult.success(projectRelService.listRel(relReqVo));
    }

    @ApiOperation(value = "各类型关联工程的数量",response = RelCountResVo.class)
    @PostMapping("countRel")
    public CommonResult<List<RelCountResVo>> countRel(ProjectRelReqVo relReqVo){
        return CommonResult.success(projectRelService.countRel(relReqVo));
    }
}
