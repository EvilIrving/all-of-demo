package com.dcy.datamanage.biz.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dcy.datamanage.biz.enums.DataManageRoleEnum;
import com.dcy.datamanage.api.model.common.PageModel;
import com.dcy.datamanage.api.model.common.PageResult;
import com.dcy.datamanage.api.model.common.R;
import com.dcy.datamanage.api.model.BusDataApply;
import com.dcy.datamanage.api.model.BusDataDeptRel;
import com.dcy.datamanage.biz.dsm.DataSourceRegistry;
import com.dcy.datamanage.biz.dsm.DefaultDataSource;
import com.dcy.datamanage.biz.dto.input.*;
import com.dcy.datamanage.biz.dto.output.BusDataColumnOutputDTO;
import com.dcy.datamanage.biz.enums.DataApplyEnum;
import com.dcy.datamanage.biz.enums.DataApplySearchEnum;
import com.dcy.datamanage.biz.mapper.DataApplyMapper;
import com.dcy.datamanage.biz.util.PageUtil;
import com.dcy.datamanage.biz.util.SnowFlake;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class DataApplyService extends ServiceImpl<DataApplyMapper, BusDataApply> {

    @Autowired
    private DataSourceRegistry dataSourceRegistry;

    @Autowired
    private DataService dataService;

    @Autowired
    private DataDeptRelService dataDeptRelService;

    @Autowired
    private DataAssessDeptService dataAssessDeptService;

    private final String noPrimaryMsg = "暂不支持删除和修改无主键的表中的数据";
    private final String noPermissionMsg = "您无修改该条数据的权限";
    private final String pendingMsg = "该数据还有未完成的审批";

    public R<Boolean> applySave(DataSaveInputDTO dto) {
        DefaultDataSource dataSource = dataSourceRegistry.getDataSource(dto.getDataSourceId());
//        List<BusDataColumnOutputDTO> colList = dataSource.getColList(dto);
//        List<String> primaryKeyList = colList.stream().filter(c -> c.getPrimary() == 1)
//                .map(BusDataColumnOutputDTO::getColumnName).collect(Collectors.toList());

//        //主键为空自动填上
//        if (!CollectionUtils.isEmpty(primaryKeyList)) {
//            Map<String, Object> data = dto.getData();
//            for (String primaryKey:primaryKeyList){
//                Object o = data.get(primaryKey);
//                if(ObjectUtils.isEmpty(o)){
//                    data.put(primaryKey,SnowFlake.zero.nextId());
//                }
//            }
//        }

        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        DataSourceTransactionManager dataSourceTransactionManager = dataSource.getDataSourceTransactionManager();
        TransactionStatus status = dataSourceTransactionManager.getTransaction(def);

        //首先尝试存入数据
        String msg = dataSource.save(dto);
        if (msg != null) {
            return R.error(msg);
        }

        //回滚数据
        dataSourceTransactionManager.rollback(status);

        BusDataApply busDataApply = initBusDataApply(dto, DataApplyEnum.ADD.getApplyType());
        save(busDataApply);
        return R.success(true);
    }

    private BusDataApply initBusDataApply(DataEditInputDTO dto, Integer applyType) {
        BusDataApply busDataApply = new BusDataApply();
        busDataApply.setId(SnowFlake.zero.nextId());
        busDataApply.setApplyStatus(0);
        busDataApply.setApplyType(applyType);
        busDataApply.setCreateTime(new Date());
        busDataApply.setDataSourceId(dto.getDataSourceId());
        busDataApply.setTableName(dto.getTableName());
        busDataApply.setDataObject(JSONObject.toJSONString(dto));
        busDataApply.setDeptId(dto.getOperateDeptId());
        busDataApply.setCreateId(dto.getOperateUserId());
        return busDataApply;
    }

    public R<Boolean> applyDel(DataDelInputDTO dto) {
        List<String> primaryList = getPrimaryList(dto);
        if (CollectionUtils.isEmpty(primaryList)) {
            return R.error(noPrimaryMsg);
        }
        List<ColValInputDTO> colValList = new ArrayList<>();
        //首先查询是否有编辑权限
        if (!checkEdit(dto, dto.getData(), primaryList, colValList)) {
            return R.error(noPermissionMsg);
        }

        return addDataEditApply(dto, DataApplyEnum.DELETE.getApplyType(), colValList);
    }

    private synchronized R<Boolean> addDataEditApply(DataEditInputDTO dto, Integer applyType, List<ColValInputDTO> colValList) {
        //然后查询该条数据是否还有未处理的审批
        if (pending(dto, colValList)) {
            return R.error(pendingMsg);
        }
        BusDataApply initBusDataApply = initBusDataApply(dto, applyType);
        save(initBusDataApply);
        return R.success(true);
    }

    private boolean pending(DataEditInputDTO dto, List<ColValInputDTO> colValList) {
        return baseMapper.countPending(dto, colValList) > 0;
    }

    private List<String> getPrimaryList(DataEditInputDTO dto) {
        DefaultDataSource dataSource = dataSourceRegistry.getDataSource(dto.getDataSourceId());
        List<BusDataColumnOutputDTO> colList = dataSource.getColList(dto);
        List<String> primaryList = colList.stream().filter(col -> col.getPrimary() == 1)
                .map(BusDataColumnOutputDTO::getColumnName).collect(Collectors.toList());
        return primaryList;
    }

    private boolean checkEdit(DataEditInputDTO dto, Map<String, Object> data, List<String> primaryList, List<ColValInputDTO> colValList) {
        for (String primaryKey : primaryList) {
            ColValInputDTO colVal = new ColValInputDTO();
            colVal.setColName(primaryKey);
            colVal.setColVal(data.get(primaryKey));
            colValList.add(colVal);
        }

        //管理员直接有权限
        if (DataManageRoleEnum.ADMIN.getRoleType().equals(dto.getRoleType())) {
            return true;
        }

        //优先查询是否单独分配过权限
        LambdaQueryWrapper<BusDataDeptRel> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(BusDataDeptRel::getDataSourceId, dto.getDataSourceId())
                .eq(BusDataDeptRel::getTableName, dto.getTableName());
        if (dataDeptRelService.count(queryWrapper) > 0) {
            //通过主键判断有没有该条数据的权限
            List<List<ColValInputDTO>> colValListList = new ArrayList<>();
            colValListList.add(colValList);
            return !CollectionUtils.isEmpty(dataDeptRelService.listDept(dto, colValListList));
        }
        //没有任何单条权限查询是否有整表权限
        else if (dataAssessDeptService.haveTablePermission(dto)) {
            return true;
        }

        return false;
    }

    public R<Boolean> applyUpdate(DataUpdateInputDTO dto) {
        List<String> primaryList = getPrimaryList(dto);
        if (CollectionUtils.isEmpty(primaryList)) {
            return R.error(noPrimaryMsg);
        }
        List<ColValInputDTO> colValList = new ArrayList<>();
        //首先查询是否有编辑权限
        if (!checkEdit(dto, dto.getOldData(), primaryList, colValList)) {
            return R.error(noPermissionMsg);
        }

        return addDataEditApply(dto, DataApplyEnum.UPDATE.getApplyType(), colValList);
    }

    /**
     * 分页查询待处理任务
     *
     * @param dto
     * @param pageModel
     * @return
     */
    public PageResult<BusDataApply> page(DataApplySearchInputDTO dto, PageModel pageModel) {
        LambdaQueryWrapper<BusDataApply> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(dto.getApplyStatus() != null, BusDataApply::getApplyStatus, dto.getApplyStatus());
        queryWrapper.eq(dto.getApplyType() != null, BusDataApply::getApplyType, dto.getApplyType());
        Integer type = dto.getType();
        if (DataApplySearchEnum.SEND.getType().equals(type)) {
            queryWrapper.eq(BusDataApply::getCreateId, dto.getUserId());
        } else if (DataApplySearchEnum.PENDING.getType().equals(type)) {
            queryWrapper.eq(BusDataApply::getApplyStatus, 0);
            //科室管理员看到本部门的申请
            if (DataManageRoleEnum.DEPT_ADMIN.getRoleType().equals(dto.getRoleType())) {
                queryWrapper.eq(BusDataApply::getDeptId, dto.getDeptId());
            }
            //管理员看到所有的申请
            else if (DataManageRoleEnum.GUEST.getRoleType().equals(dto.getRoleType())) {
                return new PageResult();
            }
        } else {
            queryWrapper.eq(BusDataApply::getUserId, dto.getUserId());
        }
        queryWrapper.orderByDesc(BusDataApply::getUpdateTime).orderByDesc(BusDataApply::getCreateTime);
        PageHelper.startPage(pageModel.getCurrent().intValue(), pageModel.getSize().intValue());
        List<BusDataApply> list = list(queryWrapper);
        return PageUtil.toPage(list);
    }

    @Transactional(rollbackFor = Exception.class)
    public Boolean update(BusDataApply busDataApply) {
        busDataApply.setUpdateTime(new Date());
        //通过的话顺便处理数据
        if (busDataApply.getApplyStatus() == 1) {
            Integer applyType = busDataApply.getApplyType();
            String dataObject = busDataApply.getDataObject();
            JSONObject jsonObject = JSONObject.parseObject(dataObject);
            if (DataApplyEnum.ADD.getApplyType().equals(applyType)) {
                DataSaveInputDTO dto = JSONObject.toJavaObject(jsonObject, DataSaveInputDTO.class);
                dataService.save(dto);
            } else if (DataApplyEnum.DELETE.getApplyType().equals(applyType)) {
                DataDelInputDTO dto = JSONObject.toJavaObject(jsonObject, DataDelInputDTO.class);
                dataService.del(dto);
            } else {
                DataUpdateInputDTO dto = JSONObject.toJavaObject(jsonObject, DataUpdateInputDTO.class);
                dataService.update(dto);
            }
        }

        return updateById(busDataApply);
    }
}
