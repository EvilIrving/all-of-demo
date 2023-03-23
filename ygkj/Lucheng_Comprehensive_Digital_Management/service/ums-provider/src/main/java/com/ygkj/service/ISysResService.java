package com.ygkj.service;

import com.ygkj.vo.resp.PageVO;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ygkj.entity.SysRes;

import java.util.List;

/**
 * @author  wgf
 * @date  2021/1/6 14:00
 */
public interface ISysResService extends IService<SysRes> {

    PageVO<SysRes> listPage(SysRes entity);

    List<SysRes> getPidSysRes(SysRes entity);

    List<SysRes> getBandingList(String jobId, String p_id, int flag);

    List<String> permissionList(String id, String type);
}
