package com.ygkj.big.screen.mapper;

import com.ygkj.big.screen.model.HealthCodeColor;
import com.ygkj.big.screen.model.HealthCodeWeight;

import java.util.List;

public interface HealthCodeMapper {

    /**
     * 二维码权重
     *
     * @param seawallCode
     * @return
     */
    List<HealthCodeWeight> listHealthCodeWeight(String seawallCode);

    /**
     * 保存权重
     *
     * @param allList
     */
    void saveWeight(List<HealthCodeWeight> allList);

    /**
     * 子项查询
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
     * 查询各码的分数
     *
     * @param seawallCode
     * @return
     */
    List<HealthCodeColor> listColor(String seawallCode);
}
