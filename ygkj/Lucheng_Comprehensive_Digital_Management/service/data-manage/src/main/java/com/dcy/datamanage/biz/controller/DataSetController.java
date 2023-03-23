package com.dcy.datamanage.biz.controller;



import com.dcy.datamanage.api.model.UserInfo;
import com.dcy.datamanage.api.model.common.PageModel;
import com.dcy.datamanage.api.model.common.PageResult;
import com.dcy.datamanage.api.model.common.R;
import com.dcy.datamanage.biz.dto.input.BusDataSetPreviewInputDTO;
import com.dcy.datamanage.biz.dto.input.BusDataSetSaveDTO;
import com.dcy.datamanage.biz.dto.input.BusDataSetSearchInputDTO;
import com.dcy.datamanage.biz.dto.output.BusDataSetOutputDTO;
import com.dcy.datamanage.biz.dto.output.BusDataSetPreviewOutputDTO;
import com.dcy.datamanage.biz.dto.output.BusDataSetSelectOutputDTO;
import com.dcy.datamanage.biz.service.DataSetService;
import com.dcy.datamanage.biz.service.UserInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RequestMapping("dataSet")
@Api(tags = "数据集")
public class DataSetController {

    @Autowired
    private DataSetService dataSetService;

    @Autowired
    private UserInfoService userInfoService;


    @PostMapping("save")
    @ApiOperation("新增")
    public R<Boolean> save(@RequestBody BusDataSetSaveDTO dto, HttpServletRequest request) {
        UserInfo userInfo = userInfoService.getUserInfo(request);
        dto.setCreateId(userInfo.getUserId());
        dto.setDeptId(userInfo.getDeptId());
        return R.success(dataSetService.add(dto));
    }


    @DeleteMapping("delById/{id}")
    @ApiOperation("根据id删除")
    public R<Boolean> delById(@PathVariable("id") String id) {
        return R.success(dataSetService.del(id));
    }


    @PutMapping("update")
    @ApiOperation("更新")
    public R<Boolean> update(@RequestBody BusDataSetSaveDTO dto) {
        return R.success(dataSetService.update(dto));
    }


    @PostMapping("page")
    @ApiOperation("分页查询")
    public R<PageResult<BusDataSetOutputDTO>> page(BusDataSetSearchInputDTO dto, PageModel pageModel) {
        return R.success(dataSetService.list(dto, pageModel));
    }


    @PostMapping("preview")
    @ApiOperation("预览")
    public R<BusDataSetPreviewOutputDTO> preview(@RequestBody BusDataSetPreviewInputDTO dto) {
        return dataSetService.preview(dto);
    }


    @GetMapping("select/{id}")
    @ApiOperation("查询详情")
    public R<BusDataSetSelectOutputDTO> select(@PathVariable("id") String id) {
        return R.success(dataSetService.select(id));
    }


    @PostMapping("result/{id}")
    @ApiOperation("结果查询")
    public R<Object> result(@PathVariable("id") String id, @RequestBody Map<String, Object> param) {
        return R.success(dataSetService.result(id,param));
    }

}
