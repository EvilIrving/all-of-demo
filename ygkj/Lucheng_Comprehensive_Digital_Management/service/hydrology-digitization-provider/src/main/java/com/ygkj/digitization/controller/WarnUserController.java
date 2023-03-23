package com.ygkj.digitization.controller;

import com.ygkj.digitization.WarnUserControllerApi;
import com.ygkj.digitization.service.WarnUserService;
import com.ygkj.digitization.vo.request.WarnUserReqVo;
import com.ygkj.digitization.vo.response.WarnUserRespVo;
import com.ygkj.utils.StringUtils;
import com.ygkj.vo.PageVO;
import com.ygkj.water.model.CommonResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

/**
 * @description 预警人员表
 * @author hucong
 * @date 2021-07-26
 */
@RestController
@RequestMapping(value = "/WarnUser")
public class WarnUserController implements WarnUserControllerApi {

    @Resource
    private WarnUserService warnUserService;

    @PutMapping("saveOrUpdate")
    @Override
    public CommonResult saveOrUpdate(WarnUserReqVo vo) {
        if (StringUtils.isEmpty(vo.getId())) {
            warnUserService.insert(vo);
        } else {
            warnUserService.update(vo);
        }
        return CommonResult.success("操作成功!");
    }

    @DeleteMapping("del")
    @Override
    public CommonResult del(String id) {
        int flag = warnUserService.delete(id);
        if (flag > 0) {
            return CommonResult.success("删除成功!");
        }
        return CommonResult.success("删除失败!");
    }

    @PostMapping("loadPage")
    @Override
    public CommonResult<PageVO<WarnUserRespVo>> loadPage(WarnUserReqVo vo) {
        return CommonResult.success(warnUserService.loadPage(vo));
    }

    @PostMapping("importUserExcel")
    @Override
    public CommonResult importUserExcel(MultipartFile file, String deptId) {
        try {
            warnUserService.importUserExcel(file, deptId);
            return CommonResult.success("导入成功!");
        } catch (Exception e) {
            return CommonResult.failed("导入失败!");
        }
    }

}
