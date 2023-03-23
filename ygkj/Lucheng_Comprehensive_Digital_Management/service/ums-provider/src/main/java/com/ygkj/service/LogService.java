package com.ygkj.service;

import com.ygkj.entity.SysLog;
import com.ygkj.platform.management.model.SysLoginLog;
import com.ygkj.platform.management.vo.request.SysLoginLogPage;
import com.ygkj.vo.req.SysLogPageReqVO;
import com.ygkj.vo.resp.PageVO;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.ParseException;
import java.util.List;

/**
 * @ClassName: LogService
 * TODO:类文件简单描述
 * @Author: Huang.zh
 * @CreateDate: 2019/9/23 16:17
 * @UpdateUser: Huang.zh
 * @UpdateDate: 2019/9/23 16:17
 * @Version: 0.0.1
 */
public interface LogService {

    PageVO<SysLog> pageInfo(SysLogPageReqVO vo);

    void deleted(List<String> logIds);

    PageVO<SysLoginLog> loadLoginLogs(SysLoginLogPage pageVo);

    void export(HttpServletResponse response, SysLogPageReqVO vo) throws ParseException;
}
