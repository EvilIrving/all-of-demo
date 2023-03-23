package com.ygkj.controller;

import com.ygkj.service.GenerateDistributionIDService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

/**
 * @author xq
 * @Description
 * @Date 2021/3/31
 */
@Slf4j
@Order
@RestController
@RequestMapping("/common/snowFlake")
public class SnowFlakeController implements InitializingBean {

    private GenerateDistributionIDService idService;

    public SnowFlakeController(@Autowired GenerateDistributionIDService idService) {
        Assert.notNull(idService,"请检查分布式ID Service，不允许为空!");
        this.idService = idService;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        log.info("---------完成分布式ID Controller初始化---------");
    }

    @GetMapping("get/{idNum}")
    public List<String> getSnowFlake(@PathVariable Integer idNum){
        List<String> result=new ArrayList<>(idNum);
        IntStream.range(0,idNum).forEach(o->result.add(idService.nextID()));
        return result;
    }


}
