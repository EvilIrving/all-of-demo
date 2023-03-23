package com.ygkj.water.project.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ygkj.project.model.DutyManagement;
import com.ygkj.project.vo.request.DutyReqVo;
import com.ygkj.vo.PageVO;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public interface DutyManagementService extends IService<DutyManagement> {
    void importTemplate(MultipartFile file) throws IOException;

    void downloadTemplate(String yearAndMonth, HttpServletResponse response) throws Exception;

    PageVO<DutyManagement> loadPage(DutyReqVo reqVo);

    void sendDingMsgByTime();
}
