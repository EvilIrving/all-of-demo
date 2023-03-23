package com.ygkj.service.impl;

import com.github.pagehelper.PageHelper;
import com.ygkj.entity.DingDingDept;
import com.ygkj.mapper.DingDingDeptMapper;
import com.ygkj.service.DingDingDeptService;
import com.ygkj.utils.PageUtils;
import com.ygkj.vo.req.DingDingDeptVo;
import com.ygkj.vo.resp.PageVO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service("dingDingDeptRelationService")
public class DingDingDeptServiceImpl implements DingDingDeptService {

    @Resource
    private DingDingDeptMapper dingDingDeptMapper;

    /**
     * 新增绑定关系
     *
     * @param dingDingDept
     */
    @Override
    public void bind(DingDingDept dingDingDept) {
        dingDingDeptMapper.bind(dingDingDept);
    }

    /**
     * 批量新增绑定关系
     *
     * @param list
     */
    @Override
    public void batchBind(List<DingDingDept> list) {
        dingDingDeptMapper.batchBind(list);
    }

    /**
     * 分页获取绑定关系
     *
     * @param vo
     * @return
     */
    @Override
    public PageVO<DingDingDeptVo> page(DingDingDeptVo vo) {
        PageHelper.startPage(vo.getPageNum(), vo.getPageSize());
        List<DingDingDeptVo> list = dingDingDeptMapper.listBind(vo);
        return PageUtils.getPageVO(list);
    }
}
