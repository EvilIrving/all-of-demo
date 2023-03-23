package com.dcy.datamanage.biz.controller;



import com.dcy.datamanage.api.model.common.R;
import com.dcy.datamanage.biz.dto.input.DataAssessPageInputDTO;
import com.dcy.datamanage.biz.dto.output.DataAssessParamOutputDTO;
import com.dcy.datamanage.biz.service.DataAssessService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("dataAssessDept")
@Api(tags = "数据考核")
public class DataAssessController {

    @Autowired
    private DataAssessService dataAssessService;


    @PostMapping("list")
    @ApiOperation("查找各表的归属和频次")
    public R<List<DataAssessParamOutputDTO>> list(DataAssessPageInputDTO dto) {
        return R.success(dataAssessService.list(dto));
    }



}
