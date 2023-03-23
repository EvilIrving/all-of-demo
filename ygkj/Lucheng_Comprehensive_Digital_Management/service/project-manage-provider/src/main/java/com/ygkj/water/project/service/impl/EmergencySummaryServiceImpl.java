package com.ygkj.water.project.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.ygkj.project.model.FloodControlAndPresentation;
import com.ygkj.project.model.SysFile;
import com.ygkj.utils.PageUtils;
import com.ygkj.utils.StringUtils;
import com.ygkj.vo.PageVO;
import com.ygkj.water.model.CommonResult;
import com.ygkj.water.project.feign.UserFeignClient;
import com.ygkj.water.project.mapper.FloodControlAndPresentationMapper;
import com.ygkj.water.project.service.EmergencySummaryService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;

@Service
public class EmergencySummaryServiceImpl implements EmergencySummaryService {
    @Resource
    private FloodControlAndPresentationMapper presentationMapper;
    @Resource
    private UserFeignClient userFeignClient;

    @Override
    public Object loadPage(FloodControlAndPresentation entity) {
        entity.page();
        List<FloodControlAndPresentation>  presentations = presentationMapper.selectList(Wrappers.lambdaQuery(FloodControlAndPresentation.class)
                .eq(StringUtils.isNotBlank(entity.getId()), FloodControlAndPresentation::getId, entity.getId())
                .eq(StringUtils.isNotBlank(entity.getType()), FloodControlAndPresentation::getType, entity.getType())
                .likeRight(StringUtils.isNotBlank(entity.getTm()), FloodControlAndPresentation::getTm, entity.getTm())
                .orderByDesc(FloodControlAndPresentation::getTm));
        PageVO<FloodControlAndPresentation> pageVO = PageUtils.getPageVO(presentations);
        List<FloodControlAndPresentation> list = pageVO.getList();
        list.forEach(m -> {
            List<SysFile> files = getFileList(m.getFileId());
            SysFile sysFile = JSONObject.parseObject(JSON.toJSONString(files.get(0)),SysFile.class);
            m.setFile(sysFile);
        });
        pageVO.setList(list);
        return pageVO;
    }

    @Override
    public boolean delete(FloodControlAndPresentation entity) {
        int i = presentationMapper.deleteById(entity.getId());
        return i > 0?true : false;
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
