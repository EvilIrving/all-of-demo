package com.ygkj.big.screen.service.impl;

import com.github.pagehelper.PageHelper;
import com.ygkj.big.screen.mapper.BusRiskListMapper;
import com.ygkj.big.screen.service.BusRiskListService;
import com.ygkj.big.screen.vo.request.BusRiskListReq;
import com.ygkj.big.screen.vo.response.BusRiskListResp;
import com.ygkj.big.screen.vo.response.RiskTypeStaResp;
import com.ygkj.utils.PageUtils;
import com.ygkj.vo.PageVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author hucong
 * @version 1.0
 * @date 2021/6/16 15:23
 */
@Service
public class BusRiskListServiceImpl implements BusRiskListService {

    @Autowired
    private BusRiskListMapper busRiskListMapper;

    @Override
    public PageVO<BusRiskListResp> busRiskListPage(BusRiskListReq vo) {
        if (vo.isPaged()) {
            PageHelper.startPage(vo.getPageNum(), vo.getPageSize());
        }
        List<BusRiskListResp> reservoirProjectList = busRiskListMapper.busRiskListPage(vo);
        return PageUtils.getPageVO(reservoirProjectList);
    }

    @Override
    public List<RiskTypeStaResp> riskTypeStatistics() {
        List<RiskTypeStaResp> riskTypeStaResps = busRiskListMapper.riskTypeStatistics();
        Integer total = riskTypeStaResps.stream().mapToInt(e -> e.getNum()).sum();
        for (RiskTypeStaResp riskTypeStaResp : riskTypeStaResps) {
            double rate = new BigDecimal((new BigDecimal(riskTypeStaResp.getNum() + "").divide(new BigDecimal(total + ""), 4, BigDecimal.ROUND_HALF_UP).doubleValue() * 100) + "").setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            riskTypeStaResp.setRate(rate + "");
            riskTypeStaResp.setTotal(total);
        }
        return riskTypeStaResps;
    }
}
