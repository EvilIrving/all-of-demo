package com.ygkj.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ygkj.entity.SysJob;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wgf
 * @since 2021-01-06
 */
public interface ISysJobService extends IService<SysJob> {

    void removeJobRes(String id, Integer flag);

    void removeJobResAll(String id);

    List<String> getJobResId(String id);

    List<String> getBangdingJobResId(String id);
}
