package com.ygkj.big.screen.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ygkj.big.screen.model.ResponseLevel;
import com.ygkj.big.screen.service.ResponseLevelService;
import com.ygkj.utils.StringUtils;
import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author wgf
 * @date 2022/5/9 11:29
 * @Description:
 */
@RestController
@RequestMapping("responseLevel")
public class ResponseLevelController {

    @Autowired
    private ResponseLevelService responseLevelService;

    @ApiOperation(value = "修改应急响应等级")
    @PostMapping("/updateLevel")
    @Transactional(rollbackFor = Exception.class)
    public CommonResult updateLevel(ResponseLevel responseLevel) {
        if (StringUtils.isBlank(responseLevel.getId())){
           return CommonResult.failed("应急响应等级数据id必传");
        }
        //先把数据库打开的等级改成关闭，然后把现在选择的等级开启
        QueryWrapper<ResponseLevel> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("whether_open", 1);
        ResponseLevel serviceOne = responseLevelService.getOne(queryWrapper);
        serviceOne.setWhetherOpen(0);
        responseLevelService.updateById(serviceOne);
        responseLevel.setWhetherOpen(1);
        if(responseLevelService.updateById(responseLevel)){
            return CommonResult.success();
        }
        return CommonResult.failed("修改失败");
    }

    @ApiOperation(value = "所有的应急响应级别")
    @PostMapping("/list")
    public CommonResult list() {
        return CommonResult.success(responseLevelService.list());
    }

    @ApiOperation(value = "根据时间查询当天有生成的防汛简报的所有数据")
    @PostMapping("/getAllHoursByDate")
    public CommonResult getAllHoursByDate(String date) {
        return CommonResult.success(responseLevelService.getAllHoursByDate(date));
    }

    @ApiOperation(value = "根据年月查询当月手动生成的水雨情简报")
    @PostMapping("/getAllMonthByDate")
    public CommonResult getAllMonthByDate(String date) {
        return CommonResult.success(responseLevelService.getAllMonthByDate(date));
    }

}
