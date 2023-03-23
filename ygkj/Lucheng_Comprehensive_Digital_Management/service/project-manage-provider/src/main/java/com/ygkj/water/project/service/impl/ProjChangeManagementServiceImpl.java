package com.ygkj.water.project.service.impl;

import com.github.pagehelper.PageHelper;
import com.ygkj.jwt.JwtTokenUtil;
import com.ygkj.project.model.ProjChangeManagement;
import com.ygkj.project.vo.request.ProjChangeManagementInsVo;
import com.ygkj.project.vo.request.ProjChangeManagementReqVo;
import com.ygkj.project.vo.response.ProjChangeManagementRespVo;
import com.ygkj.service.GenerateDistributionIDService;
import com.ygkj.utils.PageUtils;
import com.ygkj.vo.PageVO;
import com.ygkj.water.project.mapper.ProjChangeManagementMapper;
import com.ygkj.water.project.mapper.UserMapper;
import com.ygkj.water.project.service.ProjChangeManagementService;
import com.ygkj.water.project.utils.CommonUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @author hucong
 * @description 变更管理表
 * @date 2021-07-08
 */
@Service
public class ProjChangeManagementServiceImpl implements ProjChangeManagementService {

    @Resource
    private ProjChangeManagementMapper projChangeManagementMapper;
    @Resource
    private GenerateDistributionIDService idService;
    @Autowired
    private UserMapper userMapper;

    @Override
    public int insert(ProjChangeManagementInsVo vo) {
        ProjChangeManagement changeManagement = new ProjChangeManagement();
        BeanUtils.copyProperties(vo, changeManagement);
        changeManagement.setId(idService.nextID());
        changeManagement.setUploadTime(new Date());
        changeManagement.setUploadName(JwtTokenUtil.getCurrentUserName().orElse(""));
        changeManagement.setUploadId(JwtTokenUtil.getCurrentUserId().orElse(""));
        return projChangeManagementMapper.insert(changeManagement);
    }

    @Override
    public int delete(String id) {
        return projChangeManagementMapper.delete(id);
    }

    @Override
    public int update(ProjChangeManagementInsVo vo) {
        ProjChangeManagement changeManagement = new ProjChangeManagement();
        BeanUtils.copyProperties(vo, changeManagement);
        return projChangeManagementMapper.update(changeManagement);
    }

    @Override
    public PageVO<ProjChangeManagementRespVo> loadPage(ProjChangeManagementReqVo vo) {
        //判断当前用户是否是建管中心的人员:如果是,查询所有的信息；不是，查询当前用户上传的
        if (!CommonUtil.isConstructionManagementPerson(userMapper)) {
            vo.setUploadId(JwtTokenUtil.getCurrentUserId().orElse(""));
        }
        if (vo.getPageNum() != null && vo.getPageNum() > 0
                && vo.getPageSize() != null && vo.getPageSize() > 0)
            PageHelper.startPage(vo.getPageNum(), vo.getPageSize());
        return PageUtils.getPageVO(projChangeManagementMapper.loadPage(vo));
    }
}
