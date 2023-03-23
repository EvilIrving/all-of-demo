package com.dcy.datamanage.biz.controller;



import com.dcy.datamanage.api.model.common.R;
import com.dcy.datamanage.biz.dto.input.DataAssessDeptInputDTO;
import com.dcy.datamanage.biz.dto.input.DataAssessDeptSearchInputDTO;
import com.dcy.datamanage.biz.dto.input.DataAssessFrequencyInputDTO;
import com.dcy.datamanage.biz.dto.output.DataAssessDeptSearchOutputDTO;
import com.dcy.datamanage.biz.service.DataAssessFrequencyService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("dataAssessFrequency")
@Api(tags = "数据考核频次")
public class DataAssessFrequencyController {

    @Autowired
    private DataAssessFrequencyService dataAssessFrequencyService;


    @PostMapping("batchSave")
    @ApiOperation("(批量)?((新增)|(修改))")
    public R<Boolean> batchSave(@RequestBody DataAssessFrequencyInputDTO dto) {
        return R.success(dataAssessFrequencyService.batchSave(dto));
    }


    @DeleteMapping("batchDel")
    @ApiOperation("批量删除")
    public R<Boolean> batchDel(@RequestBody List<String> idList) {
        return R.success(dataAssessFrequencyService.removeBatchByIds(idList));
    }

}
