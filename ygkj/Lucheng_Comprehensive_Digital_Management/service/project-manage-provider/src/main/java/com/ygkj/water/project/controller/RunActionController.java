package com.ygkj.water.project.controller;

import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.ygkj.project.RunActionControllerApi;
import com.ygkj.project.vo.request.BhLgLmrDisDomGsReqVo;
import com.ygkj.project.vo.request.BhLgLmrDisDomReqVo;
import com.ygkj.project.vo.request.BhLgLmrDisDomSsReqVo;
import com.ygkj.project.vo.request.OverhaulReqVo;
import com.ygkj.utils.StringUtils;
import com.ygkj.water.model.CommonResult;
import com.ygkj.water.project.service.BhLgLmrDisDomGsService;
import com.ygkj.water.project.service.BhLgLmrDisDomService;
import com.ygkj.water.project.service.BhLgLmrDisDomSsService;
import com.ygkj.water.project.service.OverhaulService;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

/**
 * @author lxl
 * @create 2022-04-18 14:12
 * @description
 */
@Api(tags = "物业管理事项-运行操作")
@RestController
@RequestMapping("/runAction")
public class RunActionController implements RunActionControllerApi {

    @Resource
    private OverhaulService overhaulService;
    @Resource
    private BhLgLmrDisDomService disDomService;
    @Resource
    private BhLgLmrDisDomGsService disDomGsService;
    @Resource
    private BhLgLmrDisDomSsService disDomSsService;
    @Override
    @PostMapping("/saveOrUpdateOverhaul")
    public CommonResult saveOrUpdateOverhaul(OverhaulReqVo vo) {
        if (ObjectUtils.isEmpty(vo.getType())){
            return CommonResult.failed("参数缺失");
        }
        if (vo.isRepeatMan()){
            return CommonResult.failed("人员选择不能相同");
        }
        if (StringUtils.isEmpty(vo.getId())) {
            overhaulService.insert(vo);
            return CommonResult.success("保存成功!");
        } else {
            overhaulService.update(vo);
            return CommonResult.success("修改成功!");
        }
    }

    @Override
    @PostMapping("/delOverhaul")
    public CommonResult delOverhaul(OverhaulReqVo vo) {
        if (ObjectUtils.isEmpty(vo.getType()) || StringUtils.isBlank(vo.getId())){
            return CommonResult.failed("参数缺失");
        }
        int flag = overhaulService.delete(vo);
        if (flag >0 ) {
            return CommonResult.success("保存成功!");
        } else {
            return CommonResult.success("修改成功!");
        }
    }

    @Override
    @PostMapping("/loadPageOverhaul")
    public CommonResult loadPageOverhaul(OverhaulReqVo vo) {
        if (ObjectUtils.isEmpty(vo.getType())){
            return CommonResult.failed("参数缺失");
        }
        return CommonResult.success(overhaulService.loadPage(vo));
    }


    @Override
    @GetMapping("/exportOverhaul")
    public void exportOverhaul(OverhaulReqVo vo, HttpServletResponse response) {
        overhaulService.exportOverhaul(vo,response);
    }


    @Override
    @PostMapping("/saveOrUpdateDisDom")
    public CommonResult saveOrUpdateDisDom(BhLgLmrDisDomReqVo vo) {
        try{
            if (StringUtils.isEmpty(vo.getId())) {
                disDomService.insert(vo);
                return CommonResult.success("保存成功!");
            } else {
                disDomService.update(vo);
                return CommonResult.success("修改成功!");
            }
        }catch (Exception e){
            return CommonResult.failed(e.getMessage());
        }
    }


    @Override
    @PostMapping("/loadPageDisDom")
    public CommonResult loadPageDisDom(BhLgLmrDisDomReqVo vo) {
        try{
            return CommonResult.success(disDomService.loadPage(vo));
        }catch (Exception e){
            e.printStackTrace();
        }
        return CommonResult.failed();
    }


    @Override
    @PostMapping("/deleteDisDom")
    public CommonResult deleteDisDom(String id) {
        try{
            return disDomService.delete(id) > 0? CommonResult.success() : CommonResult.failed();
        }catch (RuntimeException r){
            return CommonResult.failed(r.getMessage());
        } catch (Exception e){
            e.printStackTrace();
        }
        return CommonResult.failed();
    }

    @Override
    @GetMapping("/exportDisDom")
    public void exportDisDom(BhLgLmrDisDomReqVo vo, HttpServletResponse response) {
        disDomService.exportDisDom(vo,response);
    }

    @Override
    @PostMapping("/saveOrUpdateGate")
    public CommonResult saveOrUpdateGate(BhLgLmrDisDomGsReqVo vo) {
        try{
            disDomGsService.saveOrUpdateGate(vo);
        }catch (Exception e){
            return CommonResult.failed(e.getMessage());
        }
        return CommonResult.success();
    }

    @Override
    @PostMapping("/loadGate")
    public CommonResult loadGate(String id,String soId,String actlOpenTm) {
        try{
            return CommonResult.success(disDomGsService.load(id,soId,actlOpenTm));
        }catch (Exception e){
            e.printStackTrace();
        }
        return CommonResult.failed();
    }

    @Override
    @PostMapping("/deleteGate")
    public CommonResult deleteGate(String id) {
        try{
            return disDomGsService.delete(id) > 0? CommonResult.success() : CommonResult.failed();
        }catch (RuntimeException r){
            return CommonResult.failed(r.getMessage());
        } catch (Exception e){
            e.printStackTrace();
        }
        return CommonResult.failed();
    }

    @Override
    @PostMapping("/saveOrUpdateSluiceway")
    public CommonResult saveOrUpdateSluiceway(BhLgLmrDisDomSsReqVo vo) {
        try{
            disDomSsService.saveOrUpdateGate(vo);
        }catch (Exception e){
            return CommonResult.failed(e.getMessage());
        }
        return CommonResult.success();
    }

    @Override
    @PostMapping("/loadSluiceway")
    public CommonResult loadSluiceway(String id,String soId,String actlOpenTm) {
        try{
            return CommonResult.success(disDomSsService.load(id,soId,actlOpenTm));
        }catch (Exception e){
            e.printStackTrace();
        }
        return CommonResult.failed();
    }

    @Override
    @PostMapping("/deleteSluiceway")
    public CommonResult deleteSluiceway(String id) {
        try{
            return disDomSsService.delete(id) > 0? CommonResult.success() : CommonResult.failed();
        }catch (RuntimeException r){
           return CommonResult.failed(r.getMessage());
        } catch (Exception e){
            e.printStackTrace();
        }
        return CommonResult.failed();
    }
}
