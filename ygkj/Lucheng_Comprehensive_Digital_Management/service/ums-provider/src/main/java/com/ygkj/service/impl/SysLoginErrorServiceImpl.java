package com.ygkj.service.impl;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ygkj.entity.SysLoginError;
import com.ygkj.mapper.SysLoginErrorMapper;
import com.ygkj.service.ISysLoginErrorService;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author lyf
 * @since 2021-03-23
 */
@Service
public class SysLoginErrorServiceImpl extends ServiceImpl<SysLoginErrorMapper, SysLoginError> implements ISysLoginErrorService {

    @Autowired
    private SysLoginErrorMapper sysLoginErrorMapper;

    @Override
    public SysLoginError getLoginError(String userId, String ipAddr) {
        return sysLoginErrorMapper.getLoginError(userId,ipAddr);
    }

    @Override
    public SysLoginError saveLoginError(String userId, String ipAddr, Date date) {
        SysLoginError loginError = sysLoginErrorMapper.getLoginError(userId, ipAddr);
        if (loginError == null){
            //新增
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            sysLoginErrorMapper.saveLogin(IdWorker.get32UUID(),userId, ipAddr, sdf.format(date));
        }else {
            //更新
            Integer contNum = loginError.getContNum() + 1;
            loginError.setContNum(contNum);
            if(contNum==5){
                //锁定30分钟
                loginError.setUnlockTime(DateUtils.addMinutes(date,30));
            }else if(contNum==10){
                //锁定24小时
                loginError.setUnlockTime(DateUtils.addHours(date,24));
            }else if(contNum>10){
                //重置连续错误次数
                contNum -= 10;
                loginError.setContNum(contNum);
            }
            loginError.setLastErrTime(date);
            loginError.setTotalNum(loginError.getTotalNum()+1);
            sysLoginErrorMapper.updateById(loginError);
        }
        return sysLoginErrorMapper.getLoginError(userId, ipAddr);
    }
}
