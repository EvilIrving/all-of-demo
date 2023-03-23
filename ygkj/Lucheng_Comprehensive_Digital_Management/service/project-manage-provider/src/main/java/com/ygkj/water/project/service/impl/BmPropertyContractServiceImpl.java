package com.ygkj.water.project.service.impl;

import com.github.pagehelper.PageHelper;
import com.ygkj.jwt.JwtTokenUtil;
import com.ygkj.project.model.BmPropertyContract;
import com.ygkj.project.model.SysFile;
import com.ygkj.project.vo.request.BmPropertyContractReqVo;
import com.ygkj.service.GenerateDistributionIDService;
import com.ygkj.utils.PageUtils;
import com.ygkj.utils.StringUtils;
import com.ygkj.vo.PageVO;
import com.ygkj.water.project.mapper.BmPropertyContractMapper;
import com.ygkj.water.project.mapper.SysFileMapper;
import com.ygkj.water.project.service.BmPropertyContractService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class BmPropertyContractServiceImpl implements BmPropertyContractService {
    @Resource
    private BmPropertyContractMapper contractMapper;
    @Resource
    private GenerateDistributionIDService idService;
    @Resource
    private SysFileMapper sysFileMapper;

    @Override
    public void insert(BmPropertyContractReqVo vo) {
        BmPropertyContract contract = new BmPropertyContract();
        BeanUtils.copyProperties(vo,contract);
        contract.setId(idService.nextID());
        contract.setSourceType("2");
        contract.setCreatorId(JwtTokenUtil.getCurrentUserId().orElse(""));
        contract.setCreateTime(new Date());
        contractMapper.insert(contract);
    }

    @Override
    public void update(BmPropertyContractReqVo vo) {
        BmPropertyContract contract = new BmPropertyContract();
        BeanUtils.copyProperties(vo,contract);
        contract.setUpdatorId(JwtTokenUtil.getCurrentUserId().orElse(""));
        contract.setUpdateTime(new Date());
        contractMapper.updateById(contract);
    }

    @Override
    public Object load(BmPropertyContractReqVo vo) {
        if (vo.isPaged()){
            PageHelper.startPage(vo.getPageNum(),vo.getPageSize());
        }
        List<BmPropertyContractReqVo>  contracts = contractMapper.loadPage(vo);
        PageVO<BmPropertyContractReqVo> pageVO = PageUtils.getPageVO(contracts);
        List<BmPropertyContractReqVo> list = pageVO.getList();
        if (list.size() > 0){
            for (BmPropertyContractReqVo contract : list) {
                if (StringUtils.isNotBlank(contract.getContractFile()) && "2".equals(contract.getSourceType())){
                    List<SysFile> files = new ArrayList<>();
                    SysFile file = sysFileMapper.load(contract.getContractFile());
                    contract.setContractFile(file.getFileUrl());
                    files.add(file);
                    contract.setFiles(files);
                }
            }
        }
        pageVO.setList(list);
        return pageVO;
    }

    @Override
    public void delete(String id) {
        BmPropertyContract contract = new BmPropertyContract();
        contract.setId(id);
        contract.setDelFlag(1);
        contractMapper.updateById(contract);
    }
}
