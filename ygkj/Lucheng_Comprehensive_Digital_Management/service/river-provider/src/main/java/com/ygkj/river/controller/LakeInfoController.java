package com.ygkj.river.controller;

import com.ygkj.river.LakeInfoControllerApi;
import com.ygkj.river.service.LakeInfoService;
import com.ygkj.river.vo.request.LakeInfoAddVo;
import com.ygkj.river.vo.request.LakeInfoPageQueryVo;
import com.ygkj.river.vo.response.LakeInfoResVo;
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
@RequestMapping("/lakeInfo")
public class LakeInfoController implements LakeInfoControllerApi {

    private LakeInfoService lakeInfoService;

    public LakeInfoController(@Autowired LakeInfoService lakeInfoService) {
        this.lakeInfoService = lakeInfoService;
    }

    @PostMapping("")
    @Override
    public CommonResult lakes(LakeInfoPageQueryVo queryVo) {
        return CommonResult.success(lakeInfoService.lakes(queryVo));
    }

    @PutMapping("/insert")
    @Override
    public CommonResult insert(LakeInfoAddVo addVo) {
        if (lakeInfoService.insert(addVo)) {
            return CommonResult.success("操作成功");
        }
        return CommonResult.failed("操作失败");
    }

    @PutMapping("/update")
    @Override
    public CommonResult update(LakeInfoAddVo addVo) {
        if (lakeInfoService.update(addVo)) {
            return CommonResult.success("操作成功");
        }
        return CommonResult.failed("操作失败");
    }

    @DeleteMapping("/delete/{code}")
    @Override
    public CommonResult delete(@PathVariable String code) {
        if (lakeInfoService.delete(code)) {
            return CommonResult.success("操作成功");
        }
        return CommonResult.failed("操作失败");
    }

    /**
     * 导出湖泊列表
     *
     * @param vo
     * @param response
     */
    @Override
    @PostMapping("/exportLakeInfo")
    public void exportLakeInfo(LakeInfoPageQueryVo vo, HttpServletResponse response) {
        PageVO pageVO = lakeInfoService.lakes(vo);
        List<LakeInfoResVo> list = pageVO.getList();
        ExportUtils.exportExcel(list,
                "湖泊名录列表" + DateTimeFormatter.ofPattern("yyyyMMddHHmmss").format(LocalDateTime.now()),
                new String[]{"湖泊名称", "所属流域", "水域面积", "湖泊容积", "平均水深", "河长级别", "河长姓名", "河长电话", "河长职务", "所属单位"},
                new String[]{"lkName", "basin", "watArea", "meaAnnLkVol", "averageWaterDeep", "riverManagerGrade", "riverManagerName", "riverManagerPhone", "riverManagerJob", "department"},
                response, LakeInfoResVo.class);
    }
}
