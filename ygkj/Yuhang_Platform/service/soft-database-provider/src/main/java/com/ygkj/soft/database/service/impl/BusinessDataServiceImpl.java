package com.ygkj.soft.database.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.PropertyNamingStrategy;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.ygkj.soft.database.constant.Constants;
import com.ygkj.soft.database.service.BusinessDataService;
import com.ygkj.jwt.JwtTokenUtil;
import com.ygkj.soft.database.api.VO.*;
import com.ygkj.soft.database.api.model.*;
import com.ygkj.soft.database.dao.BusDataResourceDao;
import com.ygkj.soft.database.dao.BusDataSourcePersistenceDao;
import com.ygkj.soft.database.dao.BusinessDataDao;
import com.ygkj.soft.database.dao.SchemaDao;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import ygkj.com.assistant.pager.EasyuiPager;
import ygkj.com.assistant.pager.Pager;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Huang.zh
 * @date 2020/9/8 17:13
 * @Description: 业务数据服务具体实现
 */
@Service("businessDataServiceImpl")
@Slf4j
public class BusinessDataServiceImpl implements BusinessDataService {

    @Autowired
    private BusinessDataDao businessDataDao;

    @Autowired
    private SchemaDao schemaDao;

    @Autowired
    private BusDataResourceDao busDataResourceDao;

    @Autowired
    private BusDataSourcePersistenceDao busDataSourcePersistenceDao;

    SerializeConfig config = new SerializeConfig();

    {
        config.propertyNamingStrategy = PropertyNamingStrategy.SnakeCase;
    }

    @Override
    public Pager queryBusinessDataByPage(BusinessDataQueryVO queryVO, EasyuiPager easyuiPager) {
        return businessDataDao.queryBusinessDataByPage(queryVO, easyuiPager);
    }

    @Override
    public void insertSoftData(SoftDataAddVO softDataAddVO) throws Exception {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String token = request.getHeader("authorization");
        if (!softDataAddVO.getOperationType().equals("1")){
            //只能操作自己科室下的数据
            String deptId = JwtTokenUtil.getDeptId(token);
            BusinessDataQueryVO businessDataQueryVO = new BusinessDataQueryVO();
            BeanUtils.copyProperties(softDataAddVO,businessDataQueryVO);
            Pager pager = queryBusinessDataByPage(businessDataQueryVO, EasyuiPager.fetchAll());
            JSONObject object = pager.getList(JSONObject.class).get(0);
            if (org.apache.commons.lang3.StringUtils.isNotBlank(object.getString(Constants.DEFAULT_DEPARTMENT_ID_KEY)) &&
                    !deptId.equals(object.getString(Constants.DEFAULT_DEPARTMENT_ID_KEY))){
                throw new RuntimeException("当前用户所属科室没有权限操作该条数据！");
            }
        }
        businessDataDao.insertSoftData(softDataAddVO);
    }

    @Override
    public JSONObject querySoftData(SoftDataQueryVO softDataQueryVO,EasyuiPager easyuiPager) throws Exception {
        JSONObject result = new JSONObject();
        Pager pager = businessDataDao.querySoftData(softDataQueryVO, easyuiPager);
        List<SoftData> list = pager.getList(SoftData.class);
        BusDataTable table = new BusDataTable();
        table.setTableName(softDataQueryVO.getTableName());
        table = schemaDao.getBusDataTable(table).get(0);
        //组装表头
        List<BusDataColumn> column = schemaDao.getBusDataColumn(new BusDataColumn(table.getId()));
        column.sort(Comparator.comparingInt(BusDataColumn::getOrderColumn));
        List<BusDataColumn> tableColumn =
                schemaDao.getSoftTableColumn(softDataQueryVO.getDataBaseName().concat("_").concat(softDataQueryVO.getTableName()))
                .stream().filter(busDataColumn -> !busDataColumn.getName().equals("data")).collect(Collectors.toList());
        tableColumn.addAll(column);
        List<JSONObject> jsonData = new ArrayList<>();
        for (SoftData softData : list) {
            //业务数据
            JSONObject object = JSONObject.parseObject(softData.getData());
            if (object == null){
                object = JSONObject.parseObject(JSON.toJSONString(softData, config));
                jsonData.add(object);
                continue;
            }
            //中间数据
            JSONObject jsonObject = JSONObject.parseObject(JSON.toJSONString(softData, config));
            jsonObject.remove("data");
            object.putAll(jsonObject);
            jsonData.add(object);
        }
        pager.setList(jsonData);
        result.put("page",pager);
        result.put("column",tableColumn);
        return result;
    }

    @GlobalTransactional(rollbackFor = Exception.class)
    @Override
    public void updateSoftDataStatus(SoftDataUpdateVO softDataUpdateVO) throws Exception {
        int rows = businessDataDao.updateSoftDataStatus(softDataUpdateVO);
        if (rows != 1){
            throw new RuntimeException("操作失败！请联系管理员");
        }
        if (softDataUpdateVO.getStatus().equals(String.valueOf(Constants.PASS_STATUS))){
            //审核通过，操作正式库数据
            SoftDataQueryVO queryVO = new SoftDataQueryVO();
            BeanUtils.copyProperties(softDataUpdateVO,queryVO);
            List<SoftData> list = businessDataDao.querySoftData(queryVO, new EasyuiPager()).getList(SoftData.class);
            SoftData softData = list.get(0);
            rows = businessDataDao.operateBusinessTableData(softData);
            if (softData.getOperationType().equals(Constants.OPERATION_ADD)){
                //新增数据需要再做一步，更新当日入库的记录数
                BusDataSourcePersistence busDataSourcePersistence = new BusDataSourcePersistence(
                        UUID.randomUUID().toString(),softData.getDataBaseName(),new Date(),rows);
                busDataSourcePersistenceDao.updateCurrentDayPersistence(busDataSourcePersistence);
            }
//            if (rows != 1){
//                throw new RuntimeException("操作失败！请联系管理员");
//            }
        }
    }

    @Override
    public void addResource(BusDataResource busDataResource) throws Exception {
        if (StringUtils.isEmpty(busDataResource.getId())){
            busDataResource.setId(UUID.randomUUID().toString());
        }
        int row = busDataResourceDao.add(busDataResource);
        if (row != 1){
            throw new RuntimeException("操作失败！请联系管理员");
        }
    }

    @Override
    public List<BusDataResource> findDataResource(BusDataResource busDataResource) {
        return busDataResourceDao.findBusDataSource(busDataResource);
    }

    @Override
    public void divideTableResource(TableDivideVO tableDivideVO) throws Exception{
        int rows = busDataResourceDao.divideTableResource(tableDivideVO);
        if(rows < 1){
            throw new RuntimeException("操作失败！请联系管理员！");
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteBusDataResource(String resourceId) throws Exception {
        int row = busDataResourceDao.delete(resourceId);
        if (row != 1){
            throw new RuntimeException("操作失败！请联系管理员！");
        }
        businessDataDao.clearTablesResource(resourceId);
    }

    @Override
    public void updateBusDataResource(BusDataResource busDataResource) throws Exception {
        int row = busDataResourceDao.update(busDataResource);
        if (row != 1){
            throw new RuntimeException("操作失败！请联系管理员！");
        }
    }

    @Override
    public void divideBusinessData(DataSourceDivideVO divideVO) throws Exception {
        businessDataDao.divideBusinessData(divideVO);
    }
}
