package com.ygkj.water.project.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ygkj.project.model.WarningInfo;
import com.ygkj.project.model.WarningMsg;

import java.util.List;

/**
 * @author lxl
 * @create 2022-06-13 14:13
 * @description
 */
public interface WarningMsgMapper extends BaseMapper<WarningMsg> {


    /**
     * 移动端查询 预警/提醒 列表
     * @param warningInfo
     * @return
     */
    List<WarningInfo> getListByCondition(WarningInfo warningInfo);

    /**
     * 查询报警人员id
     * @param currentUserId
     * @return
     */
    Integer getTotalAlarmNum(String currentUserId);

    Integer getTotalRemindNum(String phone);
}
