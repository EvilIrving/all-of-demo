package com.ygkj.water.project.service.impl;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ygkj.project.model.DataMaintainDirectory;
import com.ygkj.water.project.mapper.DataMaintainDirectoryMapper;
import com.ygkj.water.project.service.DataMaintainDirectoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DataMaintainDirectoryServiceImpl extends ServiceImpl<DataMaintainDirectoryMapper, DataMaintainDirectory> implements DataMaintainDirectoryService {

    @Autowired
    private DataMaintainDirectoryMapper directoryMapper;

    @Override
    public void dirInsert(DataMaintainDirectory directory) {
        //查找档案路径
        List<DataMaintainDirectory> directories = directoryMapper.selectAll();
        List<DataMaintainDirectory> collect = directories.stream().filter(e -> e.getId().equals(directory.getPid())).collect(Collectors.toList());
        if (!collect.isEmpty()) {
            StringBuilder stringBuilder = new StringBuilder();
            DataMaintainDirectory directory1 = collect.get(0);
            stringBuilder.append(directory1.getDirectoryName());
            findParentDirPath(directory1, directories, stringBuilder);
            directory.setDirPath(stringBuilder.toString());
        }
        directory.setId(IdWorker.get32UUID());
        directoryMapper.insert(directory);
    }

    public void findParentDirPath(DataMaintainDirectory directory, List<DataMaintainDirectory> directories, StringBuilder stringBuilder) {
        List<DataMaintainDirectory> collect = directories.stream().filter(e -> e.getId().equals(directory.getPid())).collect(Collectors.toList());
        if (!collect.isEmpty()) {
            DataMaintainDirectory directory1 = collect.get(0);
            stringBuilder.insert(0, directory1.getDirectoryName() + "-");
            findParentDirPath(directory1, directories, stringBuilder);
        }
    }

    @Override
    public List<String> deptIds(String id) {
        String depts = directoryMapper.getDirectoryIdsString(id);
        String substring = depts.substring(1);
        String[] split = substring.split(",");

        List<String> list = Arrays.asList(split);
        return list;
    }

    @Override
    public void updInfo(String dirId, String replace) {
        directoryMapper.updInfo(dirId,replace);
    }

    @Override
    public void updateTask(String dirId, String replace) {
        directoryMapper.updateTask(dirId,replace);
    }
}
