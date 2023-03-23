package com.ygkj.water.project.service.impl;

import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.ygkj.project.model.BhLgLmrDisDomSs;
import com.ygkj.project.vo.request.BhLgLmrDisDomSsReqVo;
import com.ygkj.service.GenerateDistributionIDService;
import com.ygkj.utils.StringUtils;
import com.ygkj.water.project.mapper.BhLgLmrDisDomSsMapper;
import com.ygkj.water.project.service.BhLgLmrDisDomSsService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class BhLgLmrDisDomSsServiceImpl implements BhLgLmrDisDomSsService {
    @Resource
    private BhLgLmrDisDomSsMapper disDomSsMapper;
    @Resource
    private GenerateDistributionIDService idService;

    @Override
    public void saveOrUpdateGate(BhLgLmrDisDomSsReqVo vo) {

        BhLgLmrDisDomSs disDomSs = new BhLgLmrDisDomSs();
        BeanUtils.copyProperties(vo, disDomSs);
        if (StringUtils.isBlank(disDomSs.getId())) {
            disDomSs.setId(idService.nextID());
            disDomSs.setSourceType(2);
            disDomSsMapper.insert(disDomSs);
        } else {
            if ("1".equals(String.valueOf(disDomSs.getSourceType()))) {
                throw new RuntimeException("标化同步数据无法修改");
            }
            disDomSsMapper.updateById(disDomSs);
        }

    }

    @Override
    public int delete(String id) {
        BhLgLmrDisDomSs disDomSss = new BhLgLmrDisDomSs();
        BhLgLmrDisDomSs bhLgLmrDisDomSs = disDomSsMapper.selectById(id);
        if (1 == bhLgLmrDisDomSs.getSourceType()){
            throw new RuntimeException("标化同步数据无法删除");
        }
        disDomSss.setId(id);
        disDomSss.setDelFlag(1);
        disDomSss.setSourceType(2);
        return disDomSsMapper.updateById(disDomSss);
    }

    @Override
    public Object load(String id,String soId,String actlOpenTm) {
        List<BhLgLmrDisDomSs> bhLgLmrDisDomSses = disDomSsMapper.selectList(Wrappers.lambdaQuery(BhLgLmrDisDomSs.class)
                .eq(StringUtils.isNotBlank(id),BhLgLmrDisDomSs::getId,id)
                .eq(StringUtils.isNotBlank(soId),BhLgLmrDisDomSs::getSoId, soId)
                .like(ObjectUtils.isNotEmpty(actlOpenTm),BhLgLmrDisDomSs::getActlOpenTm, actlOpenTm)
                .eq(BhLgLmrDisDomSs::getDelFlag, 0));
        return bhLgLmrDisDomSses;
    }
}
