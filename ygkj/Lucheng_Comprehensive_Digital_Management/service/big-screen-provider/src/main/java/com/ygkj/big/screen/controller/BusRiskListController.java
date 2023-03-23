package com.ygkj.big.screen.controller;

import com.ygkj.big.screen.BusRiskListControllerApi;
import com.ygkj.big.screen.service.BusRiskListService;
import com.ygkj.big.screen.vo.request.BusRiskListReq;
import com.ygkj.big.screen.vo.response.BusRiskListResp;
import com.ygkj.big.screen.vo.response.RiskTypeStaResp;
import com.ygkj.enums.EngScalEnum;
import com.ygkj.gragh.model.AttSeawallBase;
import com.ygkj.utils.ExportUtils;
import com.ygkj.vo.PageVO;
import com.ygkj.water.model.CommonResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

/**
 * @author hucong
 * @version 1.0
 * @date 2021/6/16 15:24
 */
@RestController
@RequestMapping("busRiskList")
public class BusRiskListController implements BusRiskListControllerApi {

    @Autowired
    private BusRiskListService busRiskListService;

    @PostMapping("busRiskListPage")
    @Override
    public CommonResult<PageVO<BusRiskListResp>> busRiskListPage(BusRiskListReq entity) {
        return CommonResult.success(busRiskListService.busRiskListPage(entity));
    }

    @PostMapping("exportBusRisk")
    @Override
    public void exportBusRisk(@RequestBody BusRiskListReq reqVo, HttpServletResponse response) {
        // 禁止分页
        reqVo.noPage();
        List<BusRiskListResp> list = busRiskListService.busRiskListPage(reqVo).getList();
        Function<Object, Object> sbSignMapping = o -> {
            if ("251".equals(o)) {
                return "隐患点";
            }
            if ("252".equals(o)) {
                return "高风险点";
            }
            if ("253".equals(o)) {
                return "薄弱点";
            } else {
                return StringUtils.EMPTY;
            }
        };
        ExportUtils.exportExcel(list, "风险隐患",
                new String[]{"名称", "工程类型", "风险类型", "隐患类型", "整改情况", "行政区划", "上报人员", "整改责任单位", "发现时间", "整改完成时间"},
                new String[]{"projName", "projType", "sbSign", "dangerType", "recCondition", "county", "reportPerson", "dutyUnit", "reportTime", "rectifyFinishTime"},
                response, null, Collections.singletonMap("sbSign", sbSignMapping));
    }

    @PostMapping("riskTypeStatistics")
    @Override
    public CommonResult<List<RiskTypeStaResp>> riskTypeStatistics() {
        return CommonResult.success(busRiskListService.riskTypeStatistics());
    }
}
