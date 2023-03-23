package com.ygkj.project.controller;

import com.ygkj.project.api.vo.req.BmReachReqVo;
import com.ygkj.project.api.vo.res.BmReachRespVo;
import com.ygkj.project.service.BmReachService;
import com.ygkj.water.model.CommonResult;
import com.ygkj.water.utils.vo.PageVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author hucong
 * @description 河道基本信息表
 * @date 2022-04-27
 */
@RestController
@RequestMapping(value = "/bmReach")
@Api(tags = "河道基本信息")
public class BmReachController {

    @Resource
    private BmReachService bmReachService;

    @ApiOperation(value = "河道数据新增或修改")
    @PutMapping("saveOrUpdate")
    public CommonResult saveOrUpdate(BmReachReqVo vo) {
        if (StringUtils.isEmpty(vo.getDtsCmfId())) {
            bmReachService.insert(vo);
        } else {
            bmReachService.update(vo);
        }
        return CommonResult.success("操作成功");
    }

    @ApiOperation(value = "河道数据删除")
    @DeleteMapping("del")
    public CommonResult del(String id) {
        int flag = bmReachService.delete(id);
        if (flag > 0) {
            return CommonResult.success("删除成功");
        }
        return CommonResult.failed("删除失败");
    }

    @ApiOperation(value = "河道分页查找")
    @PostMapping("loadPage")
    public CommonResult<PageVO<BmReachRespVo>> loadPage(BmReachReqVo vo) {
        return CommonResult.success(bmReachService.loadPage(vo));
    }


}