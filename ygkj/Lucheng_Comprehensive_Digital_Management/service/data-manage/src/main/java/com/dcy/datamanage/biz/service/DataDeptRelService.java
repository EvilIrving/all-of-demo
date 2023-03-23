package com.dcy.datamanage.biz.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dcy.datamanage.api.model.common.R;
import com.dcy.datamanage.api.model.BusDataDeptRel;
import com.dcy.datamanage.biz.dsm.DataSourceRegistry;
import com.dcy.datamanage.biz.dsm.DefaultDataSource;
import com.dcy.datamanage.biz.dto.input.ColValInputDTO;
import com.dcy.datamanage.biz.dto.input.DataDeptInputDTO;
import com.dcy.datamanage.biz.dto.input.DataEditInputDTO;
import com.dcy.datamanage.biz.dto.output.BusDataColumnOutputDTO;
import com.dcy.datamanage.biz.dto.output.BusDataDeptOutputDTO;
import com.dcy.datamanage.biz.dto.output.BusDeptOutputDTO;
import com.dcy.datamanage.biz.mapper.DataDeptRelMapper;
import com.dcy.datamanage.biz.util.SnowFlake;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class DataDeptRelService extends ServiceImpl<DataDeptRelMapper, BusDataDeptRel> {

    @Autowired
    private DataSourceRegistry dataSourceRegistry;

    public R<Boolean> saveBatch(DataDeptInputDTO dto) {
        String deptId = dto.getDeptId();
        String dataSourceId = dto.getDataSourceId();
        String tableName = dto.getTableName();
        String operateUserId = dto.getOperateUserId();
        Date date = new Date();
        List<BusDataDeptOutputDTO> dataList = dto.getDataList();
        List<BusDataDeptRel> list = new ArrayList<>();
        DefaultDataSource dataSource = dataSourceRegistry.getDataSource(dataSourceId);
        List<BusDataColumnOutputDTO> colList = dataSource.getColList(dto);
        List<String> primaryKeyList = colList.stream().filter(c -> c.getPrimary() == 1)
                .map(BusDataColumnOutputDTO::getColumnName).collect(Collectors.toList());
        //没有主键的表暂时不支持分配
        if (CollectionUtils.isEmpty(primaryKeyList)) {
            return R.error("暂不支持指派无主键表中的数据");
        }

        out:
        for (BusDataDeptOutputDTO dataDeptOutputDTO : dataList) {
            List<BusDeptOutputDTO> deptList = dataDeptOutputDTO.getDeptList();
            if(!CollectionUtils.isEmpty(deptList)){
                for (BusDeptOutputDTO dept : deptList) {
                    //之前该条数据已经给这个科室分配过了就直接跳过
                    if (deptId.equals(dept.getDeptId())) {
                        continue out;
                    }
                }
            }

            BusDataDeptRel busDataDeptRel = new BusDataDeptRel();
            busDataDeptRel.setId(SnowFlake.zero.nextId());
            busDataDeptRel.setDataSourceId(dataSourceId);
            busDataDeptRel.setTableName(tableName);
            busDataDeptRel.setDeptId(deptId);
            busDataDeptRel.setCreateTime(date);
            busDataDeptRel.setCreateId(operateUserId);
            busDataDeptRel.setPrimaryData(getPrimaryData(dataDeptOutputDTO.getData(), primaryKeyList));
            list.add(busDataDeptRel);
        }

        if (!CollectionUtils.isEmpty(list)) {
            saveBatch(list);
        }

        return R.success(true);
    }

    private String getPrimaryData(Map<String, Object> data, List<String> primaryKeyList) {
        JSONObject jsonObject = new JSONObject();
        for (String primaryKey : primaryKeyList) {
            jsonObject.put(primaryKey, data.get(primaryKey));
        }
        return jsonObject.toString();
    }

    public Boolean delBatch(List<String> idList) {
        return removeBatchByIds(idList);
    }

    public List<BusDataDeptRel> listDept(DataEditInputDTO dto,List<List<ColValInputDTO>> colValListList) {
        return baseMapper.listDept(dto,colValListList);
    }
}
