package com.ygkj.consumer.auth.client.authentication;

import com.ygkj.auth.VO.request.WaterBaseByInfoPageReqVo;
import com.ygkj.auth.VO.request.WaterBaseByKeywordPageReqVo;
import com.ygkj.auth.VO.request.WaterBasePageReqVo;
import com.ygkj.auth.api.monitor.model.SysWaterBase;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @author: fml
 * @Date: 2020/9/8 17:33
 * @Description:
 */

//@FeignClient(contextId = "authentication-system",name = "authentication-system")
public interface WaterMonitorClient {

    @PostMapping("/waterbase/insert")
    Object insert(SysWaterBase sysWaterBase);

    @DeleteMapping("/waterbase/delete/{id}")
    Object delete(@PathVariable("id") String id);

    @PutMapping("/waterbase/update")
    Object update(SysWaterBase sysWaterBase);

    @GetMapping("/waterbase/load/{id}")
    Object load(@PathVariable("id") String id);

    @PostMapping("/waterbase/pageList")
    Object pageList(WaterBasePageReqVo waterBasePageReqVo);

    @PostMapping("/waterbase/pageListByInfo")
    Object pageListByInfo(WaterBaseByInfoPageReqVo vo);

    @PostMapping("/waterbase/pageListByKeyword")
    Object pageListByKeyword(WaterBaseByKeywordPageReqVo vo);

    @GetMapping("/waterbase/queryAreaNames")
    Object queryAreaNames();


}
