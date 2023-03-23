package com.ygkj.soft.database.service;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;

/**
 * @author Huang.zh
 * @date 2020/9/1 15:14
 * @Description: 数据接收服务层抽象
 */
public interface DataReceiveService {

    void DataReceive(Object data) throws Exception;

    void changeStatus(@NotNull(message = "tableName不允许为空！")String tableName,
                      @NotNull(message = "globalId不允许为空！")String globalId,
                      @NotNull(message = "status不允许为空！")int status);
}
