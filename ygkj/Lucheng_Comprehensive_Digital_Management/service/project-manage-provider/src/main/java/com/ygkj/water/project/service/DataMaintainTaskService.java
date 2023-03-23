package com.ygkj.water.project.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ygkj.project.model.DataMaintainTask;

public interface DataMaintainTaskService extends IService<DataMaintainTask> {
    void sendInfoInsert(DataMaintainTask task);

    boolean sendInfodelete(String id);

    Object queryPage(DataMaintainTask task);


    void updateTask(DataMaintainTask task);
}
