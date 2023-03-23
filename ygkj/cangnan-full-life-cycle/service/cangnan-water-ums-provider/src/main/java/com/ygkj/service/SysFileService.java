package com.ygkj.service;

import com.ygkj.entity.SysFile;

import java.util.List;

/**
 * @author Huang.zh
 * @date 2020/9/15 15:46
 * @Description: 文件服务service层抽象
 */
public interface SysFileService {

    void saveFile(SysFile sysFile) throws Exception;

    List<SysFile> load(String ids);
}
