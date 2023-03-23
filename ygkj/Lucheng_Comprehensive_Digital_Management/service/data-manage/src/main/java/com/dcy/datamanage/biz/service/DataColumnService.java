package com.dcy.datamanage.biz.service;

import com.alibaba.druid.util.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dcy.datamanage.api.model.BusDataColumn;
import com.dcy.datamanage.biz.dsm.DataSourceRegistry;
import com.dcy.datamanage.biz.dsm.DefaultDataSource;
import com.dcy.datamanage.biz.dto.input.BusDataColumnSearchInputDTO;
import com.dcy.datamanage.biz.dto.input.DataColumnSaveInputDTO;
import com.dcy.datamanage.biz.dto.output.BusDataColumnOutputDTO;
import com.dcy.datamanage.biz.mapper.DataColumnMapper;
import com.dcy.datamanage.biz.util.SnowFlake;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class DataColumnService extends ServiceImpl<DataColumnMapper, BusDataColumn> {

    @Autowired
    private DataSourceRegistry dataSourceRegistry;

    @Autowired
    private DataColumnMapper dataColumnMapper;

    public List<BusDataColumnOutputDTO> list(BusDataColumnSearchInputDTO dto) {
        String dataSourceId = dto.getDataSourceId();
        DefaultDataSource dataSource = dataSourceRegistry.getDataSource(dataSourceId);
        //原始列
        List<BusDataColumnOutputDTO> sourceList = dataSource.listColumnSource(dto);
        if (!CollectionUtils.isEmpty(sourceList)) {
            //查询别名
            List<BusDataColumn> extendList = dataColumnMapper.listColumnExtend(dto);
            if (!CollectionUtils.isEmpty(extendList)) {
                Map<String, BusDataColumn> map = extendList.stream().collect(Collectors.toMap(c -> getColUniqueKey(c), Function.identity()));
                sourceList.forEach(outputDTO -> {
                    String key = getColUniqueKey(outputDTO);
                    if (map.containsKey(key)) {
                        BusDataColumn busDataColumn = map.get(key);
                        outputDTO.setId(busDataColumn.getId());
                        outputDTO.setCustomColumnName(busDataColumn.getCustomColumnName());
                    }
                });
            }
            sourceList.forEach(outputDTO ->
                    outputDTO.setDisplayName(
                            !StringUtils.isEmpty(outputDTO.getCustomColumnName()) ?
                            outputDTO.getCustomColumnName() :
                            !StringUtils.isEmpty(outputDTO.getRemarks()) ?
                                    outputDTO.getRemarks() :
                                    outputDTO.getColumnName()
                    )
            );
        }
        return sourceList;
    }

    private String getColUniqueKey(BusDataColumn busDataColumn) {
        return busDataColumn.getColumnName();
    }

    @Transactional(rollbackFor = Exception.class)
    public Boolean batchSave(DataColumnSaveInputDTO dto) {
        String dataSourceId = dto.getDataSourceId();
        String tableName = dto.getTableName();

        //批量保存新的
        List<BusDataColumn> list = dto.getList();
        if (!CollectionUtils.isEmpty(list)) {
            list.forEach(busDataColumn -> {
                busDataColumn.setId(SnowFlake.zero.nextId());
                busDataColumn.setDataSourceId(dataSourceId);
                busDataColumn.setTableName(tableName);
            });
            baseMapper.replaceAll(list);
        }

        return true;
    }
}
