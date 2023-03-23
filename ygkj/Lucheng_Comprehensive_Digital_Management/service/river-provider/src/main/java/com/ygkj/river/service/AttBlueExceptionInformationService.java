package com.ygkj.river.service;

import com.ygkj.river.model.AttBlueExceptionInformation;
import com.ygkj.river.vo.request.AttBlueExceptionInformationReqVo;
import com.ygkj.vo.PageVO;

/**
 * @author lxl
 * @create 2021-08-06 12:18
 * @description 巡查异常条目配置项
 */
public interface AttBlueExceptionInformationService {

    /**
     * 新增配置条目
     *
     * @param attBlueExceptionInformation
     * @return
     */
    Boolean addExceptionInformation(AttBlueExceptionInformation attBlueExceptionInformation);

    /**
     * 更新配置条目
     *
     * @param attBlueExceptionInformation
     * @return
     */
    Boolean updateExceptionInformation(AttBlueExceptionInformation attBlueExceptionInformation);

    /**
     * 批量删除对应配置
     *
     * @param exceptionInformationIds
     * @return
     */
    Boolean deleteExceptionInformation(String exceptionInformationIds);

    /**
     * 根据筛选获取对应配置项列表
     *
     * @param reqVo
     * @return
     */
    PageVO exceptionInformationList(AttBlueExceptionInformationReqVo reqVo);

    /**
     * 更新排序
     *
     * @param upOrDown
     * @param id
     * @return
     */
    Boolean updateSort(String upOrDown, String id);
}
