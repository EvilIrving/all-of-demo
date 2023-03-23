package com.dcy.datamanage.biz.controller;



import com.dcy.datamanage.api.model.common.R;
import com.dcy.datamanage.biz.dto.input.*;
import com.dcy.datamanage.biz.dto.output.DataAssessDeptSearchOutputDTO;
import com.dcy.datamanage.biz.service.DataAssessDeptService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("dataAccessDept")
@Api(tags = "数据考核归属")
public class DataAssessDeptController {

    @Autowired
    private DataAssessDeptService dataAssessDeptService;


    @PostMapping("batchSave")
    @ApiOperation("(批量)?((新增)|(修改))")
    public R<Boolean> batchSave(@RequestBody DataAssessDeptInputDTO dto) {
        return R.success(dataAssessDeptService.batchSave(dto));
    }


    @DeleteMapping("batchDel")
    @ApiOperation("批量删除")
    public R<Boolean> batchDel(@RequestBody List<String> idList) {
        return R.success(dataAssessDeptService.removeBatchByIds(idList));
    }


    @PostMapping("list")
    @ApiOperation("列表查询")
    public R<List<DataAssessDeptSearchOutputDTO>> list(DataAssessDeptSearchInputDTO dto) {
        return R.success(dataAssessDeptService.list(dto));
    }
}
