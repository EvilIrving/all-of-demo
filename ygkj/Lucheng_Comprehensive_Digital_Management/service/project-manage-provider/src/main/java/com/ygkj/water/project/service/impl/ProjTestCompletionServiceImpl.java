package com.ygkj.water.project.service.impl;

import com.github.pagehelper.PageHelper;
import com.ygkj.jwt.JwtTokenUtil;
import com.ygkj.project.model.ProjTestCompletion;
import com.ygkj.project.vo.request.ProjTestCompletionInsVo;
import com.ygkj.project.vo.request.ProjTestCompletionReqVo;
import com.ygkj.project.vo.response.ProjTestCompletionRespVo;
import com.ygkj.service.GenerateDistributionIDService;
import com.ygkj.utils.PageUtils;
import com.ygkj.vo.PageVO;
import com.ygkj.water.project.mapper.ProjTestCompletionMapper;
import com.ygkj.water.project.mapper.UserMapper;
import com.ygkj.water.project.service.ProjTestCompletionService;
import com.ygkj.water.project.utils.CommonUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @author hucong
 * @description 工程监督-检测完成情况表
 * @date 2021-07-09
 */
@Service
public class ProjTestCompletionServiceImpl implements ProjTestCompletionService {

    @Resource
    private ProjTestCompletionMapper projTestCompletionMapper;
    @Resource
    private GenerateDistributionIDService idService;
    @Resource
    private UserMapper userMapper;

    @Override
    public int insert(ProjTestCompletionInsVo vo) {
        ProjTestCompletion projTestCompletion = new ProjTestCompletion();
        BeanUtils.copyProperties(vo, projTestCompletion);
        projTestCompletion.setUploadTime(new Date());
        projTestCompletion.setUploadId(JwtTokenUtil.getCurrentUserId().orElse(""));
        projTestCompletion.setUploadName(JwtTokenUtil.getCurrentUserName().orElse(""));
        projTestCompletion.setId(idService.nextID());
        return projTestCompletionMapper.insert(projTestCompletion);
    }

    @Override
    public int delete(String id) {
        return projTestCompletionMapper.delete(id);
    }

    @Override
    public int update(ProjTestCompletionInsVo vo) {
        ProjTestCompletion projTestCompletion = new ProjTestCompletion();
        BeanUtils.copyProperties(vo, projTestCompletion);
        return projTestCompletionMapper.update(projTestCompletion);
    }

    @Override
    public PageVO<ProjTestCompletionRespVo> loadPage(ProjTestCompletionReqVo vo) {
        //判断当前用户是否是建管中心的人员:如果是,查询所有的信息；不是，查询当前用户上传的
        if (!CommonUtil.isConstructionManagementPerson(userMapper)) {
            vo.setUploadId(JwtTokenUtil.getCurrentUserId().orElse(""));
        }
        if (vo.getPageNum() != null && vo.getPageNum() > 0
                && vo.getPageSize() != null && vo.getPageSize() > 0)
            PageHelper.startPage(vo.getPageNum(), vo.getPageSize());
        return PageUtils.getPageVO(projTestCompletionMapper.loadPage(vo));
    }
}
