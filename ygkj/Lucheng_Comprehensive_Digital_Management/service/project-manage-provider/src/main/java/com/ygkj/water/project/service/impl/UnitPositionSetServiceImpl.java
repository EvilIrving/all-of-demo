package com.ygkj.water.project.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageHelper;
import com.ygkj.gragh.model.AttAdcdBase;
import com.ygkj.project.model.BmProject;
import com.ygkj.project.model.SysFile;
import com.ygkj.project.model.UnitPositionSet;
import com.ygkj.project.model.UnitPositionSetBean;
import com.ygkj.project.vo.request.UnitPositionSetReqVo;
import com.ygkj.service.GenerateDistributionIDService;
import com.ygkj.utils.PageUtils;
import com.ygkj.utils.StringUtils;
import com.ygkj.vo.PageVO;
import com.ygkj.water.project.mapper.AttAdcdBaseMapper;
import com.ygkj.water.project.mapper.ProjectMapper;
import com.ygkj.water.project.mapper.SysFileMapper;
import com.ygkj.water.project.mapper.UnitPositionSetMapper;
import com.ygkj.water.project.service.UnitPositionSetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class UnitPositionSetServiceImpl implements UnitPositionSetService {
    @Autowired
    private UnitPositionSetMapper positionSetMapper;
    @Autowired
    private GenerateDistributionIDService idService;
    @Resource
    private SysFileMapper sysFileMapper;
    @Autowired
    private AttAdcdBaseMapper attAdcdBaseMapper;
    @Autowired
    private ProjectMapper projectMapper;

    @Override
    public int save(UnitPositionSet entity) {
        entity.setId(idService.nextID());
        return positionSetMapper.insert(entity);
    }

    @Override
    public int update(UnitPositionSet entity) {
        return positionSetMapper.updateById(entity);
    }

    @Override
    public PageVO<UnitPositionSet> queryPositions(UnitPositionSetReqVo vo) {
        if (vo.isPaged()) {
            PageHelper.startPage(vo.getPageNum(), vo.getPageSize());
        }
        List<UnitPositionSet> unitPositionSets = positionSetMapper.queryList(vo);
        PageVO<UnitPositionSet> pageVO = PageUtils.getPageVO(unitPositionSets);
        List<UnitPositionSet> records = pageVO.getList();
        for (UnitPositionSet record : records) {
            if (StringUtils.isNotBlank(record.getJsonarray())){
                List<UnitPositionSetBean> unitPositionSetBeans = JSONArray.parseArray(record.getJsonarray(), UnitPositionSetBean.class);
                for (UnitPositionSetBean bean : unitPositionSetBeans) {
                    if ("单位负责人".equals(bean.getLeaderPost())){
                        record.setUnitLeader(bean.getLeader());
                        record.setUnitLeaderPhone(bean.getLeaderPhone());
                    }
                    if ("技术负责人".equals(bean.getLeaderPost())){
                        record.setTechnicalLeader(bean.getLeader());
                        record.setTechnicalLeaderPhone(bean.getLeaderPhone());
                    }
                    if ("财务负责人".equals(bean.getLeaderPost())){
                        record.setFinancialLeader(bean.getLeader());
                        record.setFinancialLeaderPhone(bean.getLeaderPhone());
                    }
                    if ("巡查负责人".equals(bean.getLeaderPost())){
                        record.setPatrolLeader(bean.getLeader());
                        record.setPatrolLeaderPhone(bean.getLeaderPhone());
                    }
                    if ("安全负责人".equals(bean.getLeaderPost())){
                        record.setSafetyLeader(bean.getLeader());
                        record.setSafetyLeaderPhone(bean.getLeaderPhone());
                    }
                }

            }
        }
        pageVO.setList(records);
        return pageVO;
    }

    @Override
    public UnitPositionSet queryPosition(UnitPositionSetReqVo vo) {
        QueryWrapper<UnitPositionSet> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id",vo.getId());
        UnitPositionSet unitPositionSet = positionSetMapper.selectOne(queryWrapper);
        if (StringUtils.isNotBlank(unitPositionSet.getJsonarray())){
            List<UnitPositionSetBean> unitPositionSetBeans = JSON.parseArray(unitPositionSet.getJsonarray(), UnitPositionSetBean.class);

            for (UnitPositionSetBean unitPositionSetBean : unitPositionSetBeans) {
                if (StringUtils.isNotBlank(unitPositionSetBean.getLeaderCertificatefile())){
                    SysFile file = sysFileMapper.load(unitPositionSetBean.getLeaderCertificatefile());
                    unitPositionSetBean.setLeaderCertificatefileUrl(file.getFileUrl());
                    unitPositionSetBean.setLeaderCertificatefileName(file.getFileName());
                }
            }
            unitPositionSet.setArray(unitPositionSetBeans);
        }

        return unitPositionSet;
    }

    @Override
    public int delPosition(String id) {
        UnitPositionSet entity = new UnitPositionSet();
        entity.setId(id);
        entity.setDelFlag("1");
        return positionSetMapper.updateById(entity);
    }

    @Override
    public List<AttAdcdBase> selectLCArea(){
        return attAdcdBaseMapper.selectLCArea();
    }

    @Override
    public List<BmProject> selectProjects() {
        return projectMapper.selectAll();
    }
}
