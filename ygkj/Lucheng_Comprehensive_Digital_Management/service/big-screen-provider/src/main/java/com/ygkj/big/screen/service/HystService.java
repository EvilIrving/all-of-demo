package com.ygkj.big.screen.service;

import com.ygkj.big.screen.vo.request.HystReqVo;
import com.ygkj.vo.PageVO;

public interface HystService {

    /**
     * 按照装机容量或者行政区划统计 1-容量 2-行政区划 3-开发方式
     *
     * @param type 1-容量 2-行政区划 3-开发方式
     * @return
     */
    Object countNumByType(Byte type);

    /**
     * 达标率或者在线率排序取前三计算 1-达标率 2-在线率
     *
     * @param type 1-达标率 2-在线率
     * @param moon 月份
     * @return
     */
    Object rateOrderByMoon(Byte type, Integer moon, Integer year);

    /**
     * 包括总数  最多水电站地区  总达标率  总在线率
     *
     * @return
     */
    Object mainTotalNum();

    /**
     * 根据条件筛选水电站列表
     *
     * @param reqVo
     * @return
     */
    PageVO hystDetailListByCondition(HystReqVo reqVo);

    /**
     * 根据编码 获取详细信息
     *
     * @param hystCode
     * @return
     */
    Object hystDetailInfoByCode(String hystCode);

    /**
     * 获取流量曲线
     *
     * @param hystCode
     * @param startTime
     * @param endTime
     * @return
     */
    Object getFlowCurve(String hystCode, String startTime, String endTime);

    /**
     * 水电站落点
     *
     * @return
     */
    Object dropPointHyst();
}
