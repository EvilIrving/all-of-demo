package com.ygkj.water.project.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.pagehelper.PageHelper;
import com.ygkj.jwt.JwtTokenUtil;
import com.ygkj.project.model.ConAnnualAcceptance;
import com.ygkj.project.model.SysFile;
import com.ygkj.project.vo.request.ConAnnualAcceptanceReqVo;
import com.ygkj.service.GenerateDistributionIDService;
import com.ygkj.utils.DateUtils;
import com.ygkj.utils.ExportUtils;
import com.ygkj.utils.PageUtils;
import com.ygkj.utils.StringUtils;
import com.ygkj.vo.PageVO;
import com.ygkj.water.model.CommonResult;
import com.ygkj.water.project.feign.UserFeignClient;
import com.ygkj.water.project.mapper.ConAnnualAcceptanceMapper;
import com.ygkj.water.project.service.ConAnnualAcceptanceService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Service
public class ConAnnualAcceptanceServiceImpl implements ConAnnualAcceptanceService {
    @Resource
    private ConAnnualAcceptanceMapper annualAcceptanceMapper;
    @Resource
    private GenerateDistributionIDService idService;
    @Resource
    private UserFeignClient userFeignClient;

    @Override
    public void insert(ConAnnualAcceptanceReqVo vo) {
        ConAnnualAcceptance annualAcceptance = new ConAnnualAcceptance();
        BeanUtils.copyProperties(vo,annualAcceptance);
        annualAcceptance.setId(idService.nextID());
        annualAcceptance.setYear(DateUtils.format(new Date(),"yyyy"));
        annualAcceptance.setCreateId(JwtTokenUtil.getCurrentUserId().orElse(""));
        annualAcceptanceMapper.insert(annualAcceptance);
    }

    @Override
    public void update(ConAnnualAcceptanceReqVo vo) {
        ConAnnualAcceptance annualAcceptance = new ConAnnualAcceptance();
        BeanUtils.copyProperties(vo,annualAcceptance);
        annualAcceptance.setUpdateId(JwtTokenUtil.getCurrentUserId().orElse(""));
        annualAcceptanceMapper.updateById(annualAcceptance);
    }

    @Override
    public Object load(ConAnnualAcceptanceReqVo vo) {
        if (vo.isPaged()){
            PageHelper.startPage(vo.getPageNum(),vo.getPageSize());
        }
        List<ConAnnualAcceptance>  annualAcceptances = annualAcceptanceMapper.selectList(Wrappers.lambdaQuery(ConAnnualAcceptance.class)
                .eq(StringUtils.isNotBlank(vo.getId()), ConAnnualAcceptance::getId, vo.getId())
                .eq(StringUtils.isNotBlank(vo.getYear()), ConAnnualAcceptance::getYear, vo.getYear())
                .eq(StringUtils.isNotBlank(vo.getPropertyUnitId()), ConAnnualAcceptance::getPropertyUnitId, vo.getPropertyUnitId())
                .eq(ConAnnualAcceptance::getDelFlag, 0)
                .orderByDesc(ConAnnualAcceptance::getCreateTime));
        PageVO<ConAnnualAcceptance> pageVO = PageUtils.getPageVO(annualAcceptances);
        List<ConAnnualAcceptance> list = pageVO.getList();
        list.forEach(m -> {
            List<SysFile> fileList = getFileList(m.getAppendix());
            m.setFiles(fileList);
        });
        pageVO.setList(list);
        return pageVO;
    }

    @Override
    public int delete(String id) {
        ConAnnualAcceptance annualAcceptance = new ConAnnualAcceptance();
        annualAcceptance.setId(id);
        annualAcceptance.setDelFlag(1);
        return annualAcceptanceMapper.updateById(annualAcceptance);
    }

    @Override
    public void exportAnnualAcceptance(ConAnnualAcceptanceReqVo vo, HttpServletResponse response) {
        List<ConAnnualAcceptance> list = annualAcceptanceMapper.selectList(Wrappers.lambdaQuery(ConAnnualAcceptance.class)
                .eq(StringUtils.isNotBlank(vo.getYear()), ConAnnualAcceptance::getYear, vo.getYear())
                .eq(ConAnnualAcceptance::getDelFlag, 0));
        ExportUtils.exportExcel(list,"年度验收",
                new String[]{"物业管理单位名称","运行管理单位","验收时间","验收意见","水行政主管部门","验收时间","验收意见"},
                new String[]{"propertyUnitName","runUnitName","runAcceptanceTime","runAcceptanceComments","waterAdministrationUnitName","waterAcceptanceTime","waterAcceptanceComments"},response);
    }

    private List<SysFile> getFileList(String fileIds){
        CommonResult load = userFeignClient.load(fileIds);
        if (CommonResult.success().getCode() == load.getCode()){
            List<SysFile>  files = (List<SysFile>) load.getData();
            return files;
        }else {
            return Arrays.asList();
        }
    }
}
