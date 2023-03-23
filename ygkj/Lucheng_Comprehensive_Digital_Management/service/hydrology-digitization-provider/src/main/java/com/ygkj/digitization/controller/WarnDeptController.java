package com.ygkj.digitization.controller;

import com.ygkj.digitization.WarnDeptControllerApi;
import com.ygkj.digitization.service.WarnDeptService;
import com.ygkj.digitization.vo.request.WarnDeptReqVo;
import com.ygkj.digitization.vo.response.WarnDeptRespVo;
import com.ygkj.utils.StringUtils;
import com.ygkj.vo.PageVO;
import com.ygkj.water.model.CommonResult;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @description 预警部门表
 * @author hucong
 * @date 2021-07-26
 */
@RestController
@RequestMapping(value = "/WarnDept")
public class WarnDeptController implements WarnDeptControllerApi {

    @Resource
    private WarnDeptService warnDeptService;

    @PutMapping("saveOrUpdate")
    @Override
    public CommonResult saveOrUpdate(WarnDeptReqVo vo) {
        if (StringUtils.isEmpty(vo.getId())) {
            warnDeptService.insert(vo);
        } else {
            warnDeptService.update(vo);
        }
        return CommonResult.success("操作成功!");
    }

    @DeleteMapping("del")
    @Override
    public CommonResult del(String id) {
        int flag = warnDeptService.delete(id);
        if (flag > 0) {
            return CommonResult.success("删除成功!");
        }
        return CommonResult.success("删除失败!");
    }

    @PostMapping("loadPage")
    @Override
    public CommonResult<PageVO<WarnDeptRespVo>> loadPage(WarnDeptReqVo vo) {
        return CommonResult.success(warnDeptService.loadPage(vo));
    }

    @PostMapping("findTree")
    @Override
    public CommonResult<List<WarnDeptRespVo>> findTree(WarnDeptReqVo vo) {
        return CommonResult.success(warnDeptService.findTree(vo));
    }
}
