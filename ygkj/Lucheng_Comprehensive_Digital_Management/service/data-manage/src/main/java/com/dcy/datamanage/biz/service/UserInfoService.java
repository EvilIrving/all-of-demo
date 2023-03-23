package com.dcy.datamanage.biz.service;

import com.dcy.datamanage.api.model.UserInfo;
import com.ygkj.jwt.JwtTokenUtil;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Service
public class UserInfoService {


    /**
     * 从token中获取userInfo
     *
     * @param request
     * @return
     */
    public UserInfo getUserInfo(HttpServletRequest request) {
        String token = request.getHeader("authorization");
        UserInfo userInfo = new UserInfo();
        userInfo.setUserId(JwtTokenUtil.getUserId(token));
        userInfo.setDeptId(JwtTokenUtil.getDeptId(token));
        List<String> roleList = JwtTokenUtil.getRoleList(token);
        boolean oneFlag = false;
        boolean twoFlag = false;
        for (String roleName : roleList) {
            if("数管管理员".equals(roleName)){
                oneFlag = true;
                break;
            }
            if("部门数管管理员".equals(roleName)){
                twoFlag = true;
            }
        }
        if(oneFlag){
            userInfo.setRoleType("1");
        }else if(twoFlag){
            userInfo.setRoleType("2");
        }else {
            userInfo.setRoleType("3");
        }

        return userInfo;
    }
}
