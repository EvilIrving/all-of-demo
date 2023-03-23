package com.ygkj.water.project.service.impl;

import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.ygkj.project.model.BhControlPlan;
import com.ygkj.project.model.ProjectCodeMapping;
import com.ygkj.project.model.SysFile;
import com.ygkj.service.GenerateDistributionIDService;
import com.ygkj.utils.PageUtils;
import com.ygkj.utils.StringUtils;
import com.ygkj.vo.PageVO;
import com.ygkj.water.model.CommonResult;
import com.ygkj.water.project.feign.UserFeignClient;
import com.ygkj.water.project.mapper.BhControlPlanMapper;
import com.ygkj.water.project.mapper.ProjectRelMapper;
import com.ygkj.water.project.service.BhControlPlanService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;

@Service
public class BhControlPlanServiceImpl implements BhControlPlanService {
    @Resource
    private BhControlPlanMapper bhControlPlanMapper;
    @Resource
    private GenerateDistributionIDService idService;
    @Resource
    private ProjectRelMapper projectRelMapper;
    @Resource
    private UserFeignClient userFeignClient;
    @Override
    public void insert(BhControlPlan plan) {
        plan.setId(idService.nextID());
        String pid = projectRelMapper.selectForeignCode(ProjectCodeMapping.builder().type(1)
                .projectType(null)
                .localPrcd(plan.getPid())
                .build());
        if (StringUtils.isNotBlank(pid)){
            plan.setPid(pid);
        }
        bhControlPlanMapper.insert(plan);
    }

    @Override
    public void update(BhControlPlan plan) {
        String pid = projectRelMapper.selectForeignCode(ProjectCodeMapping.builder().type(1)
                .projectType(null)
                .localPrcd(plan.getPid())
                .build());
        if (StringUtils.isNotBlank(pid)){
            plan.setPid(pid);
        }
        bhControlPlanMapper.updateById(plan);
    }

    @Override
    public Object loadPage(BhControlPlan plan) {
        //首先将工程编码替换成标化的
        String pid = projectRelMapper.selectForeignCode(
                ProjectCodeMapping.builder()
                        .type(1)
                        .localPrcd(plan.getPid())
                        .build()
        );
        if (StringUtils.isBlank(pid)) {
            pid = plan.getPid();
        }
        plan.page();
        List<BhControlPlan>  bhControlPlans = bhControlPlanMapper.selectList(Wrappers.lambdaQuery(BhControlPlan.class)
                .eq(StringUtils.isNotBlank(plan.getId()), BhControlPlan::getId, plan.getId())
                .eq(ObjectUtils.isNotEmpty(plan.getYear()), BhControlPlan::getYear, plan.getYear())
                .eq(StringUtils.isNotBlank(plan.getPid()),BhControlPlan::getPid,pid)
                .eq(BhControlPlan::getDelFlag, 0)
                .orderByDesc(BhControlPlan::getCreateTime));
        PageVO<BhControlPlan> pageVO = PageUtils.getPageVO(bhControlPlans);
        List<BhControlPlan> list = pageVO.getList();
        list.forEach(m -> {
            List<SysFile> files = getFileList(m.getFileIds());
            m.setFileList(files);

        });
        pageVO.setList(list);
        return pageVO;
    }

    @Override
    public boolean delete(BhControlPlan plan) {
        int i = bhControlPlanMapper.updateById(plan);
        return i > 0?true:false;
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
