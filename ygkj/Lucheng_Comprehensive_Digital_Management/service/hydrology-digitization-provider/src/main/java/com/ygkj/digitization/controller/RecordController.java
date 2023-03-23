package com.ygkj.digitization.controller;

import com.ygkj.digitization.RecordControllerApi;
import com.ygkj.digitization.service.RecordService;
import com.ygkj.digitization.vo.request.*;
import com.ygkj.jwt.JwtTokenUtil;
import com.ygkj.utils.SnowFlake;
import com.ygkj.water.model.CommonResult;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("record")
public class RecordController implements RecordControllerApi {

    @Resource
    private SnowFlake snowFlake;

    @Resource
    private RecordService recordService;

    @Override
    @GetMapping("listCatalog")
    public CommonResult listCatalog() {
        return CommonResult.success(recordService.listCatalog());
    }

    @PostMapping("")
    @Override
    public CommonResult catalogs(CatalogPageQueryVo queryVo) {
        return CommonResult.success(recordService.catalogs(queryVo));
    }

    @Override
    @PostMapping("editCatalog")
    public CommonResult editCatalog(CatalogReqVo catalogReqVo) {
        recordService.editCatalog(catalogReqVo);
        return CommonResult.success("");
    }

    @Override
    @DeleteMapping("delCatalog/{id}")
    public CommonResult delCatalog(@PathVariable("id") String id) {
        recordService.delCatalog(id);
        return CommonResult.success("");
    }

    @Override
    @PostMapping("editRecord")
    public CommonResult editRecord(RecordReqVo reqVo, HttpServletRequest request) {
        if (StringUtils.isEmpty(reqVo.getId())) {
            String token = request.getHeader("authorization");
            reqVo.setDeptId(JwtTokenUtil.getDeptId(token));
            reqVo.setUploaderId(JwtTokenUtil.getUserId(token));
            reqVo.setUploaderName(JwtTokenUtil.getUserName(token));
        }
        recordService.editRecord(reqVo);
        return CommonResult.success("");
    }

    @Override
    @DeleteMapping("delRecord/{id}")
    public CommonResult delRecord(@PathVariable("id") String id) {
        recordService.delRecord(id);
        return CommonResult.success("");
    }

    @Override
    @PostMapping("listRecord")
    public CommonResult listRecord(RecordListReqVo reqVo) {
        return CommonResult.success(recordService.listRecord(reqVo));
    }

    @Override
    @GetMapping("selectRecord/{id}")
    public CommonResult selectRecord(@PathVariable("id") String id) {
        return CommonResult.success(recordService.selectRecord(id));
    }

    @Override
    @PostMapping("/collect")
    public CommonResult collect(String id) {
        if (recordService.collect(id)){
            return CommonResult.success();
        }
        return CommonResult.failed();
    }

    @Override
    @PostMapping("/unCollect")
    public CommonResult unCollect(String objectId) {
        if (recordService.unCollect(objectId)){
            return CommonResult.success();
        }
        return CommonResult.failed();
    }

    @PostMapping("/mine")
    @Override
    public CommonResult myCollection(CollectionPageQueryVo queryVo) {
        return CommonResult.success(recordService.myCollection(queryVo));
    }
}
