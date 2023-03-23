package com.ygkj.water.project.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.pagehelper.PageHelper;
import com.ygkj.project.model.BhDepPersontrain;
import com.ygkj.project.model.ProjectCodeMapping;
import com.ygkj.project.model.SysFile;
import com.ygkj.project.vo.request.BhDepPersontrainReqVo;
import com.ygkj.service.GenerateDistributionIDService;
import com.ygkj.utils.PageUtils;
import com.ygkj.utils.StringUtils;
import com.ygkj.vo.PageVO;
import com.ygkj.water.model.CommonResult;
import com.ygkj.water.project.feign.UserFeignClient;
import com.ygkj.water.project.mapper.BhDepPersontrainMapper;
import com.ygkj.water.project.mapper.BhSysAttachmentMapper;
import com.ygkj.water.project.mapper.ProjectRelMapper;
import com.ygkj.water.project.service.BhDepPersontrainService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;

@Service
public class BhDepPersontrainServiceImpl implements BhDepPersontrainService {
    @Resource
    private BhDepPersontrainMapper persontrainMapper;
    @Resource
    private GenerateDistributionIDService idService;
    @Resource
    private ProjectRelMapper projectRelMapper;
    @Resource
    private UserFeignClient userFeignClient;
    @Resource
    private BhSysAttachmentMapper bhSysAttachmentMapper;
    @Override
    public void insert(BhDepPersontrainReqVo vo) {
        BhDepPersontrain persontrain = new BhDepPersontrain();
        BeanUtils.copyProperties(vo,persontrain);
        persontrain.setSourceType(2);
        persontrain.setId(idService.nextID());
        String pjcd = projectRelMapper.selectForeignCode(ProjectCodeMapping.builder().type(1)
                .projectType(null)
                .localPrcd(vo.getPjcd())
                .build());
        if (StringUtils.isNotBlank(pjcd)){
            persontrain.setPjcd(pjcd);
        }
        persontrainMapper.insert(persontrain);
    }

    @Override
    public void update(BhDepPersontrainReqVo vo) {
        if ("1".equals(String.valueOf(vo.getSourceType()))){
            throw new RuntimeException("标化同步数据无法修改");
        }
        BhDepPersontrain persontrain = new BhDepPersontrain();
        BeanUtils.copyProperties(vo,persontrain);
        persontrainMapper.updateById(persontrain);
    }

    @Override
    public Object load(BhDepPersontrainReqVo vo) {
        //首先将工程编码替换成标化的
        String pjcd = projectRelMapper.selectForeignCode(
                ProjectCodeMapping.builder().type(1)
                .projectType(null)
                .localPrcd(vo.getPjcd())
                .build());
        pjcd = pjcd == null?vo.getPjcd():pjcd;
        if (vo.isPaged()){
            PageHelper.startPage(vo.getPageNum(),vo.getPageSize());
        }
        List<BhDepPersontrain>  bhLgLmrDisDoms = persontrainMapper.selectList(Wrappers.lambdaQuery(BhDepPersontrain.class)
                .eq(StringUtils.isNotBlank(vo.getId()), BhDepPersontrain::getId, vo.getId())
                .eq(StringUtils.isNotBlank(vo.getPjcd()), BhDepPersontrain::getPjcd, pjcd)
                .like(StringUtils.isNotBlank(vo.getPerson()), BhDepPersontrain::getPerson, vo.getPerson())
                .eq(BhDepPersontrain::getDelFlag, 0)
                .orderByDesc(BhDepPersontrain::getTime));
        PageVO<BhDepPersontrain> pageVO = PageUtils.getPageVO(bhLgLmrDisDoms);
        List<BhDepPersontrain> list = pageVO.getList();
        setFile(list);
        pageVO.setList(list);
        return pageVO;
    }

    @Override
    public void delete(String id) {
        BhDepPersontrain persontrain = new BhDepPersontrain();
        persontrain.setId(id);
        persontrain.setDelFlag(1);
        persontrain.setSourceType(2);
        persontrainMapper.updateById(persontrain);
    }


    private void setFile(List<BhDepPersontrain> list){
        list.forEach(m -> {
            if ("2".equals(String.valueOf(m.getSourceType()))){
                List<SysFile> aidFiles = getFileList(m.getAid());
                m.setFiles(aidFiles);
            }else {
                if (StringUtils.isNotBlank(m.getAid())){
                    List<SysFile> byAids = bhSysAttachmentMapper.findByAids(m.getAid());
                    m.setFiles(byAids);
                }
            }
        });
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
