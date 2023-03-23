package com.ygkj.water.project.service.impl;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.ygkj.jwt.JwtTokenUtil;
import com.ygkj.project.model.BhLgMpAf;
import com.ygkj.project.model.BhLgMpSp;
import com.ygkj.project.vo.request.BhLgMpAfReqVo;
import com.ygkj.project.vo.request.BhLgMpSpReqVo;
import com.ygkj.project.vo.request.ProjCheckReqVo;
import com.ygkj.service.GenerateDistributionIDService;
import com.ygkj.utils.ExportUtils;
import com.ygkj.utils.PageUtils;
import com.ygkj.vo.PageVO;
import com.ygkj.water.project.mapper.BhLgMpAfMapper;
import com.ygkj.water.project.mapper.BhLgMpSpMapper;
import com.ygkj.water.project.mapper.SysFileMapper;
import com.ygkj.water.project.service.LcProjCheckService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;

/**
 * @author lxl
 * @create 2022-04-28 9:35
 * @description
 */
@Service
public class LcProjCheckServiceImpl implements LcProjCheckService {

    @Resource
    private BhLgMpAfMapper bhLgMpAfMapper;

    @Resource
    private BhLgMpSpMapper bhLgMpSpMapper;
    @Resource
    private GenerateDistributionIDService idService;

    @Resource
    private SysFileMapper sysFileMapper;



    @Override
    public PageVO<BhLgMpAf> mpAfList(ProjCheckReqVo reqVo) {

        reqVo.page();
        List<BhLgMpAf> bhLgMpAfs = bhLgMpAfMapper.selectList(Wrappers.lambdaQuery(BhLgMpAf.class)
                .eq(BhLgMpAf::getDelFlag, 0)
                .likeRight(StringUtils.isNotBlank(reqVo.getDate()), BhLgMpAf::getTm, reqVo.getDate())
                .eq(StringUtils.isNotBlank(reqVo.getPid()), BhLgMpAf::getPid, reqVo.getPid())
                .eq(StringUtils.isNotBlank(reqVo.getId()), BhLgMpAf::getId, reqVo.getId())
                .like(StringUtils.isNotBlank(reqVo.getName()), BhLgMpAf::getJoinUser, reqVo.getName())
                .orderByDesc(BhLgMpAf::getTm));
        for (BhLgMpAf bhLgMpAf : bhLgMpAfs) {
            setBhLgMpAfFile(bhLgMpAf);
        }
        return PageUtils.getPageVO(bhLgMpAfs);
    }


    public void setBhLgMpAfFile(BhLgMpAf bhLgMpAf){
        if (StringUtils.isNotBlank(bhLgMpAf.getCheckPic())){
            bhLgMpAf.setCheckPicList(sysFileMapper.loadByIds(bhLgMpAf.getCheckPic()));
        }
        if (StringUtils.isNotBlank(bhLgMpAf.getFileIds())){
            bhLgMpAf.setFileList(sysFileMapper.loadByIds(bhLgMpAf.getFileIds()));
        }
    }


    @Override
    public Integer mpAfInsert(BhLgMpAfReqVo bhLgMpAfReqVo) {
        BhLgMpAf bhLgMpAf = new BhLgMpAf();
        BeanUtils.copyProperties(bhLgMpAfReqVo, bhLgMpAf);
        bhLgMpAf.setCreateId(JwtTokenUtil.getCurrentUserId().orElse(""));
        bhLgMpAf.setCreateTime(new Date());
        bhLgMpAf.setId(idService.nextID());
        return bhLgMpAfMapper.insert(bhLgMpAf);
    }

    @Override
    public Integer mpAfUpdate(BhLgMpAfReqVo bhLgMpAf) {

        BhLgMpAf af = new BhLgMpAf();
        BeanUtils.copyProperties(bhLgMpAf, af);
        af.setUpdateId(JwtTokenUtil.getCurrentUserId().orElse(""));
        return bhLgMpAfMapper.updateById(af);

    }

    @Override
    public Integer mpAfDelete(String id) {
        return bhLgMpAfMapper.deleteByAfId(id);
    }

    @Override
    public void mpAfExport(ProjCheckReqVo reqVo, HttpServletResponse response) {
        reqVo.setPageNum(null);
        PageVO<BhLgMpAf> pageVO = mpAfList(reqVo);
        List<BhLgMpAf> list = pageVO.getList();
        ExportUtils.exportExcel(list,"汛后检查表",
                new String[]{"检查时间","参加人员","水管单位负责人","隐患数量","问题类型"},
                new String[]{"tm","joinUser","responseUser","hitCount","problemType"},response);
    }


    @Override
    public PageVO<BhLgMpSp> mpSpList(ProjCheckReqVo reqVo) {

        reqVo.page();
        List<BhLgMpSp> bhLgMpSps = bhLgMpSpMapper.selectList(Wrappers.lambdaQuery(BhLgMpSp.class)
                .eq(BhLgMpSp::getDelFlag, 0)
                .likeRight(StringUtils.isNotBlank(reqVo.getDate()), BhLgMpSp::getTm, reqVo.getDate())
                .eq(StringUtils.isNotBlank(reqVo.getPid()), BhLgMpSp::getPid, reqVo.getPid())
                .eq(StringUtils.isNotBlank(reqVo.getId()), BhLgMpSp::getId, reqVo.getId())
                .like(StringUtils.isNotBlank(reqVo.getName()), BhLgMpSp::getCheckUser, reqVo.getName()));
        for (BhLgMpSp bhLgMpSp : bhLgMpSps) {
            setMpSpFile(bhLgMpSp);
        }
        return PageUtils.getPageVO(bhLgMpSps);
    }


    public void setMpSpFile(BhLgMpSp bhLgMpSp){
           if (StringUtils.isNotBlank(bhLgMpSp.getCheckPic())){
               bhLgMpSp.setCheckPicList(sysFileMapper.loadByIds(bhLgMpSp.getCheckPic()));
           }
           if (StringUtils.isNotBlank(bhLgMpSp.getFileIds())){
               bhLgMpSp.setFileList(sysFileMapper.loadByIds(bhLgMpSp.getFileIds()));
           }
    }


    @Override
    public Integer mpSpInsert(BhLgMpSpReqVo bhLgMpSp) {
        BhLgMpSp sp = new BhLgMpSp();
        BeanUtils.copyProperties(bhLgMpSp, sp);
        sp.setCreateId(JwtTokenUtil.getCurrentUserId().orElse(""));
        sp.setCreateTime(new Date());
        sp.setId(idService.nextID());
        return bhLgMpSpMapper.insert(sp);
    }

    @Override
    public Integer mpSpUpdate(BhLgMpSpReqVo bhLgMpSp) {
        BhLgMpSp sp = new BhLgMpSp();
        BeanUtils.copyProperties(bhLgMpSp, sp);
        sp.setUpdateId(JwtTokenUtil.getCurrentUserId().orElse(""));
        return bhLgMpSpMapper.updateById(sp);

    }

    @Override
    public Integer mpSpDelete(String id) {
        return bhLgMpSpMapper.deleteBySpId(id);
    }



    @Override
    public void mpSpExport(ProjCheckReqVo reqVo, HttpServletResponse response) {
        reqVo.setPageNum(null);
        PageVO<BhLgMpSp> pageVO = mpSpList(reqVo);
        List<BhLgMpSp> list = pageVO.getList();
        ExportUtils.exportExcel(list,"专项检查",
                new String[]{"检查时间","检查人员","组织单位","存在问题","问题类型"},
                new String[]{"tm","checkUser","unitName","problemDesc","problemType"},response);
    }
}
