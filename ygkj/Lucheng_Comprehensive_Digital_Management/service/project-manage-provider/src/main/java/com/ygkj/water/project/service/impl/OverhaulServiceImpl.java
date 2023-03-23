package com.ygkj.water.project.service.impl;


import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.ygkj.jwt.JwtTokenUtil;
import com.ygkj.project.model.AttOverhaulElectrical;
import com.ygkj.project.model.AttOverhaulMechanical;
import com.ygkj.project.model.SysFile;
import com.ygkj.project.vo.request.OverhaulReqVo;
import com.ygkj.service.GenerateDistributionIDService;
import com.ygkj.utils.ExportUtils;
import com.ygkj.utils.PageUtils;
import com.ygkj.vo.PageVO;
import com.ygkj.water.model.CommonResult;
import com.ygkj.water.project.feign.UserFeignClient;
import com.ygkj.water.project.mapper.AttOverhaulElectricalMapper;
import com.ygkj.water.project.mapper.AttOverhaulMechanicalMapper;
import com.ygkj.water.project.mapper.SysFileMapper;
import com.ygkj.water.project.service.OverhaulService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;

/**
 * @author lxl
 * @create 2022-04-18 14:21
 * @description
 */
@Service
public class OverhaulServiceImpl implements OverhaulService {

    @Resource
    private AttOverhaulElectricalMapper attOverhaulElectricalMapper;
    @Resource
    private AttOverhaulMechanicalMapper attOverhaulMechanicalMapper;
    @Resource
    private GenerateDistributionIDService idService;
    @Resource
    private SysFileMapper sysFileMapper;


    @Override
    public int insert(OverhaulReqVo vo) {

        if (1 == vo.getType()){
            AttOverhaulElectrical electrical = new AttOverhaulElectrical();
            BeanUtils.copyProperties(vo, electrical);
            electrical.setId(idService.nextID());
            electrical.setCreateId(JwtTokenUtil.getCurrentUserId().orElse(""));
            return attOverhaulElectricalMapper.insert(electrical);
        }else {
            AttOverhaulMechanical mechanical = new AttOverhaulMechanical();
            BeanUtils.copyProperties(vo, mechanical);
            mechanical.setId(idService.nextID());
            mechanical.setCreateId(JwtTokenUtil.getCurrentUserId().orElse(""));
            return attOverhaulMechanicalMapper.insert(mechanical);
        }
    }

    @Override
    public int update(OverhaulReqVo vo) {
        if (1 == vo.getType()){
            AttOverhaulElectrical electrical = new AttOverhaulElectrical();
            BeanUtils.copyProperties(vo, electrical);
            electrical.setUpdateId(JwtTokenUtil.getCurrentUserId().orElse(""));
            return attOverhaulElectricalMapper.updateById(electrical);
        }else {
            AttOverhaulMechanical mechanical = new AttOverhaulMechanical();
            BeanUtils.copyProperties(vo, mechanical);
            mechanical.setUpdateId(JwtTokenUtil.getCurrentUserId().orElse(""));
            return attOverhaulMechanicalMapper.updateById(mechanical);
        }
    }

    @Override
    public int delete(OverhaulReqVo vo) {
        if (1 == vo.getType()){
            return attOverhaulElectricalMapper.deleteByElectricalId(vo.getId());
        }else {
            return attOverhaulMechanicalMapper.deleteByMechanicalId(vo.getId());
        }
    }

    @Override
    public PageVO loadPage(OverhaulReqVo vo) {

        vo.page();
        //筛选条件为 人名 或者 日期
        if (1 == vo.getType()){
            List<AttOverhaulElectrical> list = attOverhaulElectricalMapper.selectList(Wrappers.lambdaQuery(AttOverhaulElectrical.class)
                    .like(StringUtils.isNotBlank(vo.getName()), AttOverhaulElectrical::getGuardian, vo.getName())
                    .or().like(StringUtils.isNotBlank(vo.getName()), AttOverhaulElectrical::getLeader, vo.getName())
                    .likeRight(ObjectUtils.isNotEmpty(vo.getDate()), AttOverhaulElectrical::getBeginTime, vo.getDate())
                    .eq(StringUtils.isNotBlank(vo.getPid()),AttOverhaulElectrical::getPid,vo.getPid())
                    .eq(StringUtils.isNotBlank(vo.getId()),AttOverhaulElectrical::getId,vo.getId())
                    .eq(AttOverhaulElectrical::getDelFlag, 0));
            list.forEach( e -> {
                List<SysFile> fileList = getFileList(e.getFileIds());
                e.setFiles(fileList);
            });
            return PageUtils.getPageVO(list);
        }else {
            List<AttOverhaulMechanical> list = attOverhaulMechanicalMapper.selectList(Wrappers.lambdaQuery(AttOverhaulMechanical.class)
                    .like(StringUtils.isNotBlank(vo.getName()), AttOverhaulMechanical::getIssueMan, vo.getName())
                    .or().like(StringUtils.isNotBlank(vo.getName()), AttOverhaulMechanical::getPrincipal, vo.getName())
                    .likeRight(ObjectUtils.isNotEmpty(vo.getDate()), AttOverhaulMechanical::getIssueDate, vo.getDate())
                    .eq(StringUtils.isNotBlank(vo.getPid()),AttOverhaulMechanical::getPid,vo.getPid())
                    .eq(StringUtils.isNotBlank(vo.getId()),AttOverhaulMechanical::getId,vo.getId())
                    .eq(AttOverhaulMechanical::getDelFlag, 0));
            list.forEach( e -> {
                List<SysFile> fileList = getFileList(e.getFileIds());
                e.setFiles(fileList);
            });
            return PageUtils.getPageVO(list);
        }
    }



    private List<SysFile> getFileList(String fileIds){

        return sysFileMapper.loadByIds(fileIds);
    }


    @Override
    public void exportOverhaul(OverhaulReqVo vo, HttpServletResponse response) {
        if (1 == vo.getType()){
            List<AttOverhaulElectrical> list = attOverhaulElectricalMapper.selectList(Wrappers.lambdaQuery(AttOverhaulElectrical.class)
                    .like(StringUtils.isNotBlank(vo.getName()), AttOverhaulElectrical::getGuardian, vo.getName())
                    .or().like(StringUtils.isNotBlank(vo.getName()), AttOverhaulElectrical::getLeader, vo.getName())
                    .likeRight(ObjectUtils.isNotEmpty(vo.getDate()), AttOverhaulElectrical::getBeginTime, vo.getDate())
                    .eq(AttOverhaulElectrical::getDelFlag, 0));
            ExportUtils.exportExcel(list,"电气操作",
                    new String[]{"操作时间","结束时间","操作人","监护人","值班领导"},
                    new String[]{"beginTime","endTime","operator","guardian","leader"},response);
        }else {
            List<AttOverhaulMechanical> list = attOverhaulMechanicalMapper.selectList(Wrappers.lambdaQuery(AttOverhaulMechanical.class)
                    .like(StringUtils.isNotBlank(vo.getName()), AttOverhaulMechanical::getIssueMan, vo.getName())
                    .or().like(StringUtils.isNotBlank(vo.getName()), AttOverhaulMechanical::getPrincipal, vo.getName())
                    .likeRight(ObjectUtils.isNotEmpty(vo.getDate()), AttOverhaulMechanical::getIssueDate, vo.getDate())
                    .eq(AttOverhaulMechanical::getDelFlag, 0));
            ExportUtils.exportExcel(list,"机械操作",
                    new String[]{"工作负责人","签发负责人","签发日期"},
                    new String[]{"principal","issueMan","issueDate"},response);
        }
    }
}
