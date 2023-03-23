package com.ygkj.big.screen.controller;

import com.ygkj.big.screen.service.GiveAnAlarmService;
import com.ygkj.big.screen.vo.request.QrCodeReqVo;
import com.ygkj.big.screen.vo.request.SeawallAmbitusVo;
import com.ygkj.big.screen.vo.response.SeawallWarnResVo;
import com.ygkj.project.model.WarningRule;
import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;

/**
 * @author wgf
 * @date 2021/10/18 19:17
 * @Description: 报警组件
 */
@RestController
@RequestMapping("/giveAnAlarm")
@Api(tags = "报警组件")
public class GiveAnAlarmController {

    @Autowired
    GiveAnAlarmService giveAnAlarmService;


    @GetMapping("seawallWarning")
    public CommonResult seawallWarning(@RequestParam(required = false) String seawallCode, @RequestParam(required = false) String time ,Boolean warning) {
        return giveAnAlarmService.seawallWarning(seawallCode, time, warning);
    }

    @GetMapping("seawallGiveAnAlarm")
    public CommonResult seawallGiveAnAlarm(@RequestParam(required = false) String seawallCode, @RequestParam(required = false) String time,Boolean warning) {
        return giveAnAlarmService.seawallGiveAnAlarm(seawallCode, time ,warning);
    }

    @GetMapping("waterGiveAnAlarm")
    public CommonResult waterGiveAnAlarm() {
        return giveAnAlarmService.waterGiveAnAlarm();
    }

    @PostMapping("/waterGiveAnAlarm2")
    public CommonResult waterGiveAnAlarm2(@RequestBody WarningRule warningRule) {
        return giveAnAlarmService.waterGiveAnAlarm2(warningRule);
    }

    @GetMapping("rainGiveAnAlarm")
    public CommonResult rainGiveAnAlarm() {
        return giveAnAlarmService.rainGiveAnAlarm();
    }

    @PostMapping("/rainGiveAnAlarm2")
    public CommonResult rainGiveAnAlarm2(@RequestBody WarningRule warningRule) {
        return giveAnAlarmService.rainGiveAnAlarm2(warningRule);
    }

    @PostMapping("/rainGiveAnAlarm3")
    public CommonResult rainGiveAnAlarm3(@RequestBody WarningRule warningRule) {
        return giveAnAlarmService.rainGiveAnAlarm3(warningRule);
    }

    @GetMapping("typhoonGiveAnAlarm")
    public CommonResult typhoonGiveAnAlarm() {
        return giveAnAlarmService.typhoonGiveAnAlarm();
    }

    @GetMapping("runGiveAnAlarm")
    public CommonResult runGiveAnAlarm() {
        return giveAnAlarmService.runGiveAnAlarm();
    }

    @GetMapping("giveAnAlarmCount")
    public CommonResult giveAnAlarmCount() {
        return giveAnAlarmService.giveAnAlarmCount();
    }


    @GetMapping("selectWarn/{seawallCode}")
    @ApiOperation(value = "海塘预警", response = SeawallWarnResVo.class)
    public CommonResult selectWarn(@PathVariable("seawallCode") String seawallCode,String time,String fsctId) {
        return CommonResult.success(giveAnAlarmService.selectWarn(seawallCode,time,fsctId));
    }

    @GetMapping("typhoonInfluence")
    public CommonResult typhoonInfluence(@RequestParam(required = false) String date) {
        try {
            return giveAnAlarmService.typhoonInfluence(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return CommonResult.failed();
        }
    }

    @GetMapping("seawallTyphoon")
    public CommonResult seawallTyphoon(@RequestParam(required = false) String date, @RequestParam String id) {
        try {
            return giveAnAlarmService.seawallTyphoon(date, id);
        } catch (ParseException e) {
            e.printStackTrace();
            return CommonResult.failed();
        }
    }

    @GetMapping("seawallAmbitus")
    public CommonResult seawallAmbitus(SeawallAmbitusVo vo) throws UnsupportedEncodingException {
        return giveAnAlarmService.seawallAmbitus(vo);
    }

    @GetMapping("displacement")
    public CommonResult displacement(@RequestParam(required = false)String date,@RequestParam Integer jclx){
        return giveAnAlarmService.displacement(date,jclx);
    }

    @GetMapping("getAqjcData")
    public CommonResult getAqjcData(@RequestParam(required = false) Integer days,@RequestParam String stationCode,@RequestParam(required = false) String startTime,@RequestParam(required = false) String endTime){
        return giveAnAlarmService.getAqjcData(days, stationCode,startTime,endTime);
    }

    @GetMapping("exportGetAqjcData")
    public void exportGetAqjcData(@RequestParam(required = false) Integer days,@RequestParam String stationCode,@RequestParam(required = false) String startTime,
                                  @RequestParam(required = false) String endTime,String stationName,HttpServletResponse response){
        giveAnAlarmService.exportGetAqjcData(days, stationCode,startTime,endTime,stationName,response);
    }

    @PostMapping("seawallTyphoonDif")
    public CommonResult seawallTyphoonDif(Double lng, Double lat) {
        return CommonResult.success(giveAnAlarmService.fuckingCurrentTyphoon(lat, lng));
    }


    @PostMapping("qrCode")
    @ApiOperation("二维码查询")
    public CommonResult qrCode(QrCodeReqVo reqVo) {
        return CommonResult.success(giveAnAlarmService.qrCode(reqVo));
    }

    @GetMapping("qrCode/{seawallCode}")
    @ApiOperation("二维码查询")
    public CommonResult qrCode(@PathVariable("seawallCode") String seawallCode) {
        return CommonResult.success(giveAnAlarmService.qrCode(seawallCode));
    }

    @GetMapping("aqjcWarn")
    @ApiOperation("安全监测警报")
    public CommonResult aqjcWarn(@RequestParam(required = false)String date,@RequestParam String seawallCode) {
        return CommonResult.success(giveAnAlarmService.aqjcWarn(date,seawallCode));
    }

    @GetMapping("/floodPreventionBulletin")
    @ApiOperation("防汛防台简报")
    public void floodPreventionBulletin(){
        giveAnAlarmService.floodPreventionBulletin();
    }

    @GetMapping("/floodPreventionBulletinApp")
    @ApiOperation("汛情摘要水情")
    public CommonResult floodPreventionBulletinApp(){
        return CommonResult.success(giveAnAlarmService.floodPreventionBulletinApp());
    }

    @GetMapping("/waterAndRainBriefing")
    @ApiOperation("水雨情简报")
    public void waterAndRainBriefing(){
        giveAnAlarmService.waterAndRainBriefing();
    }

    @GetMapping("/rainfallReport")
    @ApiOperation("雨量报表")
    public CommonResult rainfallReport(@RequestParam(required = false) String startTime,
                                       @RequestParam(required = false) String endTime,
                                       @RequestParam(required = false) Double minRain,
                                       @RequestParam(required = false) Double maxRain){
         return CommonResult.success(giveAnAlarmService.rainfallReport(startTime,endTime,minRain,maxRain));
    }

    @GetMapping("/exportRainfallReport")
    @ApiOperation("雨量报表导出")
    public void exportRainfallReport(@RequestParam(required = false) String startTime,
                                       @RequestParam(required = false) String endTime,
                                       @RequestParam(required = false) Double minRain,
                                       @RequestParam(required = false) Double maxRain,
                                     HttpServletResponse response) throws IOException {
        giveAnAlarmService.exportRainfallReport(startTime,endTime,minRain,maxRain,response);
    }

    @PostMapping("/riskHiddenDanger")
    @ApiOperation("新版大屏风险隐患")
    public CommonResult RiskHiddenDanger() {
        return CommonResult.success(giveAnAlarmService.riskHiddenDanger());
    }

    @GetMapping("/latestMonitoringData")
    @ApiOperation("安全监测最新数据")
    public CommonResult latestMonitoringData() {
        return CommonResult.success(giveAnAlarmService.latestMonitoringData());
    }
}
