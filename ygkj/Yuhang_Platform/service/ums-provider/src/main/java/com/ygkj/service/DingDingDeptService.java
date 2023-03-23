package com.ygkj.service;

import com.ygkj.entity.DingDingDept;
import com.ygkj.vo.req.DingDingDeptVo;
import com.ygkj.vo.resp.PageVO;

import java.util.List;

public interface DingDingDeptService {

    /**
     * 新增绑定关系
     *
     * @param dingDingDept
     */
    void bind(DingDingDept dingDingDept);

    /**
     * 批量新增绑定关系
     *
     * @param list
     */
    void batchBind(List<DingDingDept> list);

    /**
     * 分页获取绑定关系
     *
     * @param vo
     * @return
     */
    PageVO<DingDingDeptVo> page(DingDingDeptVo vo);
}
