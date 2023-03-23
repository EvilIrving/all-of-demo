package com.ygkj.big.screen.feign;

import com.alibaba.fastjson.JSONObject;
import com.ygkj.big.screen.vo.request.FloodPointReqVo;
import com.ygkj.flood.vo.request.AttPrevTfBaseVo;
import com.ygkj.flood.vo.request.AttTeamBasePageQueryVo;
import com.ygkj.flood.vo.request.AttWarehousePageQueryVo;
import com.ygkj.vo.PageVO;
import com.ygkj.water.model.CommonResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author xq
 * @Description
 * @Date 2021/4/20
 */
@FeignClient(contextId = "flood-protect", name = "flood-protect")
public interface FloodProtectClient {
    /**
     * 雨量测站预警数量
     *
     * @param hours
     * @return
     */
    @GetMapping("rainfallAnalysis/getRainWarningStationNum")
    CommonResult<Integer> getRainWarningStationNum(@RequestParam("hours") Integer hours);

    /**
     * 预警数量
     *
     * @return
     */
    @GetMapping("waterLevelAnalyse/overWarnStatistics")
    CommonResult<JSONObject> overWarnStatistics();

    /**
     * 积水点
     *
     * @param pageNum
     * @param pageSize
     * @return
     */
    @Deprecated
    @GetMapping("resInfo/newestFloodPoint")
    CommonResult<PageVO<LinkedHashMap>> newestFloodPoint(@RequestParam("pageNum") Integer pageNum, @RequestParam("pageSize") Integer pageSize);

    /**
     * 可蓄水量/纳蓄水能力
     *
     * @param type 1:可蓄水量;2:纳蓄水能力
     * @return
     */
    @GetMapping("floodProject/countStorage")
    CommonResult<List<LinkedHashMap>> countStorage(@RequestParam("type") Integer type, @RequestParam(value = "resScal", required = false) Integer resScal);

    /**
     * type=1 为平原水位
     * type=2 为流域水位
     * type=3 为内河水位
     *
     * @param stName 测站名搜索关键字
     * @param type
     * @return
     */
    @GetMapping("waterLevelAnalyse/watLevOfPlainBasInnerRiv")
    CommonResult<List<LinkedHashMap>> watLevOfPlainBasInnerRiv(@RequestParam(value = "stName", required = false) String stName,
                                                               @RequestParam(value = "type") Integer type);

    /**
     * 雨量历史极值
     *
     * @return
     */
    @GetMapping("rainfallAnalysis/getHistoricRainfallExtremum")
    CommonResult<List<LinkedHashMap>> getHistoricRainfallExtremum();

    /**
     * 水位历史极值
     *
     * @param stName
     * @return
     */
    @GetMapping("waterLevelAnalyse/getHistoricWaterLevelExtremum")
    CommonResult<List<LinkedHashMap>> getHistoricWaterLevelExtremum(@RequestParam(value = "stName", required = false) String stName);

    @GetMapping("waterAndRain/topNRes")
    CommonResult<List<LinkedHashMap>> topNReservoir(@RequestParam(value = "index", required = false) Integer index);

    /**
     * 内河水位
     *
     * @param stName
     * @return
     */
    @GetMapping("waterLevelAnalyse/innerRiverWaterLevel")
    CommonResult<List<LinkedHashMap>> innerRiverWaterLevel(@RequestParam(value = "stName", required = false) String stName);

    /**
     * 积水点
     *
     * @return
     */
    @GetMapping("waterLogging/floodPoint")
    CommonResult<List<LinkedHashMap>> listFloodPoint(@SpringQueryMap FloodPointReqVo reqVo);

    /**
     * @return
     */
    @GetMapping("floodProject/listRsvrStorageWaterLevel")
    CommonResult<List<LinkedHashMap>> listRsvrStorageWaterLevel();

    @GetMapping("/emergencySupport/emergencySupportCount")
    CommonResult emergencySupportCount();

    /**
     * 积水点统计
     *
     * @param type 1：根据行政区划统计；2：根据流域统计;3：统计无数据、无积水、有积水的点位数量
     * @return
     */
    @GetMapping("waterLogging/floodPointStatistic")
    CommonResult floodPointStatistic(@RequestParam("type") int type);

    @GetMapping("waterLogging/floodPointCWindows")
    CommonResult floodPointCWindows(@RequestParam(value = "dataTime", required = false)
                                    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date dataTime);

    @GetMapping("torrentialFlood/listPrevInfo")
    CommonResult listPrevInfo();

    @PostMapping("team/teams")
    CommonResult<PageVO<LinkedHashMap>> teams(@SpringQueryMap AttTeamBasePageQueryVo queryVo);

    @PostMapping("warehouse/warehouses")
    CommonResult<PageVO<LinkedHashMap>> warehouses(@SpringQueryMap AttWarehousePageQueryVo queryVo);

    @GetMapping("torrentialFlood/listPrevObj")
    CommonResult listPrevObj(@SpringQueryMap AttPrevTfBaseVo vo);
}
