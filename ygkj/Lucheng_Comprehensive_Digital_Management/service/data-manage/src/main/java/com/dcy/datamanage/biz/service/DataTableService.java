package com.dcy.datamanage.biz.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dcy.datamanage.api.model.BusDataTable;
import com.dcy.datamanage.api.model.common.PageModel;
import com.dcy.datamanage.api.model.common.PageResult;
import com.dcy.datamanage.biz.dsm.DataSourceRegistry;
import com.dcy.datamanage.biz.dsm.DefaultDataSource;
import com.dcy.datamanage.biz.dto.input.BusDataTableSearchInputDTO;
import com.dcy.datamanage.biz.dto.input.DataTableSaveInputDTO;
import com.dcy.datamanage.biz.dto.output.BusDataTableOutputDTO;
import com.dcy.datamanage.biz.mapper.DataTableMapper;
import com.dcy.datamanage.biz.mapper.ResourceSourceRelMapper;
import com.dcy.datamanage.biz.util.SnowFlake;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class DataTableService extends ServiceImpl<DataTableMapper, BusDataTable> {

    @Autowired
    private DataSourceRegistry dataSourceRegistry;

    @Autowired
    private ResourceSourceRelMapper resourceSourceRelMapper;

    @Autowired
    private DataTableMapper dataTableMapper;

    public PageResult<BusDataTableOutputDTO> page(BusDataTableSearchInputDTO dto, PageModel pageModel) {
        String dataSourceId = dto.getDataSourceId();
        DefaultDataSource dataSource = dataSourceRegistry.getDataSource(dataSourceId);

        //原始表
        PageResult<BusDataTableOutputDTO> sourcePage = dataSource.listTableSource(pageModel);
        List<BusDataTableOutputDTO> sourceList = sourcePage.getRecords();
        if (!CollectionUtils.isEmpty(sourceList)) {
            //表的扩展信息
            List<String> tableNameList = sourceList.stream().map(BusDataTableOutputDTO::getTableName).collect(Collectors.toList());
            //合并表的扩展信息
            List<BusDataTable> extendList = dataTableMapper.listTableExtend(tableNameList, dataSourceId);
            if (!CollectionUtils.isEmpty(extendList)) {
                Map<String, BusDataTable> extendMap = extendList.stream()
                        .collect(Collectors.toMap(b -> getTableUniqueKey(b), Function.identity()));
                //遍历加上扩展名
                sourceList.forEach(source -> {
                    String key = getTableUniqueKey(source);
                    if (extendMap.containsKey(key)) {
                        BusDataTable busDataTable = extendMap.get(key);
                        source.setId(busDataTable.getId());
                        source.setCustomTableName(busDataTable.getCustomTableName());
                    }
                });
            }

            List<BusDataTableOutputDTO> relList = resourceSourceRelMapper.list2(dataSourceId, tableNameList);
            if (!CollectionUtils.isEmpty(relList)) {
                Map<String, BusDataTableOutputDTO> relMap = relList.stream()
                        .collect(Collectors.toMap(b -> getTableUniqueKey(b), Function.identity()));
                //遍历加上资源目录
                sourceList.forEach(source -> {
                    String key = getTableUniqueKey(source);
                    if (relMap.containsKey(key)) {
                        BusDataTableOutputDTO rel = relMap.get(key);
                        source.setDataResourceId(rel.getDataResourceId());
                        source.setResourceName(rel.getResourceName());
                    }
                });
            }
        }
        //筛选
        if (!StringUtils.isEmpty(dto.getTableName())){
            List<BusDataTableOutputDTO> list = sourcePage.getRecords().stream().filter(s -> s.getTableName().contains(dto.getTableName())).collect(Collectors.toList());
            sourcePage.setRecords(list);
        }

        return sourcePage;
    }

    private String getTableUniqueKey(BusDataTable busDataTable) {
        return busDataTable.getTableName();
    }

    @Transactional(rollbackFor = Exception.class)
    public Boolean batchSave(DataTableSaveInputDTO dto) {
        String dataSourceId = dto.getDataSourceId();

        //批量保存新的
        List<BusDataTable> list = dto.getList();
        if (!CollectionUtils.isEmpty(list)) {
            list.forEach(busDataColumn -> {
                busDataColumn.setId(SnowFlake.zero.nextId());
                busDataColumn.setDataSourceId(dataSourceId);
            });
            baseMapper.replaceAll(list);
        }

        return true;
    }
}
