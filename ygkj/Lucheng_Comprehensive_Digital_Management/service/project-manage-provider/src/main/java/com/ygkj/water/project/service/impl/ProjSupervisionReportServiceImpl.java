package com.ygkj.water.project.service.impl;

import com.github.pagehelper.PageHelper;
import com.ygkj.jwt.JwtTokenUtil;
import com.ygkj.project.model.ProjSupervisionReport;
import com.ygkj.project.vo.request.ProjSupervisionReportInsVo;
import com.ygkj.project.vo.request.ProjSupervisionReportReqVo;
import com.ygkj.project.vo.response.ProjSupervisionReportRespVo;
import com.ygkj.service.GenerateDistributionIDService;
import com.ygkj.utils.PageUtils;
import com.ygkj.vo.PageVO;
import com.ygkj.water.project.mapper.ProjSupervisionReportMapper;
import com.ygkj.water.project.mapper.UserMapper;
import com.ygkj.water.project.service.ProjSupervisionReportService;
import com.ygkj.water.project.utils.CommonUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @author hucong
 * @description 工程监督-监理月报表
 * @date 2021-07-09
 */
@Service
public class ProjSupervisionReportServiceImpl implements ProjSupervisionReportService {

    @Resource
    private ProjSupervisionReportMapper projSupervisionReportMapper;
    @Resource
    private UserMapper userMapper;
    @Resource
    private GenerateDistributionIDService idService;

    @Override
    public int insert(ProjSupervisionReportInsVo vo) {
        ProjSupervisionReport entity = new ProjSupervisionReport();
        BeanUtils.copyProperties(vo, entity);
        entity.setUploadTime(new Date());
        entity.setUploadId(JwtTokenUtil.getCurrentUserId().orElse(""));
        entity.setUploadName(JwtTokenUtil.getCurrentUserName().orElse(""));
        entity.setId(idService.nextID());
        return projSupervisionReportMapper.insert(entity);
    }

    @Override
    public int delete(String id) {
        return projSupervisionReportMapper.delete(id);
    }

    @Override
    public int update(ProjSupervisionReportInsVo vo) {
        ProjSupervisionReport entity = new ProjSupervisionReport();
        BeanUtils.copyProperties(vo, entity);
        return projSupervisionReportMapper.update(entity);
    }

    @Override
    public PageVO<ProjSupervisionReportRespVo> loadPage(ProjSupervisionReportReqVo vo) {
        //判断当前用户是否是建管中心的人员:如果是,查询所有的信息；不是，查询当前用户上传的
        if (!CommonUtil.isConstructionManagementPerson(userMapper)) {
            vo.setUploadId(JwtTokenUtil.getCurrentUserId().orElse(""));
        }
        if (vo.getPageNum() != null && vo.getPageNum() > 0
                && vo.getPageSize() != null && vo.getPageSize() > 0)
            PageHelper.startPage(vo.getPageNum(), vo.getPageSize());
        return PageUtils.getPageVO(projSupervisionReportMapper.loadPage(vo));
    }
}
