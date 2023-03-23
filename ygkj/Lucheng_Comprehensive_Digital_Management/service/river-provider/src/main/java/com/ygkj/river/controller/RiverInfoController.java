package com.ygkj.river.controller;

import com.ygkj.river.RiverInfoControllerApi;
import com.ygkj.river.service.RiverService;
import com.ygkj.river.vo.request.RiverInfoAddVo;
import com.ygkj.river.vo.request.RiverInfoPageQueryVo;
import com.ygkj.river.vo.response.RiverInfoResVo;
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
@RequestMapping("/riverInfo")
public class RiverInfoController implements RiverInfoControllerApi {

    private RiverService riverService;

    public RiverInfoController(@Autowired RiverService riverService) {
        this.riverService = riverService;
    }

    @PostMapping("/rivers")
    @Override
    public CommonResult rivers(RiverInfoPageQueryVo queryVo) {
        return CommonResult.success(riverService.riverPageQuery(queryVo));
    }

    @PutMapping("/insert")
    @Override
    public CommonResult insertRiver(RiverInfoAddVo addVo) {
        if (riverService.insert(addVo)) {
            return CommonResult.success("操作成功");
        }
        return CommonResult.failed("操作失败");
    }

    @PutMapping("/update")
    @Override
    public CommonResult updateRiver(RiverInfoAddVo addVo) {
        if (riverService.update(addVo)) {
            return CommonResult.success("操作成功");
        }
        return CommonResult.failed("操作失败");
    }

    @DeleteMapping("/delete/{ids}")
    @Override
    public CommonResult deleteRiver(@PathVariable String ids) {
        if (riverService.delete(ids)) {
            return CommonResult.success("操作成功");
        }
        return CommonResult.failed("操作失败");
    }

    /**
     * 导出河流列表
     *
     * @param vo
     * @param response
     */
    @Override
    @PostMapping("/exportRiverInfo")
    public void exportRiverInfo(RiverInfoPageQueryVo vo, HttpServletResponse response) {
        PageVO pageVO = riverService.riverPageQuery(vo);
        List<RiverInfoResVo> list = pageVO.getList();
        ExportUtils.exportExcel(list,
                "河道名录列表" + DateTimeFormatter.ofPattern("yyyyMMddHHmmss").format(LocalDateTime.now()),
                new String[]{"河流名称", "河流类型", "河流等级", "所属流域", "河流长度", "河源位置", "河口位置", "河长级别", "河长姓名", "河长电话", "河长职务", "所属单位"},
                new String[]{"name", "riverType", "riverGrade", "basin", "riverLength", "resourceLocation", "estuaryLocation", "riverManagerGrade", "riverManagerName", "riverManagerPhone", "riverManagerJob", "department"},
                response, RiverInfoResVo.class);
    }
}
