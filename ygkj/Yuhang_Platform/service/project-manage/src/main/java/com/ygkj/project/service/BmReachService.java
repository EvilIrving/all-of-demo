package com.ygkj.project.service;

import com.github.pagehelper.PageHelper;
import com.ygkj.project.api.vo.model.BmReach;
import com.ygkj.project.api.vo.req.BmReachReqVo;
import com.ygkj.project.api.vo.res.BmReachRespVo;
import com.ygkj.project.mapper.BmReachMapper;
import com.ygkj.water.utils.PageUtils;
import com.ygkj.water.utils.vo.PageVO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.UUID;

@Service
public class BmReachService {

    @Resource
    private BmReachMapper bmReachMapper;

    public int insert(BmReachReqVo vo) {
        BmReach bmReach = new BmReach();
        BeanUtils.copyProperties(vo,bmReach);
        bmReach.setDtsCmfId(UUID.randomUUID().toString());
        bmReach.setDtsCmfCreateTime(new Date());
        vo.setDtsCmfId(vo.getDtsCmfId());
        return bmReachMapper.insert(bmReach);
    }

    public int update(BmReachReqVo vo) {
        BmReach bmReach = new BmReach();
        BeanUtils.copyProperties(vo,bmReach);
        bmReach.setDtsCmfUpdateTime(new Date());
        return bmReachMapper.update(bmReach);
    }


    public int delete(String id) {
        return bmReachMapper.delete(id);
    }

    /**
     * 数据分页查找
     * @param vo
     * @return
     */
    public PageVO<BmReachRespVo> loadPage(BmReachReqVo vo) {
        if (vo.getPageNum() != null && vo.getPageNum() > 0
                && vo.getPageSize() != null && vo.getPageSize() > 0) {
            PageHelper.startPage(vo.getPageNum(), vo.getPageSize());
        }
        return PageUtils.getPageVO(bmReachMapper.loadPage(vo));

    }
}
