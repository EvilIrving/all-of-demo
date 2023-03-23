package com.ygkj.river.service.impl;

import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.pagehelper.PageHelper;
import com.ygkj.river.mapper.AttBlueExceptionInformationMapper;
import com.ygkj.river.service.AttBlueExceptionInformationService;
import com.ygkj.river.utils.UserUtils;
import com.ygkj.river.model.AttBlueExceptionInformation;
import com.ygkj.river.vo.request.AttBlueExceptionInformationReqVo;
import com.ygkj.service.GenerateDistributionIDService;
import com.ygkj.utils.PageUtils;
import com.ygkj.utils.StringUtils;
import com.ygkj.vo.PageVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @author lxl
 * @create 2021-08-06 12:32
 * @description
 */
@Service
public class AttBlueExceptionInformationServiceImpl implements AttBlueExceptionInformationService {


    private AttBlueExceptionInformationMapper exInfoMapper;
    private GenerateDistributionIDService generateDistributionIDService;

    public AttBlueExceptionInformationServiceImpl(@Autowired AttBlueExceptionInformationMapper exInfoMapper,
                                                  @Autowired GenerateDistributionIDService generateDistributionIDService) {
        this.exInfoMapper = exInfoMapper;
        this.generateDistributionIDService = generateDistributionIDService;
    }

    @Override
    public Boolean addExceptionInformation(AttBlueExceptionInformation attBlueExceptionInformation) {
        attBlueExceptionInformation.setId(generateDistributionIDService.nextID());
        attBlueExceptionInformation.setCreateId(UserUtils.getCurrentUserId());
        attBlueExceptionInformation.setCreateTime(new Date());
        Integer sort = exInfoMapper.selectMaxSort();
        attBlueExceptionInformation.setSort(ObjectUtils.isEmpty(sort) ? 0 : sort + 1);
        if (exInfoMapper.insert(attBlueExceptionInformation) > 0) {
            return true;
        }
        return false;
    }

    @Override
    public Boolean updateExceptionInformation(AttBlueExceptionInformation attBlueExceptionInformation) {

        attBlueExceptionInformation.setUpdateId(UserUtils.getCurrentUserId());
        attBlueExceptionInformation.setUpdateTime(new Date());
        if (StringUtils.isBlank(attBlueExceptionInformation.getId())) {
            return false;
        }
        if (exInfoMapper.updateById(attBlueExceptionInformation) > 0) {
            return true;
        }
        return false;
    }

    @Override
    public Boolean deleteExceptionInformation(String exceptionInformationIds) {

        if (exInfoMapper.deleteExceptionInformation(exceptionInformationIds) > 0) {
            return true;
        }
        return false;
    }

    @Override
    public PageVO exceptionInformationList(AttBlueExceptionInformationReqVo reqVo) {
        if (ObjectUtils.isNotEmpty(reqVo.getPageNum()) && ObjectUtils.isNotEmpty(reqVo.getPageSize())) {
            PageHelper.startPage(reqVo.getPageNum(), reqVo.getPageSize());
        }
        List<AttBlueExceptionInformation> exInfoList = exInfoMapper.selectList(Wrappers.lambdaQuery(AttBlueExceptionInformation.class)
                .eq(AttBlueExceptionInformation::getProjectStage, ObjectUtils.isNotEmpty(reqVo.getProjectStage()) ? reqVo.getProjectStage() : null)
                .eq(AttBlueExceptionInformation::getDelFlag, 0)
                .orderByDesc(AttBlueExceptionInformation::getSort));

        return PageUtils.getPageVO(exInfoList);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean updateSort(String upOrDown, String id) {
        AttBlueExceptionInformation attBlueExceptionInformation = exInfoMapper.selectById(id);
        Integer sort = null;
        if (upOrDown.equals("up")) {
            //找出下一个排序
            sort = exInfoMapper.selectUpToSort(id);
        } else {
            //找出下一个排序
            sort = exInfoMapper.selectDownToSort(id);
        }
        //如果有 下一个排序 修改原有的排序，替换
        if (ObjectUtils.isNotEmpty(sort)) {
            exInfoMapper.updateOldSort(sort, attBlueExceptionInformation.getSort());
            attBlueExceptionInformation.setSort(sort);
            //更新排序字段
            Integer flag = exInfoMapper.updateById(attBlueExceptionInformation);
            return flag > 0;
        }
        return false;
    }
}
