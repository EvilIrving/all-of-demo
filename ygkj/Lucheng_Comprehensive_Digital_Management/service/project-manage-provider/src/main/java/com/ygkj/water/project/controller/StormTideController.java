package com.ygkj.water.project.controller;

import com.ygkj.project.vo.response.AllStormTideResVo;
import com.ygkj.project.vo.response.StormResVo;
import com.ygkj.water.model.CommonResult;
import com.ygkj.water.project.enums.StormTideEnum;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("stormTide")
@Api(tags = "风暴潮警报")
public class StormTideController {

    @Resource
    private RedisTemplate redisTemplate;

    @GetMapping("select")
    @ApiOperation(value = "获取风暴潮预警",response = AllStormTideResVo.class)
    public CommonResult select(){
        AllStormTideResVo allStormTideResVo = new AllStormTideResVo();

        //台风风暴潮
        if(redisTemplate.hasKey(StormTideEnum.TYPHOON.getRedisKey())){
            allStormTideResVo.setTyphoon((StormResVo) redisTemplate.opsForValue().get(StormTideEnum.TYPHOON.getRedisKey()));
        }

        //温带风暴潮
        if(redisTemplate.hasKey(StormTideEnum.TEMPERATE.getRedisKey())){
            allStormTideResVo.setTemperate((StormResVo) redisTemplate.opsForValue().get(StormTideEnum.TEMPERATE.getRedisKey()));
        }

        return CommonResult.success(allStormTideResVo);
    }
}
