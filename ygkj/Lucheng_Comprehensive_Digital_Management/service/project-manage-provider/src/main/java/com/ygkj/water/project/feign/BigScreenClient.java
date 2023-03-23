package com.ygkj.water.project.feign;

import com.ygkj.project.model.WarningRule;
import com.ygkj.water.model.CommonResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author wgf
 * @date 2021/10/26 11:32
 * @Description:
 */
@FeignClient(contextId = "big-screen", name = "big-screen")
public interface BigScreenClient {
    /**
     * 海塘预警
     * @return
     */
    @GetMapping("giveAnAlarm/seawallWarning")
    CommonResult seawallWarning(@RequestParam(required = false)String seawallCode, @RequestParam(required = false) String time);
    /**
     * 海塘报警
     * @return
     */
    @GetMapping("giveAnAlarm/seawallGiveAnAlarm")
    CommonResult seawallGiveAnAlarm(@RequestParam(required = false) String seawallCode, @RequestParam(required = false) String time);
    /**
     * 水位报警
     * @return
     */
    @GetMapping("giveAnAlarm/waterGiveAnAlarm")
    CommonResult waterGiveAnAlarm();
    /**
     * 降雨报警
     * @return
     */
    @GetMapping("giveAnAlarm/rainGiveAnAlarm")
    CommonResult rainGiveAnAlarm();

    @GetMapping("giveAnAlarm/displacement")
    CommonResult displacement(@RequestParam(required = false)String date,@RequestParam Integer jclx);

    /**
     * 水位报警
     * @return
     */
    @PostMapping("giveAnAlarm/waterGiveAnAlarm2")
    CommonResult waterGiveAnAlarm2(@RequestBody WarningRule warningRule);

    /**
     * 降雨报警
     * @return
     */
    @PostMapping("giveAnAlarm/rainGiveAnAlarm2")
    CommonResult rainGiveAnAlarm2(@RequestBody WarningRule warningRule);

    /**
     * 降雨报警
     * @return
     */
    @PostMapping("giveAnAlarm/rainGiveAnAlarm3")
    CommonResult rainGiveAnAlarm3(@RequestBody WarningRule warningRule);
}
