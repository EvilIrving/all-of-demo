package com.ygkj.water.project.service.impl;

import com.github.pagehelper.PageHelper;
import com.ygkj.project.model.DtsRaBasicDataDbWaterExpert;
import com.ygkj.project.vo.request.WaterExpertReqVo;
import com.ygkj.project.vo.response.WaterExpertResVo;
import com.ygkj.utils.PageUtils;
import com.ygkj.vo.PageVO;
import com.ygkj.water.project.mapper.WaterExpertMapper;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @author pqf
 * @DESCRIPTION:
 * @create 2021-11-12 14:22
 */
@Service
public class WaterExpertServiceImpl implements WaterExpertService{
    @Resource
    private WaterExpertMapper waterExpertMapper;

    /**
     * 水利专家列表查询
     *
     * @param reqVo
     * @return
     */
    @Override
    public PageVO<WaterExpertResVo> listWaterExpert(WaterExpertReqVo reqVo) {
        if (reqVo.getPageNum() != null && reqVo.getPageSize() != null) {
            PageHelper.startPage(reqVo.getPageNum(), reqVo.getPageSize());
        }
        List<WaterExpertResVo> list = waterExpertMapper.listWaterExpert(reqVo);
        return PageUtils.getPageVO(list);
    }

    /**
     * 新增或修改水利专家
     *
     * @param waterExpert
     */
    @Override
    public void editWaterExpert(WaterExpertResVo waterExpert) {
        if (StringUtils.isEmpty(waterExpert.getDtsCmfId())) {
            waterExpert.setDtsCmfId(UUID.randomUUID().toString());
            waterExpertMapper.saveWaterExpert(waterExpert);
        } else {
            waterExpertMapper.updateWaterExpert(waterExpert);
        }
    }

    /**
     * 查询水利专家详情
     *
     * @param dtsCmfId
     */
    @Override
    public DtsRaBasicDataDbWaterExpert selectWaterExpert(String dtsCmfId) {
        return waterExpertMapper.selectWaterExpert(dtsCmfId);
    }

    /**
     * 删除水利专家
     *
     * @param dtsCmfId
     */
    @Override
    public void delWaterExpert(String dtsCmfId) {
        waterExpertMapper.delWaterExpert(dtsCmfId);
    }

    /**
     * 统计水利专家数量
     *
     * @param reqVo
     * @return
     */
    @Override
    public List<Map<String, Object>> countWaterExpert(WaterExpertReqVo reqVo) {
        return waterExpertMapper.countWaterExpert(reqVo);
    }
}
