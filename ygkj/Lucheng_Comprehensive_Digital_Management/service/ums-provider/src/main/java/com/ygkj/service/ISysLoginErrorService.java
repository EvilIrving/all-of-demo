package com.ygkj.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ygkj.entity.SysLoginError;

import java.util.Date;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author lyf
 * @since 2021-03-23
 */
public interface ISysLoginErrorService extends IService<SysLoginError> {


    SysLoginError getLoginError(String userId, String ipAddr);

    /**
     *账号锁定机制
     * @param userId
     * @param ipAddr
     * @param date
     * @return
     */
    SysLoginError saveLoginError(String userId, String ipAddr, Date date);
}
