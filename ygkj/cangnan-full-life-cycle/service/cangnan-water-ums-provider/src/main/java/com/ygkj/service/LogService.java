package com.ygkj.service;

import com.ygkj.entity.SysLog;
import com.ygkj.vo.req.SysLogPageReqVO;
import com.ygkj.vo.resp.PageVO;

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
}
