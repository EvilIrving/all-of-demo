package com.ygkj.water.project.service.impl;

import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.ygkj.project.model.BhLgLmrDisDomGs;
import com.ygkj.project.vo.request.BhLgLmrDisDomGsReqVo;
import com.ygkj.service.GenerateDistributionIDService;
import com.ygkj.utils.StringUtils;
import com.ygkj.water.project.mapper.BhLgLmrDisDomGsMapper;
import com.ygkj.water.project.service.BhLgLmrDisDomGsService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class BhLgLmrDisDomGsServiceImpl implements BhLgLmrDisDomGsService {
    @Resource
    private BhLgLmrDisDomGsMapper disDomGsMapper;
    @Resource
    private GenerateDistributionIDService idService;

    @Override
    public void saveOrUpdateGate(BhLgLmrDisDomGsReqVo vo) {

        BhLgLmrDisDomGs disDomGs = new BhLgLmrDisDomGs();
        BeanUtils.copyProperties(vo, disDomGs);
        if (StringUtils.isBlank(disDomGs.getId())) {
            disDomGs.setId(idService.nextID());
            disDomGs.setSourceType(2);
            disDomGsMapper.insert(disDomGs);
        } else {
            if ("1".equals(String.valueOf(disDomGs.getSourceType()))) {
                throw new RuntimeException("标化同步数据无法修改");
            }
            disDomGsMapper.updateById(disDomGs);
        }

    }

    @Override
    public int delete(String id) {
        BhLgLmrDisDomGs disDomGs = new BhLgLmrDisDomGs();
        BhLgLmrDisDomGs bhLgLmrDisDomGs = disDomGsMapper.selectById(id);
        if (bhLgLmrDisDomGs.getSourceType() == 1){
            throw new RuntimeException("标化同步数据无法删除");
        }
        disDomGs.setId(id);
        disDomGs.setDelFlag(1);
//        disDomGs.setSourceType(2);
        return disDomGsMapper.updateById(disDomGs);
    }

    @Override
    public Object load(String id,String soId,String actlOpenTm) {
        List<BhLgLmrDisDomGs> bhLgLmrDisDomGs = disDomGsMapper.selectList(Wrappers.lambdaQuery(BhLgLmrDisDomGs.class)
                .eq(StringUtils.isNotBlank(id),BhLgLmrDisDomGs::getId, id)
                .eq(StringUtils.isNotBlank(soId),BhLgLmrDisDomGs::getSoId, soId)
                .like(ObjectUtils.isNotEmpty(actlOpenTm),BhLgLmrDisDomGs::getActlOpenTm, actlOpenTm)
                .eq(BhLgLmrDisDomGs::getDelFlag, 0).orderByDesc(BhLgLmrDisDomGs::getReportTm));
        return bhLgLmrDisDomGs;
    }
}
