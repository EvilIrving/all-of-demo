package com.ygkj.water.project.service;

import com.ygkj.project.model.AttStBase;
import com.ygkj.project.vo.request.AttStBaseReqVo;
import com.ygkj.vo.PageVO;

public interface StService {

    /**
     * 分页查询
     *
     * @param reqVo
     * @return
     */
    PageVO<AttStBase> page(AttStBaseReqVo reqVo);

    /**
     * 更新
     *
     * @param attStBase
     */
    void update(AttStBase attStBase);
}
