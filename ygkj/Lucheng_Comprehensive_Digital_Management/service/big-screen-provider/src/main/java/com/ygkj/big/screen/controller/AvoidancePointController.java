package com.ygkj.big.screen.controller;

import com.ygkj.big.screen.AvoidancePointControllerApi;
import com.ygkj.big.screen.mapper.AvoidancePointMapper;
import com.ygkj.big.screen.service.AvoidancePointService;
import com.ygkj.big.screen.vo.request.AvoidancePointReqVo;
import com.ygkj.big.screen.vo.response.AvoidancePointRespVo;
import com.ygkj.project.model.WatersReport;
import com.ygkj.utils.ExportUtils;
import com.ygkj.water.model.CommonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * @author pqf
 * @DESCRIPTION:避灾点组件
 * @create 2021-10-20 17:08
 */
@RestController
@RequestMapping("avoidancePoint")
public class AvoidancePointController implements AvoidancePointControllerApi{
    @Autowired
    private AvoidancePointService avoidancePointService;
    @Resource
    private AvoidancePointMapper avoidancePointMapper;

    /**
     * 查询 分页查询
     **/

    @PostMapping("/pageList")
    @Override
    public CommonResult pageList(AvoidancePointReqVo reqVo) {
        return avoidancePointService.pageList(reqVo);
    }

    @PostMapping("/exportList")
    public void exportList(AvoidancePointReqVo reqVo, HttpServletResponse response) {

        List<AvoidancePointRespVo> list = avoidancePointMapper.pageList(reqVo);
        ExportUtils.exportExcel(list,
                "避灾点" + DateTimeFormatter.ofPattern("yyyyMMddHHmmss").format(LocalDateTime.now()),
                new String[]{"名称","避灾点地址","可容纳人数","现容纳人数","联系人","联系电话","经度","纬度"},
                new String[]{"pointName","pointAddress","pointCapacity","pointNowCapacity","contacts","phone","longitude","latitude"},
                response, AvoidancePointRespVo.class);
    }

    /**
     * 查询 根据主键 id 查询
     **/
    @PostMapping("/load")
    public CommonResult load(AvoidancePointReqVo reqVo){
        return avoidancePointService.load(reqVo);
    }

    /**
     * C位指标项
     **/
    @PostMapping("/totalList")
    public CommonResult totalList(){
        return avoidancePointService.totalList();
    }

    @PostMapping("/saveOrUpdate")
    @Override
    public CommonResult saveOrUpdate(AvoidancePointReqVo reqVo) {
        int count=avoidancePointService.saveOrUpdate(reqVo);
        return count>0?CommonResult.success("新增修改成功"):CommonResult.success("新增修改失败");
    }

    @PostMapping("/delete")
    @Override
    public CommonResult delete(AvoidancePointReqVo reqVo) {
        int count=avoidancePointService.delete(reqVo);
        return count>0?CommonResult.success("删除成功"):CommonResult.success("删除失败");
    }

}
