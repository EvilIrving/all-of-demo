package com.ygkj.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ygkj.aop.annotation.LogAnnotation;
import com.ygkj.auth.VO.request.SingleSignOnRequestVo;
import com.ygkj.auth.VO.request.ValidateTicketVo;
import com.ygkj.constants.Constant;
import com.ygkj.entity.SysUser;
import com.ygkj.exception.BusinessException;
import com.ygkj.exception.code.BaseResponseCode;
import com.ygkj.jwt.JwtTokenUtil;
import com.ygkj.service.UserService;
import com.ygkj.utils.DESUtil;
import com.ygkj.utils.DataResult;
import com.ygkj.utils.PageUtils;
import com.ygkj.utils.RSAUtils;
import com.ygkj.vo.req.*;
import com.ygkj.vo.resp.LoginRespVO;
import com.ygkj.vo.resp.PageVO;
import com.ygkj.vo.resp.UserOwnRoleRespVO;
import com.ygkj.water.model.CommonCode;
import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName: UserController
 * TODO:类文件简单描述
 * @Author: Huang.zh
 * @CreateDate: 2019/9/3 18:16
 * @UpdateUser: Huang.zh
 * @UpdateDate: 2019/9/3 18:16
 * @Version: 0.0.1
 */
@RestController
@Api(tags = "组织模块-用户管理")
@RequestMapping("/sys")
public class UserController {
    @Autowired
    private UserService userService;

    @Value("${access.key}")
    private String key;

    @Value("${checkUrl}")
    private String checkUrl;

    @Value("${access.secret}")
    private String secret;

    @PostMapping(value = "/user/login")
    @ApiOperation(value = "用户登录接口")
    public LoginRespVO login(@RequestBody @Valid LoginReqVO vo) {
        return userService.login(vo);
    }

    @PostMapping("/user/rdspwd")
    public Map<String, Object> rdspwd(){
        Map<String, Object> map = new HashMap<>();
        try {
            String publicKey = RSAUtils.generateBase64PublicKey();
            map.put("success", true);
            map.put("key", publicKey);
        } catch (Exception e) {
            map.put("success", false);
        }
        return map;
    }

    @PostMapping("/user/validateTicket")
    @ApiOperation(value = "水管理平台登录")
    public LoginRespVO validateTicket(@RequestBody ValidateTicketVo vo) {
        return userService.validateTicket(vo);
    }

    @PostMapping("/user/zzdLogin")
    @ApiOperation(value = "浙政钉登录")
    public CommonResult zzdLogin(@RequestBody SingleSignOnRequestVo vo) {
        LoginRespVO login = new LoginRespVO();
        try{
            login = userService.zzdLogin(vo);
            if (login.getIsBind().equals(false)){
                return CommonResult.success(login, "首次需要和手机号绑定");
            }
            if (org.springframework.util.StringUtils.isEmpty(login.getAccessToken())) {
                return CommonResult.failed("用户名或密码错误");
            }
        }catch (Exception e){
            if (CommonCode.FORBIDDEN.message().equals(e.getMessage())){
                return CommonResult.failed(CommonCode.FORBIDDEN,CommonCode.FORBIDDEN.message());
            }
            if (CommonCode.VERIFICATION_CODE_FAILED.message().equals(e.getMessage())){
                return CommonResult.failed(CommonCode.VERIFICATION_CODE_FAILED,CommonCode.VERIFICATION_CODE_FAILED.message());
            }
            if (CommonCode.NOT_ACCOUNT.message().equals(e.getMessage())){
                return CommonResult.failed(CommonCode.NOT_ACCOUNT,CommonCode.NOT_ACCOUNT.message());
            }
        }
        return CommonResult.success(login, "登录成功");
    }

    @PostMapping("/user/zlbLogin")
    @ApiOperation(value = "浙里办微信登录")
    public CommonResult zlbLogin(@RequestBody SingleSignOnRequestVo vo) {
        LoginRespVO login = new LoginRespVO();
        try{
            login = userService.zlbLogin(vo);
            if (org.springframework.util.StringUtils.isEmpty(login.getAccessToken())) {
                return CommonResult.failed("用户名或密码错误");
            }
        }catch (Exception e){
            if (CommonCode.FORBIDDEN.message().equals(e.getMessage())){
                return CommonResult.failed(CommonCode.FORBIDDEN,CommonCode.FORBIDDEN.message());
            }
            return CommonResult.failed(e.getMessage());
        }
        return CommonResult.success(login, "登录成功");
    }

    @PostMapping("/user/zwwLogin")
    @ApiOperation(value = "浙里办APP、支付宝登录")
    public CommonResult zwwLogin(@RequestBody SingleSignOnRequestVo vo) {
        LoginRespVO login = new LoginRespVO();
        try{
            login = userService.zwwLogin(vo);
            if (org.springframework.util.StringUtils.isEmpty(login.getAccessToken())) {
                return CommonResult.failed("用户名或密码错误");
            }
        }catch (Exception e){
            if (CommonCode.FORBIDDEN.message().equals(e.getMessage())){
                return CommonResult.failed(CommonCode.FORBIDDEN,CommonCode.FORBIDDEN.message());
            }
            return CommonResult.failed(e.getMessage());
        }
        return CommonResult.success(login, "登录成功");
    }

    @GetMapping("/user/getInfoByTicket")
    @ApiOperation(value = "浙里办app跳转获取用户信息")
    public CommonResult getInfoByTicket(@RequestParam String ticketId) {
        LoginRespVO login = userService.getInfoByTicket(ticketId);
        if (ObjectUtils.isEmpty(login)){
            return CommonResult.failed("登录信息不存在");
        }
        return CommonResult.success(login, "登录成功");
    }

    @GetMapping("/user/sendVerificationCode")
    @ApiOperation(value = "浙政钉绑定手机号")
    public void sendVerificationCode(String phone) {
        userService.sendVerificationCode(phone);
    }

    @PostMapping("/user/register")
    @ApiOperation(value = "用户注册接口")
    public DataResult<String> register(@RequestBody @Valid RegisterReqVO vo) {
        DataResult<String> result = DataResult.success();
        result.setData(userService.register(vo));
        return result;
    }

    @GetMapping("/user/token")
    @ApiOperation(value = "用户刷新token接口")
    @LogAnnotation(title = "用户管理", action = "用户刷新token")
    public DataResult<String> refreshToken(HttpServletRequest request) {
        String refreshToken = request.getHeader(Constant.REFRESH_TOKEN);
        String accessToken = request.getHeader(Constant.ACCESS_TOKEN);
        DataResult<String> result = DataResult.success();
        result.setData(userService.refreshToken(refreshToken, accessToken));
        return result;
    }

    @GetMapping("/user/unLogin")
    @ApiOperation(value = "引导客户端去登录")
    public DataResult unLogin() {
        DataResult result = DataResult.getResult(BaseResponseCode.TOKEN_ERROR);
        return result;
    }

    @PutMapping("/user")
    @ApiOperation(value = "更新用户信息接口")
    @LogAnnotation(title = "用户管理", action = "更新用户信息")
    //@RequiresPermissions("sys:user:update")
    public DataResult updateUserInfo(@RequestBody @Valid UserUpdateReqVO vo, HttpServletRequest request) {
        String token = request.getHeader(Constant.ACCESS_TOKEN);
        String operationId = JwtTokenUtil.getUserId(token);
        String userType = JwtTokenUtil.getUserType(token);
        boolean userAdmin = JwtTokenUtil.getUserAdmin(token);

        userService.updateUserInfo(vo, operationId, userType, userAdmin);
        return DataResult.success();
    }

    @PutMapping("/user/info")
    @ApiOperation(value = "更新用户信息接口")
    @LogAnnotation(title = "用户管理", action = "更新用户信息")
    public DataResult updateUserInfoById(@RequestBody @Valid UserUpdateReqVO vo, HttpServletRequest request) {
        String token = request.getHeader(Constant.ACCESS_TOKEN);
        String operationId = JwtTokenUtil.getUserId(token);
        String userType = JwtTokenUtil.getUserType(token);
        boolean userAdmin = JwtTokenUtil.getUserAdmin(token);

        userService.updateUserInfo(vo, operationId, userType, userAdmin);
        return DataResult.success();
    }

    @GetMapping("/user/{id}")
    @ApiOperation(value = "查询用户详情接口")
    @LogAnnotation(title = "用户管理", action = "查询用户详情")
    ////@RequiresPermissions("sys:user:detail")
    public SysUser detailInfo(@PathVariable("id") String id) {
        return userService.detailInfo(id);
    }

    @GetMapping("/user")
    @ApiOperation(value = "查询用户详情接口")
    @LogAnnotation(title = "用户管理", action = "查询用户详情")
    public DataResult<SysUser> youSelfInfo(HttpServletRequest request) {
        String id = JwtTokenUtil.getUserId(request.getHeader(Constant.ACCESS_TOKEN));
        DataResult<SysUser> result = DataResult.success();
        result.setData(userService.detailInfo(id));
        return result;
    }

    @PostMapping("/users")
    @ApiOperation(value = "分页获取用户列表接口")
//    //@RequiresPermissions("sys:user:list")
    @LogAnnotation(title = "用户管理", action = "分页获取用户列表")
    public PageVO<SysUser> pageInfo(@RequestBody UserPageReqVO vo) {
        return userService.pageInfo(vo);
    }

    /**
     * 查询所有用户
     * @param key
     * @return
     */
    @ApiOperation("水利局用户列表")
    @GetMapping("/userList")
    public CommonResult userList(String key){
        UserPageReqVO vo = new UserPageReqVO();
        vo.setFetchAll(true);
        vo.setUsername(key);
        //固定为水利局下部门人员
        vo.setDeptId("67011533");
        PageVO<SysUser> page = userService.getUserListByKey(vo);
//        PageVO<SysUser> sysUserPageVO = userService.pageInfo(vo);
        return CommonResult.success(page);
    }

    @PostMapping("/user")
    @ApiOperation(value = "新增用户接口")
//    //@RequiresPermissions("sys:user:add")
    @LogAnnotation(title = "用户管理", action = "新增用户")
    public DataResult addUser(@RequestBody @Valid UserAddReqVO vo) {
        try {
            userService.addUser(vo);
            return DataResult.success();
        } catch (BusinessException e) {
            return DataResult.getResult(e.getMessageCode(), e.getDetailMessage());
        }
    }

    @PostMapping("/user/logout")
    @ApiOperation(value = "退出接口")
    @LogAnnotation(title = "用户管理", action = "退出")
    public DataResult logout(String accessToken, String refreshToken) {
        userService.logout(accessToken, refreshToken);
        return DataResult.success();
    }

    @PutMapping("/user/pwd")
    @ApiOperation(value = "修改密码接口")
    @LogAnnotation(title = "用户管理", action = "更新密码")
    public DataResult updatePwd(@RequestBody UpdatePasswordReqVO vo, HttpServletRequest request) {
        String accessToken = request.getHeader(Constant.ACCESS_TOKEN);
        String refreshToken = request.getHeader(Constant.REFRESH_TOKEN);
//        String userId = JwtTokenUtil.getUserId(accessToken);
        String userId = vo.getId();
        try{
            userService.updatePwd(vo, userId, accessToken, refreshToken);
            return DataResult.success();
        }catch (BusinessException e){
            return DataResult.getResult(e.getMessageCode(),e.getDetailMessage());
        }

    }

    @DeleteMapping("/user")
    @ApiOperation(value = "删除用户接口")
    @LogAnnotation(title = "用户管理", action = "删除用户")
    //@RequiresPermissions("sys:user:deleted")
    public DataResult deletedUser(@RequestBody @ApiParam(value = "用户id集合") List<String> userIds, HttpServletRequest request) {
        String operationId = JwtTokenUtil.getUserId(request.getHeader(Constant.ACCESS_TOKEN));
        userService.deletedUsers(userIds, operationId);
        return DataResult.success();
    }

    @GetMapping("/user/roles/{userId}")
    @ApiOperation(value = "赋予角色-获取所有角色接口")
    @LogAnnotation(title = "用户管理", action = "赋予角色-获取所有角色接口")
    //@RequiresPermissions("sys:user:role:detail")
    public DataResult<UserOwnRoleRespVO> getUserOwnRole(@PathVariable("userId") String userId) {
        DataResult<UserOwnRoleRespVO> result = DataResult.success();
        result.setData(userService.getUserOwnRole(userId));
        return result;
    }

    @PutMapping("/user/roles/{userId}")
    @ApiOperation(value = "赋予角色-用户赋予角色接口")
    @LogAnnotation(title = "用户管理", action = "赋予角色-用户赋予角色接口")
    //@RequiresPermissions("sys:user:update:role")
    public DataResult<UserOwnRoleRespVO> setUserOwnRole(@PathVariable("userId") String userId, @RequestBody List<String> roleIds) {
        DataResult result = DataResult.success();
        userService.setUserOwnRole(userId, roleIds);
        return result;
    }

    @GetMapping("/users")
    public CommonResult users(String ids) {
        return CommonResult.success(userService.users(ids));
    }

    @GetMapping("/putUserChooseProject")
    public void putUserChooseProject(String key, String jsonString) {
        userService.putUserChooseProject(key, jsonString);
    }

    @GetMapping("/getUserChooseProject")
    public String getUserChooseProject(String key) {
        return userService.getUserChooseProject(key);
    }

    @PostMapping("/getOutsideToken")
    public CommonResult getOutsideToken(HttpServletRequest request, String prcd) {
        //里面必传4个参数 登录手机号phone，工程编码prcd, accesskey,accesssecret
        if (StringUtils.isBlank(prcd)) {
            return CommonResult.validateFailed("工程编码必传");
        }
        String userId = JwtTokenUtil.getUserId(request.getHeader(Constant.ACCESS_TOKEN));
        SysUser sysUser = userService.detailInfo(userId);
        if ("3".equals(sysUser.getUserType()) || "admin".equals(sysUser.getUsername())){
            //物业化默认放程劲钢
            sysUser.setPhone("13858860396");
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("phone", sysUser.getPhone());
        jsonObject.put("prcd", prcd);
        jsonObject.put("accesskey", key);
        jsonObject.put("accesssecret", secret);
        String data = JSON.toJSONString(jsonObject);
        return CommonResult.success(DESUtil.encrypt(DESUtil.KEY, data));
    }

    @PostMapping("/rscpLogin")
    public CommonResult rscpLogin(String userCode) {
        //根据userCode获取用户
        return CommonResult.success(userService.rscpLogin(userCode));
    }

    @GetMapping("/delWyhPerson")
    public void delWyhPerson(String wyhId) {
        userService.delWyhPerson(wyhId);
    }

    @PostMapping("/saveWyhUser")
    @ApiOperation(value = "新增物业化用户接口")
    @LogAnnotation(title = "用户管理", action = "新增物业化用户")
    public DataResult saveWyhUser(@Valid UserAddReqVO vo) {
        try {
            userService.saveWyhUser(vo);
            return DataResult.success();
        } catch (BusinessException e) {
            return DataResult.getResult(e.getMessageCode(), e.getDetailMessage());
        }
    }

    @PostMapping("/getWyhUserList")
    @ApiOperation(value = "查询物业化公司的用户接口")
    public CommonResult getWyhUserList(WyhUserVo wyhUserVo) {
        if (StringUtils.isBlank(wyhUserVo.getDeptId())){
            return CommonResult.failed("物业化公司编码不能为空");
        }
        wyhUserVo.page();
        List<WyhUserVo> wyhUserVos = userService.getWyhUserList(wyhUserVo);
        return CommonResult.success(PageUtils.getPageVO(wyhUserVos));
    }

    @GetMapping("/delWyhUser")
    @ApiOperation(value = "删除物业化公司的用户接口")
    @LogAnnotation(title = "用户管理", action = "删除物业化公司用户")
    public CommonResult delWyhUser(String id) {
        if(StringUtils.isBlank(id)){
            return CommonResult.failed("用户id不能为空");
        } else {
            userService.delWyhUser(id);
            return CommonResult.success();
        }
    }

    @PostMapping("/wyhUser/pwd")
    @ApiOperation(value = "物业化用户修改密码接口")
    @LogAnnotation(title = "用户管理", action = "更新密码")
    public CommonResult updateWyhPwd(SysUser vo) {
        String password = vo.getPassword();
        String id = vo.getId();
        if (StringUtils.isBlank(password) || StringUtils.isBlank(id)){
            return CommonResult.failed("缺少必传参数");
        }
        SysUser sysUser = userService.detailInfo(id);
        if (sysUser == null) {
            throw new BusinessException(BaseResponseCode.DATA_ERROR);
        }
        if (!"3".equals(sysUser.getUserType())){
            return CommonResult.failed("此处只能修改物业化公司用户密码");
        }
        //修改密码
        userService.updateWyhPwd(sysUser,password);
        return CommonResult.success("修改成功");
    }

    @PostMapping("/selectUserByIdList")
    public List<com.ygkj.auth.api.user.model.SysUser> selectUserByIdList(@RequestBody List<String> idList) {
        return userService.selectUserByIdList(idList);
    }


    @PostMapping("/getWaterManageUserList")
    @ApiOperation(value = "查询水管单位用户接口")
    public CommonResult getWaterManageUserList(String key, @RequestParam(required = false) Boolean flag) {
        if (ObjectUtils.isEmpty(flag)){
            flag = false;
        }
        List<String> waterManageDeptIds = userService.getWaterManageDeptIds();
        List<SysUser> waterManageUsers = userService.getWaterManageUserListByDeptIds(waterManageDeptIds,key,flag);
        return CommonResult.success(PageUtils.getPageVO(waterManageUsers));
    }

    @PostMapping("/bindUserJob")
    @ApiOperation(value = "绑定用户角色")
    @LogAnnotation(title = "用户管理", action = "绑定用户角色")
    public Object bindUserJob(String id, String jobIds) {
        userService.bindUserJob(id,jobIds);
        return CommonResult.success();
    }

    @PostMapping("/enableUser")
    @ApiOperation(value = "停用启用用户")
    @LogAnnotation(title = "用户管理", action = "停用启用用户")
    public Object enableUser(String id, String status) {
        try{
            userService.enableUser(id,status);
        }catch (Exception e){
            return CommonResult.failed("修改失败!");
        }
        return CommonResult.success("修改成功");
    }

    @RequestMapping("/healthCheck")
    @ApiOperation(value = "监控检查")
    public Object healthCheck() {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("status",200);
        try {
            // 检查web端是否完好
            if(!isWebRun(checkUrl)){
                result.put("status",500);
            }else{
                UserPageReqVO vo = new UserPageReqVO();
                vo.setPageNum(0);
                vo.setPageSize(1);
                PageVO<SysUser> pager = userService.pageInfo(vo);
                if(pager.getTotalRows()==0){
                    result.put("status",500);
                }
            }
        }catch (Exception e){
                result.put("status",500);
        }

        return result;
    }

    /**
     * 检查网页地址是否正常运行
     * @param checkUrl
     * @return
     */
    public static boolean isWebRun(String checkUrl){
        boolean webRun = true;
        try {
            URL url = new URL(checkUrl);
            URLConnection URLconnection = url.openConnection();
            HttpURLConnection httpConnection = (HttpURLConnection) URLconnection;
            httpConnection.setConnectTimeout(5000);
            httpConnection.setReadTimeout(5000);
            int responseCode = httpConnection.getResponseCode();
            if(responseCode!=200){
                webRun = false;
            }
        } catch (Exception e) {
            webRun = false;
        }
        return webRun;
    }
}
