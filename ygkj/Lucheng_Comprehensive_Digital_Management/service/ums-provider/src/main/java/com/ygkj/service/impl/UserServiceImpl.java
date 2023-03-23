package com.ygkj.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.nacos.common.utils.UuidUtils;
import com.dcxx.framework.watermanagesdk.SSOSdk;
import com.dcxx.framework.watermanagesdk.config.AppConfig;
import com.github.pagehelper.PageHelper;
import com.ygkj.auth.VO.request.SingleSignOnRequestVo;
import com.ygkj.auth.VO.request.ValidateTicketVo;
import com.ygkj.client.AdcdClient;
import com.ygkj.client.SendFeignClient;
import com.ygkj.constants.Constant;
import com.ygkj.entity.*;
import com.ygkj.exception.BusinessException;
import com.ygkj.exception.code.BaseResponseCode;
import com.ygkj.jwt.JwtTokenUtil;
import com.ygkj.jwt.TokenSettings;
import com.ygkj.mapper.*;
import com.ygkj.platform.management.model.SysLoginLog;
import com.ygkj.properties.JudgeSystemAuthMap;
import com.ygkj.service.*;
import com.ygkj.utils.*;
import com.ygkj.utils.loginUtils.ZlbLoginUtils;
import com.ygkj.utils.loginUtils.ZwwLoginUtils;
import com.ygkj.utils.loginUtils.ZzdLoginUtils;
import com.ygkj.vo.req.*;
import com.ygkj.vo.resp.LoginRespVO;
import com.ygkj.vo.resp.PageVO;
import com.ygkj.vo.resp.UserOwnRoleRespVO;
import com.ygkj.warning.model.MsgSendRecord;
import com.ygkj.water.model.CommonCode;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.net.ssl.*;
import javax.servlet.http.HttpServletRequest;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Base64;
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

    private SnowFlake snowFlake = new SnowFlake(0, 0);

    @Value("${watermanage.user.roleid}")
    private String ROLE_ID;
    @Value("${watermanage.user.deptid}")
    private String DEPT_ID;
    @Value("${zzd.template.codemsg}")
    private String codeTemplate;

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
    private DingDingDeptMapper dingDingDeptMapper;
    @Resource
    private DingDingUserMapper dingDingUserMapper;
    @Resource
    private SysUserRoleMapper sysUserRoleMapper;
    @Resource
    private SysCompanyMapper sysCompanyMapper;

    @Autowired
    private AdcdClient adcdClient;

    @Autowired
    private JudgeSystemAuthMap judgeSystemAuthMap;
    @Resource
    private SysJobMapper sysJobMapper;
    @Resource
    private SysOpenUserBindMapper openUserBindMapper;
    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private SendFeignClient sendFeignClient;
    @Autowired
    private ISysLoginErrorService sysLoginErrorService;
    @Autowired
    private SysLogMapper sysLogMapper;
    @Resource
    protected HttpServletRequest request;
    @Autowired
    private ZlbLoginUtils zlbLoginUtils;
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
        String phone = "";
        if (ObjectUtils.isNotEmpty(vo.getIsEncryption()) && vo.getIsEncryption()){
            //进行这一步是因为单点登录时没有对手机号加密
            phone = new String(Base64.getDecoder().decode(vo.getUsername()));
            String passWord = RSAUtils.decryptBase64(vo.getPassword());
            vo.setPassword(passWord);
        }else {
            phone = vo.getUsername();
        }
        pageReqVO.setPhone(phone);
        List<SysUser> list = sysUserMapper.selectAll(pageReqVO);
        LoginRespVO respVO = new LoginRespVO();
        if (list.isEmpty()) {
            //PC端不会进入这个分支
            //只有外部用户会进入这个分支
            //校核验证码
            String key = Constant.VERIFICATION_CODE_KEY.concat(phone);
            if (redisService.hasKey(key) && redisService.get(key).equals(vo.getPassword())) {
                List<SysUser> users = sysUserMapper.selectBlockUser(phone);
                if (!users.isEmpty()) {
                    SysUser sysUser = users.get(0);
                    Map<String, Object> claims = new HashMap<>();
                    claims.put(Constant.EXTERNAL_KEY, Boolean.TRUE);
                    String username = sysUser.getUsername();
                    claims.put(Constant.JWT_USER_NAME, username);
                    respVO.setUsername(username);
                    String token = JwtTokenUtil.getAccessToken(sysUser.getPhone(), claims);
                    respVO.setAccessToken(token);
                    redisService.set(Constant.ACCESS_TOKEN.concat(token), token, 3l, TimeUnit.DAYS);
                    redisService.delete(key);
                    return respVO;
                }
            } else {
                throw new BusinessException(BaseResponseCode.PASSWORD_ERROR);
            }
        } else {
            SysUser sysUser = list.get(0);
            if (sysUser.getStatus() == 2) {
                throw new BusinessException(BaseResponseCode.USER_LOCK);
            }
            if (sysUser.getStatus() == 3) {
                throw new BusinessException(BaseResponseCode.USER_DEACTIVATE);
            }
            System.out.println(PasswordUtils.encode(vo.getPassword(), sysUser.getSalt()));
            String ipAddr = IPUtils.getIpAddr(request);

            //有这个账号存在，判断是否锁定
            SysLoginError loginError = sysLoginErrorService.getLoginError(sysUser.getId(), ipAddr);
            if (loginError != null && loginError.getUnlockTime() != null && loginError.getUnlockTime().compareTo(new Date()) > 0) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                respVO.setLockMsg("登录失败次数过多，账号已锁定！解锁时间:" + sdf.format(loginError.getUnlockTime()));
                return respVO;
            }
            if (!vo.getPassword().equals(sysUser.getPassword()) && !PasswordUtils.matches(sysUser.getSalt(), vo.getPassword(), sysUser.getPassword())) {
                String err;
                SysLoginError sysLoginError = sysLoginErrorService.saveLoginError(sysUser.getId(), ipAddr, new Date());
                int num = sysLoginError.getContNum();
                if (num == 5 || num == 10) {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    err = "密码错误帐号已锁定！解锁时间:" + sdf.format(loginError.getUnlockTime());
                } else {
                    num = num > 5 ? (10 - num) : (5 - num);
                    err = "密码错误今日再失败" + num + "次，帐号将锁定无法登录。";
                }
                respVO.setLockMsg(err);
                return respVO;
//                throw new BusinessException(BaseResponseCode.PASSWORD_ERROR);
            }
            //登录错误次数清空
            if (loginError != null) {
                loginError.setContNum(0);
                sysLoginErrorService.updateById(loginError);
            }
            BeanUtils.copyProperties(sysUser, respVO);
            Map<String, Object> claims = new HashMap<>();
            claims.put(Constant.EXTERNAL_KEY, Boolean.FALSE);
            String userId = sysUser.getId();
            List<SysPermission> permissions = permissionService.getPermission(userId, org.apache.commons.lang3.StringUtils.EMPTY);
            if ( !CollectionUtils.isEmpty(permissions)) {
                List<SysPermission> apiPermissions = permissions.stream().filter(sysPermission -> sysPermission.getType().intValue() == 3)
                        .collect(Collectors.toList());
                permissions.removeAll(apiPermissions);
                //返回用户可访问的目录和菜单
                respVO.setList(permissionService.getAllMenusTree(permissions));
                //缓存用户角色对应的按钮权限
                redisService.set(Constant.PERMISSION_CODE_KEY.concat(userId), apiPermissions);
            }
            if ("3".equals(sysUser.getUserType())){
                SysDept sysDept = new SysDept();
                JSONObject companybyPhone = sysUserMapper.getCompanybyPhone(phone);
                sysDept.setName(companybyPhone.getString("company_name"));
                sysUser.setDept(sysDept);
            }
            claims.put(Constant.DEPARTMENT_NAME_KEY, sysUser.getDept().getName());
            List<String> roleList = getRolesByUserId(userId);
            claims.put(Constant.JWT_ROLES_KEY, roleList);
            claims.put(Constant.JWT_USER_NAME, sysUser.getUsername());
            //用户类型 1水利局用户 2普通企业用户
            claims.put(Constant.JWT_USER_TYPE, sysUser.getUserType());
            claims.put(Constant.DEPT_CODE_KEY, sysUser.getDeptId());
            //用户管理员标志
            claims.put(Constant.JWT_USER_ADMIN, sysUser.isAdmin());
            String access_token = JwtTokenUtil.getAccessToken(userId, claims);
            String refresh_token;
//        if(vo.getType().equals("1")){
//            refresh_token=JwtTokenUtil.getRefreshToken(sysUser.getId(),claims);
//        }else {
//            refresh_token=JwtTokenUtil.getRefreshAppToken(sysUser.getId(),claims);
//        }
            refresh_token = JwtTokenUtil.getRefreshToken(userId, claims);
            respVO.setAccessToken(access_token);
            respVO.setRefreshToken(refresh_token);
            respVO.setSlogan(sysUser.getSlogan());
            respVO.setUserType(sysUser.getUserType());
            respVO.setDepartment(sysUser.getDept().getName());
            respVO.setDeptId(sysUser.getDeptId());
            respVO.setJobId(sysUser.getJobId());
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            SysLoginLog loginLog = new SysLoginLog(userId, sysUser.getUsername(), sysUser.getDeptId(), sysUser.getDept().getName(), IPUtils.getIpAddr(request), new Date());
            sysUserMapper.insertLoginLog(loginLog);
            redisService.set(Constant.ACCESS_TOKEN.concat(access_token), access_token, 3l, TimeUnit.HOURS);
            //总登录排行榜
            redisService.zincrby(Constant.LOGIN_RANK_KEY, 1, userId);
            //部门登录排行榜
            redisService.zincrby(Constant.LOGIN_RANK_KEY.concat("_").concat(sysUser.getDeptId()), 1, userId);
            return respVO;
        }
        throw new BusinessException(BaseResponseCode.PASSWORD_ERROR);
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
        List<String> adcdList = new ArrayList<>();
        if (!sysUsers.isEmpty()) {
            for (SysUser sysUser : sysUsers) {
                SysDept sysDept = sysDeptMapper.selectByPrimaryKey(sysUser.getDeptId());
                if (sysDept != null) {
                    sysUser.setDeptName(sysDept.getName());
                    String adcd = sysDept.getAdcd();
                    String replace = adcd.replace("\\", "");
                    sysUser.setAdcd(replace);
                    if (!adcdList.contains(adcdList.add(replace))) {
                        adcdList.add(replace);
                    }
                }
                if (!StringUtils.isEmpty(sysUser.getJobId())){
                    List<SysJob> jobs = sysJobMapper.getJobByIds(sysUser.getJobId());
                    List<String> strings = jobs.stream().map(SysJob::getJobName).collect(Collectors.toList());
                    sysUser.setJobName(String.join(",",strings));
                }
            }
            if (!CollectionUtils.isEmpty(adcdList)){
                List<LinkedHashMap> attAdcdList = (List<LinkedHashMap>) adcdClient.selectAdnmByAdcdList(adcdList).getData();
                Map<Object, List<LinkedHashMap>> collect = attAdcdList.stream().collect(Collectors.groupingBy(map -> map.get("adcd")));
                sysUsers.stream().filter(u -> collect.containsKey(u.getAdcd())).forEach(u -> u.setAdnm(String.valueOf(collect.get(u.getAdcd()).get(0).get("adnm"))));
            }
        }
        return PageUtils.getPageVO(sysUsers);
    }

    @Override
    public SysUser detailInfo(String userId) {

        return sysUserMapper.selectByPrimaryKey(userId);
    }

    @Override
    public PageVO<SysUser> selectUserInfoByDeptIds(String deptIds) {

//        PageHelper.startPage(pageNum, pageSize);
        List<SysUser> list = sysUserMapper.selectUserInfoByDeptIds(Arrays.asList(deptIds));
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

//        redisService.set(Constant.JWT_REFRESH_TOKEN_BLACKLIST + accessToken, userId, JwtTokenUtil.getRemainingTime(accessToken), TimeUnit.MILLISECONDS);
//
//        redisService.set(Constant.JWT_REFRESH_TOKEN_BLACKLIST + refreshToken, userId, JwtTokenUtil.getRemainingTime(refreshToken), TimeUnit.MILLISECONDS);

        redisService.delete("authorization".concat(accessToken));
        redisService.delete(Constant.IDENTIFY_CACHE_KEY + userId);
    }

    @Override
    public void updatePwd(UpdatePasswordReqVO vo, String userId, String accessToken, String refreshToken) {
        if (ObjectUtils.isNotEmpty(vo.getIsEncryption()) && vo.getIsEncryption()){
            String oldPwd = new String(Base64.getDecoder().decode(vo.getOldPwd()));
            vo.setOldPwd(oldPwd);
            String newPwd = new String(Base64.getDecoder().decode(vo.getNewPwd()));
            vo.setNewPwd(newPwd);
        }
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
    public List<SysUser> getWaterManageUserListByDeptIds(List<String> waterManageDeptIds,String key,Boolean flag) {
        return sysUserMapper.selectWaterManageUserListByDeptIds(waterManageDeptIds,key,flag);
    }


    @Override
    public PageVO<SysUser> getUserListByKey(UserPageReqVO vo) {

        List<SysDept> parentChild = sysDeptMapper.findParentChild(vo.getDeptId());
        List<String> deptIds = parentChild.stream().map(SysDept::getId).collect(Collectors.toList());
        deptIds.add(vo.getDeptId());
        List<SysUser> sysUsers = sysUserMapper.selectUserInfoByDeptIds(deptIds);
        if (!StringUtils.isEmpty(vo.getUsername())){
            sysUsers = sysUsers.stream().filter(s -> s.getUsername().contains(vo.getUsername())).collect(Collectors.toList());
        }
        return PageUtils.getPageVO(sysUsers);
    }

    @Override
    public void bindUserJob(String userId, String jobIds) {
        sysUserMapper.bindUserJob(userId,jobIds);
    }

    @Override
    public void removeJob(String jobId) {
        //先查询所有绑定该角色id的用户
        List<SysUser> users = sysUserMapper.loadUserByJob(jobId);
        if (users.size() > 0){
            for (SysUser user : users) {
                String jobIds = user.getJobId();
                String[] split = jobIds.split(",");
                List<String> jobIdList = Arrays.asList(split);
                jobIdList.remove(jobId);
                String join = String.join(",", jobIdList);
                user.setJobId(join);
            }
            sysUserMapper.batchUpdate(users);
        }
    }

    @Override
    public void enableUser(String id, String status) {
        sysUserMapper.enableUser(id,status);
    }

    @Override
    public LoginRespVO zzdLogin(SingleSignOnRequestVo vo) throws Exception {
        if (vo.getIsNeedBind() == true){

            String key = getVerificationKey(vo.getUsername());
            String code = (String) redisService.get(key);
            if (StringUtils.isEmpty(code) || !code.equals(vo.getVerificationCode())) {
                throw new Exception(CommonCode.VERIFICATION_CODE_FAILED.message());
            }

            LoginReqVO reqVO = sysUserMapper.selectByPhone(vo.getUsername());
            if (ObjectUtils.isEmpty(reqVO)){
                throw new Exception(CommonCode.NOT_ACCOUNT.message());
            }

            LoginRespVO login = login(reqVO);
            if (login != null && !StringUtils.isEmpty(login.getAccessToken())){
                SysOpenUserBind userBind = new SysOpenUserBind();
//                log.info("绑定的openid："+vo.getOpenid());
                userBind.setOpenid(vo.getOpenid());
                UserPageReqVO pageReqVO = new UserPageReqVO();
                String phone = vo.getUsername();
                pageReqVO.setPhone(phone);
                List<SysUser> list = sysUserMapper.selectAll(pageReqVO);
                userBind.setUserid(list.get(0).getId());
                if (StringUtils.isEmpty(vo.getPlattype())){
                    userBind.setPlattype("1");
                }
                if (userBind.getOpenid() != null) {
                    openUserBindMapper.insert(userBind);
                    login.setIsBind(true);
                }else {
                    login.setIsBind(false);
                }
            }
            //清除验证码
            if (redisService.hasKey(key)){
                redisService.delete(key);
            }
            return login;

        }

        String token = ZzdLoginUtils.getToken();

        JSONObject dingTalkAppUser = ZzdLoginUtils.getDingTalkAppUser(token, vo.getAuthCode());
        if (dingTalkAppUser != null){
            Long tenantId = dingTalkAppUser.getLong("tenantId");
            String accountId = dingTalkAppUser.getString("accountId");
            ZzdLoginUtils.getAppUsedRange(tenantId, new Long(1),accountId, new Long(1));
//            log.info("isPermission:"+ZzdLoginUtils.isPermission);
            if (!ZzdLoginUtils.isPermission){
                throw new Exception(CommonCode.FORBIDDEN.message());
            }
            String openid = dingTalkAppUser.getString("openid");
//            log.info("第一次的openid："+openid);
            vo.setOpenid(openid);
        }else {
            throw new Exception(CommonCode.NOT_ACCOUNT.message());
        }

        //根据openid查询钉用户是否跟本地用户有绑定关系
        if (StringUtils.isEmpty(vo.getPlattype())) {
            vo.setPlattype("1");
        }
        SysOpenUserBind bind = openUserBindMapper.selectBind(vo);
        if (bind == null) {
            LoginRespVO loginRespVO = new LoginRespVO();
            loginRespVO.setOpenid(vo.getOpenid());
//            log.info("返回的openid："+loginRespVO.getOpenid());
            loginRespVO.setIsBind(false);
//            log.info("第一次跳转失败的返回信息:"+loginRespVO);
            return loginRespVO;
        }
        SysUser user = sysUserMapper.selectByPrimaryKey(bind.getUserid());
        LoginReqVO loginReqVO = sysUserMapper.selectByPhone(user.getPhone());
        LoginRespVO login = login(loginReqVO);
        login.setIsBind(true);
        return login;
    }

    @Override
    public void sendVerificationCode(String phone) {

        //生成一个随机数
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 6; i++) {
            sb.append(random.nextInt(10));
        }
        String verificationCode = sb.toString();
        String msg = String.format(codeTemplate, verificationCode);
        MsgSendRecord msgSendRecord = new MsgSendRecord();
        msgSendRecord.setReceivePhone(phone);
        msgSendRecord.setSendType(1);
        msgSendRecord.setContext(msg);
        msgSendRecord.setBusinessType(3);
        //将验证码加入redis
        redisService.set(getVerificationKey(phone), verificationCode, 10, TimeUnit.MINUTES);
        sendFeignClient.send(msgSendRecord);

    }

    @Override
    public LoginRespVO zlbLogin(SingleSignOnRequestVo vo) throws Exception {
        log.info("ticketId====" + vo.getTicketId());
        //获取token
        String token = zlbLoginUtils.getTokenByTicketId(vo.getTicketId());
        log.info("token====" +token);
        //获取用户信息
        JSONObject userInfo = zlbLoginUtils.getUserInfoByToken(token);
        JSONObject personInfo = userInfo.getJSONObject("personInfo");
        log.info(JSON.toJSONString(userInfo));

        LoginReqVO loginReqVO = sysUserMapper.selectByPhone(personInfo.getString("phone"));
        if (loginReqVO == null){
            throw new Exception(CommonCode.FORBIDDEN.message());
        }
        LoginRespVO login = login(loginReqVO);
        //前端跳转标准版和老化版需要登录信息，给一个票据存redis,通过redis获取用户信息
        String ticketId = "ZlbTicket_" + UUID.randomUUID().toString();
        redisService.set(ticketId, JSON.toJSONString(login),3l, TimeUnit.HOURS);
        login.setTicketId(ticketId);
        return login;
    }

    @Override
    public LoginRespVO zwwLogin(SingleSignOnRequestVo vo) throws Exception{
        log.info("ticketId====" + vo.getTicketId());
        //接入资源验证临时票据的有效性
        String token = zlbLoginUtils.ZwwGetTokenByTicketId(vo.getTicketId());
        log.info("token====" +token);
        //接入资源使用令牌获取用户详细信息
        JSONObject userInfo = zlbLoginUtils.ZwwGetUserInfoByToken(token);
        log.info(JSON.toJSONString(userInfo));

        LoginReqVO loginReqVO = sysUserMapper.selectByPhone(userInfo.getString("mobile"));
        if (loginReqVO == null){
            throw new Exception(CommonCode.FORBIDDEN.message());
        }
        LoginRespVO login = login(loginReqVO);
        //前端跳转标准版和老化版需要登录信息，给一个票据存redis,通过redis获取用户信息
        String ticketId = "ZlbTicket_" + UUID.randomUUID().toString();
        redisService.set(ticketId, JSON.toJSONString(login),3l, TimeUnit.HOURS);
        login.setTicketId(ticketId);
        return login;
    }

    @Override
    public LoginRespVO getInfoByTicket(String ticketId) {
        if (!StringUtils.isEmpty(ticketId)){
            Object object = redisService.get(ticketId);
            if (object != null){
                LoginRespVO loginRespVO = JSONObject.parseObject(String.valueOf(object), LoginRespVO.class);
                return loginRespVO;
            }
        }
        return null;
    }

    private String getVerificationKey(String phone) {
        return new StringBuilder("bindphone_").append(phone)
                .toString();
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
     * @param ticketVo{"ticket":"","type":""}
     * @return
     */
    @Override
    public LoginRespVO validateTicket(ValidateTicketVo ticketVo) {
        String ticket = ticketVo.getTicket();
        log.info("[params is ] {}", ticketVo.toString());
        try {
            String systemType = ticketVo.getType();
            Map<String, String> judgeMap = judgeSystemAuthMap.getMap(systemType);
//            AppConfig.WATERMANAGE_APPKEY = judgeMap.get("appKey");
//            AppConfig.WATERMANAGE_APPSECRET = judgeMap.get("appSecret");
            SSOSdk ssoSdk = new SSOSdk(judgeMap.get("appKey"),judgeMap.get("appSecret"), AppConfig.WATERMANAGE_AUTH_URL);
            disableSslVerification();
            //ticket不为空则解析ticket
            //解析ticket
            Map<String, Object> map = ssoSdk.validateSsoTicket(ticket);
            log.info("[水管理平台ticket解析结果] {}", map);
            String mobile = (String) map.get("mobile");
            String username = (String) map.get("trueName");
            String userCode = (String) map.get("userCode");
            //用户编码为空返回用户不存在
            if (StringUtils.isEmpty(mobile)) {
                throw new BusinessException(BaseResponseCode.NOT_ACCOUNT);
            }
            //根据手机号查询用户
            LoginReqVO vo = sysUserMapper.selectByPhone(mobile);

            if (vo == null) {
                vo = new LoginReqVO();
                //第一次登陆的用户就先加到库里
                initUser(mobile, username,userCode);
                vo.setUsername(mobile);
                vo.setPassword(Constant.DEFAULT_PASSWORD);
            }
            vo.setIsEncryption(false);
            return login(vo);
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
        sysUser.setDeptId(DEPT_ID);
        sysUser.setNickName(username);
        sysUser.setUserType("1");
//        String userid = SnowFlake.norSnow.nextId() + "";
        sysUser.setId(userCode);

        //顺便初始化用户角色
        List<SysJob> sysJobs = sysJobMapper.getJobByName("普通管理员");
        if (sysJobs.size() > 0){
            SysJob sysJob = sysJobs.get(0);
            sysUser.setJobId(sysJob.getId());
        }

        sysUserMapper.insert(sysUser);

        //顺便初始化用户角色
//        SysUserRole sysUserRole = new SysUserRole();
//        sysUserRole.setCreateTime(new Date());
//        sysUserRole.setId(UUID.randomUUID().toString());
//        sysUserRole.setRoleId(ROLE_ID);
//        sysUserRole.setUserId(userCode);
//        sysUserRoleMapper.insert(sysUserRole);
    }

    @Override
    public List<SysUser> users(String ids) {
        List<String> list = StringUtils.isEmpty(ids) ? null : Arrays.asList(ids.split(","));
        return sysUserMapper.selectByIds(list);
    }

    @Override
    public void putUserChooseProject(String key, String jsonString) {
        redisService.set(key, jsonString);
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

    /**
     * 工程运管登陆
     *
     * @param userCode
     * @return
     */
    @Override
    public LoginRespVO rscpLogin(String userCode) {
        LoginReqVO loginReqVO = sysUserMapper.selectByUserCode(userCode);
        LoginRespVO respVO;
        if (loginReqVO == null) {
            loginReqVO = sysUserMapper.selectByPhone("15724219840");
            respVO = login(loginReqVO);
            respVO.setUsername("");
            respVO.setPhone("");
            respVO.setDepartment("");
            respVO.setUserType("");
            respVO.setId("");
        } else {
            respVO = login(loginReqVO);
        }
        return respVO;
    }

    @Override
    public void delWyhPerson(String wyhId) {
        sysUserMapper.delWyhPerson(wyhId);
    }

    @Override
    public void saveWyhUser(UserAddReqVO vo) {
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
            String userId = UUID.randomUUID().toString();
            sysUser.setId(userId);
            sysUser.setCreateTime(new Date());
            List<SysJob> sysJobs = sysJobMapper.getJobByName("普通用户");
            if (sysJobs.size() > 0){
                SysJob sysJob = sysJobs.get(0);
                sysUser.setJobId(sysJob.getId());
            }
            int i = sysUserMapper.insertSelective(sysUser);
            if (i != 1) {
                throw new BusinessException(BaseResponseCode.OPERATION_ERRO);
            }
//
//            SysUserRole sysUserRole = new SysUserRole();
//            sysUserRole.setCreateTime(new Date());
//            sysUserRole.setId(UUID.randomUUID().toString());
//            sysUserRole.setRoleId("1");
//            sysUserRole.setUserId(userId);
//            sysUserRoleMapper.insert(sysUserRole);
        } else {
            throw new BusinessException(BaseResponseCode.DUPLICATE_PHONE);
        }
    }

    @Override
    public List<WyhUserVo> getWyhUserList(WyhUserVo wyhUserVo) {
        return sysUserMapper.getWyhUserList(wyhUserVo);
    }

    @Override
    public void delWyhUser(String id) {
        sysUserMapper.delWyhUser(id);
    }

    @Override
    public void updateWyhPwd(SysUser sysUser, String password) {
        sysUser.setUpdateTime(new Date());
        sysUser.setPassword(PasswordUtils.encode(password, sysUser.getSalt()));
        int i = sysUserMapper.updateByPrimaryKeySelective(sysUser);
        if (i != 1) {
            throw new BusinessException(BaseResponseCode.OPERATION_ERRO);
        }
    }

    @Override
    public List<com.ygkj.auth.api.user.model.SysUser> selectUserByIdList(List<String> idList) {
        return sysUserMapper.selectUserByIdList(idList);
    }

    @Override
    public List<String> getWaterManageDeptIds() {
        return sysUserMapper.selectWaterManageDeptIds();
    }
}
