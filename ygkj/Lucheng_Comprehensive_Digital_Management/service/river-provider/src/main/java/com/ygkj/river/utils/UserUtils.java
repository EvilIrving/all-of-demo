package com.ygkj.river.utils;

import com.ygkj.jwt.JwtTokenUtil;
import com.ygkj.utils.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * @author: bearjun
 * @description:
 * @create: 2021-03-26 20:17
 **/
public class UserUtils {

    public static String getCurrentUserId() {
        HttpServletRequest req = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String token = req.getHeader("authorization");
        if (StringUtils.isNotBlank(token)) {
            return JwtTokenUtil.getUserId(token);
        }
        return null;
    }

    public static String getCurrentUserName() {
        HttpServletRequest req = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String token = req.getHeader("authorization");
        if (StringUtils.isNotBlank(token)) {
            return JwtTokenUtil.getUserName(token);
        }
        return null;
    }

    public static String getDeptId() {
        HttpServletRequest req = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String token = req.getHeader("authorization");
        if (StringUtils.isNotBlank(token)) {
            return JwtTokenUtil.getDeptId(token);
        }
        return null;
    }


}
