package com.ygkj.service;

import com.github.pagehelper.PageInfo;
import com.ygkj.auth.api.file.model.SysFile;
import com.ygkj.vo.req.SysFileReqVo;
import com.ygkj.water.model.CommonResult;

import java.util.List;

/**
 * @author Huang.zh
 * @date 2020/9/15 15:46
 * @Description: 文件服务service层抽象
 */
public interface SysFileService {

    void saveFile(SysFile sysFile) throws Exception;

    List<SysFile> load(String ids);

    /**
     * 档案数据统计
     *
     * @return
     */
    CommonResult archivalDataStatistics();

    /**
     * 数字档案上传统计
     *
     * @param year
     * @return
     */
    CommonResult statUploadFile(Integer year);

    /**
     * 文件参数类型
     *
     * @return
     */
    CommonResult fileParameterType();

    /**
     * 文件分页查找
     *
     * @param vo
     * @return
     */
    PageInfo<SysFile> pageList(SysFileReqVo vo);

    /**
     * 文件删除
     *
     * @param id 主键id
     * @return
     */
    int del(String id);

    int update(SysFile entity);
}
