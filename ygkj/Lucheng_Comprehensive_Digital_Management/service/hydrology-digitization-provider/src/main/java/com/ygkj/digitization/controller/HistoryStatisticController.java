package com.ygkj.digitization.controller;

import com.alibaba.fastjson.JSONArray;
import com.ygkj.digitization.HistoryStatisticControllerApi;
import com.ygkj.digitization.service.RainAndWaterService;
import com.ygkj.digitization.service.StationService;
import com.ygkj.digitization.vo.request.StationDivideQueryVo;
import com.ygkj.digitization.vo.request.StationHistoryQueryVo;
import com.ygkj.digitization.vo.response.StationHistoryResVo;
import com.ygkj.utils.ExportUtils;
import com.ygkj.water.model.CommonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("/historyStatistic")
public class HistoryStatisticController implements HistoryStatisticControllerApi {

    @Autowired
    private StationService stationService;
    @Autowired
    private RainAndWaterService rainAndWaterService;

    @PostMapping("/stationDivide")
    @Override
    public CommonResult stationDivide(StationDivideQueryVo queryVo) {
        return CommonResult.success(stationService.stationDivide(queryVo));
    }

    @PostMapping("/historyStatistic")
    @Override
    public CommonResult historyStatistic(StationHistoryQueryVo queryVo) {
        return CommonResult.success(rainAndWaterService.historyStatistic(queryVo));
    }

    @Override
    @PostMapping("/exportHistoryStatistic")
    public void exportHistoryStatistic(StationHistoryQueryVo vo, HttpServletResponse response) {
        JSONArray objects = rainAndWaterService.historyStatistic(vo);
        List<StationHistoryResVo> list = JSONArray.parseArray(objects.toJSONString(),StationHistoryResVo.class);
        ExportUtils.exportExcel(list,
                "历史报表列表"+ DateTimeFormatter.ofPattern("yyyyMMddHHmmss").format(LocalDateTime.now()),
                new String[]{"名称","时间","行政区划","实时水位（m）","闸上水位（m）"},
                new String[]{"name","tm","adcd","z","UPZ"},
                response,StationHistoryResVo.class);
    }
}
