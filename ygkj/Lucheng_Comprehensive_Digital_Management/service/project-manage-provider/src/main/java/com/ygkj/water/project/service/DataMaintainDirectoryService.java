package com.ygkj.water.project.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ygkj.project.model.DataMaintainDirectory;

import java.util.List;

public interface DataMaintainDirectoryService extends IService<DataMaintainDirectory> {
    void dirInsert(DataMaintainDirectory directory);

    List<String> deptIds(String id);

    void updInfo(String dirId, String replace);

    void updateTask(String dirId, String replace);
}
