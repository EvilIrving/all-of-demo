package com.ygkj.flood.controller;


import com.ygkj.flood.MaterialControllerApi;
import com.ygkj.flood.service.AttMaterialService;
import com.ygkj.flood.vo.request.AttMaterialBaseAddVo;
import com.ygkj.flood.vo.request.AttMaterialQueryVo;
import com.ygkj.water.model.CommonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/material")
public class MaterialController implements MaterialControllerApi {

    @Autowired
    private AttMaterialService materialService;

    @PostMapping("/material")
    @Override
    public CommonResult material(AttMaterialQueryVo queryVo) {
        return CommonResult.success(materialService.selectByCondition(queryVo));
    }

    @PostMapping("/addMaterial")
    @Override
    public CommonResult addMaterial(@RequestBody List<AttMaterialBaseAddVo> list) {
        if (materialService.addMaterials(list)) {
            return CommonResult.success("操作成功");
        }
        return CommonResult.failed("操作失败");
    }
}
