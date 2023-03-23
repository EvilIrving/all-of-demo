package com.ygkj.water.project.service.impl;

import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.pagehelper.PageHelper;
import com.ygkj.project.model.BhLgLmrDisDom;
import com.ygkj.project.model.ProjectCodeMapping;
import com.ygkj.project.model.SysFile;
import com.ygkj.project.vo.request.BhLgLmrDisDomReqVo;
import com.ygkj.service.GenerateDistributionIDService;
import com.ygkj.utils.ExportUtils;
import com.ygkj.utils.PageUtils;
import com.ygkj.utils.StringUtils;
import com.ygkj.vo.PageVO;
import com.ygkj.water.model.CommonResult;
import com.ygkj.water.project.feign.UserFeignClient;
import com.ygkj.water.project.mapper.BhLgLmrDisDomMapper;
import com.ygkj.water.project.mapper.ProjectRelMapper;
import com.ygkj.water.project.service.BhLgLmrDisDomService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
public class BhLgLmrDisDomServiceImpl implements BhLgLmrDisDomService {
    @Resource
    private BhLgLmrDisDomMapper disDomMapper;
    @Resource
    private GenerateDistributionIDService idService;
    @Resource
    private ProjectRelMapper projectRelMapper;
    @Resource
    private UserFeignClient userFeignClient;

    @Override
    public int insert(BhLgLmrDisDomReqVo vo) {
        BhLgLmrDisDom disDom = new BhLgLmrDisDom();
        BeanUtils.copyProperties(vo,disDom);
        disDom.setSourceType(2);
        disDom.setId(idService.nextID());
        String pid = projectRelMapper.selectForeignCode(ProjectCodeMapping.builder().type(1)
                .projectType(null)
                .localPrcd(vo.getPid())
                .build());
        if (StringUtils.isNotBlank(pid)){
            disDom.setPid(pid);
        }
        return disDomMapper.insert(disDom);
    }

    @Override
    public int update(BhLgLmrDisDomReqVo vo) {
        if ("1".equals(String.valueOf(vo.getSourceType()))){
            throw new RuntimeException("标化同步数据无法修改");
        }
        BhLgLmrDisDom disDom = new BhLgLmrDisDom();
        BeanUtils.copyProperties(vo,disDom);
        return disDomMapper.updateById(disDom);
    }

    @Override
    public int delete(String id) {
        BhLgLmrDisDom bhLgLmrDisDom = disDomMapper.selectById(id);
        if (bhLgLmrDisDom.getSourceType() == 1){
            throw new RuntimeException("标化同步数据无法删除");
        }
        BhLgLmrDisDom disDom = new BhLgLmrDisDom();
        disDom.setId(id);
        disDom.setDelFlag(1);
//        disDom.setSourceType(2);
        return disDomMapper.updateById(disDom);
    }

    @Override
    public Object loadPage(BhLgLmrDisDomReqVo vo) {
        //首先将工程编码替换成标化的
        String foreignCode = projectRelMapper.selectForeignCode(
                ProjectCodeMapping.builder()
                        .type(1)
                        .localPrcd(vo.getPid())
                        .build()
        );
        if (StringUtils.isBlank(foreignCode)) {
            foreignCode = vo.getPid();
        }
        vo.page();
        List<BhLgLmrDisDom>  bhLgLmrDisDoms = disDomMapper.selectList(Wrappers.lambdaQuery(BhLgLmrDisDom.class)
                .eq(StringUtils.isNotBlank(vo.getId()), BhLgLmrDisDom::getId, vo.getId())
                .eq(ObjectUtils.isNotEmpty(vo.getDYear()), BhLgLmrDisDom::getdYear, vo.getDYear())
                .eq(ObjectUtils.isNotEmpty(vo.getDomType()), BhLgLmrDisDom::getDomType, vo.getDomType())
                .eq(StringUtils.isNotBlank(vo.getPid()),BhLgLmrDisDom::getPid,foreignCode)
                .eq(BhLgLmrDisDom::getDelFlag, 0)
                .orderByDesc(BhLgLmrDisDom::getTm));
        PageVO<BhLgLmrDisDom> pageVO = PageUtils.getPageVO(bhLgLmrDisDoms);
        List<BhLgLmrDisDom> list = pageVO.getList();
        list.forEach(m -> {
            if ("2".equals(String.valueOf(m.getSourceType()))){
                List<SysFile> aidFiles = getFileList(m.getUploadfile());
                m.setUploadFiles(aidFiles);
            }
        });
        pageVO.setList(list);
        return pageVO;
    }

    @Override
    public void exportDisDom(BhLgLmrDisDomReqVo vo, HttpServletResponse response) {
        List<BhLgLmrDisDom>  list = disDomMapper.selectList(Wrappers.lambdaQuery(BhLgLmrDisDom.class)
                .eq(ObjectUtils.isNotEmpty(vo.getDYear()), BhLgLmrDisDom::getdYear, vo.getDYear())
                .eq(ObjectUtils.isNotEmpty(vo.getDomType()), BhLgLmrDisDom::getDomType, vo.getDomType())
                .eq(BhLgLmrDisDom::getDelFlag, 0));
        list.forEach(m -> {
            if ("1".equals(m.getDomType())){
                m.setDomType("上级调度");
            }
            if ("2".equals(m.getDomType())){
                m.setDomType("计划内调度");
            }
        });
        ExportUtils.exportExcel(list,"调度运行",
                new String[]{"年份","调度下达时间","上传人","调度类型","调度编号","调令闸门开启时间","调令闸门关闭时间"},
                new String[]{"dYear","tm","uploaderName","domType","sOrderCode","gateOpenTm","gateCloseTm"},response);
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
