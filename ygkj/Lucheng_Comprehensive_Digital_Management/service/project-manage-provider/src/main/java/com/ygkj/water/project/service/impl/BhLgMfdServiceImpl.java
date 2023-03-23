package com.ygkj.water.project.service.impl;

import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.pagehelper.PageHelper;
import com.ygkj.jwt.JwtTokenUtil;
import com.ygkj.project.model.BhLgMfd;
import com.ygkj.project.model.ProjectCodeMapping;
import com.ygkj.project.model.SysFile;
import com.ygkj.project.vo.request.BhLgMfdReqVo;
import com.ygkj.service.GenerateDistributionIDService;
import com.ygkj.utils.ExportUtils;
import com.ygkj.utils.PageUtils;
import com.ygkj.utils.StringUtils;
import com.ygkj.vo.PageVO;
import com.ygkj.water.model.CommonResult;
import com.ygkj.water.project.feign.UserFeignClient;
import com.ygkj.water.project.mapper.BhLgMfdServiceMapper;
import com.ygkj.water.project.mapper.BhSysAttachmentMapper;
import com.ygkj.water.project.mapper.ProjectRelMapper;
import com.ygkj.water.project.service.BhLgMfdService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;

@Service
public class BhLgMfdServiceImpl implements BhLgMfdService {
    @Resource
    private BhLgMfdServiceMapper mfdServiceMapper;
    @Resource
    private GenerateDistributionIDService idService;
    @Resource
    private ProjectRelMapper projectRelMapper;
    @Resource
    private UserFeignClient userFeignClient;
    @Resource
    private BhSysAttachmentMapper bhSysAttachmentMapper;


    @Override
    public int insert(BhLgMfdReqVo vo) {
        BhLgMfd bhLgMfd = new BhLgMfd();
        BeanUtils.copyProperties(vo,bhLgMfd);
        bhLgMfd.setId(idService.nextID());
        bhLgMfd.setSourceType(2);
        bhLgMfd.setCreateId(JwtTokenUtil.getCurrentUserId().orElse(""));
        String pid = projectRelMapper.selectForeignCode(ProjectCodeMapping.builder().type(1)
                .projectType(null)
                .localPrcd(vo.getPid())
                .build());
        if (StringUtils.isNotBlank(pid)){
            bhLgMfd.setPid(pid);
        }
        return mfdServiceMapper.insert(bhLgMfd);
    }

    @Override
    public int update(BhLgMfdReqVo vo) {
        if ("1".equals(String.valueOf(vo.getSourceType()))){
            throw new RuntimeException("标化同步数据无法修改");
        }
        BhLgMfd bhLgMfd = new BhLgMfd();
        BeanUtils.copyProperties(vo,bhLgMfd);
        bhLgMfd.setUpdateId(JwtTokenUtil.getCurrentUserId().orElse(""));
        return mfdServiceMapper.updateById(bhLgMfd);
    }

    @Override
    public int delete(String id) {
        BhLgMfd mfd = mfdServiceMapper.selectById(id);
        if (mfd.getSourceType() == 1){
            throw new RuntimeException("标化同步数据无法删除");
        }
        BhLgMfd bhLgMfd = new BhLgMfd();
        bhLgMfd.setDelFlag(1);
        bhLgMfd.setId(id);
//        bhLgMfd.setSourceType(2);
        return mfdServiceMapper.updateById(bhLgMfd);
    }

    @Override
    public Object loadPage(BhLgMfdReqVo vo) {

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
        if (vo.isPaged()){
            PageHelper.startPage(vo.getPageNum(),vo.getPageSize());
        }
        List<BhLgMfd>  bhLgMfds = mfdServiceMapper.selectList(Wrappers.lambdaQuery(BhLgMfd.class)
                .eq(StringUtils.isNotBlank(vo.getId()), BhLgMfd::getId, vo.getId())
                .eq(ObjectUtils.isNotEmpty(vo.getYear()), BhLgMfd::getYear, vo.getYear())
                .eq(StringUtils.isNotBlank(vo.getPid()),BhLgMfd::getPid,foreignCode)
                .eq(StringUtils.isNotBlank(vo.getApprovalUnit()),BhLgMfd::getApprovalUnit,vo.getApprovalUnit())
                .eq(BhLgMfd::getDelFlag, 0).orderByDesc(BhLgMfd::getYear));
        PageVO<BhLgMfd> pageVO = PageUtils.getPageVO(bhLgMfds);
        List<BhLgMfd> list = pageVO.getList();
        //添加文件路径
        setFile(list);
        pageVO.setList(list);
        return pageVO;
    }

    @Override
    public void exportMtFund(BhLgMfdReqVo vo, HttpServletResponse response) {
        List<BhLgMfd>  list = mfdServiceMapper.selectList(Wrappers.lambdaQuery(BhLgMfd.class)
                .eq(ObjectUtils.isNotEmpty(vo.getYear()), BhLgMfd::getYear, vo.getYear())
                .eq(BhLgMfd::getDelFlag, 0));
        ExportUtils.exportExcel(list,"维修养护资金",
                new String[]{"年份","申报单位","申报时间","批复单位","批复时间","申报资金(万元)","上级下达的年度维修养护资金(万元)","自筹资金(万元)","已使用资金(万元)","已支付资金(万元)"},
                new String[]{"year","declarationUnit","declarationTime","approvalUnit","approvalTime","dclrFunds","superiorfunds","selffunds","usedamount","paidamount"},response);
    }


    private void setFile(List<BhLgMfd> list){

        list.forEach(m -> {
            if ("2".equals(String.valueOf(m.getSourceType()))){
                List<SysFile> aidFiles = getFileList(m.getAid());
                m.setAidFiles(aidFiles);
                List<SysFile> mtnPlanFiles = getFileList(m.getMtnPlan());
                m.setMtnPlanFiles(mtnPlanFiles);
            }else {
                if (StringUtils.isNotBlank(m.getAid())){
                    List<SysFile> byAids = bhSysAttachmentMapper.findByAids(m.getAid());
                    m.setAidFiles(byAids);
                }
                if (StringUtils.isNotBlank(m.getMtnPlan())){
                    List<SysFile> byAids = bhSysAttachmentMapper.findByAids(m.getMtnPlan());
                    m.setMtnPlanFiles(byAids);
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
