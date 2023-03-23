package com.dcy.datamanage.biz.controller;



import com.dcy.datamanage.api.model.UserInfo;
import com.dcy.datamanage.api.model.common.R;
import com.dcy.datamanage.api.model.BusDataResource;
import com.dcy.datamanage.biz.dto.input.BusDataResourceSearchInputDTO;
import com.dcy.datamanage.biz.dto.output.BusDataResourceOutputDTO;
import com.dcy.datamanage.biz.service.DataResourceService;
import com.dcy.datamanage.biz.service.UserInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("dataResource")
@Api(tags = "资源目录")
public class DataResourceController {

    @Autowired
    private DataResourceService dataResourceService;

    @Autowired
    private UserInfoService userInfoService;

    @PostMapping("save")
    @ApiOperation("新增")
    public R<Boolean> save(@RequestBody BusDataResource busDataResource, HttpServletRequest request){
        UserInfo userInfo = userInfoService.getUserInfo(request);
        busDataResource.setCreateId(userInfo.getUserId());
        busDataResource.setDeptId(userInfo.getDeptId());
        return R.success(dataResourceService.save(busDataResource));
    }


    @DeleteMapping("delById/{id}")
    @ApiOperation("根据id删除")
    public R<Boolean> delById(@PathVariable("id") String id){
        return R.success(dataResourceService.delById(id));
    }


    @PutMapping("update")
    @ApiOperation("更新")
    public R<Boolean> update(@RequestBody BusDataResource busDataResource){
        return R.success(dataResourceService.update(busDataResource));
    }


    @PostMapping("list")
    @ApiOperation("列表查询")
    public R<List<BusDataResourceOutputDTO>> list(BusDataResourceSearchInputDTO dto){
        return R.success(dataResourceService.list(dto));
    }

}
