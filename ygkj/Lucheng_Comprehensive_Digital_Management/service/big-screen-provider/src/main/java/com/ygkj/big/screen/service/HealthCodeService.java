package com.ygkj.big.screen.service;

import com.ygkj.big.screen.model.HealthCodeColor;
import com.ygkj.big.screen.model.HealthCodeWeight;

import java.util.List;

public interface HealthCodeService {

    /**
     * 权重配置
     *
     * @param seawallCode
     * @return
     */
    List<HealthCodeWeight> listHealthCodeWeight(String seawallCode);

    /**
     * 保存权重
     *
     * @param list
     */
    void saveWeight(List<HealthCodeWeight> list);

    /**
     * 查询子项
     *
     * @param parentId
     * @return
     */
    List<HealthCodeWeight> listSubWeight(String parentId);

    /**
     * 保存各颜色分数
     *
     * @param list
     */
    void saveColor(List<HealthCodeColor> list);

    /**
     * 查询各颜色分数
     *
     * @param seawallCode
     * @return
     */
    List<HealthCodeColor> listColor(String seawallCode);
}
