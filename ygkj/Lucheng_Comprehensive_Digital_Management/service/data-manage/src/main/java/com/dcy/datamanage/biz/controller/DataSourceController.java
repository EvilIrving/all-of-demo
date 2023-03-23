package com.dcy.datamanage.biz.controller;



import com.dcy.datamanage.api.model.BusDataSource;
import com.dcy.datamanage.api.model.UserInfo;
import com.dcy.datamanage.api.model.common.PageModel;
import com.dcy.datamanage.api.model.common.PageResult;
import com.dcy.datamanage.biz.dto.input.BusDataSourceSearchInputDTO;
import com.dcy.datamanage.biz.dto.output.BusDataSourceOutputDTO;
import com.dcy.datamanage.biz.service.DataSourceService;
import com.dcy.datamanage.api.model.common.R;
import com.dcy.datamanage.biz.service.UserInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;


@RestController
@RequestMapping("dataSource")
@Api(tags = "数据源")
public class DataSourceController {

    @Autowired
    private DataSourceService dataSourceService;

    @Autowired
    private UserInfoService userInfoService;


    @PostMapping("save")
    @ApiOperation("新增")
    public R<Boolean> save(@RequestBody BusDataSource busDataSource, HttpServletRequest request){
        UserInfo userInfo = userInfoService.getUserInfo(request);
        busDataSource.setCreateId(userInfo.getUserId());
        busDataSource.setDeptId(userInfo.getDeptId());
        return R.success(dataSourceService.save(busDataSource));
    }


    @DeleteMapping("delById/{id}")
    @ApiOperation("根据id删除")
    public R<Boolean> delById(@PathVariable("id") String id){
        return R.success(dataSourceService.delById(id));
    }


    @PutMapping("update")
    @ApiOperation("更新")
    public R<Boolean> update(@RequestBody BusDataSource busDataSource){
        return R.success(dataSourceService.update(busDataSource));
    }


    @PostMapping("page")
    @ApiOperation("分页查询")
    public R<PageResult<BusDataSourceOutputDTO>> page(BusDataSourceSearchInputDTO dto, PageModel pageModel){
        return R.success(dataSourceService.page(dto,pageModel));
    }


    @PostMapping("testConnection")
    @ApiOperation("测试连接")
    public R<Boolean> testConnection(@RequestBody BusDataSource busDataSource){
        return R.success(dataSourceService.testConnection(busDataSource));
    }

}
