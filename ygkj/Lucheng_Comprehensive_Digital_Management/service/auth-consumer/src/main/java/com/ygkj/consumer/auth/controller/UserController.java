package com.ygkj.consumer.auth.controller;

import com.ygkj.auth.VO.request.UpdatePasswordReqVO;
import com.ygkj.auth.VO.request.UserAddReqVO;
import com.ygkj.auth.VO.request.UserPageReqVO;
import com.ygkj.auth.VO.request.UserUpdateReqVO;
import com.ygkj.auth.VO.response.PageVO;
import com.ygkj.auth.api.user.UserControllerApi;
import com.ygkj.auth.api.user.model.SysUser;
import com.ygkj.consumer.auth.client.authentication.AuthenticationClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * @author Huang.zh
 * @date 2020/8/26 14:24
 * @Description: 用户微服务调用
 */
@RestController
@RequestMapping("/user")
public class UserController implements UserControllerApi {

    @Autowired
    private AuthenticationClient authenticationClient;

    @GetMapping("/{id}")
    @Override
    public SysUser detailInfo(@PathVariable("id") String id) {
        return authenticationClient.detailInfo(id);
    }

    @PostMapping("/pageInfo")
    @Override
    public PageVO<SysUser> pageInfo(UserPageReqVO userPageReqVO) {
        return authenticationClient.pageInfo(userPageReqVO);
    }

    @PostMapping("/addUser")
    @Override
    public Object addUser(UserAddReqVO userAddReqVO) {
        return authenticationClient.addUser(userAddReqVO);
    }

    @PostMapping("/deleteUser")
    @Override
    public Object deleteUser(String userIds) {
        return authenticationClient.deleteUser(Arrays.asList(userIds.split(",")));
    }

    @PutMapping("/updateUser")
    @Override
    public Object updateUser(UserUpdateReqVO userUpdateReqVO) {
        return authenticationClient.updateUser(userUpdateReqVO);
    }

    @PostMapping("/updatePassword")
    @Override
    public Object updatePassword(UpdatePasswordReqVO updatePasswordReqVO) {
        return authenticationClient.updatePassword(updatePasswordReqVO);
    }

    @PutMapping("/changeUserStatus/{userId}/{status}")
    @Override
    public Object changeUserStatus(@PathVariable String userId, @PathVariable Integer status) {
        UserUpdateReqVO userUpdateReqVO = new UserUpdateReqVO();
        userUpdateReqVO.setStatus(status);
        userUpdateReqVO.setId(userId);
        Object o = authenticationClient.updateUser(userUpdateReqVO);
        return o;
    }





}
