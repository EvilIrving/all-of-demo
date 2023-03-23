package com.dcy.datamanage.biz.service;

import com.alibaba.druid.util.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dcy.datamanage.api.model.DataAssessFrequency;
import com.dcy.datamanage.biz.dto.input.DataAssessFrequencyInputDTO;
import com.dcy.datamanage.biz.dto.input.DataAssessFrequencySearchInputDTO;
import com.dcy.datamanage.biz.mapper.DataAssessFrequencyMapper;
import com.dcy.datamanage.biz.util.SnowFlake;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

@Service
public class DataAssessFrequencyService extends ServiceImpl<DataAssessFrequencyMapper, DataAssessFrequency> {

    /**
     * (批量)?((新增)|(修改))
     *
     * @param dto
     * @return
     */
    public Boolean batchSave(DataAssessFrequencyInputDTO dto) {
        List<DataAssessFrequency> frequencyList = new ArrayList<>();
        List<String> tableNameList = dto.getTableNameList();
        if (CollectionUtils.isEmpty(tableNameList)) {
            //列表为空,保存单条
            dto.setId(SnowFlake.zero.nextId());
            frequencyList.add(dto);
        } else {
            //列表不为空，批量保存
            for (String tableName : tableNameList) {
                DataAssessFrequency dataAssessFrequency = new DataAssessFrequency();
                dataAssessFrequency.setDataSourceId(dto.getDataSourceId());
                dataAssessFrequency.setTableName(tableName);
                dataAssessFrequency.setId(SnowFlake.zero.nextId());
                dataAssessFrequency.setFrequencyUnit(dto.getFrequencyUnit());
                frequencyList.add(dataAssessFrequency);
            }
        }
        baseMapper.batchReplace(frequencyList);
        return true;
    }


    /**
     * 列表查询
     *
     * @param dto
     * @return
     */
    public List<DataAssessFrequency> list(DataAssessFrequencySearchInputDTO dto) {
        LambdaQueryWrapper<DataAssessFrequency> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DataAssessFrequency::getDataSourceId, dto.getDataSourceId());
        queryWrapper.eq(!StringUtils.isEmpty(dto.getTableName()),DataAssessFrequency::getTableName, dto.getTableName());
        queryWrapper.in(!CollectionUtils.isEmpty(dto.getTableNameList()),DataAssessFrequency::getTableName, dto.getTableNameList());
        return list(queryWrapper);
    }
}
