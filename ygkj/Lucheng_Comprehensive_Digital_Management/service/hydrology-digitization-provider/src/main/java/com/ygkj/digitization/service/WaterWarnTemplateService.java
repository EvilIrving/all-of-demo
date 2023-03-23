package com.ygkj.digitization.service;

import com.ygkj.digitization.model.WaterWarnTemplate;
import com.ygkj.digitization.vo.request.WaterWarnTemplateReqVo;
import com.ygkj.digitization.vo.response.WaterWarnTemplateRespVo;
import com.ygkj.vo.PageVO;

/**
 * @description 水位预警模板表
 * @author hucong
 * @date 2021-07-13
 */
public interface WaterWarnTemplateService {

    /**
     * 新增
     */
    int insert(WaterWarnTemplateReqVo vo);

    /**
     * 删除
     */
    int delete(String id);

    /**
     * 更新
     */
    int update(WaterWarnTemplateReqVo vo);

    /**
     * 分页查找
     * @param vo
     * @return
     */
    PageVO<WaterWarnTemplateRespVo> loadPage(WaterWarnTemplateReqVo vo);
}
