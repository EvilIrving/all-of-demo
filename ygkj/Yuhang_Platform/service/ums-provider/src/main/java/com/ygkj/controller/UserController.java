package com.ygkj.controller;

import com.ygkj.aop.annotation.LogAnnotation;
import com.ygkj.auth.VO.request.ValidateTicketVo;
import com.ygkj.constants.Constant;
import com.ygkj.entity.SysUser;
import com.ygkj.exception.BusinessException;
import com.ygkj.exception.code.BaseResponseCode;
import com.ygkj.jwt.JwtTokenUtil;
import com.ygkj.service.UserService;
import com.ygkj.utils.DataResult;
import com.ygkj.vo.req.*;
import com.ygkj.vo.resp.LoginRespVO;
import com.ygkj.vo.resp.PageVO;
import com.ygkj.vo.resp.UserOwnRoleRespVO;
import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ygkj.com.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;

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

    @PostMapping(value = "/user/login")
    @ApiOperation(value = "用户登录接口")
    public LoginRespVO login(@RequestBody @Valid LoginReqVO vo, HttpServletRequest request) {
        String ipAddr = StringUtils.getIpAddr(request);
        vo.setIpAddr(ipAddr);
        return userService.login(vo, request);
    }

    @GetMapping(value = "/user/dingLogin/{accountId}")
    @ApiOperation(value = "用户登录接口")
    public LoginRespVO dingLogin(@PathVariable("accountId") String accountId, HttpServletRequest request) {
        return userService.dingLogin(accountId, request);
    }

    @PostMapping("/user/validateTicket")
    @ApiOperation(value = "水管理平台登录")
    public LoginRespVO validateTicket(@RequestBody ValidateTicketVo vo, HttpServletRequest request) {
        return userService.validateTicket(vo, request);
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
        String userId = JwtTokenUtil.getUserId(accessToken);
        userService.updatePwd(vo, userId, accessToken, refreshToken);
        return DataResult.success();
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

    @PostMapping("/findUserNotInIds")
    @ApiOperation(value = "查找不在ids范围内的用户list")
    public CommonResult findUserNotInIds(String userIds) {
        if (StringUtils.isEmpty(userIds)) {
            return CommonResult.success(userService.selectAll(null));
        }
        return CommonResult.success(userService.findUserNotInIds(Arrays.asList(userIds.split(","))));
    }

    @PostMapping("/findDataByIds")
    @ApiOperation(value = "根据用户ids查找")
    public CommonResult findByIds(String userIds) {
        if (StringUtils.isEmpty(userIds)) {
            CommonResult.failed("用户ids为空");
        }
        return CommonResult.success(userService.findByIds(userIds));
    }

    @GetMapping("/mobileTicket")
    @ApiOperation(value = "查询mobileTicket")
    public CommonResult mobileTicket(HttpServletRequest request) {
        String accessToken = request.getHeader(Constant.ACCESS_TOKEN);
        return CommonResult.success(userService.mobileTicket(JwtTokenUtil.getUserId(accessToken)));
    }
}
