package com.dcy.datamanage.biz.controller;




import com.dcy.datamanage.api.model.common.PageModel;
import com.dcy.datamanage.api.model.common.PageResult;
import com.dcy.datamanage.api.model.common.R;
import com.dcy.datamanage.api.model.BusDataResourceSourceRel;
import com.dcy.datamanage.biz.dto.input.BusDataSourceSearchInputDTO;
import com.dcy.datamanage.biz.dto.input.ResourceSourceRelSaveInputDTO;
import com.dcy.datamanage.biz.dto.input.ResourceSourceRelSearchInputDTO;
import com.dcy.datamanage.biz.dto.output.BusDataSourceOutputDTO;
import com.dcy.datamanage.biz.dto.output.ResourceSourceRelOutputDTO;
import com.dcy.datamanage.biz.service.ResourceSourceRelService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("resourceSourceRel")
@Api(tags = "表格资源关系")
public class ResourceSourceRelController {

    @Autowired
    private ResourceSourceRelService resourceSourceRelService;


    @PostMapping("save")
    @ApiOperation("新增")
    public R<Boolean> save(@RequestBody BusDataResourceSourceRel rel){
        return R.success(resourceSourceRelService.add(rel));
    }


    @PostMapping("batchSave")
    @ApiOperation("批量新增")
    public R<Boolean> batchSave(@RequestBody ResourceSourceRelSaveInputDTO dto){
        return R.success(resourceSourceRelService.batchSave(dto));
    }


    @DeleteMapping("delById/{id}")
    @ApiOperation("根据id删除")
    public R<Boolean> delById(@PathVariable("id") String id){
        return R.success(resourceSourceRelService.delById(id));
    }


    @PostMapping("batchDel")
    @ApiOperation("批量删除")
    public R<Boolean> batchDel(@RequestBody List<BusDataResourceSourceRel> list){
        return R.success(resourceSourceRelService.batchDel(list));
    }


    @PutMapping("update")
    @ApiOperation("更新")
    public R<Boolean> update(@RequestBody BusDataResourceSourceRel rel){
        return R.success(resourceSourceRelService.update(rel));
    }


    @PostMapping("page")
    @ApiOperation("分页查询")
    public R<PageResult<ResourceSourceRelOutputDTO>> page(ResourceSourceRelSearchInputDTO dto, PageModel pageModel){
        return R.success(resourceSourceRelService.page(dto,pageModel));
    }

}
