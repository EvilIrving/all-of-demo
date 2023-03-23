package com.ygkj.digitization.mapper;

import com.ygkj.digitization.model.WaterWarnTemplate;
import com.ygkj.digitization.vo.request.WaterWarnTemplateReqVo;
import com.ygkj.digitization.vo.response.WaterWarnTemplateRespVo;

import java.util.List;

/**
 * @description 水位预警模板表
 * @author hucong
 * @date 2021-07-13
 */
public interface WaterWarnTemplateMapper {

    /**
     * 新增
     * @author hucong
     * @date 2021/07/13
     **/
    int insert(WaterWarnTemplate entity);

    /**
     * 刪除
     * @author hucong
     * @date 2021/07/13
     **/
    int delete(String id);

    /**
     * 更新
     * @author hucong
     * @date 2021/07/13
     **/
    int update(WaterWarnTemplate entity);

    List<WaterWarnTemplateRespVo> loadPage(WaterWarnTemplateReqVo vo);
}
