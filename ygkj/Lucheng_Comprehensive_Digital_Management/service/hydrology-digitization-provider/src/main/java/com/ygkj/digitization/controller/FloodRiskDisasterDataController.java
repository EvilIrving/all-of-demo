package com.ygkj.digitization.controller;

import com.ygkj.digitization.FloodRiskDisasterDataControllerApi;
import com.ygkj.digitization.service.FloodRiskDisasterDataService;
import com.ygkj.digitization.vo.request.FloodRiskDisasterDataReqVo;
import com.ygkj.digitization.vo.response.FloodRiskDisasterDataRespVo;
import com.ygkj.digitization.vo.response.ResSoftResVo;
import com.ygkj.utils.ExportUtils;
import com.ygkj.vo.PageVO;
import com.ygkj.water.model.CommonResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @description 洪水风险图灾情数据表
 * @author hucong
 * @date 2021-07-21
 */
@RestController
@RequestMapping(value = "/FloodRiskDisasterData")
public class FloodRiskDisasterDataController implements FloodRiskDisasterDataControllerApi {

    @Resource
    private FloodRiskDisasterDataService floodRiskDisasterDataService;

    @PostMapping("loadPage")
    @Override
    public CommonResult<PageVO<FloodRiskDisasterDataRespVo>> loadPage(FloodRiskDisasterDataReqVo vo) {
        return CommonResult.success(floodRiskDisasterDataService.loadPage(vo));
    }

    @PostMapping("/export")
    @Override
    public void export(FloodRiskDisasterDataReqVo reqVo, HttpServletResponse response) {
        List<FloodRiskDisasterDataRespVo> list = this.loadPage(reqVo).getData().getList();
        String[] headers = new String[]{"流域名称", "设计洪水标准", "淹没面积", "淹没人口", "淹没区洪水损失"};
        String[] cols = new String[]{"name", "designStandard", "floodedArea", "submergedPopulation", "floodLoss"};
        ExportUtils.exportExcel(list, "洪水风险图", headers, cols, response);
    }
}
