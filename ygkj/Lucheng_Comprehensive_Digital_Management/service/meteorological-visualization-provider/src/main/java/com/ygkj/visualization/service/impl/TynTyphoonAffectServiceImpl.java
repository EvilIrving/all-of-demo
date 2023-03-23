package com.ygkj.visualization.service.impl;

import com.github.pagehelper.PageHelper;
import com.ygkj.utils.PageUtils;
import com.ygkj.visualization.mapper.TynTyphoonAffectMapper;
import com.ygkj.visualization.model.TynTyphoonAffect;
import com.ygkj.visualization.service.TynTyphoonAffectService;
import com.ygkj.visualization.vo.request.TyphoonAffectReqVo;
import com.ygkj.vo.PageVO;
import com.ygkj.water.model.CommonResult;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class TynTyphoonAffectServiceImpl implements TynTyphoonAffectService {

    @Resource
    private TynTyphoonAffectMapper tynTyphoonAffectMapper;

    @Override
    public CommonResult<PageVO<TynTyphoonAffect>> typhoonAffects(TyphoonAffectReqVo vo) {

        if (!vo.isFetchAll()) {
            PageHelper.startPage(vo.getPageNum(), vo.getPageSize());
        }

        List<TynTyphoonAffect> typhoonAffects = tynTyphoonAffectMapper.getTyphoonAffects(vo);

        return CommonResult.success(PageUtils.getPageVO(typhoonAffects));
    }
}
