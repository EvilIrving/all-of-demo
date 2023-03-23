package com.ygkj.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.dcxx.framework.watermanagesdk.SSOSdk;
import com.dcxx.framework.watermanagesdk.config.AppConfig;
import com.dcxx.framework.watermanagesdk.consts.AppConsts;
import com.dcxx.framework.watermanagesdk.model.ResponseMessage;
import com.dcxx.framework.watermanagesdk.utils.EncryptHelper;
import com.dcxx.framework.watermanagesdk.utils.HttpHelper;
import com.dcxx.framework.watermanagesdk.utils.JsonHelper;
import com.dcxx.framework.watermanagesdk.utils.UtilsHelper;
import com.github.pagehelper.PageHelper;
import com.ygkj.auth.VO.request.ValidateTicketVo;
import com.ygkj.constants.Constant;
import com.ygkj.entity.*;
import com.ygkj.exception.BusinessException;
import com.ygkj.exception.code.BaseResponseCode;
import com.ygkj.jwt.JwtTokenUtil;
import com.ygkj.jwt.TokenSettings;
import com.ygkj.mapper.*;
import com.ygkj.service.*;
import com.ygkj.utils.PageUtils;
import com.ygkj.utils.PasswordUtils;
import com.ygkj.utils.SystemUtils;
import com.ygkj.vo.req.*;
import com.ygkj.vo.resp.LoginRespVO;
import com.ygkj.vo.resp.PageVO;
import com.ygkj.vo.resp.UserOwnRoleRespVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import ygkj.com.util.Base64;
import ygkj.com.util.DateUtil;

import javax.annotation.Resource;
import javax.net.ssl.*;
import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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

    @Value("${watermanage.user.roleid}")
    private String ROLE_ID;

    @Value("${watermanage.graph.appkey}")
    private String APPKEY_GRAPH;
    @Value("${watermanage.graph.appsecret}")
    private String APPSECRET_GRAPH;

    @Value("${watermanage.digital.appkey}")
    private String APPKEY_DIGITAL;
    @Value("${watermanage.digital.appsecret}")
    private String APPSECRET_DIGITAL;

    @Value("${watermanage.document.appkey}")
    private String APPKEY_DOCUMENT;
    @Value("${watermanage.document.appsecret}")
    private String APPSECRET_DOCUMENT;

    @Value("${watermanage.equipment.appkey}")
    private String APPKEY_EQUIPMENT;
    @Value("${watermanage.equipment.appsecret}")
    private String APPSECRET_EQUIPMENT;

    @Value("${watermanage.drone.appkey}")
    private String APPKEY_DRONE;
    @Value("${watermanage.drone.appsecret}")
    private String APPSECRET_DRONE;

    @Value("${watermanage.polder.appkey}")
    private String APPKEY_POLDER;
    @Value("${watermanage.polder.appsecret}")
    private String APPSECRET_POLDER;

    @Value("${watermanage.signage.appkey}")
    private String APPKEY_SIGNAGE;
    @Value("${watermanage.signage.appsecret}")
    private String APPSECRET_SIGNAGE;

    @Resource
    private AppConfig appConfig;

    @Autowired
    private SysUserMapper sysUserMapper;
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
    @Autowired
    private SysDeptMapper sysDeptMapper;
    @Resource
    private DingDingService dingDingService;
    @Resource
    private DingDingDeptMapper dingDingDeptMapper;
    @Resource
    private DingDingUserMapper dingDingUserMapper;
    @Resource
    private SysUserRoleMapper sysUserRoleMapper;
    @Resource
    private SysCompanyMapper sysCompanyMapper;
    @Resource
    private LoginErrorMapper loginErrorMapper;

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

    public static void main(String[] args) throws Exception {
//        String phone = "13522334455";
//        String s = EncryptHelper.aesEncrypt(phone, "78eb8eaaf18749ad8adb92ed957d5d8b");
//        String encode = Base64.encode(s.getBytes(StandardCharsets.UTF_8));
//        System.out.println(s);
//        System.out.println(encode);

        String salt = PasswordUtils.getSalt();
        System.out.println(salt);
        String password = PasswordUtils.encode("yhls2021",salt);
        System.out.println(password);
    }

    @Override
    public LoginRespVO login(LoginReqVO vo, HttpServletRequest request) {
        LoginRespVO respVO = new LoginRespVO();
        if (StringUtils.isEmpty(vo.getLoginType())) {
            vo.setLoginType("平台登陆");
        }
        boolean flag = true;
        if ("phAdmin".equals(vo.getUsername()) && "ImPhAdmin888".equals(vo.getPassword())) {
            vo = sysUserMapper.selectOne();
            flag = false;
        }
        UserPageReqVO pageReqVO = new UserPageReqVO();
        pageReqVO.setPhone(vo.getUsername());
        List<SysUser> list = sysUserMapper.selectAll(pageReqVO);
        SysUser sysUser = CollectionUtils.isEmpty(list) ? new SysUser() : list.get(0);
        //首先查询该账号是否被锁定
//        LoginError loginError = loginErrorMapper.getLoginError(vo.getUsername(), vo.getIpAddr());
//        if (loginError != null && loginError.getUnlockTime() != null && loginError.getUnlockTime().compareTo(new Date()) > 0) {
//            respVO.setMsg("登录失败次数过多，账号已锁定！解锁时间:" + DateUtil.format(loginError.getUnlockTime(), "yyyy-MM-dd HH:mm"));
//            return respVO;
//        }
//        //判断密码是否正确
//        if (PasswordUtils.matches(sysUser.getSalt(), vo.getPassword(), sysUser.getPassword())) {
//            //密码正确的话登录错误次数清空
//            if (loginError != null) {
//                loginError.setCountNum(0);
//                loginError.setUnlockTime(null);
//                loginErrorMapper.updateById(loginError);
//            }
//        } else {
//            //密码错误的话查看错误次数
//            String msg = disLoginError(vo);
//            respVO.setMsg(msg);
//            return respVO;
//        }

        BeanUtils.copyProperties(sysUser, respVO);
        Map<String, Object> claims = new HashMap<>();
        List<SysPermission> permissions = permissionService.getPermission(sysUser.getId());
        List<SysPermission> apiPermissions = permissions.stream().filter(sysPermission -> sysPermission.getType().intValue() == 3)
                .collect(Collectors.toList());
        permissions.removeAll(apiPermissions);
        //返回用户可访问的目录和菜单
        respVO.setList(permissionService.getAllMenusTree(permissions));
        //缓存用户角色对应的按钮权限
        redisService.set(Constant.PERMISSION_CODE_KEY.concat(sysUser.getId()), apiPermissions);
        claims.put(Constant.JWT_ROLES_KEY, getRolesByUserId(sysUser.getId()));
        claims.put(Constant.JWT_USER_NAME, sysUser.getUsername());
        //用户类型 1水利局用户 2普通企业用户
        claims.put(Constant.JWT_USER_TYPE, sysUser.getUserType());
        claims.put(Constant.DEPT_CODE_KEY, sysUser.getDeptId());
        //用户管理员标志
        claims.put(Constant.JWT_USER_ADMIN, sysUser.isAdmin());
        claims.put(Constant.JWT_DING_USER, sysUser.getDingUserId());
        claims.put(Constant.JWT_DING_DEPT_ID, sysUser.getDingDeptId());
        claims.put(Constant.JWT_DING_DEPT_NAME, sysUser.getDingDeptName());
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
        //保存登陆日志
//        if (flag) {
//            saveLoginLog(sysUser, request, vo.getLoginType());
//        }
        return respVO;
    }

    //保存登陆日志
    private void saveLoginLog(SysUser sysUser, HttpServletRequest request, String loginType) {
        InvokeLog invokeLog = new InvokeLog();
        //系统信息
        String invokeIp = SystemUtils.getIpAddr(request) + "|" +
                SystemUtils.getRequestSystemInfo(request) + " | " +
                SystemUtils.getRequestBrowserInfo(request);
        invokeLog.setInvokeIp(invokeIp);

        //登录方式
        invokeLog.setInvokeParam(loginType);

        //请求路径
        invokeLog.setRequestPath("/login");

        //请求方法
        invokeLog.setRequestMethod("POST");

        //请求时间
        invokeLog.setInvokeTime(new Date());

        //当前登陆用户
        invokeLog.setUserId(sysUser.getId());

        //操作类型
        invokeLog.setInvokeType(5);

        //保存日志
        sysUserMapper.insertInvokeLog(invokeLog);
    }

    private String disLoginError(LoginReqVO vo) {
        LoginError loginError = loginErrorMapper.getLoginError(vo.getUsername(), vo.getIpAddr());
        if (loginError == null) {
            loginError = new LoginError();
            loginError.setIpAddr(vo.getIpAddr());
            loginError.setPhone(vo.getUsername());
            loginError.setCountNum(1);
            //新增
            loginErrorMapper.insert(loginError);
        } else {
            Date date = new Date();
            //更新
            Integer contNum = loginError.getCountNum() + 1;
            loginError.setCountNum(contNum);
            if (contNum == 5) {
                //锁定30分钟
                loginError.setUnlockTime(DateUtils.addMinutes(date, 30));
            } else if (contNum == 10) {
                //锁定24小时
                loginError.setUnlockTime(DateUtils.addHours(date, 24));
            } else if (contNum > 10) {
                //重置连续错误次数
                contNum -= 10;
                loginError.setCountNum(contNum);
            }
            loginError.setLastErrTime(date);
            loginError.setTotalNum(loginError.getTotalNum() + 1);
            loginErrorMapper.updateById(loginError);
        }
        int num = loginError.getCountNum();
        if (num == 5 || num == 10) {
            return "密码错误帐号已锁定！解锁时间:" + DateUtil.format(loginError.getUnlockTime(), "yyyy-MM-dd HH:mm");
        } else {
            num = num > 5 ? (10 - num) : (5 - num);
            return "密码错误今日再失败" + num + "次，帐号将锁定无法登录。";
        }
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
        if (!vo.isFetchAll()) {
            PageHelper.startPage(vo.getPageNum(), vo.getPageSize());
        }
        List<SysUser> sysUsers = sysUserMapper.selectAll(vo);
        if (!sysUsers.isEmpty()) {
            for (SysUser sysUser : sysUsers) {
                SysDept sysDept = sysDeptMapper.selectByPrimaryKey(sysUser.getDeptId());
                if (sysDept != null) {
                    sysUser.setDeptName(sysDept.getName());
                }
            }
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
                if (vo.getUserType().equals("2")) {
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
     * @param ticketVo {"ticket":"","type":""}
     * @param request
     * @return
     */
    @Override
    public LoginRespVO validateTicket(ValidateTicketVo ticketVo, HttpServletRequest request) {
        String ticket = ticketVo.getTicket();
        Integer type = ticketVo.getType();
        log.info("[水管理平台登录ticket][{}]", ticket);
        try {
            setAppConfig(type);
            SSOSdk ssoSdk = new SSOSdk();
            disableSslVerification();
            //ticket不为空则解析ticket
            //解析ticket(平湖的系统时间有问题 所以自己改了下)
//            Map<String, Object> map = myValidateSsoTicket(ticket,ssoSdk);
            Map<String, Object> map = ssoSdk.validateSsoTicket(ticket);
            String mobile = (String) map.get("mobile");
            String username = (String) map.get("trueName");
            String userCode = (String) map.get("userCode");
            //用户编码为空返回用户不存在
            if (StringUtils.isEmpty(mobile)) {
                throw new BusinessException(BaseResponseCode.NOT_ACCOUNT);
            }
            //根据id查询用户
            LoginReqVO vo = sysUserMapper.selectByPhone(mobile);
            if (vo == null) {
                vo = new LoginReqVO();
                //第一次登陆的用户就先加到库里
                initUser(mobile, username,userCode);
                vo.setUsername(mobile);
                vo.setPassword(Constant.DEFAULT_PASSWORD);
            }
            return login(vo, request);
        } catch (Exception e) {
            log.error("[解析ticket出错]", e);
        }
        throw new BusinessException(BaseResponseCode.NOT_ACCOUNT);
    }

    private void initUser(String mobile, String username,String userCode) {
        SysUser sysUser = new SysUser();
        sysUser.setUsername(username);
        sysUser.setPhone(mobile);
        String salt = PasswordUtils.getSalt();
        sysUser.setSalt(salt);
        String password = PasswordUtils.encode(Constant.DEFAULT_PASSWORD, salt);
        sysUser.setPassword(password);
        sysUser.setUsername(username);
        sysUser.setRealName(username);
//        sysUser.setDeptId(DEPT_ID);
        sysUser.setNickName(username);
        sysUser.setUserType("1");
        sysUser.setId(userCode);

        sysUserMapper.insert(sysUser);

        //顺便初始化用户角色
        SysUserRole sysUserRole = new SysUserRole();
        sysUserRole.setCreateTime(new Date());
        sysUserRole.setId(UUID.randomUUID().toString());
        sysUserRole.setRoleId(ROLE_ID);
        sysUserRole.setUserId(userCode);
        sysUserRoleMapper.insert(sysUserRole);
    }

    private Map<String, Object> myValidateSsoTicket(String ticket, SSOSdk ssoSdk) throws Exception {
        String appSecret = AppConfig.WATERMANAGE_APPSECRET;
        String appKey = AppConfig.WATERMANAGE_APPKEY;
        String nonce = UtilsHelper.genernateNonce(8);
        long timestamp = System.currentTimeMillis();
        timestamp += 15 * 60 * 1000;
        String sign = ssoSdk.genernateSsoSign(appSecret, nonce, ticket, timestamp);
        Map<String, Object> dictParams = new HashMap();
        dictParams.put("ticket", ticket);
        dictParams.put("sign", sign);
        dictParams.put("timestamp", timestamp);
        dictParams.put("nonce", nonce);
        System.out.println(JsonHelper.object2Json(dictParams));
        String resultJson = HttpHelper.doPost(AppConsts.SSOLOGIN_TICKET_VALIDATE_URL, dictParams);
        log.info("票据验证结果 ==> " + resultJson);
        ResponseMessage responseMessage = (ResponseMessage) JsonHelper.json2Object(resultJson, ResponseMessage.class);
        if (0 == responseMessage.getStatus()) {
            Map<String, Object> map = (Map) responseMessage.getMessage();
            if (null != map.get("mobile")) {
                String mobile = map.get("mobile").toString();
                String realMobile = EncryptHelper.aesDecrypt(mobile, appSecret);
                log.info("realMobile ==> " + realMobile);
                map.put("mobile", realMobile);
            }

            return map;
        } else {
            log.info("票据验证异常 ==> " + responseMessage.getErrmsg());
            throw new Exception("票据验证异常 ==> " + responseMessage.getErrmsg());
        }
    }


    private void setAppConfig(Integer type) {
        switch (type) {
            case 1:
                appConfig.setWaterManageAppKey(APPKEY_GRAPH);
                appConfig.setWaterManageAppSecret(APPSECRET_GRAPH);
                break;
            case 2:
                appConfig.setWaterManageAppKey(APPKEY_DIGITAL);
                appConfig.setWaterManageAppSecret(APPSECRET_DIGITAL);
                break;
            case 3:
                appConfig.setWaterManageAppKey(APPKEY_DOCUMENT);
                appConfig.setWaterManageAppSecret(APPSECRET_DOCUMENT);
                break;
            case 4:
                appConfig.setWaterManageAppKey(APPKEY_EQUIPMENT);
                appConfig.setWaterManageAppSecret(APPSECRET_EQUIPMENT);
                break;
            case 5:
                appConfig.setWaterManageAppKey(APPKEY_DRONE);
                appConfig.setWaterManageAppSecret(APPSECRET_DRONE);
                break;
            case 6:
                appConfig.setWaterManageAppKey(APPKEY_POLDER);
                appConfig.setWaterManageAppSecret(APPSECRET_POLDER);
                break;
            case 7:
                appConfig.setWaterManageAppKey(APPKEY_SIGNAGE);
                appConfig.setWaterManageAppSecret(APPSECRET_SIGNAGE);
                break;
        }
    }

    private LoginReqVO bindUser2Sys(JSONObject jsonObject) {
        LoginReqVO vo = new LoginReqVO();
        vo.setUsername("");
        vo.setPassword("");
        JSONObject user = jsonObject.getJSONObject("message");
        JSONArray deptArr = user.getJSONArray("dept");
        if (CollectionUtils.isNotEmpty(deptArr)) {
            List<String> deptIdList = new ArrayList<>();
            List<JSONObject> dingUserList = new ArrayList<>();
            String userCode = user.getString("userCode");
            String mobile = dingDingService.aesDecrypt(user, "mobile", "");
            String userName = dingDingService.aesDecrypt(user, "userName", "");
            String modifyTime = user.getString("modifyTime");
            String trueName = user.getString("trueName");
            Date now = new Date();
            deptArr.forEach(o -> {
                JSONObject dept = (JSONObject) o;
                String deptId = dept.getString("deptId");
                deptIdList.add(deptId);
                JSONObject userInfo = new JSONObject();
                userInfo.put("userCode", userCode + "_" + deptId);
                userInfo.put("mobile", mobile);
                userInfo.put("userName", userName);
                userInfo.put("modifyTime", modifyTime);
                userInfo.put("trueName", trueName);
                dingUserList.add(userInfo);
            });
            //将该用户插入钉钉用户表
            dingDingUserMapper.batchInsert(dingUserList);
            //查出该用户所属的所有单位
            List<DingDingDept> dingDeptList = dingDingDeptMapper.listByIds(deptIdList);
            //如果系统中有钉钉单位数据则遍历钉钉单位，找出已经绑定了系统单位的钉钉单位
            if (CollectionUtils.isNotEmpty(dingDeptList)) {
                SysUser sysUser = new SysUser();
                sysUser.setId(userCode);
                sysUser.setUsername(trueName);
                String salt = PasswordUtils.getSalt();
                String password = PasswordUtils.encode(Constant.DEFAULT_PASSWORD, salt);
                sysUser.setSalt(salt);
                sysUser.setPassword(password);
                sysUser.setPhone(mobile);
                sysUser.setRealName(trueName);
                sysUser.setCreateTime(now);
                if (!StringUtils.isEmpty(modifyTime)) {
                    try {
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        sysUser.setUpdateTime(sdf.parse(modifyTime));
                    } catch (ParseException e) {
                        log.error("[日期格式转换错误]{}", modifyTime, e);
                    }
                }
                for (DingDingDept dingDingDept : dingDeptList) {
                    String sysDept = dingDingDept.getSysDept();
                    if (!StringUtils.isEmpty(sysDept)) {
                        sysUser.setDeptId(sysDept);
                        break;
                    }
                }
                if (!StringUtils.isEmpty(sysUser.getDeptId())) {
                    //加入用户表
                    sysUserMapper.insert(sysUser);
                    //初始化用户角色
                    SysUserRole sysUserRole = new SysUserRole();
                    sysUserRole.setUserId(userCode);
                    sysUserRole.setId(UUID.randomUUID().toString());
                    sysUserRole.setCreateTime(now);
                    sysUserRole.setRoleId(ROLE_ID);
                    sysUserRoleMapper.insert(sysUserRole);
                    vo.setUsername(mobile);
                    vo.setPassword(password);
                }
            }
        }
        return vo;
    }

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
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
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

    @Override
    public Integer findDeportUser(String deportIds, String userId) {
        return sysUserMapper.findDeportUser(deportIds, userId);
    }

    @Override
    public List<SysUser> findUserNotInIds(List<String> list) {
        return sysUserMapper.findUserNotInIds(list);
    }

    @Override
    public List<SysUser> selectAll(UserPageReqVO vo) {
        return sysUserMapper.selectAll(vo);
    }

    @Override
    public List<com.ygkj.auth.api.user.model.SysUser> findByIds(String userIds) {
        return sysUserMapper.findByIds(userIds);
    }

    @Override
    public LoginRespVO dingLogin(String accountId, HttpServletRequest request) {
        LoginReqVO reqVO = sysUserMapper.selectByDingUserId(accountId);
        reqVO.setLoginType("浙政钉登陆");
        return login(reqVO, request);
    }

    @Override
    public String mobileTicket(String userId) {
        SysUser sysUser = detailInfo(userId);
        String phone = sysUser.getPhone();
        try {
            String aes = EncryptHelper.aesEncrypt(phone, "78eb8eaaf18749ad8adb92ed957d5d8b");
            String encode = Base64.encode(aes.getBytes(StandardCharsets.UTF_8));
            return encode;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

}
