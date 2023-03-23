package com.ygkj.water.project.controller;

import com.ygkj.project.ProjectDivisionControllerApi;
import com.ygkj.project.vo.request.ProjectDivisionInsVo;
import com.ygkj.project.vo.request.ProjectDivisionReqVo;
import com.ygkj.project.vo.response.ProjectDivisionRespVo;
import com.ygkj.utils.StringUtils;
import com.ygkj.vo.PageVO;
import com.ygkj.water.model.CommonResult;
import com.ygkj.water.project.service.ProjectDivisionService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @description 项目划分表
 * @author hucong
 * @date 2021-07-08
 */
@RestController
@RequestMapping(value = "/ProjectDivision")
public class ProjectDivisionController implements ProjectDivisionControllerApi {

    @Resource
    private ProjectDivisionService projectDivisionService;

    @PostMapping("saveOrUpdate")
    @Override
    public CommonResult saveOrUpdate(ProjectDivisionInsVo vo) {
        if (StringUtils.isEmpty(vo.getId())) {
            projectDivisionService.insert(vo);
            return CommonResult.success("保存成功!");
        } else {
            projectDivisionService.update(vo);
            return CommonResult.success("修改成功!");
        }
    }

    @DeleteMapping("del")
    @Override
    public CommonResult del(String id) {
        int flag = projectDivisionService.delete(id);
        if (flag >0 ) {
            return CommonResult.success("保存成功!");
        } else {
            return CommonResult.success("修改成功!");
        }
    }

    @PostMapping("loadPage")
    @Override
    public CommonResult<PageVO<ProjectDivisionRespVo>> loadPage(ProjectDivisionReqVo vo) {
        return CommonResult.success(projectDivisionService.loadPage(vo));
    }

    @PostMapping("loadList")
    @Override
    public CommonResult loadList(ProjectDivisionReqVo vo) {
        return CommonResult.success(projectDivisionService.loadList(vo));
    }
}
