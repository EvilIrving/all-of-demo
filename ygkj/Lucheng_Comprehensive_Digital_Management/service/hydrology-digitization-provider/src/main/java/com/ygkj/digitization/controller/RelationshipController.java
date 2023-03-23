package com.ygkj.digitization.controller;

import com.ygkj.digitization.RelationshipControllerApi;
import com.ygkj.digitization.service.RelationshipService;
import com.ygkj.digitization.vo.request.*;
import com.ygkj.digitization.vo.response.RelBaseResVo;
import com.ygkj.digitization.vo.response.RelSeawallListResVo;
import com.ygkj.jwt.JwtTokenUtil;
import com.ygkj.utils.ExportUtils;
import com.ygkj.vo.PageVO;
import com.ygkj.water.model.CommonResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("relationship")
public class RelationshipController implements RelationshipControllerApi {

    @Resource
    private RelationshipService relationshipService;

    @Override
    @PostMapping("edit")
    public CommonResult edit(RelBaseReqVo reqVo, HttpServletRequest request) {
        reqVo.setDeptId(JwtTokenUtil.getDeptId(request.getHeader("authorization")));
        relationshipService.edit(reqVo);
        return CommonResult.success("");
    }

    @Override
    @PostMapping("del")
    public CommonResult del(String ids) {
        relationshipService.del(Arrays.asList(ids.split(",")));
        return CommonResult.success("");
    }

    @Override
    @PostMapping("list")
    public CommonResult list(RelListReqVo reqVo) {
        return CommonResult.success(relationshipService.list(reqVo));
    }

    @Override
    @PostMapping("exportList")
    public void exportList(RelListReqVo reqVo, HttpServletResponse response) {
        reqVo.setFetch(true);
        PageVO<RelBaseResVo> page = relationshipService.list(reqVo);
        List<RelBaseResVo> list = page.getList();
        String[] headers = new String[]{"关联名称", "关联水库", "备注"};
        String[] cols = new String[]{"relName", "psName", "remark"};
        String prefix = reqVo.getType() == 1 ? "旱情关联" : "水位关联";
        ExportUtils.exportExcel(list, prefix + getTime(), headers, cols, response);
    }

    private static DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

    private String getTime() {
        return dtf.format(LocalDateTime.now());
    }

    @Override
    @GetMapping("select")
    public CommonResult select(RelDetailReqVo reqVo) {
        return CommonResult.success(relationshipService.select(reqVo));
    }

    @Override
    @PostMapping("editTide")
    public CommonResult editTide(RelSeawallReqVo reqVo) {
        relationshipService.editTide(reqVo);
        return CommonResult.success("");
    }

    @Override
    @PostMapping("delTide")
    public CommonResult delTide(String stCodes) {
        relationshipService.delTide(stCodes);
        return CommonResult.success("");
    }

    @Override
    @PostMapping("listTide")
    public CommonResult listTide(RelListReqVo reqVo) {
        return CommonResult.success(relationshipService.listTide(reqVo));
    }

    @Override
    @PostMapping("exportListTide")
    public void exportListTide(RelListReqVo reqVo, HttpServletResponse response) {
        reqVo.setFetch(true);
        PageVO<RelSeawallListResVo> page = relationshipService.listTide(reqVo);
        List<RelSeawallListResVo> list = page.getList();
        String[] headers = new String[]{"潮位站名称", "关联海塘", "备注"};
        String[] cols = new String[]{"stName", "seawallNames", "seawallRemark"};
        ExportUtils.exportExcel(list, "潮位关联" + getTime(), headers, cols, response);
    }

    @Override
    @PostMapping("selectTide")
    public CommonResult selectTide(String stCode) {
        return CommonResult.success(relationshipService.selectTide(stCode));
    }

    @Override
    @PostMapping("listProject")
    public CommonResult listProject(ProjectListReqVo reqVo) {
        return CommonResult.success(relationshipService.listProject(reqVo));
    }

    @Override
    @PostMapping("listSt")
    public CommonResult listSt(StListReqVo reqVo) {
        return CommonResult.success(relationshipService.listSt(reqVo));
    }
}
