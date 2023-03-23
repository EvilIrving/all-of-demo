package com.dcy.datamanage.biz.controller;



import com.dcy.datamanage.api.model.common.R;
import com.dcy.datamanage.api.model.BusDataColumn;
import com.dcy.datamanage.biz.dto.input.BusDataColumnSearchInputDTO;
import com.dcy.datamanage.biz.dto.input.DataColumnSaveInputDTO;
import com.dcy.datamanage.biz.dto.output.BusDataColumnOutputDTO;
import com.dcy.datamanage.biz.service.DataColumnService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("dataColumn")
@Api(tags = "数据列")
public class DataColumnController {

    @Autowired
    private DataColumnService dataColumnService;


    @PostMapping("list")
    @ApiOperation("列表查询")
    public R<List<BusDataColumnOutputDTO>> list(@RequestBody BusDataColumnSearchInputDTO dto) {
        return R.success(dataColumnService.list(dto));
    }


    @PostMapping("batchSave")
    @ApiOperation("批量新增列别名")
    public R<Boolean> batchSave(@RequestBody DataColumnSaveInputDTO dto) {
        return R.success(dataColumnService.batchSave(dto));
    }
}
