package com.ygkj.river.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.ygkj.river.mapper.AttRiverOutletBaseMapper;
import com.ygkj.river.service.RiverOutletService;
import com.ygkj.river.vo.request.RiverOutletQueryVo;
import com.ygkj.river.vo.response.RiverOutletResVo;
import com.ygkj.utils.PageUtils;
import com.ygkj.vo.PageVO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;

/**
 * @author xq
 * @Description 入河排污口服务层
 * @Date 2021/10/9
 */
@Service
public class RiverOutletServiceImpl implements RiverOutletService {

    @Resource
    AttRiverOutletBaseMapper riverOutletBaseMapper;

    /**
     * 入河排污口分页查询
     *
     * @param queryVo
     * @return
     */
    @Override
    public PageVO<RiverOutletResVo> pageList(RiverOutletQueryVo queryVo) {
        if (queryVo.isPaged()) {
            PageHelper.startPage(queryVo.getPageNum(), queryVo.getPageSize());
        }
        return PageUtils.getPageVO(riverOutletBaseMapper.selectByCondition(queryVo));
    }

    /**
     * 入河排污口表头统计
     *
     * @param queryVo
     * @return
     */
    @Override
    public JSONObject stats4PageList(RiverOutletQueryVo queryVo) {
        queryVo.noPage();
        // 不计算最近的
        queryVo.setComputeNearestWain(false);
        List<RiverOutletResVo> list = pageList(queryVo).getList();
        list = list == null ? Collections.emptyList() : list;
        JSONObject result = new JSONObject();
        result.put("totalNum", list.size());

        // 规模以上数量
        result.put("gtGm", list.stream().filter(o -> outletGradGt.equals(o.getOutletGrad())).count());

        // 规模以下数量
        result.put("ltGm", list.stream().filter(o -> outletGradLt.equals(o.getOutletGrad())).count());
        return result;
    }
}
