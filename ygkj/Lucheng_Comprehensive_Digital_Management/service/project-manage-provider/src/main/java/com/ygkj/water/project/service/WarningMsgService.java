package com.ygkj.water.project.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ygkj.project.model.WarningInfo;
import com.ygkj.project.model.WarningMsg;

import java.util.List;

/**
 * @author lxl
 * @create 2022-06-13 14:32
 * @description
 */
public interface WarningMsgService extends IService<WarningMsg> {


    List<WarningInfo> getListByCondition(WarningInfo warningInfo);

    /**
     * 查询报警数量
     * @return
     */
    Integer getTotalAlarmNum(String phone);

    Integer getTotalRemindNum(String phone);
}
