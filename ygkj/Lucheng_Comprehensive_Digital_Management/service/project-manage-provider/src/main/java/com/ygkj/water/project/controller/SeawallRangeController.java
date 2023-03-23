package com.ygkj.water.project.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.ygkj.project.model.SeawallRange;
import com.ygkj.utils.StringUtils;
import com.ygkj.water.model.CommonResult;
import com.ygkj.water.project.service.SeawallRangeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author wgf
 * @date 2021/11/12 18:33
 * @Description:
 */
@Api(tags = "海塘保护范围线,管保区域")
@RestController
@RequestMapping("seawallRange")
public class SeawallRangeController {

    @Autowired
    private SeawallRangeService seawallRangeService;

    @ApiOperation(value = "新增或者修改海塘保护范围线,管保区域")
    @PostMapping("/saveOrUpdate")
    public CommonResult saveOrUpdate(SeawallRange seawallRange) {
        if (StringUtils.isBlank(seawallRange.getPrcd()) || seawallRange.getType()== null){
            return CommonResult.failed("缺少必传参数");
        }
        if (StringUtils.isNotBlank(seawallRange.getId())){
            //查询是否有除开自己重复编码和类型的数据
            QueryWrapper<SeawallRange> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("prcd", seawallRange.getPrcd());
            queryWrapper.eq("type", seawallRange.getType());
            queryWrapper.ne("id", seawallRange.getId());
            int count = seawallRangeService.count(queryWrapper);
            if (count > 0){
                return CommonResult.failed("已存在该数据");
            }
            if(seawallRangeService.updateById(seawallRange)){
                return CommonResult.success();
            }
        } else {
            //查询是否有重复编码和类型的数据
            QueryWrapper<SeawallRange> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("prcd", seawallRange.getPrcd());
            queryWrapper.eq("type", seawallRange.getType());
            int count = seawallRangeService.count(queryWrapper);
            if (count > 0){
                return CommonResult.failed("已存在该数据");
            }
            seawallRange.setId(IdWorker.get32UUID());
            if(seawallRangeService.save(seawallRange)){
                return CommonResult.success();
            }
        }
        return CommonResult.failed("新增或者修改失败");
    }

    @ApiOperation(value = "海塘保护范围线,管保区域记录")
    @GetMapping("/getById")
    public CommonResult getById(String id) {
       return CommonResult.success(seawallRangeService.getById(id));
    }

    @ApiOperation(value = "删除海塘保护范围线,管保区域")
    @GetMapping("/delete")
    public CommonResult delete(String id) {
        if (seawallRangeService.removeById(id)) {
            return CommonResult.success();
        }
        return CommonResult.failed("删除失败");
    }

    @ApiOperation(value = "根据海塘编码查询，保护范围线,管保区域")
    @GetMapping("/getRangebyCode")
    public CommonResult getRangebyCode(SeawallRange seawallRange) {
        QueryWrapper<SeawallRange> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(StringUtils.isNotBlank(seawallRange.getPrcd()), "prcd", seawallRange.getPrcd());
        queryWrapper.eq(seawallRange.getType() != null, "type", seawallRange.getType());
        List<SeawallRange> list = seawallRangeService.list(queryWrapper);
        return CommonResult.success(list);
    }
}
