package com.ygkj.water.project.service.impl;

import com.github.pagehelper.PageHelper;
import com.ygkj.project.vo.request.BmSeawallStructinfoReqVo;
import com.ygkj.project.vo.response.BmSeawallStructinfoRespVo;
import com.ygkj.utils.PageUtils;
import com.ygkj.vo.PageVO;
import com.ygkj.water.project.mapper.BmSeawallStructinfoMapper;
import com.ygkj.water.project.service.BmSeawallStructinfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author pqf
 * @DESCRIPTION:
 * @create 2021-11-12 9:23
 */
@Service
public class BmSeawallStructinfoServiceImpl implements BmSeawallStructinfoService {

    @Autowired
    private BmSeawallStructinfoMapper bmSeawallStructinfoMapper;
    @Override
    public PageVO<BmSeawallStructinfoRespVo> loadPage(BmSeawallStructinfoReqVo reqVo) {
        if (reqVo.getPageNum() != null && reqVo.getPageNum() > 0
                && reqVo.getPageSize() != null && reqVo.getPageSize() > 0)
            PageHelper.startPage(reqVo.getPageNum(), reqVo.getPageSize());
        List<BmSeawallStructinfoRespVo> list=bmSeawallStructinfoMapper.loadPage(reqVo);
        PageVO<BmSeawallStructinfoRespVo> pageVO=PageUtils.getPageVO(list);
        return pageVO;
    }
}
