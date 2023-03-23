package com.ygkj.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ygkj.auth.VO.request.AchievementFilePageQueryVO;
import com.ygkj.auth.VO.request.AppAchievementReqVo;
import com.ygkj.auth.VO.request.SysAchievementAddVO;
import com.ygkj.auth.VO.request.SysAchievementUpdateVO;
import com.ygkj.auth.api.achievement.model.SysAchievement;
import com.ygkj.auth.api.achievement.model.SysAchievementJurisdiction;
import com.ygkj.auth.api.department.model.SysDept;
import com.ygkj.auth.api.file.model.SysFile;
import com.ygkj.auth.api.user.model.SysUser;
import com.ygkj.jwt.JwtTokenUtil;
import com.ygkj.mapper.*;
import com.ygkj.project.model.CompanyBindingPrcd;
import com.ygkj.service.SysAchievementService;
import com.ygkj.utils.PageUtils;
import com.ygkj.vo.req.SysFileReqVo;
import com.ygkj.vo.resp.PageVO;
import com.ygkj.water.model.CommonResult;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

//import com.ygkj.utils.SmsSendUtil;

/**
 * @author Huang.zh
 * @date 2020/9/15 17:14
 * @Description: 归档管理服务层具体实现
 */
@Service("sysAchievementService")
public class SysAchievementServiceImpl implements SysAchievementService {
    @Autowired
    private SysAchievementMapper sysAchievementMapper;
    @Autowired
    private SysFileMapper sysFileMapper;
    @Autowired
    private SysUserMapper sysUserMapper;
    @Autowired
    private SysDeptMapper sysDeptMapper;
    @Autowired
    private SysAchievementJurisdictionMapper jurisdictionMapper;

    @Override
    public List<SysAchievement> selectAll(String name) {
        if (StringUtils.isEmpty(name)) {
            SysAchievement sysAchievement=new SysAchievement();
            return sysAchievementMapper.selectAll(sysAchievement.getProjectCode());
        } else {
            List<SysAchievement> sysAchievements = sysAchievementMapper.selectAllByName(name);
            return sysAchievements;
        }
    }

    @Override
    public String addSysAchievement(SysAchievementAddVO sysAchievementAddVO) throws Exception {
        SysAchievement sysAchievement = new SysAchievement();
        BeanUtils.copyProperties(sysAchievementAddVO, sysAchievement);
        if (StringUtils.isEmpty(sysAchievement.getParentId())) {
            sysAchievement.setParentId(null);
        }
        sysAchievement.setId(UUID.randomUUID().toString());
        int row = sysAchievementMapper.insert(sysAchievement);
        if (row != 1) {
            throw new RuntimeException("操作失败！请联系管理员");
        }
        return sysAchievement.getId();
    }

    @Override
    public void updateSysAchievement(SysAchievementUpdateVO sysAchievementUpdateVO) throws Exception {
        SysAchievement achievement = new SysAchievement();
        BeanUtils.copyProperties(sysAchievementUpdateVO, achievement);
        int row = sysAchievementMapper.update(achievement);
        if (row != 1) {
            throw new RuntimeException("操作失败！请联系管理员");
        }
    }

    @Override
    public void deleteSysAchievement(String id) {
        List<SysAchievement> treeList = sysAchievementMapper.load(id);
        if (!treeList.isEmpty()) {
            StringBuffer buffer = new StringBuffer();
            for (SysAchievement achievement : treeList) {
                buffer.append(recursionGetAchievementIds(achievement, new StringBuffer()));
            }
            String ids = buffer.toString();
            SysAchievement achievement = new SysAchievement();
            achievement.setId(ids);
            achievement.setDeleted(true);
            int row = sysAchievementMapper.update(achievement);
            if (row < 1) {
                throw new RuntimeException("操作失败！请联系管理员");
            }
        } else {
            List<SysFile> files = sysFileMapper.load(id);
            if (!files.isEmpty()) {
                sysFileMapper.batchLogicDelete(id);
            }
        }
    }

    /**
     * @Author Huang.zh
     * @Description 递归拼接id
     * @Date 2020/9/15 20:11
     */
    private String recursionGetAchievementIds(SysAchievement sysAchievement, StringBuffer buffer) {
        buffer.append(sysAchievement.getId() + ",");
        if (sysAchievement.hasChild()) {
            List<SysAchievement> children = sysAchievement.getChildren();
            for (SysAchievement child : children) {
                buffer = new StringBuffer(recursionGetAchievementIds(child, buffer));
            }
        }
        return buffer.toString();
    }

    @Override
    public PageVO<SysFile> findFiles(AchievementFilePageQueryVO achievementFilePageQueryVO) {
        PageHelper.startPage(achievementFilePageQueryVO.getPageNum(), achievementFilePageQueryVO.getPageSize());
        List<SysFile> files = sysFileMapper.load(achievementFilePageQueryVO.getAchievementFileIds());
        return PageUtils.getPageVO(files);
    }

    @Override
    public CommonResult statisticsTotalArchives() {
        try {
            List<JSONObject> jsonObjectList = sysAchievementMapper.statisticsTotalArchives();
            return CommonResult.success(jsonObjectList);
        } catch (Exception e) {
            return CommonResult.success(e.getMessage());
        }
    }

    @Override
    public List<SysAchievement> achievementStructure(String projectCode,String userId) {
        com.ygkj.entity.SysUser sysUser = sysUserMapper.selectByPrimaryKey(userId);
        List<SysAchievement> sysAchievements;
        if ("3".equals(sysUser.getUserType())){
            //查询绑定的海塘
            CompanyBindingPrcd companyBindingPrcd = new CompanyBindingPrcd();
            companyBindingPrcd.setWyhId(sysUser.getDeptId());
            CompanyBindingPrcd prcdServiceOne = sysDeptMapper.selectCompany(companyBindingPrcd);
            List<String> prcdList = new ArrayList<>();
            if(prcdServiceOne != null){
                prcdList = Arrays.asList(prcdServiceOne.getPrcd().split(","));
            }
            sysAchievements = sysAchievementMapper.selectAll2(prcdList);
        }else {
            sysAchievements = sysAchievementMapper.selectAll(projectCode);
        }
//        checkJurisdiction(sysAchievements);
        List<SysAchievement> list = sysAchievements.stream().filter(e -> "-1".equals(e.getParentId()))
                .map((entity) -> {
                    entity.setChildren(getChildrens(entity, sysAchievements));
                    return entity;
                })
                .sorted(Comparator.comparingInt(s -> (s.getSort() == null ? 0 : s.getSort()))).collect(Collectors.toList());
//        SysAchievement sysAchievement = new SysAchievement();
//        sysAchievement.setName("全部文件夹");
//        sysAchievement.setId("0");
//        sysAchievement.setChildren(list);
//        List<SysAchievement> res = new ArrayList<>();
//        res.add(sysAchievement);
        return list;
    }

    @Override
    public List<JSONObject> appAchievement(AppAchievementReqVo reqVo) {
        List<JSONObject> fList = new ArrayList<>();
        Map<String,String> map = new HashMap<>();
        map.put("海塘","0");
        map.put("水库","1");
        map.put("水闸","2");
        map.put("堤防","3");
        if ("back".equals(reqVo.getFlag())){
            //后退就要拿到父文件夹的父id
        }
        //1.工程档案 2.物业档案
        if ("1".equals(reqVo.getAchievementType())){
            if (StringUtils.isBlank(reqVo.getParentId()) && StringUtils.isBlank(reqVo.getProjectType())){
                reqVo.setParentId("0,1,2,3");
            }else if (StringUtils.isBlank(reqVo.getParentId()) && StringUtils.isNotBlank(reqVo.getProjectType())){
                reqVo.setParentId(map.get(reqVo.getProjectType()));
            }else if (StringUtils.isNotBlank(reqVo.getParentId()) && StringUtils.isBlank(reqVo.getProjectType())){
                reqVo.setParentId(reqVo.getParentId());
            }
            fList = getAppAchievement(reqVo);
        }else if ("2".equals(reqVo.getAchievementType())){
            if (StringUtils.isBlank(reqVo.getParentId()) && StringUtils.isBlank(reqVo.getProjectType())){
                reqVo.setParentId("0,1,2,3");
            }else if (StringUtils.isBlank(reqVo.getParentId()) && StringUtils.isNotBlank(reqVo.getProjectType())){
                reqVo.setParentId(map.get(reqVo.getProjectType()));
            }else if (StringUtils.isNotBlank(reqVo.getParentId()) && StringUtils.isBlank(reqVo.getProjectType())){
                reqVo.setParentId(reqVo.getParentId());
            }
            fList = getAppAchievement(reqVo);
        }
        return fList;
    }
    private List<JSONObject> getAppAchievement(AppAchievementReqVo reqVo){
        List<JSONObject> fList = new LinkedList<>();
        List<String> pId = Arrays.asList("0","1","2","3");
        //获取文件夹下子文件夹
        List<SysAchievement> sysAchievements = sysAchievementMapper.getFolders(reqVo);
        //获取子文件下所有文件
        if (sysAchievements != null && sysAchievements.size() > 0){
            for (SysAchievement sysAchievement : sysAchievements) {
                List<SysAchievement> treeList = sysAchievementMapper.load(sysAchievement.getId());
                StringBuffer buffer = new StringBuffer();
                if (!treeList.isEmpty()) {
                    for (SysAchievement achievement : treeList) {
                        buffer.append(recursionGetAchievementFileIds(achievement, new StringBuffer()));
                    }

                }
                String ids = buffer.toString();
                List<SysFile> sysFiles = new ArrayList<>();
                if (ids.length() > 0){
                    ids = ids.substring(0,ids.length()-1);
                    SysFileReqVo vo  = new SysFileReqVo();
                    vo.setId(ids);
                    sysFiles = sysFileMapper.selectAppAchievement(vo);
                }
                Double fileSize = 0.0;
                if (sysFiles.size() > 0){
                    for (SysFile sysFile : sysFiles) {
                        fileSize += sysFile.getFileSize();
                    }
                }
                fileSize = new BigDecimal(fileSize / 1024 + "").setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                sysAchievement.setFileSize(fileSize);
            }

        }
        List<JSONObject> folderlist = JSONArray.parseArray(JSON.toJSONStringWithDateFormat(sysAchievements,"yyyy-MM-dd HH:mm"), JSONObject.class);
        folderlist.forEach(o->o.put("type","folder"));
        fList.addAll(folderlist);

        //获取该文件夹下文件
        if (!pId.contains(reqVo.getParentId())){
            SysFileReqVo vo  = new SysFileReqVo();
            vo.setYear(reqVo.getYear());
            vo.setAchievementId(reqVo.getParentId());
            vo.setFileName(reqVo.getKeyWord());
            List<SysFile> sysFiles = sysFileMapper.selectAppAchievement(vo);
            if(sysFiles != null && sysFiles.size() > 0){
                List<JSONObject> syslist = JSONArray.parseArray(JSON.toJSONStringWithDateFormat(sysFiles,"yyyy-MM-dd HH:mm"), JSONObject.class);
                syslist.forEach(o->{
                    o.put("type","file");
                    Double fileSize = new BigDecimal(o.getDouble("fileSize") / 1024 + "").setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                    o.put("fileSize",fileSize);
                });
                fList.addAll(syslist);
            }
        }
        return fList;
    }
    private String recursionGetAchievementFileIds(SysAchievement sysAchievement, StringBuffer buffer) {
        if (StringUtils.isNotBlank(sysAchievement.getFileIds())){
            buffer.append(sysAchievement.getFileIds() + ",");
        }
        if (sysAchievement.hasChild()) {
            List<SysAchievement> children = sysAchievement.getChildren();
            for (SysAchievement child : children) {
                buffer = new StringBuffer(recursionGetAchievementFileIds(child, buffer));
            }
        }
        return buffer.toString();
    }
    /**
     * 检查访问用户得文件夹权限
     *
     * @param sysAchievements
     */
    private void checkJurisdiction(List<SysAchievement> sysAchievements) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String token = request.getHeader("authorization");
        if (StringUtils.isEmpty(token)) {
            return;
        }
        String userId = JwtTokenUtil.getUserId(token);
        if (StringUtils.isEmpty(userId)) {
            return;
        }
        for (SysAchievement sysAchievement : sysAchievements) {
            //权限类型 1:查看权限 2:上传权限 3:删除权限 4:下载权限
            if (checkachievementIdUserHasPer(sysAchievement.getId(), "1", userId)) {
                sysAchievement.setLookFlag("1");
            }

            if (checkachievementIdUserHasPer(sysAchievement.getId(), "2", userId)) {
                sysAchievement.setUploadFlag("1");
            }

            if (checkachievementIdUserHasPer(sysAchievement.getId(), "3", userId)) {
                sysAchievement.setDeleteFlag("1");
            }

            if (checkachievementIdUserHasPer(sysAchievement.getId(), "4", userId)) {
                sysAchievement.setDownloadFlag("1");
            }

            if (checkachievementIdUserHasPer(sysAchievement.getId(), "6", userId)) {
                sysAchievement.setAdminFlag("1");
            }
        }
    }

    /**
     * 检查用户文件夹权限
     *
     * @param achievementId
     * @param type
     * @return
     */
    private Boolean checkachievementIdUserHasPer(String achievementId, String type, String userId) {
        boolean flag = false;
        List<com.ygkj.auth.api.achievement.model.SysAchievementJurisdiction> jurisdictionList = jurisdictionMapper.findDataByAchievementIdAndType(achievementId, type);
        if (CollectionUtils.isNotEmpty(jurisdictionList) && jurisdictionList.size() > 0) {
            for (com.ygkj.auth.api.achievement.model.SysAchievementJurisdiction jurisdiction : jurisdictionList) {
                String userIds = jurisdiction.getUserIds();//授权的用户ids
                if (StringUtils.isNotEmpty(userIds)) {
                    String[] userIdSpi = userIds.split(",");
                    for (int i = 0; i < userIdSpi.length; i++) {
                        if (userId.equals(userIdSpi[i])) {//判断用户id是否相等，相等，则有权限，返回true；反之，没有权限
                            return true;
                        }
                    }
                }
                String deportIds = jurisdiction.getDeportIds();//授权的单位id
                if (StringUtils.isNotEmpty(deportIds)) {
                    Integer num = sysUserMapper.findDeportUser(deportIds, userId);//查找当前请求的用户在授权的部门，大于0，则是；反之不是
                    if (num > 0) {
                        return true;
                    }
                }
            }
        }
        return flag;
    }

    /**
     * 全部数据,递归查找文档子结构
     *
     * @param root
     * @param all
     * @return
     */
    private List<SysAchievement> getChildrens(SysAchievement root, List<SysAchievement> all) {

        List<SysAchievement> list = all.stream().filter(e -> e.getParentId().equals(root.getId().toString()))
                .map(sysAchievement -> {
                    sysAchievement.setChildren(getChildrens(sysAchievement, all));
                    return sysAchievement;
                })
                .sorted((s1, s2) -> {
                    return (s1.getSort() == null ? 0 : s1.getSort()) - (s2.getSort() == null ? 0 : s2.getSort());
                }).collect(Collectors.toList());
        return list;
    }

    @Override
    public List<SysAchievement> findAchievementByFileId(String fileId) {
        return sysAchievementMapper.findAchievementByFileId(fileId);
    }

    @Override
    public SysAchievement findById(String id) {
        List<SysAchievement> achievementList = sysAchievementMapper.load(id);
        if (CollectionUtils.isNotEmpty(achievementList) && achievementList.size() > 0) {
            return achievementList.get(0);
        }
        return null;
    }

    @Override
    public PageInfo<SysAchievement> selectDataAll(AchievementFilePageQueryVO vo) {
        if (!vo.isFetchAll()) {
            PageHelper.startPage(vo.getPageNum(), vo.getPageSize());
        }
        List<SysAchievement> selectAll = sysAchievementMapper.selectDataAll(vo);
        if (CollectionUtils.isNotEmpty(selectAll) && selectAll.size() > 0) {
            for (SysAchievement achievement : selectAll) {
                if (StringUtils.isNotEmpty(achievement.getUserIds())) {
                    List<SysUser> users = sysUserMapper.findByIds(achievement.getUserIds());
                    if (CollectionUtils.isNotEmpty(users) && users.size() > 0) {
                        achievement.setUserList(users);
                    }
                }

                if (StringUtils.isNotEmpty(achievement.getDeportIds())) {
                    List<SysDept> sysDeptList = sysDeptMapper.findByIds(achievement.getDeportIds());
                    if (CollectionUtils.isNotEmpty(sysDeptList) && sysDeptList.size() > 0) {
                        achievement.setDeptList(sysDeptList);
                    }
                }

                List<SysAchievementJurisdiction> jurisdictionListction = jurisdictionMapper
                        .findDataByAchievementIdAndType(achievement.getId(), null);
                achievement.setJurisdictionListction(jurisdictionListction);

                if (StringUtils.isEmpty(achievement.getFileIds())) {
                    achievement.setStatus("1");
                } else {
                    achievement.setStatus("0");
                }
            }
        }
        return new PageInfo<SysAchievement>(selectAll);
    }

    @Override
    public int insert(SysAchievement entity) {
        return sysAchievementMapper.insert(entity);
    }

    @Override
    public int delete(String id) {
        return sysAchievementMapper.delete(id);
    }

    @Override
    public int update(SysAchievement entity) {
        return sysAchievementMapper.update(entity);
    }

    @Override
    public void findSysAchievementAchiPath(SysAchievement entity) {
        if (StringUtils.isEmpty(entity.getParentId())) {
            return;
        }
        List<SysAchievement> sysAchievements = sysAchievementMapper.selectAll(entity.getProjectCode());
        List<SysAchievement> collect = sysAchievements.stream().filter(e -> e.getId().equals(entity.getParentId())).collect(Collectors.toList());
        if (!collect.isEmpty()) {
            StringBuilder stringBuilder = new StringBuilder();
            SysAchievement sysAchievement = collect.get(0);
            stringBuilder.append(sysAchievement.getName());
            findParentAchiPath(sysAchievement, sysAchievements, stringBuilder);
            entity.setAchiPath(stringBuilder.toString());
        }
    }

    //return 1:发送成功 2:发送失败 3: 不需要督办
//    @Override
//    public Integer achievementOversee(String id) {
//        SysAchievementJurisdictionReqVo vo = new SysAchievementJurisdictionReqVo();
//        vo.setAchievementId(id);
//        vo.setType("6");
//        List<com.ygkj.entity.SysAchievementJurisdiction> list = jurisdictionMapper.selectAll(vo);
//        if (!list.isEmpty()) {
//            com.ygkj.entity.SysAchievementJurisdiction sysAchievementJurisdiction = list.get(0);
//            try {
//                String userIds = sysAchievementJurisdiction.getUserIds();
//                if (!StringUtils.isEmpty(userIds)) {
//                    String phones = sysUserMapper.findByIds(userIds).stream().map(e -> e.getPhone()).collect(Collectors.joining(","));
//                    SysAchievement sysAchievement = findById(id);
//                    if (sysAchievement != null && "1".equals(sysAchievement.getAllowNull()) && StringUtils.isEmpty(sysAchievement.getFileIds())  && !StringUtils.isEmpty(phones)) {
//                        Map<String, Object> map = SmsSendUtil.sendSms(phones, "档案设置不为空，实际检查却为空");
//                        if (1 == (Integer) map.get("rt")) {
//                            return 1;
//                        } else {
//                            return 2;
//                        }
//                    }
//                }
//            }catch (Exception e) {
//                return 2;
//            }
//        }
//        return 3;
//    }

    @Override
    public void modifyAdmin(com.ygkj.entity.SysAchievementJurisdiction jurisdiction) {
        if (StringUtils.isEmpty(jurisdiction.getAchievementId())) {
            return;
        }
        SysAchievement sysAchievement = findById(jurisdiction.getAchievementId());
        if (!StringUtils.isEmpty(jurisdiction.getUserIds()) || !StringUtils.isEmpty(jurisdiction.getDeportIds())) {
            String userName = JwtTokenUtil.getCurrentUserName().orElse("");
            if (!StringUtils.isEmpty(userName)) {
                if (!StringUtils.isEmpty(sysAchievement.getAdminName()) && !sysAchievement.getAdminName().contains(userName)) {
                    sysAchievement.setAdminName(sysAchievement.getAdminName() + "," + userName);
                } else {
                    sysAchievement.setAdminName(userName);
                }
            }

            String userId = JwtTokenUtil.getCurrentUserId().orElse("");
            if (!StringUtils.isEmpty(userId)) {
                if (!StringUtils.isEmpty(sysAchievement.getAdminId())) {
                    String[] ids = sysAchievement.getAdminId().split(",");
                    boolean appearFlag = false;
                    for (String id : ids) {
                        if (id.equals(userId)) {
                            appearFlag = true;
                        }
                    }
                    if (!appearFlag) {
                        sysAchievement.setAdminId(sysAchievement.getAdminId() + "," + userId);
                    }
                } else {
                    sysAchievement.setAdminId(userId);
                }
            }
            sysAchievementMapper.update(sysAchievement);
        }
    }

    @Override
    public void editFileOfSysAchievement(String fileId,String achievementId, String AfterachievementId) {
        int a=0;
        int b=0;
        if(AfterachievementId!=null) {
            List<SysAchievement> sysAchievement = sysAchievementMapper.load(achievementId);
            String fileIds = sysAchievement.get(0).getFileIds();
            if (fileIds != null) {
                String[] fileIdsList = fileIds.split(",");
                List<String> list = Arrays.asList(fileIdsList);
                List<String> newlist=new ArrayList<>();
                for (String s:list) {
                    if(fileId.equals(s)){
                        continue;
                    }else
                        newlist.add(s);
                }
                String newFileIds = String.join(",", newlist);
                sysAchievement.get(0).setFileIds(newFileIds);
                a = sysAchievementMapper.update(sysAchievement.get(0));
            }
            List<SysAchievement> afterAchievement = sysAchievementMapper.load(AfterachievementId);
            String AfterFileIds = afterAchievement.get(0).getFileIds();
            if (AfterFileIds != null) {
                afterAchievement.get(0).setFileIds(AfterFileIds + "," + fileId);
            } else {
                afterAchievement.get(0).setFileIds(fileId);
            }
            b = sysAchievementMapper.update(afterAchievement.get(0));
        }
    }

    /**
     * 查找父文档名称
     *
     * @param sysAchievement
     * @param sysAchievements
     * @param stringBuilder
     */
    public void findParentAchiPath(SysAchievement sysAchievement, List<SysAchievement> sysAchievements, StringBuilder stringBuilder) {
        List<SysAchievement> collect = sysAchievements.stream().filter(e -> e.getId().equals(sysAchievement.getParentId())).collect(Collectors.toList());
        if (!collect.isEmpty()) {
            SysAchievement achievement = collect.get(0);
            stringBuilder.insert(0, achievement.getName() + "-");
            findParentAchiPath(achievement, sysAchievements, stringBuilder);
        }
    }
}
