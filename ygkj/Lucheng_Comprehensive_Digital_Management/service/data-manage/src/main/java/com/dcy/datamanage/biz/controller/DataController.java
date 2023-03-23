package com.dcy.datamanage.biz.controller;



import com.dcy.datamanage.api.model.common.PageModel;
import com.dcy.datamanage.api.model.common.PageResult;
import com.dcy.datamanage.api.model.common.R;
import com.dcy.datamanage.biz.dto.input.DataDelInputDTO;
import com.dcy.datamanage.biz.dto.input.DataSaveInputDTO;
import com.dcy.datamanage.biz.dto.input.DataSearchInputDTO;
import com.dcy.datamanage.biz.dto.input.DataUpdateInputDTO;
import com.dcy.datamanage.biz.dto.output.BusDataDeptOutputDTO;
import com.dcy.datamanage.biz.service.DataService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("data")
@Api(tags = "数据")
public class DataController {

    @Autowired
    private DataService dataService;


    @PostMapping("save")
    @ApiOperation("数据新增")
    public R<String> save(@RequestBody DataSaveInputDTO dto) {
        String msg = dataService.save(dto);
        return msg == null ? R.success(null) : R.error(msg);
    }


    @PostMapping("del")
    @ApiOperation("数据删除")
    public R<Boolean> del(@RequestBody DataDelInputDTO dto) {
        return R.success(dataService.del(dto));
    }


    @PostMapping("update")
    @ApiOperation("数据修改")
    public R<Boolean> update(@RequestBody DataUpdateInputDTO dto) {
        return R.success(dataService.update(dto));
    }


    @PostMapping("page")
    @ApiOperation("数据查询")
    public R<PageResult<BusDataDeptOutputDTO>> page(DataSearchInputDTO dto, PageModel pageModel) {
        return R.success(dataService.page(dto, pageModel));
    }
}
