package com.dcy.datamanage.biz.service;

import com.alibaba.fastjson.JSONObject;
import com.dcy.datamanage.api.model.BusDataDeptRel;
import com.dcy.datamanage.api.model.DataAssessExtendMsg;
import com.dcy.datamanage.api.model.common.PageModel;
import com.dcy.datamanage.api.model.common.PageResult;
import com.dcy.datamanage.biz.dsm.DataSourceRegistry;
import com.dcy.datamanage.biz.dsm.DefaultDataSource;
import com.dcy.datamanage.biz.dto.input.*;
import com.dcy.datamanage.biz.dto.output.BusDataColumnOutputDTO;
import com.dcy.datamanage.biz.dto.output.BusDataDeptOutputDTO;
import com.dcy.datamanage.biz.dto.output.BusDeptOutputDTO;
import com.dcy.datamanage.biz.feign.UmsFeign;
import com.dcy.datamanage.biz.util.SnowFlake;
import com.ygkj.auth.api.department.model.SysDept;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class DataService {

    @Autowired
    private DataSourceRegistry dataSourceRegistry;

    @Autowired
    private DataAssessExtendMsgService dataAssessExtendMsgService;

    @Autowired
    private DataDeptRelService dataDeptRelService;

    @Autowired
    private UmsFeign umsFeign;

    public PageResult<BusDataDeptOutputDTO> page(DataSearchInputDTO dto, PageModel pageModel) {
        DefaultDataSource dataSource = dataSourceRegistry.getDataSource(dto.getDataSourceId());
        PageResult<Map<String, Object>> dataPage = dataSource.page(dto, pageModel);
        PageResult<BusDataDeptOutputDTO> result = new PageResult<>();
        result.setTotal(dataPage.getTotal());
        result.setSize(dataPage.getSize());
        result.setCurrent(dataPage.getCurrent());
        result.setPages(dataPage.getPages());

        //每条数据的责任科室
        List<Map<String, Object>> dataList = dataPage.getRecords();
        if (!CollectionUtils.isEmpty(dataList)) {
            List<BusDataDeptOutputDTO> list = initList(dataList);
            result.setRecords(list);

            DataEditInputDTO editDTO = new DataDeptInputDTO();
            editDTO.setDataSourceId(dto.getDataSourceId());
            editDTO.setTableName(dto.getTableName());
            List<BusDataColumnOutputDTO> colList = dataSource.getColList(editDTO);
            List<String> primaryKeyList = colList.stream().filter(c -> c.getPrimary() == 1)
                    .map(BusDataColumnOutputDTO::getColumnName).collect(Collectors.toList());

            //有主键才去查所属科室
            if (!CollectionUtils.isEmpty(primaryKeyList)) {
                Map<String, BusDataDeptOutputDTO> primaryJointMap = new HashMap<>();
                List<List<ColValInputDTO>> colValListList = new ArrayList<>();
                for (BusDataDeptOutputDTO busDataDeptOutputDTO : list) {
                    Map<String, Object> data = busDataDeptOutputDTO.getData();
                    List<ColValInputDTO> colValList = new ArrayList<>();
                    StringBuilder sb = new StringBuilder();
                    for (String primaryKey : primaryKeyList) {
                        Object val = data.get(primaryKey);
                        sb.append(val).append("_");
                        ColValInputDTO colValInputDTO = new ColValInputDTO();
                        colValInputDTO.setColName(primaryKey);
                        colValInputDTO.setColVal(val);
                        colValList.add(colValInputDTO);
                    }
                    primaryJointMap.put(sb.toString(), busDataDeptOutputDTO);
                    colValListList.add(colValList);
                }
                //根据主键查询科室
                List<BusDataDeptRel> deptRelList = dataDeptRelService.listDept(new DataEditInputDTO(),colValListList);
                //有的话补上
                if (!CollectionUtils.isEmpty(deptRelList)) {
                    fillDept(deptRelList, primaryJointMap, primaryKeyList);
                }
            }
        }
        return result;
    }

    private List<BusDataDeptOutputDTO> initList(List<Map<String, Object>> dataList) {
        List<BusDataDeptOutputDTO> list = new ArrayList<>();
        for (Map<String, Object> data : dataList) {
            BusDataDeptOutputDTO dto = new BusDataDeptOutputDTO();
            dto.setData(data);
            list.add(dto);
        }
        return list;
    }

    private void fillDept(List<BusDataDeptRel> deptRelList, Map<String, BusDataDeptOutputDTO> primaryJointMap, List<String> primaryKeyList) {
        //部门名称
        List<String> deptIdList = deptRelList.stream().map(BusDataDeptRel::getDeptId).distinct().collect(Collectors.toList());
        List<SysDept> deptList = umsFeign.selectDeptByIdList(deptIdList);
        Map<String, String> deptNameMap = CollectionUtils.isEmpty(deptList) ? Collections.emptyMap() :
                deptList.stream().collect(Collectors.toMap(SysDept::getId, SysDept::getName));

        for (BusDataDeptRel busDataDeptRel : deptRelList) {
            StringBuilder sb = new StringBuilder();
            String primaryData = busDataDeptRel.getPrimaryData();
            JSONObject jsonObject = JSONObject.parseObject(primaryData);
            //根据所有主键找到对应的数据然后加上科室信息
            for (String primaryKey : primaryKeyList) {
                sb.append(jsonObject.get(primaryKey)).append("_");
            }
            String primaryJoint = sb.toString();
            if (primaryJointMap.containsKey(primaryJoint)) {
                BusDataDeptOutputDTO busDataDeptOutputDTO = primaryJointMap.get(primaryJoint);
                List<BusDeptOutputDTO> relDeptList = busDataDeptOutputDTO.getDeptList();
                if (relDeptList == null) {
                    relDeptList = new ArrayList<>();
                    busDataDeptOutputDTO.setDeptList(relDeptList);
                }
                BusDeptOutputDTO relDept = new BusDeptOutputDTO();
                relDept.setId(busDataDeptRel.getId());
                String deptId = busDataDeptRel.getDeptId();
                relDept.setDeptId(deptId);
                relDept.setDeptName(deptNameMap.get(deptId));
                relDeptList.add(relDept);
            }
        }
    }

    public String save(DataSaveInputDTO dto) {
        DefaultDataSource dataSource = dataSourceRegistry.getDataSource(dto.getDataSourceId());
        String result = dataSource.save(dto);
        saveUpdate(dto.getDataSourceId(), dto.getTableName());
        return result;
    }

    public Boolean del(DataDelInputDTO dto) {
        DefaultDataSource dataSource = dataSourceRegistry.getDataSource(dto.getDataSourceId());
        dataSource.del(dto);
        saveUpdate(dto.getDataSourceId(), dto.getTableName());
        return true;
    }

    public Boolean update(DataUpdateInputDTO dto) {
        DefaultDataSource dataSource = dataSourceRegistry.getDataSource(dto.getDataSourceId());
        dataSource.update(dto);
        saveUpdate(dto.getDataSourceId(), dto.getTableName());
        return true;
    }

    private void saveUpdate(String dataSourceId, String tableName) {
        DataAssessExtendMsg dataAssessExtendMsg = new DataAssessExtendMsg();
        dataAssessExtendMsg.setDataSourceId(dataSourceId);
        dataAssessExtendMsg.setTableName(tableName);
        dataAssessExtendMsg.setId(SnowFlake.zero.nextId());
        dataAssessExtendMsg.setUpdateTime(new Date());
        dataAssessExtendMsgService.replace(dataAssessExtendMsg);
    }
}
