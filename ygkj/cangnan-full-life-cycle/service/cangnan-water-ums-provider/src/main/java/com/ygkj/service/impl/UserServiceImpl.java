package com.ygkj.service.impl;

import com.dcxx.framework.watermanagesdk.SSOSdk;
import com.dcxx.framework.watermanagesdk.config.AppConfig;
import com.github.pagehelper.PageHelper;
import com.ygkj.auth.VO.request.ValidateTicketVo;
import com.ygkj.client.AdcdClient;
import com.ygkj.constants.Constant;
import com.ygkj.entity.*;
import com.ygkj.exception.BusinessException;
import com.ygkj.exception.code.BaseResponseCode;
import com.ygkj.jwt.JwtTokenUtil;
import com.ygkj.jwt.TokenSettings;
import com.ygkj.mapper.*;
import com.ygkj.properties.JudgeSystemAuthMap;
import com.ygkj.service.*;
import com.ygkj.utils.PageUtils;
import com.ygkj.utils.PasswordUtils;
import com.ygkj.vo.req.*;
import com.ygkj.vo.resp.LoginRespVO;
import com.ygkj.vo.resp.PageVO;
import com.ygkj.vo.resp.UserOwnRoleRespVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.net.ssl.*;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @ClassName: UserServiceImpl
 * TODO:类文件简单描述
 * @Author: Huang.zh
 * @CreateDate: 2019/9/7 22:56
 * @UpdateUser: Huang.zh
 * @UpdateDate: 2019/9/7 22:56
 * @Version: 0.0.1
 */
@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    private SysUserMapper sysUserMapper;
    @Autowired
    private SysDeptMapper sysDeptMapper;
    @Autowired
    private RedisService redisService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private PermissionService permissionService;
    @Autowired
    private UserRoleService userRoleService;
    @Autowired
    private TokenSettings tokenSettings;
//    @Resource
//    private DingDingService dingDingService;
    @Resource
    private SysCompanyMapper sysCompanyMapper;

    @Autowired
    private AdcdClient adcdClient;

    @Autowired
    private JudgeSystemAuthMap judgeSystemAuthMap;

    @Override
    public String register(RegisterReqVO vo) {
        SysUser sysUser = new SysUser();
        BeanUtils.copyProperties(vo, sysUser);
        sysUser.setSalt(PasswordUtils.getSalt());
        String encode = PasswordUtils.encode(vo.getPassword(), sysUser.getSalt());
        sysUser.setPassword(encode);
        sysUser.setId(UUID.randomUUID().toString());
        sysUser.setCreateTime(new Date());
        int i = sysUserMapper.insertSelective(sysUser);
        if (i != 1) {
            throw new BusinessException(BaseResponseCode.OPERATION_ERRO);
        }
        return sysUser.getId();
    }

    @Override
    public LoginRespVO login(LoginReqVO vo) {
        UserPageReqVO pageReqVO = new UserPageReqVO();
        pageReqVO.setPhone(vo.getUsername());
        List<SysUser> list = sysUserMapper.selectAll(pageReqVO);
        if (list.isEmpty()) {
            throw new BusinessException(BaseResponseCode.PASSWORD_ERROR);
        }
        SysUser sysUser = list.get(0);
        if (sysUser.getStatus() == 2) {
            throw new BusinessException(BaseResponseCode.USER_LOCK);
        }
//        System.out.println(PasswordUtils.encode("ygkj0818",sysUser.getSalt()));
        if (!vo.getPassword().equals(sysUser.getPassword()) && !PasswordUtils.matches(sysUser.getSalt(), vo.getPassword(), sysUser.getPassword())) {
            throw new BusinessException(BaseResponseCode.PASSWORD_ERROR);
        }

        LoginRespVO respVO = new LoginRespVO();
        BeanUtils.copyProperties(sysUser, respVO);
        Map<String, Object> claims = new HashMap<>();
        List<SysPermission> permissions = permissionService.getPermission(sysUser.getId(), org.apache.commons.lang3.StringUtils.EMPTY);
        if (permissions != null && !permissions.isEmpty()){
            List<SysPermission> apiPermissions = permissions.stream().filter(sysPermission -> sysPermission.getType().intValue() == 3)
                    .collect(Collectors.toList());
            permissions.removeAll(apiPermissions);
            //返回用户可访问的目录和菜单
            respVO.setList(permissionService.getAllMenusTree(permissions));
            //缓存用户角色对应的按钮权限
            redisService.set(Constant.PERMISSION_CODE_KEY.concat(sysUser.getId()), apiPermissions);
        }
        claims.put(Constant.DEPARTMENT_NAME_KEY,sysUser.getDept().getName());
        claims.put(Constant.JWT_ROLES_KEY, getRolesByUserId(sysUser.getId()));
        claims.put(Constant.JWT_USER_NAME, sysUser.getUsername());
        //用户类型 1水利局用户 2普通企业用户
        claims.put(Constant.JWT_USER_TYPE, sysUser.getUserType());
        claims.put(Constant.DEPT_CODE_KEY, sysUser.getDeptId());
        //用户管理员标志
        claims.put(Constant.JWT_USER_ADMIN, sysUser.isAdmin());
        String access_token = JwtTokenUtil.getAccessToken(sysUser.getId(), claims);
        String refresh_token;
//        if(vo.getType().equals("1")){
//            refresh_token=JwtTokenUtil.getRefreshToken(sysUser.getId(),claims);
//        }else {
//            refresh_token=JwtTokenUtil.getRefreshAppToken(sysUser.getId(),claims);
//        }
        refresh_token = JwtTokenUtil.getRefreshToken(sysUser.getId(), claims);
        respVO.setAccessToken(access_token);
        respVO.setRefreshToken(refresh_token);
        respVO.setSlogan(sysUser.getSlogan());
        respVO.setUserType(sysUser.getUserType());
        respVO.setDepartment(sysUser.getDept().getName());
        redisService.set(Constant.ACCESS_TOKEN.concat(access_token),access_token, 3l,TimeUnit.HOURS);
        return respVO;
    }

    private List<String> getRolesByUserId(String userId) {
        return roleService.getRoleNames(userId);
    }

    private Set<String> getPermissionsByUserId(String userId) {
        return permissionService.getPermissionsByUserId(userId);
    }

    @Override
    public String refreshToken(String refreshToken, String accessToken) {

        if (redisService.hasKey(Constant.JWT_ACCESS_TOKEN_BLACKLIST + refreshToken) || !JwtTokenUtil.validateToken(refreshToken)) {
            throw new BusinessException(BaseResponseCode.TOKEN_ERROR);
        }
        String userId = JwtTokenUtil.getUserId(refreshToken);
        log.info("userId={}", userId);
        SysUser sysUser = sysUserMapper.selectByPrimaryKey(userId);
        if (null == sysUser) {
            throw new BusinessException(BaseResponseCode.TOKEN_PARSE_ERROR);
        }
        Map<String, Object> claims = null;

        if (redisService.hasKey(Constant.JWT_REFRESH_KEY + userId)) {
            claims = new HashMap<>();
            claims.put(Constant.JWT_ROLES_KEY, getRolesByUserId(userId));
            claims.put(Constant.JWT_PERMISSIONS_KEY, getPermissionsByUserId(userId));
        }
        String newAccessToken = JwtTokenUtil.refreshToken(refreshToken, claims);

        redisService.setifAbsen(Constant.JWT_REFRESH_STATUS + accessToken, userId, 1, TimeUnit.MINUTES);


        if (redisService.hasKey(Constant.JWT_REFRESH_KEY + userId)) {
            redisService.set(Constant.JWT_REFRESH_IDENTIFICATION + newAccessToken, userId, redisService.getExpire(Constant.JWT_REFRESH_KEY + userId, TimeUnit.MILLISECONDS), TimeUnit.MILLISECONDS);
        }
        return newAccessToken;
    }

    @Override
    public void updateUserInfo(UserUpdateReqVO vo, String operationId, String userType, boolean userAdmin) {

        SysUser sysUser = sysUserMapper.selectByPrimaryKey(vo.getId());
        if (null == sysUser) {
            log.error("传入 的 id:{}不合法", vo.getId());
            throw new BusinessException(BaseResponseCode.DATA_ERROR);
        }

        // 判断操作用户是否为企业用户
        if ("1".equals(userType)) {
            // 水利用户

            BeanUtils.copyProperties(vo, sysUser);
            //sysUser.setUpdateTime(new Date());
//        if(!StringUtils.isEmpty(vo.getPassword())){
//            String newPassword=PasswordUtils.encode(vo.getPassword(),sysUser.getSalt());
//            sysUser.setPassword(newPassword);
//        }else {
//            sysUser.setPassword(null);
//        }
            sysUser.setUpdateId(operationId);
            int count = sysUserMapper.updateByPrimaryKeySelective(sysUser);
            if (count != 1) {
                throw new BusinessException(BaseResponseCode.OPERATION_ERRO);
            }
//        setUserOwnRole(sysUser.getId(),vo.getRoleIds());
//        if(vo.getStatus()==2){
//            redisService.set(Constant.ACCOUNT_LOCK_KEY+sysUser.getId(),sysUser.getId());
//        }else {
//            redisService.delete(Constant.ACCOUNT_LOCK_KEY+sysUser.getId());
//        }
        } else if ("2".equals(userType)) {
            // 企业用户
            // 获取企业状态
            String deptId = sysUser.getDeptId();
            if (StringUtils.isEmpty(deptId)) {
                throw new BusinessException(BaseResponseCode.ACCOUNT_ERROR);
            }
            SysCompany sysCompany = sysCompanyMapper.selectByPrimaryKey(deptId);
            Integer companyStatus = sysCompany.getStatus();
            if (companyStatus == 2) {
                // 判断是否为管理员
                if (userAdmin) {
                    // 管理员
                    BeanUtils.copyProperties(vo, sysUser);
                    sysUser.setUpdateId(operationId);
                    int count = sysUserMapper.updateByPrimaryKeySelective(sysUser);
                    if (count != 1) {
                        throw new BusinessException(BaseResponseCode.OPERATION_ERRO);
                    }
                } else {
                    throw new BusinessException(500, "当前操作用户非企业管理员，请联系管理员修改信息");
                }
            } else {
                throw new BusinessException(500, "当前用户企业状态下不允许修改用户状态");
            }
        }

    }

    @Override
    public PageVO<SysUser> pageInfo(UserPageReqVO vo) {
        if (!vo.isFetchAll()){
            PageHelper.startPage(vo.getPageNum(), vo.getPageSize());
        }
        List<SysUser> sysUsers = sysUserMapper.selectAll(vo);
        List<String> adcdList = new ArrayList<>();
        if (!sysUsers.isEmpty()) {
            for (SysUser sysUser : sysUsers) {
                SysDept sysDept = sysDeptMapper.selectByPrimaryKey(sysUser.getDeptId());
                if (sysDept != null) {
                    sysUser.setDeptName(sysDept.getName());
                    String adcd = sysDept.getAdcd();
                    sysUser.setAdcd(adcd);
                    if (!adcdList.contains(adcdList.add(adcd))){
                        adcdList.add(adcd);
                    }
                }
            }
            List<LinkedHashMap> attAdcdList = (List<LinkedHashMap>) adcdClient.selectAdnmByAdcdList(adcdList).getData();
            Map<Object, List<LinkedHashMap>> collect = attAdcdList.stream().collect(Collectors.groupingBy(map -> map.get("adcd")));
            sysUsers.stream().filter(u -> collect.containsKey(u.getAdcd())).forEach(u -> u.setAdnm(String.valueOf(collect.get(u.getAdcd()).get(0).get("adnm"))));
        }
        return PageUtils.getPageVO(sysUsers);
    }

    @Override
    public SysUser detailInfo(String userId) {

        return sysUserMapper.selectByPrimaryKey(userId);
    }

    @Override
    public PageVO<SysUser> selectUserInfoByDeptIds(int pageNum, int pageSize, List<String> deptIds) {

        PageHelper.startPage(pageNum, pageSize);
        List<SysUser> list = sysUserMapper.selectUserInfoByDeptIds(deptIds);
        return PageUtils.getPageVO(list);
    }

    @Override
    public void addUser(UserAddReqVO vo) {
        UserPageReqVO pageReqVO = new UserPageReqVO();
        pageReqVO.setPhone(vo.getPhone());
        List<SysUser> sysUsers = sysUserMapper.selectAll(pageReqVO);
        if (sysUsers.isEmpty()) {
            SysUser sysUser = new SysUser();
            BeanUtils.copyProperties(vo, sysUser);
            sysUser.setSalt(PasswordUtils.getSalt());
            String encode = PasswordUtils.encode(StringUtils.isEmpty(vo.getPassword()) ? Constant.DEFAULT_PASSWORD : vo.getPassword()
                    , sysUser.getSalt());
            sysUser.setPassword(encode);
            sysUser.setId(UUID.randomUUID().toString());
            sysUser.setCreateTime(new Date());
            int i = sysUserMapper.insertSelective(sysUser);
            if (i != 1) {
                throw new BusinessException(BaseResponseCode.OPERATION_ERRO);
            }
            if (null != vo.getRoleIds() && !vo.getRoleIds().isEmpty()) {
                UserRoleOperationReqVO reqVO = new UserRoleOperationReqVO();
                reqVO.setUserId(sysUser.getId());
                reqVO.setRoleIds(vo.getRoleIds());
                userRoleService.addUserRoleInfo(reqVO);
            } else {
                UserRoleOperationReqVO reqVO = new UserRoleOperationReqVO();
                reqVO.setUserId(sysUser.getId());
                List<String> roleIds = new ArrayList<>();
                //初始化企业用户权限
                if (vo.getUserType().equals("2")){
                    roleIds.add("b95c69b7-84be-430f-ae57-27a703ae3998");
                } else if (vo.getUserType().equals("1")) {
                    //初始化水利局用户权限，默认最高权限
                    roleIds.add("11b3b80c-4a0b-4a92-96ea-fdd4f7a4a7e9");
                }
                reqVO.setRoleIds(roleIds);
                userRoleService.addUserRoleInfo(reqVO);
            }
        } else {
            throw new BusinessException(BaseResponseCode.DUPLICATE_PHONE);
        }
    }


    @Override
    public void logout(String accessToken, String refreshToken) {
        if (StringUtils.isEmpty(accessToken) || StringUtils.isEmpty(refreshToken)) {
            throw new BusinessException(BaseResponseCode.DATA_ERROR);
        }
//        Subject subject = SecurityUtils.getSubject();
//        log.info("subject.getPrincipals()={}",subject.getPrincipals());
//        if (subject.isAuthenticated()) {
//            subject.logout();
//        }
        String userId = JwtTokenUtil.getUserId(accessToken);

        redisService.set(Constant.JWT_REFRESH_TOKEN_BLACKLIST + accessToken, userId, JwtTokenUtil.getRemainingTime(accessToken), TimeUnit.MILLISECONDS);

        redisService.set(Constant.JWT_REFRESH_TOKEN_BLACKLIST + refreshToken, userId, JwtTokenUtil.getRemainingTime(refreshToken), TimeUnit.MILLISECONDS);


        redisService.delete(Constant.IDENTIFY_CACHE_KEY + userId);
    }

    @Override
    public void updatePwd(UpdatePasswordReqVO vo, String userId, String accessToken, String refreshToken) {

        SysUser sysUser = sysUserMapper.selectByPrimaryKey(userId);
        if (sysUser == null) {
            throw new BusinessException(BaseResponseCode.DATA_ERROR);
        }
        if (!PasswordUtils.matches(sysUser.getSalt(), vo.getOldPwd(), sysUser.getPassword())) {
            throw new BusinessException(BaseResponseCode.OLD_PASSWORD_ERROR);
        }
        sysUser.setUpdateTime(new Date());
        sysUser.setPassword(PasswordUtils.encode(vo.getNewPwd(), sysUser.getSalt()));
        int i = sysUserMapper.updateByPrimaryKeySelective(sysUser);
        if (i != 1) {
            throw new BusinessException(BaseResponseCode.OPERATION_ERRO);
        }

        redisService.set(Constant.JWT_REFRESH_TOKEN_BLACKLIST + accessToken, userId, JwtTokenUtil.getRemainingTime(accessToken), TimeUnit.MILLISECONDS);

//        redisService.set(Constant.JWT_REFRESH_TOKEN_BLACKLIST+refreshToken,userId,JwtTokenUtil.getRemainingTime(refreshToken),TimeUnit.MILLISECONDS);


        redisService.delete(Constant.IDENTIFY_CACHE_KEY + userId);

    }

    @Override
    public List<SysUser> getUserListByDeptId(String deptId) {
        return sysUserMapper.getUserListByDeptId(deptId);
    }

    @Override
    public List<SysUser> getUserListByDeptIds(List<String> deptIds) {
        return sysUserMapper.selectUserInfoByDeptIds(deptIds);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deletedUsers(List<String> userIds, String operationId) {
        SysUser sysUser = new SysUser();
        sysUser.setUpdateId(operationId);
        sysUser.setUpdateTime(new Date());
        sysUser.setDeleted(0);
        int i = sysUserMapper.deletedUsers(sysUser, userIds);
        if (i == 0) {
            throw new BusinessException(BaseResponseCode.OPERATION_ERRO);
        }

        for (String userId : userIds) {
            redisService.set(Constant.DELETED_USER_KEY + userId, userId, tokenSettings.getRefreshTokenExpireAppTime().toMillis(), TimeUnit.MILLISECONDS);
            //清空权鉴缓存
            redisService.delete(Constant.IDENTIFY_CACHE_KEY + userId);
        }

    }

    @Override
    public UserOwnRoleRespVO getUserOwnRole(String userId) {
        List<String> roleIdsByUserId = userRoleService.getRoleIdsByUserId(userId);
        List<SysRole> list = roleService.selectAllRoles();
        UserOwnRoleRespVO vo = new UserOwnRoleRespVO();
        vo.setAllRole(list);
        vo.setOwnRoles(roleIdsByUserId);
        return vo;
    }

    @Override
    public void setUserOwnRole(String userId, List<String> roleIds) {
        userRoleService.removeByUserId(userId);
        if (null != roleIds && !roleIds.isEmpty()) {

            UserRoleOperationReqVO reqVO = new UserRoleOperationReqVO();
            reqVO.setUserId(userId);
            reqVO.setRoleIds(roleIds);
            userRoleService.addUserRoleInfo(reqVO);
        }


        redisService.set(Constant.JWT_REFRESH_KEY + userId, userId, tokenSettings.getAccessTokenExpireTime().toMillis(), TimeUnit.MILLISECONDS);
        //清空权鉴缓存
        redisService.delete(Constant.IDENTIFY_CACHE_KEY + userId);
    }

    /*@Override
    public void updateStatus(String deptId) {
        int i = sysUserMapper.updateByDeptId(deptId);
        if (i <= 0){
            throw new BusinessException(BaseResponseCode.OPERATION_ERRO);
        }
    }

    @Override
    public SysUser selectUserInfoById(String userId) {
        return sysUserMapper.selectByPrimaryKey(userId);
    }*/


    /**
     * 水管理平台用户登录
     *
     * @param params{"ticket":"","page":""}
     * @return
     */
    @Override
    public LoginRespVO validateTicket(ValidateTicketVo ticketVo) {
        String ticket = ticketVo.getTicket();
        if (ticket.equals("61fe30e6ffb64e7288d9f2cdb79e6658")){
            return login(new LoginReqVO("15858500288","ygkj0818"));
        } else {
            //        String page = params.get("page");
            String type = ticketVo.getType();
            //如果ticket和page都为空，直接跳转到login页面
//        if (StringUtils.isEmpty(ticket) && StringUtils.isEmpty(page)) {
//            throw new BusinessException(BaseResponseCode.NOT_ACCOUNT);
//        }
            log.info("[水管理平台登录ticket][{}]", ticket);
            try {
                String systemType = ticketVo.getType();
                Map<String, String> judgeMap = judgeSystemAuthMap.getMap(systemType);
                AppConfig.WATERMANAGE_APPKEY = judgeMap.get("appKey");
                AppConfig.WATERMANAGE_APPSECRET = judgeMap.get("appSecret");
//                judgeStstem(systemType);
                SSOSdk ssoSdk = new SSOSdk();
                disableSslVerification();
                //ticket不为空则解析ticket
                //解析ticket
                Map<String, Object> map = ssoSdk.validateSsoTicket(ticket);
                String mobile = (String)map.get("mobile");
                String id = (String) map.get("userCode");
                //用户编码为空返回用户不存在
                if (StringUtils.isEmpty(mobile)) {
                    throw new BusinessException(BaseResponseCode.NOT_ACCOUNT);
                }
                //根据id查询用户
                LoginReqVO vo = sysUserMapper.selectByPhone(mobile);
                //用户不为空，用该用户登录
                if (vo != null) {
                    return login(vo);
                }
            } catch (Exception e) {
                log.error("[解析ticket出错]", e);
            }
            throw new BusinessException(BaseResponseCode.NOT_ACCOUNT);
        }

    }

    @Override
    public List<SysUser> users(String ids) {
        return sysUserMapper.selectByIds(Arrays.asList(ids.split(",")));
    }

    @Override
    public void putUserChooseProject(String key, String jsonString) {
        redisService.set(key,jsonString);
    }

    @Override
    public String getUserChooseProject(String key) {
        Object data = redisService.get(key);
        return data == null ? null : (String) data;
    }

    //    private LoginReqVO bindUser2Sys(JSONObject jsonObject) {
//        LoginReqVO vo = new LoginReqVO();
//        vo.setUsername("");
//        vo.setPassword("");
//        JSONObject user = jsonObject.getJSONObject("message");
//        JSONArray deptArr = user.getJSONArray("dept");
//        if (CollectionUtils.isNotEmpty(deptArr)) {
//            List<String> deptIdList = new ArrayList<>();
//            List<JSONObject> dingUserList = new ArrayList<>();
//            String userCode = user.getString("userCode");
//            String mobile = dingDingService.aesDecrypt(user, "mobile","");
//            String userName = dingDingService.aesDecrypt(user, "userName","");
//            String modifyTime = user.getString("modifyTime");
//            String trueName = user.getString("trueName");
//            Date now = new Date();
//            deptArr.forEach(o -> {
//                JSONObject dept = (JSONObject) o;
//                String deptId = dept.getString("deptId");
//                deptIdList.add(deptId);
//                JSONObject userInfo = new JSONObject();
//                userInfo.put("userCode", userCode + "_" + deptId);
//                userInfo.put("mobile", mobile);
//                userInfo.put("userName", userName);
//                userInfo.put("modifyTime", modifyTime);
//                userInfo.put("trueName", trueName);
//                dingUserList.add(userInfo);
//            });
//            //将该用户插入钉钉用户表
//            dingDingUserMapper.batchInsert(dingUserList);
//            //查出该用户所属的所有单位
//            List<DingDingDept> dingDeptList = dingDingDeptMapper.listByIds(deptIdList);
//            //如果系统中有钉钉单位数据则遍历钉钉单位，找出已经绑定了系统单位的钉钉单位
//            if (CollectionUtils.isNotEmpty(dingDeptList)) {
//                SysUser sysUser = new SysUser();
//                sysUser.setId(userCode);
//                sysUser.setUsername(trueName);
//                String salt = PasswordUtils.getSalt();
//                String password = PasswordUtils.encode(Constant.DEFAULT_PASSWORD, salt);
//                sysUser.setSalt(salt);
//                sysUser.setPassword(password);
//                sysUser.setPhone(mobile);
//                sysUser.setRealName(trueName);
//                sysUser.setCreateTime(now);
//                if (!StringUtils.isEmpty(modifyTime)) {
//                    try {
//                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//                        sysUser.setUpdateTime(sdf.parse(modifyTime));
//                    } catch (ParseException e) {
//                        log.error("[日期格式转换错误]{}", modifyTime, e);
//                    }
//                }
//                for (DingDingDept dingDingDept : dingDeptList) {
//                    String sysDept = dingDingDept.getSysDept();
//                    if (!StringUtils.isEmpty(sysDept)) {
//                        sysUser.setDeptId(sysDept);
//                        break;
//                    }
//                }
//                if (!StringUtils.isEmpty(sysUser.getDeptId())) {
//                    //加入用户表
//                    sysUserMapper.insert(sysUser);
//                    //初始化用户角色
//                    SysUserRole sysUserRole = new SysUserRole();
//                    sysUserRole.setUserId(userCode);
//                    sysUserRole.setId(UUID.randomUUID().toString());
//                    sysUserRole.setCreateTime(now);
//                    sysUserRole.setRoleId(ROLE_ID);
//                    sysUserRoleMapper.insert(sysUserRole);
//                    vo.setUsername(mobile);
//                    vo.setPassword(password);
//                }
//            }
//        }
//        return vo;
//    }

    /**
     * @return
     * @Author MJ
     * @Description SSL 安全验证
     * @Date 2019/9/12 12:48
     **/
    private void disableSslVerification() {
        try {
            // Create a trust manager that does not validate certificate chains
            TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }

                public void checkClientTrusted(X509Certificate[] certs, String authType) {
                }

                public void checkServerTrusted(X509Certificate[] certs, String authType) {
                }
            }
            };

            // Install the all-trusting trust manager
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

            // Create all-trusting host name verifier
            HostnameVerifier allHostsValid = new HostnameVerifier() {
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            };

            // Install the all-trusting host verifier
            HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
        } catch (NoSuchAlgorithmException e) {
            //logger.error(e.getMessage());
        } catch (KeyManagementException e) {
            // logger.error(e.getMessage());
        }
    }


    private void judgeStstem(String systemType){
        if (systemType.equals("2")){
            //数据管理系统
            AppConfig.WATERMANAGE_APPKEY = "7568dedf05264df69c2cf4105c790b4d";
            AppConfig.WATERMANAGE_APPSECRET = "58eb56d0da52473a";
        } else if (systemType.equals("1")){
            //一张图
            AppConfig.WATERMANAGE_APPKEY = "82f5607189d941e9baac01b197e11478";
            AppConfig.WATERMANAGE_APPSECRET = "99d88e20f18b4fe9";
        } else if (systemType.equals("3")){
            //水旱灾害防御分析
            AppConfig.WATERMANAGE_APPKEY = "3c10a52851624b78bbfd313f4baf054b";
            AppConfig.WATERMANAGE_APPSECRET = "9a989878ae2b466b";
        } else if (systemType.equals("4")){
            //水文数字化
            AppConfig.WATERMANAGE_APPKEY = "c359ae5473aa4b8080c285768a5b336e";
            AppConfig.WATERMANAGE_APPSECRET = "d2906fa819e74d23";
        } else if (systemType.equals("5")){
            //防洪减灾调度支撑
            AppConfig.WATERMANAGE_APPKEY = "108f0c3184a64c62a77ab450b32d7268";
            AppConfig.WATERMANAGE_APPSECRET = "ac0aa6e2cd7c4501";
        } else if (systemType.equals("6")){
            //取节水管理系统
            AppConfig.WATERMANAGE_APPKEY = "bf671d5b043b4ca798ceb8e00b762641";
            AppConfig.WATERMANAGE_APPSECRET = "33af1d77faa747ea";
        } else if (systemType.equals("7")){
            //万元GDP
            AppConfig.WATERMANAGE_APPKEY = "81ae455b1a3c43a8aa0868b9e5e0146a";
            AppConfig.WATERMANAGE_APPSECRET = "9a0ce83d0a7f4f96";
        } else if (systemType.equals("8")){
            //水资源承载能力
            AppConfig.WATERMANAGE_APPKEY = "133925b64de54fe0aa0886cc793e747f";
            AppConfig.WATERMANAGE_APPSECRET = "167c9c888b7d4081";
        } else if (systemType.equals("9")){
            //水利工程可视化监管
            AppConfig.WATERMANAGE_APPKEY = "dce5a48fca50488b8e38b9e329b83c48";
            AppConfig.WATERMANAGE_APPSECRET = "28224e613be944b3";
        } else if (systemType.equals("10")){
            //水土流失监管系统
            AppConfig.WATERMANAGE_APPKEY = "a69b85a2558d4a52ae3371238842d416";
            AppConfig.WATERMANAGE_APPSECRET = "c234db554c774840";
        } else if (systemType.equals("11")){
            //规划数字化系统
            AppConfig.WATERMANAGE_APPKEY = "1a6998bc57184e6baed5e4fc2c555e77";
            AppConfig.WATERMANAGE_APPSECRET = "3b8b67a8e1f646a0";
        } else if (systemType.equals("12")){
            //河湖综合监管系统
            AppConfig.WATERMANAGE_APPKEY = "60adb23ffedf4de4aded11d830cf1393";
            AppConfig.WATERMANAGE_APPSECRET = "773a60222d7a456e";
        } else if (systemType.equals("13")){
            //水域动态监管系统
            AppConfig.WATERMANAGE_APPKEY = "f0d36504d0f74b44bced019d73615b1b";
            AppConfig.WATERMANAGE_APPSECRET = "26bc71a61e7c481c";
        } else if (systemType.equals("14")){
            //水研究管理系统
            AppConfig.WATERMANAGE_APPKEY = "db0d4219471844eb93ed96d49082c7e4";
            AppConfig.WATERMANAGE_APPSECRET = "39822a11f9094c8d";
        } else if (systemType.equals("15")){
            //河道蓝线管理系统
            AppConfig.WATERMANAGE_APPKEY = "9b6a4b0cfa6746e4bb4022bc5e97a986";
            AppConfig.WATERMANAGE_APPSECRET = "bb762f0a73a640fe";
        } else if (systemType.equals("16")){
            //数据汇聚系统
            AppConfig.WATERMANAGE_APPKEY = "0205515f5c4346bfa3f87dde728ab2de";
            AppConfig.WATERMANAGE_APPSECRET = "4fae72fea7194bc7";
        } else if (systemType.equals("17")){
            //数据管理系统
            AppConfig.WATERMANAGE_APPKEY = "9deea5b1dcce42a3a062baae6074a489";
            AppConfig.WATERMANAGE_APPSECRET = "a22e2d7d39cd4f52";
        } else if (systemType.equals("18")){
            //平台综合管理
            AppConfig.WATERMANAGE_APPKEY = "f9c2fb0539cd40b295f652d53a92f0a3";
            AppConfig.WATERMANAGE_APPSECRET = "8619b7796fb04525";
        } else if (systemType.equals("19")){
            //移动综合应用
            AppConfig.WATERMANAGE_APPKEY = "27b0fe190b9b4e49afd1c43eb4c2ec78";
            AppConfig.WATERMANAGE_APPSECRET = "13652f81e50647c5";
        } else if (systemType.equals("20")){
            //智慧工地PC
            AppConfig.WATERMANAGE_APPKEY = "37da36411cf149d7a34438fdc2d9d135";
            AppConfig.WATERMANAGE_APPSECRET = "4d1e5e34bee74c03";
        } else if (systemType.equals("21")){
            //水利工程建设周期管理系统
            AppConfig.WATERMANAGE_APPKEY = "d9f7fe1a39984c3294306f108f0ba9bb";
            AppConfig.WATERMANAGE_APPSECRET = "634bb619e5f741a1";
        } else if (systemType.equals("22")){
            //OA系统
            AppConfig.WATERMANAGE_APPKEY = "398f756ec683420ba3d8156e4f58b88f";
            AppConfig.WATERMANAGE_APPSECRET = "2b2e90b263714dff";
        } else if (systemType.equals("23")){
            //智慧工地app
            AppConfig.WATERMANAGE_APPKEY = "6e03996b34f64966b9dccf3c348dd5df";
            AppConfig.WATERMANAGE_APPSECRET = "81e7bd5b11c34355";
        }
    }
}
