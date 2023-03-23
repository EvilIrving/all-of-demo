package com.ygkj.water.project.controller;

import com.ygkj.project.AttServiceItemControllerApi;
import com.ygkj.project.vo.request.AttServiceItemReqVo;
import com.ygkj.utils.StringUtils;
import com.ygkj.water.model.CommonResult;
import com.ygkj.water.project.service.AttServiceItemService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author lxl
 * @create 2022-04-12 11:26
 * @description 服务项目
 */
@RestController
@RequestMapping(value = "/serviceItem")
public class AttServiceItemController implements AttServiceItemControllerApi {

    @Resource
    private AttServiceItemService attServiceItemService;


    @Override
    @PostMapping("/loadSkPage")
    public CommonResult loadSkPage(AttServiceItemReqVo vo) {
        return CommonResult.success(attServiceItemService.loadSkPage(vo));
    }

    @Override
    @PostMapping("/saveOrUpdate")
    public CommonResult saveOrUpdate(AttServiceItemReqVo vo) {
        if (StringUtils.isEmpty(vo.getId())) {
            attServiceItemService.insert(vo);
            return CommonResult.success("保存成功!");
        } else {
            attServiceItemService.update(vo);
            return CommonResult.success("修改成功!");
        }
    }

    @Override
    @GetMapping("/del")
    public CommonResult del(String id) {
        int flag = attServiceItemService.delete(id);
        if (flag >0 ) {
            return CommonResult.success("保存成功!");
        } else {
            return CommonResult.success("修改成功!");
        }
    }

    @Override
    @PostMapping("/loadPage")
    public CommonResult loadPage(AttServiceItemReqVo vo) {
        return CommonResult.success(attServiceItemService.loadPage(vo));
    }
}
