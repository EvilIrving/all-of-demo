package com.ygkj.water.project.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ygkj.project.model.DataMaintainInfo;
import com.ygkj.project.model.DataMaintainTask;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


public interface DataMaintainInfoService extends IService<DataMaintainInfo> {
    Object queryPage(DataMaintainInfo info);

    boolean judgeAdmin(String userId);

    void updTask(DataMaintainInfo info);

    void exportMaintainInfo(HttpServletResponse response,DataMaintainInfo info);
}
