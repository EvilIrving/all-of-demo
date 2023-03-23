package com.ygkj.big.screen.service.impl;

import com.ygkj.big.screen.mapper.HealthCodeMapper;
import com.ygkj.big.screen.model.HealthCodeColor;
import com.ygkj.big.screen.model.HealthCodeWeight;
import com.ygkj.big.screen.service.HealthCodeService;
import com.ygkj.utils.CollectionUtils;
import com.ygkj.water.exception.BusinessException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

@Service
public class HealthCodeServiceImpl implements HealthCodeService {

    @Resource
    private HealthCodeMapper healthCodeMapper;


    /**
     * 权重配置
     *
     * @param seawallCode
     * @return
     */
    @Override
    public List<HealthCodeWeight> listHealthCodeWeight(String seawallCode) {
        //查询该海塘所有健康码权重
        List<HealthCodeWeight> list = healthCodeMapper.listHealthCodeWeight(seawallCode);
//        list.sort(Comparator.comparing(HealthCodeWeight::getParentId).thenComparing(HealthCodeWeight::getSort));

        List<HealthCodeWeight> result = new ArrayList<>();

        //合并子项
        Map<String, HealthCodeWeight> map = new HashMap<>();
        for (HealthCodeWeight healthCodeWeight : list) {
            if (StringUtils.isEmpty(healthCodeWeight.getParentId())) {
                healthCodeWeight.setSubWeight(new ArrayList<>());
                result.add(healthCodeWeight);
                map.put(healthCodeWeight.getId(), healthCodeWeight);
            } else {
                HealthCodeWeight parent = map.get(healthCodeWeight.getParentId());
                parent.getSubWeight().add(healthCodeWeight);
            }
        }

        return result;
    }

    /**
     * 保存权重
     *
     * @param list
     */
    @Override
    public void saveWeight(List<HealthCodeWeight> list) {
        int score = 0;
        //校验总分
        for (HealthCodeWeight healthCodeWeight : list) {
            score += healthCodeWeight.getScore();
        }
        if (score != 100) {
            throw new BusinessException("权重总得分需等于100");
        }
        List<HealthCodeWeight> allList = new ArrayList<>();
        for (HealthCodeWeight healthCodeWeight : list) {
            allList.add(healthCodeWeight);
            if (!CollectionUtils.isNotBlank(healthCodeWeight.getSubWeight())) {
                allList.addAll(healthCodeWeight.getSubWeight());
            }
        }
        healthCodeMapper.saveWeight(allList);
    }

    /**
     * 查询子项
     *
     * @param parentId
     * @return
     */
    @Override
    public List<HealthCodeWeight> listSubWeight(String parentId) {
        return healthCodeMapper.listSubWeight(parentId);
    }

    /**
     * 保存各颜色分数
     *
     * @param list
     */
    @Override
    public void saveColor(List<HealthCodeColor> list) {
        HealthCodeColor green = null;
        HealthCodeColor yellow = null;
        HealthCodeColor red = null;

        //检验
        for (HealthCodeColor healthCodeColor : list) {
            String category = healthCodeColor.getCategory();
            if(healthCodeColor.getMaxVal() < healthCodeColor.getMinVal()){
                throw new BusinessException("分数设置异常");
            }
            if ("绿码".equals(category)) {
                if(healthCodeColor.getMaxVal() > 100){
                    throw new BusinessException("分数设置异常");
                }
                green = healthCodeColor;
            } else if ("黄码".equals(category)) {
                yellow = healthCodeColor;
            } else {
                if(healthCodeColor.getMinVal() < 0){
                    throw new BusinessException("分数设置异常");
                }
                red = healthCodeColor;
            }
        }
        if(green.getMinVal() != yellow.getMaxVal() + 1 || red.getMaxVal() + 1 != yellow.getMinVal()){
            throw new BusinessException("分数设置异常");
        }

        healthCodeMapper.saveColor(list);
    }

    /**
     * 查询各颜色分数
     *
     * @param seawallCode
     * @return
     */
    @Override
    public List<HealthCodeColor> listColor(String seawallCode) {
        return healthCodeMapper.listColor(seawallCode);
    }
}
