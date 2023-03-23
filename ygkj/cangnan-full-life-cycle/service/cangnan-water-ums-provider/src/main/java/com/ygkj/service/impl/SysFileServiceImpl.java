package com.ygkj.service.impl;

import com.ygkj.entity.SysFile;
import com.ygkj.mapper.SysFileMapper;
import com.ygkj.service.SysFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Huang.zh
 * @date 2020/9/15 15:47
 * @Description: 文件服务service层具体实现
 */
@Service("sysFileService")
public class SysFileServiceImpl implements SysFileService {
    @Autowired
    private SysFileMapper sysFileMapper;

    @Override
    public void saveFile(SysFile sysFile) throws Exception {
        int row = sysFileMapper.insert(sysFile);
        if (row != 1) {
            throw new RuntimeException("操作失败，请联系管理员！");
        }
    }

    @Override
    public List<SysFile> load(String ids) {
        return sysFileMapper.load(ids);
    }
}
