package com.ygkj.water.project.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.ygkj.project.model.*;
import com.ygkj.project.vo.request.ProjCheckReqVo;
import com.ygkj.project.vo.request.ProjectPatrolReqVo;
import com.ygkj.project.vo.response.BhLgMpFcResVo;
import com.ygkj.project.vo.response.ProjectPatrolResVo;
import com.ygkj.service.GenerateDistributionIDService;
import com.ygkj.utils.CollectionUtils;
import com.ygkj.utils.ExportUtils;
import com.ygkj.utils.PageUtils;
import com.ygkj.utils.StringUtils;
import com.ygkj.vo.PageVO;
import com.ygkj.water.project.mapper.*;
import com.ygkj.water.project.service.ProjCheckService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author lxl
 * @create 2022-04-26 15:30
 * @description 工程检查接口实现
 */
@Service
public class ProjCheckServiceImpl implements ProjCheckService {

    @Resource
    private BhLgMpFcMapper bhLgMpFcMapper;
    @Resource
    private ProjectRelMapper projectRelMapper;
    @Resource
    private BhMapper bhMapper;
    @Resource
    private BhLgMpAcMapper bhLgMpAcMapper;
    @Resource
    private GenerateDistributionIDService idService;
    @Resource
    private SysFileMapper sysFileMapper;
    @Resource
    private BhSlPatrolGisPositionMapper bhSlPatrolGisPositionMapper;
    @Resource
    private BhLgPsSpecialCheckMapper bhLgPsSpecialCheckMapper;
    @Resource
    private BhSysAttachmentMapper bhSysAttachmentMapper;

    @Override
    public PageVO<BhLgMpFcResVo> bhLgMpFcList(ProjCheckReqVo reqVo) {

        //首先将工程编码替换成标化的
        String foreignCode = projectRelMapper.selectForeignCode(
                ProjectCodeMapping.builder()
                        .type(1)
                        .localPrcd(reqVo.getPid())
                        .build()
        );
        if (StringUtils.isBlank(foreignCode)) {
            return PageUtils.getPageVO(Collections.emptyList());
        }
        reqVo.setPid(foreignCode);
        reqVo.page();
        List<BhLgMpFcResVo> resVos = bhLgMpFcMapper.selectBhLgMpFcList(reqVo);
        for (BhLgMpFcResVo resVo : resVos) {
            if (StringUtils.isNotBlank(resVo.getJoinUid())){
                String joinUser = bhMapper.selectUserList(resVo.getJoinUid()).stream().collect(Collectors.joining(","));
                resVo.setJoinUser(joinUser);
            }
            setBhLgMpFcFile(resVo);
        }
        return PageUtils.getPageVO(resVos);
    }


    public void setBhLgMpFcFile(BhLgMpFcResVo resVo){

        if (resVo.getSourceType() == 1){
            resVo.setAidFileList(StringUtils.isNotBlank(resVo.getAid())?
                    bhSysAttachmentMapper.findByAids(resVo.getAid()):Collections.emptyList());
            resVo.setCheckPicList(StringUtils.isNotBlank(resVo.getCheckPic())?
                    bhSysAttachmentMapper.findByAids(resVo.getCheckPic()): Collections.emptyList());
            resVo.setRunActionRecordFiles(StringUtils.isNotBlank(resVo.getRunActionRecord())?
                    bhSysAttachmentMapper.findByAids(resVo.getRunActionRecord()): Collections.emptyList());
            resVo.setProjCheckRecordFiles(StringUtils.isNotBlank(resVo.getProjCheckRecord())?
                    bhSysAttachmentMapper.findByAids(resVo.getProjCheckRecord()): Collections.emptyList());
        }else {
            resVo.setAidFileList(StringUtils.isNotBlank(resVo.getAid())?
                    sysFileMapper.loadByIds(resVo.getAid()): Collections.emptyList());
            resVo.setCheckPicList(StringUtils.isNotBlank(resVo.getCheckPic())?
                    sysFileMapper.loadByIds(resVo.getCheckPic()): Collections.emptyList());
            resVo.setRunActionRecordFiles(StringUtils.isNotBlank(resVo.getRunActionRecord())?
                    sysFileMapper.loadByIds(resVo.getRunActionRecord()): Collections.emptyList());
            resVo.setProjCheckRecordFiles(StringUtils.isNotBlank(resVo.getProjCheckRecord())?
                    sysFileMapper.loadByIds(resVo.getProjCheckRecord()): Collections.emptyList());

        }
    }

    @Override
    public void exportLgMpFc(ProjCheckReqVo reqVo, HttpServletResponse response) {
        reqVo.setPageNum(null);
        List<BhLgMpFcResVo> resVos = bhLgMpFcList(reqVo).getList();
        ExportUtils.exportExcel(resVos,"汛前检查表",
                new String[]{"检查时间","参加人员","隐患数量","问题类型"} ,
                new String[]{"tm","joinUser","hidCount","problemType"} ,response);
    }

    @Override
    public PageVO<ProjectPatrolResVo> dailyCheckList(ProjCheckReqVo reqVo) {

        //首先将工程编码替换成标化的
        String foreignCode = projectRelMapper.selectForeignCode(
                ProjectCodeMapping.builder()
                        .type(1)
                        .localPrcd(reqVo.getPid())
                        .build()
        );
        if (StringUtils.isBlank(foreignCode)) {
            return PageUtils.getPageVO(Collections.emptyList());
        }

        ProjectPatrolReqVo vo = new ProjectPatrolReqVo();
        vo.setProjectCode(foreignCode);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            if (StringUtils.isNotBlank(reqVo.getDate())){
                vo.setEndTime(sdf.parse(reqVo.getDate()));
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        reqVo.page();
        return PageUtils.getPageVO(projectRelMapper.listPatrol(vo));

    }


    @Override
    public void exportDailyCheck(ProjCheckReqVo reqVo, HttpServletResponse response) {

        PageVO<ProjectPatrolResVo> pageVO = dailyCheckList(reqVo);
        List<ProjectPatrolResVo> list = pageVO.getList();

        ExportUtils.exportExcel(list,"日常巡查",
                new String[]{"开始巡查时间","结束巡查时间","巡查人","隐患数量"},
                new String[]{"patrolBegintime","patrolEndtime","realName","hiddenTroubleCount"},response);
    }

    @Override
    public PageVO<BhLgMpAc> bhLgMpAcList(ProjCheckReqVo reqVo) {

        //首先将工程编码替换成标化的
        String foreignCode = projectRelMapper.selectForeignCode(
                ProjectCodeMapping.builder()
                        .type(1)
                        .localPrcd(reqVo.getPid())
                        .build()
        );
        if (StringUtils.isBlank(foreignCode)) {
            return PageUtils.getPageVO(Collections.emptyList());
        }
        reqVo.setPid(foreignCode);
        reqVo.page();
        List<BhLgMpAc> bhLgMpAcs = bhLgMpAcMapper.selectList(Wrappers.lambdaQuery(BhLgMpAc.class)
                .eq(BhLgMpAc::getPid, reqVo.getPid())
                .orderByDesc(BhLgMpAc::getTm));
        for (BhLgMpAc bhLgMpAc : bhLgMpAcs) {
            if (StringUtils.isNotBlank(bhLgMpAc.getCheckUid())){
                String checkUser = bhMapper.selectUserList(bhLgMpAc.getCheckUid()).stream().collect(Collectors.joining(","));
                bhLgMpAc.setCheckUid(checkUser);
            }
            if (StringUtils.isNotBlank(bhLgMpAc.getJoinUid())){
                String joinUser = bhMapper.selectUserList(bhLgMpAc.getJoinUid()).stream().collect(Collectors.joining(","));
                bhLgMpAc.setCheckUid(joinUser);
            }
            setBhLgMpAcFile(bhLgMpAc);
        }
        return PageUtils.getPageVO(bhLgMpAcs);
    }


    @Override
    public void exportBhLgMpAc(ProjCheckReqVo reqVo, HttpServletResponse response) {
        reqVo.setPageNum(null);
        PageVO<BhLgMpAc> pageVO = bhLgMpAcList(reqVo);
        List<BhLgMpAc> list = pageVO.getList();
        ExportUtils.exportExcel(list,"年度检查",
                new String[]{"检查时间","带队检查人","参加人员","隐患数量","问题类型"},
                new String[]{"tm","checkUid","joinUid","hidCount","problemType"},response);

    }

    public void setBhLgMpAcFile(BhLgMpAc bhLgMpAc){

        if (bhLgMpAc.getSourceType() == 1){
            bhLgMpAc.setAidFileList( StringUtils.isNotBlank(bhLgMpAc.getAid())?
                    bhSysAttachmentMapper.findByAids(bhLgMpAc.getAid()):Collections.emptyList());
            bhLgMpAc.setCheckPicList(StringUtils.isNotBlank(bhLgMpAc.getCheckPic())?
                    bhSysAttachmentMapper.findByAids(bhLgMpAc.getCheckPic()): Collections.emptyList());
            bhLgMpAc.setRunActionRecordFiles(StringUtils.isNotBlank(bhLgMpAc.getRunActionRecord())?
                    bhSysAttachmentMapper.findByAids(bhLgMpAc.getRunActionRecord()) : Collections.emptyList());
            bhLgMpAc.setProjCheckRecordFiles(StringUtils.isNotBlank(bhLgMpAc.getProjCheckRecord())?
                    bhSysAttachmentMapper.findByAids(bhLgMpAc.getProjCheckRecord()) : Collections.emptyList());
        }else {
            bhLgMpAc.setAidFileList( StringUtils.isNotBlank(bhLgMpAc.getAid())?
                    sysFileMapper.loadByIds(bhLgMpAc.getAid()):Collections.emptyList());
            bhLgMpAc.setCheckPicList(StringUtils.isNotBlank(bhLgMpAc.getCheckPic())?
                    sysFileMapper.loadByIds(bhLgMpAc.getCheckPic()): Collections.emptyList());
            bhLgMpAc.setRunActionRecordFiles(StringUtils.isNotBlank(bhLgMpAc.getRunActionRecord())?
                    sysFileMapper.loadByIds(bhLgMpAc.getRunActionRecord()) : Collections.emptyList());
            bhLgMpAc.setProjCheckRecordFiles(StringUtils.isNotBlank(bhLgMpAc.getProjCheckRecord())?
                    sysFileMapper.loadByIds(bhLgMpAc.getProjCheckRecord()) : Collections.emptyList());
        }

    }




    @Override
    public Integer insertBhLgMpFc(BhLgMpFc bhLgMpFc) {

        //转为标化的工程
        String foreignCode = projectRelMapper.selectForeignCode(
                ProjectCodeMapping.builder()
                        .type(1)
                        .localPrcd(bhLgMpFc.getPid())
                        .build()
        );
        if (StringUtils.isBlank(foreignCode)) {
            throw new RuntimeException("对应工程在标化平台无关联，无法进行新增");
        }
        bhLgMpFc.setPid(foreignCode);
        //设置数据来源为 鹿城平台
        bhLgMpFc.setSourceType(2);
        bhLgMpFc.setId(idService.nextID());
        return bhLgMpFcMapper.insert(bhLgMpFc);
    }

    @Override
    public Integer updateBhLgMpFc(BhLgMpFc bhLgMpFc) {
        //转为标化的工程
        String foreignCode = projectRelMapper.selectForeignCode(
                ProjectCodeMapping.builder()
                        .type(1)
                        .localPrcd(bhLgMpFc.getPid())
                        .build()
        );
        if (StringUtils.isBlank(foreignCode)) {
            throw new RuntimeException("对应工程在标化平台无关联，无法进行更新");
        }
        bhLgMpFc.setPid(foreignCode);
        return bhLgMpFcMapper.updateById(bhLgMpFc);
    }

    @Override
    public Integer deleteBhLgMpFc(String id) {

        BhLgMpFc bhLgMpFc = bhLgMpFcMapper.selectById(id);
        if (bhLgMpFc.getSourceType() == 1){
            throw new RuntimeException("标化平台数据无法删除");
        }
        return bhLgMpFcMapper.deleteById(id);
    }

    @Override
    public Integer insertBhLgMpAc(BhLgMpAc bhLgMpAc) {
        //转为标化的工程
        String foreignCode = projectRelMapper.selectForeignCode(
                ProjectCodeMapping.builder()
                        .type(1)
                        .localPrcd(bhLgMpAc.getPid())
                        .build()
        );
        if (StringUtils.isBlank(foreignCode)) {
            throw new RuntimeException("对应工程在标化平台无关联，无法进行新增");
        }
        bhLgMpAc.setPid(foreignCode);
        //设置数据来源为 鹿城平台
        bhLgMpAc.setSourceType(2);
        bhLgMpAc.setId(idService.nextID());
        return bhLgMpAcMapper.insert(bhLgMpAc);
    }

    @Override
    public Integer updateBhLgMpAc(BhLgMpAc bhLgMpAc) {
        //转为标化的工程
        String foreignCode = projectRelMapper.selectForeignCode(
                ProjectCodeMapping.builder()
                        .type(1)
                        .localPrcd(bhLgMpAc.getPid())
                        .build()
        );
        if (StringUtils.isBlank(foreignCode)) {
            throw new RuntimeException("对应工程在标化平台无关联，无法进行更新");
        }
        bhLgMpAc.setPid(foreignCode);
        return bhLgMpAcMapper.updateById(bhLgMpAc);
    }

    @Override
    public Integer deleteBhLgMpAc(String id) {
        BhLgMpAc bhLgMpAc = bhLgMpAcMapper.selectById(id);
        if (bhLgMpAc.getSourceType() == 1){
            throw new RuntimeException("标化平台数据无法删除");
        }
        return bhLgMpAcMapper.deleteById(id);
    }


    @Override
    public Map<String,Object> checkTrack(String id) {

        Map<String,Object> result = new HashMap<>(4);
        List<BhSlPatrolGisPosition> positionList = bhSlPatrolGisPositionMapper.selectList(Wrappers.lambdaQuery(BhSlPatrolGisPosition.class)
                .eq(BhSlPatrolGisPosition::getGid, id).orderByAsc(BhSlPatrolGisPosition::getId));
        String positionJsonStr = bhMapper.selectPositionGisInfo(id);
        List<JSONObject> JSON = JSONArray.parseArray(positionJsonStr,JSONObject.class);
        if (CollectionUtils.isNotBlank(JSON)){
            JSONObject start = JSON.get(0);
            JSONObject end = JSON.get(JSON.size() - 1);
            result.put("start",start);
            result.put("end",end);
        }
        result.put("position",JSON);
        result.put("list",positionList);
        return result;
    }


    @Override
    public PageVO<BhLgPsSpecialCheck> specialCheckList(ProjCheckReqVo reqVo) {
        //首先将工程编码替换成标化的
        String foreignCode = projectRelMapper.selectForeignCode(
                ProjectCodeMapping.builder()
                        .type(1)
                        .localPrcd(reqVo.getPid())
                        .build()
        );
        if (StringUtils.isBlank(foreignCode)) {
            return PageUtils.getPageVO(Collections.emptyList());
        }
        reqVo.setPid(foreignCode);
        reqVo.page();
        List<BhLgPsSpecialCheck> checks = bhLgPsSpecialCheckMapper.selectList(Wrappers.lambdaQuery(BhLgPsSpecialCheck.class)
                .likeRight(StringUtils.isNotBlank(reqVo.getDate()), BhLgPsSpecialCheck::getCheckTm, reqVo.getDate())
                .eq(StringUtils.isNotBlank(reqVo.getId()), BhLgPsSpecialCheck::getId, reqVo.getId())
                .eq(BhLgPsSpecialCheck::getPid, reqVo.getPid())
                .orderByDesc(BhLgPsSpecialCheck::getCheckTm));
        for (BhLgPsSpecialCheck check : checks) {
            setBhLgPsSpecialFile(check);
        }
        return PageUtils.getPageVO(checks);
    }


    public void setBhLgPsSpecialFile(BhLgPsSpecialCheck check){
        if (StringUtils.isNotBlank(check.getAid())){
            check.setAidList(sysFileMapper.loadByIds(check.getAid()));
        }
    }


    @Override
    public void exportSpecialCheck(ProjCheckReqVo reqVo, HttpServletResponse response) {
        reqVo.setPageNum(null);
        PageVO<BhLgPsSpecialCheck> pageVO = specialCheckList(reqVo);
        List<BhLgPsSpecialCheck> list = pageVO.getList();
        ExportUtils.exportExcel(list,"特别检查",
                new String[]{"检查时间","检查人员","组织单位","存在问题","问题类型"},
                new String[]{"checkTm","peopleDow","responUnit","checkContent","problemType"},response);
    }

    @Override
    public Integer insertSpecialCheck(BhLgPsSpecialCheck check) {

        //转为标化的工程
        String foreignCode = projectRelMapper.selectForeignCode(
                ProjectCodeMapping.builder()
                        .type(1)
                        .localPrcd(check.getPid())
                        .build()
        );
        if (StringUtils.isBlank(foreignCode)) {
            throw new RuntimeException("对应工程在标化平台无关联，无法进行更新");
        }
        check.setPid(foreignCode);
        //设置数据来源为 鹿城平台
        check.setSourceType(2);
        check.setId(idService.nextID());
        return bhLgPsSpecialCheckMapper.insert(check);
    }

    @Override
    public Integer updateSpecialCheck(BhLgPsSpecialCheck check) {
        //转为标化的工程
        String foreignCode = projectRelMapper.selectForeignCode(
                ProjectCodeMapping.builder()
                        .type(1)
                        .localPrcd(check.getPid())
                        .build()
        );
        if (StringUtils.isBlank(foreignCode)) {
            throw new RuntimeException("对应工程在标化平台无关联，无法进行更新");
        }
        check.setPid(foreignCode);
        return bhLgPsSpecialCheckMapper.updateById(check);
    }

    @Override
    public Integer deleteSpecialCheck(String id) {
        BhLgPsSpecialCheck check = bhLgPsSpecialCheckMapper.selectById(id);
        if (check.getSourceType() == 1){
            throw new RuntimeException("标化平台数据无法删除");
        }
        return bhLgPsSpecialCheckMapper.deleteById(id);
    }
}
