package com.ygkj.water.project.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.pagehelper.PageHelper;
import com.ygkj.jwt.JwtTokenUtil;
import com.ygkj.project.model.ConCovenant;
import com.ygkj.project.model.SysFile;
import com.ygkj.project.vo.request.ConCovenantReqVo;
import com.ygkj.service.GenerateDistributionIDService;
import com.ygkj.utils.PageUtils;
import com.ygkj.utils.StringUtils;
import com.ygkj.vo.PageVO;
import com.ygkj.water.model.CommonResult;
import com.ygkj.water.project.feign.UserFeignClient;
import com.ygkj.water.project.mapper.ConCovenantMapper;
import com.ygkj.water.project.service.ConCovenantService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;

@Service
public class ConCovenantServiceImpl implements ConCovenantService {
    @Resource
    private ConCovenantMapper covenantMapper;
    @Resource
    private GenerateDistributionIDService idService;
    @Resource
    private UserFeignClient userFeignClient;

    @Override
    public void insert(ConCovenantReqVo vo) {
        ConCovenant covenant = new ConCovenant();
        BeanUtils.copyProperties(vo,covenant);
        covenant.setId(idService.nextID());
        covenant.setCreateId(JwtTokenUtil.getCurrentUserId().orElse(""));
        covenantMapper.insert(covenant);
    }

    @Override
    public void update(ConCovenantReqVo vo) {
        ConCovenant covenant = new ConCovenant();
        BeanUtils.copyProperties(vo,covenant);
        covenant.setUpdateId(JwtTokenUtil.getCurrentUserId().orElse(""));
        covenantMapper.updateById(covenant);
    }

    @Override
    public Object load(ConCovenantReqVo vo) {
        if (vo.isPaged()){
            PageHelper.startPage(vo.getPageNum(),vo.getPageSize());
        }
        List<ConCovenant>  covenants = covenantMapper.selectList(Wrappers.lambdaQuery(ConCovenant.class)
                .eq(StringUtils.isNotBlank(vo.getId()), ConCovenant::getId, vo.getId())
                .like(StringUtils.isNotBlank(vo.getPropertyUnitName()), ConCovenant::getPropertyUnitName, vo.getPropertyUnitName())
                .eq(StringUtils.isNotBlank(vo.getPropertyUnitId()), ConCovenant::getPropertyUnitId, vo.getPropertyUnitId())
                .ge(StringUtils.isNotBlank(vo.getStartTime()), ConCovenant::getServiceStartTime, vo.getStartTime())
                .le(StringUtils.isNotBlank(vo.getEndTime()), ConCovenant::getServiceEndTime, vo.getEndTime())
                .eq(ConCovenant::getDelFlag, 0)
                .orderByDesc(ConCovenant::getCreateTime));
        PageVO<ConCovenant> pageVO = PageUtils.getPageVO(covenants);
        List<ConCovenant> list = pageVO.getList();
        list.forEach(m -> {
            List<SysFile> fileList = getFileList(m.getFile());
            m.setFiles(fileList);
        });
        pageVO.setList(list);
        return pageVO;
    }

    @Override
    public void delete(String id) {
        ConCovenant covenant = new ConCovenant();
        covenant.setId(id);
        covenant.setDelFlag(1);
        covenantMapper.updateById(covenant);
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
