package com.dcy.datamanage.biz.service;

import com.dcy.datamanage.api.model.DataAssessFrequency;
import com.dcy.datamanage.api.model.common.PageResult;
import com.dcy.datamanage.biz.dsm.DataSourceRegistry;
import com.dcy.datamanage.biz.dsm.DefaultDataSource;
import com.dcy.datamanage.biz.dto.input.DataAssessDeptSearchInputDTO;
import com.dcy.datamanage.biz.dto.input.DataAssessFrequencySearchInputDTO;
import com.dcy.datamanage.biz.dto.input.DataAssessPageInputDTO;
import com.dcy.datamanage.biz.dto.output.BusDataTableOutputDTO;
import com.dcy.datamanage.biz.dto.output.DataAssessDeptSearchOutputDTO;
import com.dcy.datamanage.biz.dto.output.DataAssessParamOutputDTO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class DataAssessService {

    @Autowired
    private DataSourceRegistry dataSourceRegistry;

    @Autowired
    private DataAssessDeptService dataAssessDeptService;

    @Autowired
    private DataAssessFrequencyService dataAssessFrequencyService;

    public List<DataAssessParamOutputDTO> list(DataAssessPageInputDTO dto) {
        String dataSourceId = dto.getDataSourceId();
        DefaultDataSource dataSource = dataSourceRegistry.getDataSource(dataSourceId);
        PageResult<BusDataTableOutputDTO> tablePage = dataSource.listTableSource(null);
        List<BusDataTableOutputDTO> tableList = tablePage.getRecords();

        //筛选
        if (!StringUtils.isEmpty(dto.getTableName())){
            tableList = tableList.stream().filter( t -> t.getTableName().contains(dto.getTableName())).collect(Collectors.toList());
        }
        List<DataAssessParamOutputDTO> result = new ArrayList<>();
        if (!CollectionUtils.isEmpty(tableList)) {
            //转换类型
            tableList.forEach(table -> {
                DataAssessParamOutputDTO outputDTO = new DataAssessParamOutputDTO();
                BeanUtils.copyProperties(table, outputDTO);
                result.add(outputDTO);
            });

            //表归属
            DataAssessDeptSearchInputDTO deptDto = new DataAssessDeptSearchInputDTO();
            deptDto.setDataSourceId(dataSourceId);
            List<DataAssessDeptSearchOutputDTO> list = dataAssessDeptService.list(deptDto);
            if (!CollectionUtils.isEmpty(list)) {
                Map<String, List<DataAssessDeptSearchOutputDTO>> deptMap = list.stream().collect(
                        Collectors.groupingBy(DataAssessDeptSearchOutputDTO::getTableName));
                result.forEach(table -> table.setDeptList(deptMap.get(table.getTableName())));
            }

            //频次
            DataAssessFrequencySearchInputDTO frequencyDto = new DataAssessFrequencySearchInputDTO();
            frequencyDto.setDataSourceId(dataSourceId);
            List<DataAssessFrequency> frequencyList = dataAssessFrequencyService.list(frequencyDto);
            if (!CollectionUtils.isEmpty(frequencyList)) {
                Map<String, DataAssessFrequency> frequencyMap = frequencyList.stream().collect(
                        Collectors.toMap(DataAssessFrequency::getTableName, Function.identity()));
                result.forEach(table -> table.setDataAssessFrequency(frequencyMap.get(table.getTableName())));
            }

        }
        return result;
    }
}
