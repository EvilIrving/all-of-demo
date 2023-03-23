package com.ygkj.water.project.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ygkj.project.model.BhDutyRecord;
import com.ygkj.project.model.ProjectCodeMapping;
import com.ygkj.vo.PageVO;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

/**
 * @author wgf
 * @date 2022/4/26 14:02
 * @Description:
 */
public interface BhDutyRecordService extends IService<BhDutyRecord> {

    PageVO<BhDutyRecord> getList(BhDutyRecord bhDutyRecord);

    String selectForeignCode(ProjectCodeMapping projectCodeMapping);

    /**
     * 导出
     * @param response
     * @param bhDutyRecord
     */
    void exportDuty(HttpServletResponse response, BhDutyRecord bhDutyRecord);

    /**
     * 导入
     * @param file
     */
    JSONObject importDuty(MultipartFile file, String pid) throws Exception;
}
