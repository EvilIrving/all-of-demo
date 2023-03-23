package com.ygkj.water.project.service.impl;

import com.github.pagehelper.PageHelper;
import com.ygkj.jwt.JwtTokenUtil;
import com.ygkj.project.model.ProjInspectionManagement;
import com.ygkj.project.vo.request.ProjInspectionManagementInsReqVo;
import com.ygkj.project.vo.request.ProjInspectionManagementReqVo;
import com.ygkj.project.vo.response.ProjInspectionManagementRespVo;
import com.ygkj.service.GenerateDistributionIDService;
import com.ygkj.utils.PageUtils;
import com.ygkj.vo.PageVO;
import com.ygkj.water.project.mapper.ProjInspectionManagementMapper;
import com.ygkj.water.project.mapper.UserMapper;
import com.ygkj.water.project.service.ProjInspectionManagementService;
import com.ygkj.water.project.utils.CommonUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * @author hucong
 * @description 检查管理
 * @date 2021-07-08
 */
@Service
public class ProjInspectionManagementServiceImpl implements ProjInspectionManagementService {

    @Resource
    private ProjInspectionManagementMapper projInspectionManagementMapper;

    @Resource
    private GenerateDistributionIDService idService;

    @Resource
    private UserMapper userMapper;

    @Override
    public int insert(ProjInspectionManagementInsReqVo vo) {
        ProjInspectionManagement inspectionManagement = new ProjInspectionManagement();
        BeanUtils.copyProperties(vo, inspectionManagement);
        inspectionManagement.setUploadTime(new Date());
        inspectionManagement.setUploadName(JwtTokenUtil.getCurrentUserName().orElse(""));
        inspectionManagement.setUploadId(JwtTokenUtil.getCurrentUserId().orElse(""));
        inspectionManagement.setId(idService.nextID());
        return projInspectionManagementMapper.insert(inspectionManagement);
    }

    @Override
    public int delete(String id) {
        return projInspectionManagementMapper.delete(id);
    }

    @Override
    public int update(ProjInspectionManagementInsReqVo vo) {
        ProjInspectionManagement inspectionManagement = new ProjInspectionManagement();
        BeanUtils.copyProperties(vo, inspectionManagement);
        return projInspectionManagementMapper.update(inspectionManagement);
    }

    @Override
    public PageVO<ProjInspectionManagementRespVo> loadPage(ProjInspectionManagementReqVo vo) {
        //判断当前用户是否是建管中心的人员:如果是,查询所有的信息；不是，查询当前用户上传的
        boolean constructPersonFlag = CommonUtil.isConstructionManagementPerson(userMapper);
        if (constructPersonFlag) {
            vo.setConstructPersonFlag(true);
        }
        if (vo.getPageNum() != null && vo.getPageNum() > 0
                && vo.getPageSize() != null && vo.getPageSize() > 0)
            PageHelper.startPage(vo.getPageNum(), vo.getPageSize());
        vo.setUploadId(JwtTokenUtil.getCurrentUserId().orElse(""));
        List<ProjInspectionManagementRespVo> list = projInspectionManagementMapper.loadPage(vo);
        for (ProjInspectionManagementRespVo projInspectionManagementRespVo : list) {
            if (constructPersonFlag) {
                projInspectionManagementRespVo.setCheckFlag(true);
            }
        }
        return PageUtils.getPageVO(list);
    }
}
