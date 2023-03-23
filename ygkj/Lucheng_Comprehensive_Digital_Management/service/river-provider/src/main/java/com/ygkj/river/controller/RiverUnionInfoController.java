package com.ygkj.river.controller;

import com.ygkj.river.RiverUnionInfoControllerApi;
import com.ygkj.river.service.RiverUnionInfoService;
import com.ygkj.river.vo.request.RiverUnionInfoAddVo;
import com.ygkj.river.vo.request.RiverUnionInfoPageQueryVo;
import com.ygkj.river.vo.response.RiverUnionInfoResVo;
import com.ygkj.utils.ExportUtils;
import com.ygkj.vo.PageVO;
import com.ygkj.water.model.CommonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("/riverUnionInfo")
public class RiverUnionInfoController implements RiverUnionInfoControllerApi {

    private RiverUnionInfoService riverUnionInfoService;

    public RiverUnionInfoController(@Autowired RiverUnionInfoService riverUnionInfoService) {
        this.riverUnionInfoService = riverUnionInfoService;
    }

    @PostMapping("")
    @Override
    public CommonResult riverUnionInfos(RiverUnionInfoPageQueryVo queryVo) {
        return CommonResult.success(riverUnionInfoService.riverUnionInfos(queryVo));
    }

    @PutMapping("/add")
    @Override
    public CommonResult add(RiverUnionInfoAddVo addVo) {
        if (riverUnionInfoService.insert(addVo)) {
            return CommonResult.success("操作成功");
        }
        return CommonResult.failed("操作失败");
    }

    @PutMapping("/update")
    @Override
    public CommonResult update(RiverUnionInfoAddVo addVo) {
        if (riverUnionInfoService.update(addVo)) {
            return CommonResult.success("操作成功");
        }
        return CommonResult.failed("操作失败");
    }

    @DeleteMapping("/delete/{id}")
    @Override
    public CommonResult delete(@PathVariable String id) {
        if (riverUnionInfoService.delete(id)) {
            return CommonResult.success("操作成功");
        }
        return CommonResult.failed("操作失败");
    }

    /**
     * 关联信息导出
     *
     * @param vo
     * @param response
     */
    @Override
    @PostMapping("/exportRiverUnionInfo")
    public void exportRiverUnionInfo(RiverUnionInfoPageQueryVo vo, HttpServletResponse response) {
        PageVO pageVO = riverUnionInfoService.riverUnionInfos(vo);
        List<RiverUnionInfoResVo> list = pageVO.getList();
        ExportUtils.exportExcel(list,
                "关联信息列表" + DateTimeFormatter.ofPattern("yyyyMMddHHmmss").format(LocalDateTime.now()),
                new String[]{"名称", "行政区划", "所属流域", "经度", "纬度", "附件", "描述"},
                new String[]{"name", "areaName", "basin", "longitude", "latitude", "fileIds", "description"},
                response, RiverUnionInfoResVo.class);
    }
}
