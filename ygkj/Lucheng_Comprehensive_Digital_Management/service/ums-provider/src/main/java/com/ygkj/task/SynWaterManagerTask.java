package com.ygkj.task;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ygkj.constants.Constant;
import com.ygkj.entity.*;
import com.ygkj.enums.WaterManageEnum;
import com.ygkj.mapper.*;
import com.ygkj.service.DingDingService;
import com.ygkj.utils.PasswordUtils;
import com.ygkj.vo.req.DingDingDeptVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 水管理平台用户同步
 */
@Slf4j
@Component
public class SynWaterManagerTask {

    private static final SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Value("${watermanage.user.roleid}")
    private String ROLE_ID;

    @Resource
    private DingDingService dingDingService;
    @Resource
    private SysUserMapper sysUserMapper;
    @Resource
    private DingDingDeptMapper dingDingDeptMapper;
    @Resource
    private DingDingUserMapper dingDingUserMapper;
    @Resource
    private SysUserRoleMapper sysUserRoleMapper;
    @Resource
    private SysJobMapper sysJobMapper;

    /**
     * 钉钉数据同步
     */
//    @Scheduled(cron = "0 0 1 * * ?")
    public void synWaterManagerHandler() {
        log.info("====================同步水管理平台数据开始====================");
        //同步部门
        synDept();
        //同步用户
        synUser();
        //用户绑定
        bindUser();
        log.info("====================同步水管理平台数据结束====================");
    }

    /**
     * 钉钉用户绑定系统部门加入用户表中
     */
    private void bindUser() {
        log.info("用户绑定开始");
        //查出所有还没绑定或者信息修改了的人员
        List<DingDingUser> dingDingUserList = dingDingUserMapper.listBind();
        if (CollectionUtils.isNotEmpty(dingDingUserList)) {
            log.info("[该次操作绑定用户数量]{}", dingDingUserList.size());
            //查出所有绑定了系统单位的钉钉部门
            DingDingDeptVo param = new DingDingDeptVo();
            param.setSysDept("bind");
            List<DingDingDeptVo> list = dingDingDeptMapper.listBind(param);
            Map<String, String> deptMap = list.stream().collect(Collectors.toMap(DingDingDeptVo::getId, DingDingDeptVo::getSysDept));

            List<SysUser> saveList = new ArrayList<>();
            List<SysUser> updateList = new ArrayList<>();

            //一个用户在这里只能绑定一个单位
            Set<String> set = new HashSet<>(dingDingUserList.size());
            List<SysJob> sysJobs = sysJobMapper.getJobByName("普通管理员");
            SysJob sysJob = null;
            if (sysJobs.size() > 0){
                sysJob = sysJobs.get(0);
            }
            //遍历钉钉用户
            SysJob finalSysJob = sysJob;
            dingDingUserList.forEach(dingDingUser -> {
                String id = dingDingUser.getId();
                String userCode = dingDingUser.getUserCode();
                String deptId = dingDingUser.getDeptId();
                //首先判断该用户是否操作过,未操作过才进行后续操作
                if (!set.contains(userCode)) {
                    SysUser sysUser = dingUser2SysUser(dingDingUser);
                    //如果ID为空说明该用户还未绑定过
                    if (StringUtils.isEmpty(id)) {
                        //查看是否有单位可绑定
                        if (deptMap.containsKey(deptId)) {
                            //换上对应的系统单位
                            sysUser.setDeptId(deptMap.get(deptId));
                            String salt = PasswordUtils.getSalt();
                            String password = PasswordUtils.encode(Constant.DEFAULT_PASSWORD, salt);
                            sysUser.setSalt(salt);
                            sysUser.setPassword(password);
                            sysUser.setJobId(finalSysJob.getId());
                            set.add(userCode);
                            saveList.add(sysUser);
                        }
                    } else {
                        set.add(userCode);
                        updateList.add(sysUser);
                    }
                }
            });
            if (saveList.size() > 0) {
                sysUserMapper.batchInsert(saveList);
                //同样的初始化一下用户的角色
//                Date now = new Date();
//                List<SysUserRole> sysUserRoleList = new ArrayList<>();
//                saveList.forEach(sysUser -> {
//                    SysUserRole sysUserRole = new SysUserRole();
//                    sysUserRole.setCreateTime(now);
//                    sysUserRole.setId(UUID.randomUUID().toString());
//                    sysUserRole.setRoleId(ROLE_ID);
//                    sysUserRole.setUserId(sysUser.getId());
//                    sysUserRoleList.add(sysUserRole);
//                });
//                sysUserRoleMapper.batchUserRole(sysUserRoleList);
            }
            if (updateList.size() > 0) {
                sysUserMapper.batchUpdate(updateList);
            }
        }
        log.info("用户绑定结束");
    }

    private SysUser dingUser2SysUser(DingDingUser dingDingUser) {
        SysUser sysUser = new SysUser();
        String userName = dingDingUser.getTrueName();
        sysUser.setUsername(userName);
        sysUser.setId(dingDingUser.getUserCode());
        String modifyTime = dingDingUser.getModifyTime();
        sysUser.setPhone(dingDingUser.getMobile());
        sysUser.setRealName(userName);
        if (!StringUtils.isEmpty(modifyTime)) {
            try {
                sysUser.setUpdateTime(SDF.parse(modifyTime));
            } catch (ParseException e) {
                log.error("[日期转换错误]{}", modifyTime);
            }
        }
        sysUser.setSort(dingDingUser.getOrderId());
        return sysUser;
    }

    /**
     * 同步用户
     */
    private void synUser() {
        log.info("====================同步水管理平台用户开始====================");
        //获取本地同步的所有钉钉部门
        List<SysDept> list = dingDingDeptMapper.listAll();
        final Integer success = 0;
        list.forEach(sysDept -> {
            Map<String, JSONObject> allUsers = new HashMap<>();  // 用来判断有没有重复用户
            String deptId = sysDept.getId();
            //因为接口每分钟只能请求300次
            //获取部门下所有用户
            JSONObject userInfo = dingDingService.post(WaterManageEnum.DEPT_USER, deptId, "");
            if (null != userInfo && success.equals(userInfo.getIntValue("status"))) {
                List<JSONObject> userList = new ArrayList<>();
                JSONArray userJArr = userInfo.getJSONArray("message");
                // 解密电话号码、用户名、邮箱
                userJArr.forEach(o -> {
                    JSONObject user = (JSONObject) o;
                    aesDecrypt(user, "userName");
                    aesDecrypt(user, "mobile");
                    aesDecrypt(user, "mail");
                    String id = user.getString("userCode");
                    if (!hasUserExist(allUsers, user, id)) {
                        user.put("deptId", deptId);
                        user.put("id", id);
                        userList.add(user);
                        allUsers.put(id, user);
                    }
                });

                batchSaveUser(userList);
            }
        });

        log.info("====================同步水管理平台用户结束====================");
    }

    private void aesDecrypt(JSONObject user, String key) {
        user.put(key, dingDingService.aesDecrypt(user, key, ""));
    }

    private void batchSaveUser(List<JSONObject> userList) {
        if (userList.size() > 0) {
            //获取所有用户
            List<SysUser> sysUserList = dingDingUserMapper.listAllUser();
            Map<String, SysUser> userMap = sysUserList.stream().collect(Collectors.toMap(SysUser::getId, Function.identity()));
            List<JSONObject> saveList = new ArrayList<>();
            List<JSONObject> updateList = new ArrayList<>();
            List<SysUserRole> sysUserRoleList = new ArrayList<>();
            Date now = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            List<SysJob> sysJobs = sysJobMapper.getJobByName("普通管理员");
            SysJob sysJob = null;
            if (sysJobs.size() > 0){
                sysJob = sysJobs.get(0);
            }
            SysJob finalSysJob = sysJob;
            userList.forEach(user -> {
                String id = user.getString("id");
                if (userMap.containsKey(id)) {
                    SysUser sysUser = userMap.get(id);
                    String obj = (String) user.get("modifyTime");
                    if (!StringUtils.isEmpty(obj)) {
                        try {
                            Date newTime = sdf.parse(obj);
                            Date modifyTime = sysUser.getUpdateTime();
                            //编辑时间不同代表编辑过了,更新
                            if (!newTime.equals(modifyTime)) {
                                updateList.add(user);
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                    String deptId = user.getString("deptId");
                    //当前部门还不存在则加上当前部门
                    if (!sysUser.getDeptId().contains(deptId)) {
                        deptId += "," + sysUser.getDeptId();
                        user.put("deptId", deptId);
                        updateList.add(user);
                    }
                } else {
                    //给用户初始化密码
                    String salt = PasswordUtils.getSalt();
                    String password = PasswordUtils.encode(Constant.DEFAULT_PASSWORD, salt);
                    user.put("salt", salt);
                    user.put("password", password);
                    user.put("jobId", finalSysJob.getId());
                    saveList.add(user);
                    //顺便初始化用户角色
//                    SysUserRole sysUserRole = new SysUserRole();
//                    sysUserRole.setCreateTime(now);
//                    sysUserRole.setId(UUID.randomUUID().toString());
//                    sysUserRole.setRoleId(ROLE_ID);
//                    sysUserRole.setUserId(user.getString("id"));
//                    sysUserRoleList.add(sysUserRole);
                }
            });
            if (saveList.size() > 0) {
                dingDingUserMapper.batchInsert(saveList);
//                sysUserRoleMapper.batchUserRole(sysUserRoleList);
            }
            if (updateList.size() > 0) {
//                dingDingUserMapper.batchUpdate(updateList);
            }
        }
    }

    private boolean hasUserExist(Map<String, JSONObject> allUsers, JSONObject curUser, String id) {
        if (allUsers.containsKey(id)) {
            JSONObject jsonObject = allUsers.get(id);

            // 三个字段都相同，就是同一个用户
            if (jsonObject.getString("userName").equals(curUser.getString("userName"))
                    && jsonObject.getString("trueName").equals(curUser.getString("trueName"))
                    && jsonObject.getString("mobile").equals(curUser.getString("mobile"))) {
                return true;
            }
        }
        return false;
    }

    /**
     * 同步部门
     */
    private void synDept() {
        log.info("====================同步水管理平台部门开始====================");
        JSONObject orgScope = dingDingService.post(WaterManageEnum.ORG_SCOPE);
        //获取所有部门ID
        final Integer success = 0;
        if (orgScope != null && success.equals(orgScope.getIntValue("status"))) {

            JSONArray authedDept = orgScope.getJSONObject("message").getJSONObject("authOrgScopes").getJSONArray("authedDept");
            List<JSONObject> deptList = new ArrayList<>();
            //用来判断有没有重复部门
            Map<String, JSONObject> allDeptMap = new HashMap<>();
            //遍历所有部门ID
            authedDept.forEach(deptObj -> {
                String deptId = deptObj.toString();

                //获取部门基本信息
                JSONObject deptInfo = dingDingService.post(WaterManageEnum.DEPT, deptId, "");
                if (success.equals(deptInfo.getIntValue("status"))) {
                    JSONObject deptJson = deptInfo.getJSONObject("message");

                    //排除相同的部门
                    distinct(allDeptMap, deptJson, deptId, deptList);

                    //获取部门下的所有子部门
                    JSONObject subDeptObj = dingDingService.post(WaterManageEnum.SUB_DEPT, deptId, "");
                    JSONArray subDeptArr = subDeptObj.getJSONArray("message");
                    subDeptArr.forEach(o -> {
                        JSONObject subDept = (JSONObject) o;
                        String organizationId = subDept.getString("organizationId");
                        if (success.equals(deptInfo.getIntValue("status"))) {
                            //子部门排除自己
                            if (!deptId.equals(organizationId)) {
                                distinct(allDeptMap, subDept, organizationId, deptList);
                            }
                        }
                    });
                }
            });
            batchSaveDept(deptList);
        }
        log.info("====================同步水管理平台部门结束====================");
    }

    private void batchSaveDept(List<JSONObject> deptList) {
        //查出目前所有的部门
        List<SysDept> dingDingDeptList = dingDingDeptMapper.listAll();
        Set<String> deptSet = dingDingDeptList.stream().map(sysDept -> sysDept.getId()).collect(Collectors.toSet());
        List<JSONObject> saveList = new ArrayList<>();
        List<JSONObject> updateList = new ArrayList<>();
        deptList.forEach(dept -> {
            //格式化行政区划
            String adcd = dept.getString("adcd");
            if (!StringUtils.isEmpty(adcd)) {
                for (int i = adcd.length(); i < 12; i++) {
                    adcd += "0";
                }
                dept.put("adcd", adcd);
            }
            //数据库里已经有的数据做更新
            if (deptSet.contains(dept.getString("id"))) {
                updateList.add(dept);
            } else {
                saveList.add(dept);
            }
        });
        if (saveList.size() > 0) {
            dingDingDeptMapper.batchInsert(saveList);
        }
        if (updateList.size() > 0) {
            dingDingDeptMapper.batchUpdate(updateList);
        }
    }

    private void distinct(Map<String, JSONObject> allDeptMap, JSONObject deptJson, String deptId, List<JSONObject> deptList) {
        deptJson.put("id", deptId);
        if (!hasDeptExist(allDeptMap, deptJson)) {
            deptList.add(deptJson);
            allDeptMap.put(deptId, deptJson);
        }
    }

    /**
     * 判断是否是相同部门
     *
     * @param allDepts
     * @param curDept
     * @return
     */
    private boolean hasDeptExist(Map<String, JSONObject> allDepts, JSONObject curDept) {
        String curDeptid = curDept.getString("id");
        if (allDepts.containsKey(curDeptid)) {
            JSONObject jsonObject = allDepts.get(curDeptid);
            // 三个字段都相同，就是同一个部门
            if (jsonObject.getString("organizationId").equals(curDept.getString("organizationId"))
                    && jsonObject.getString("parentId").equals(curDept.getString("parentId"))
                    && jsonObject.getString("name").equals(curDept.getString("name"))) {
                return true;
            }
        }
        return false;
    }


}
