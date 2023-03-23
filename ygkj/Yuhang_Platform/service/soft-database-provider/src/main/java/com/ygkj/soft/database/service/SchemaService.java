package com.ygkj.soft.database.service;

import com.alibaba.fastjson.JSONObject;
import com.ygkj.soft.database.api.VO.BusDataBaseAddVO;
import com.ygkj.soft.database.api.VO.BusDataTableAddVo;
import com.ygkj.soft.database.api.VO.BusDataTablePageQueryVo;
import com.ygkj.soft.database.api.VO.BusDataTableStatusChangeVo;
import com.ygkj.soft.database.api.model.BusDataColumn;
import com.ygkj.soft.database.api.model.BusDataSource;
import com.ygkj.soft.database.api.model.BusDataTable;
import ygkj.com.assistant.pager.Pager;

import java.sql.SQLException;
import java.util.List;

/**
 * @author Huang.zh
 * @date 2020/9/7 17:51
 * @Description: 数据源元数据服务层抽象
 */
public interface SchemaService {

    List<JSONObject> getBusDataSource(BusDataSource dataSource);

    List<BusDataTable> getBusDataTable(BusDataTable busDataTable);

    List<BusDataColumn> getBusDataColumn(BusDataColumn busDataColumn);

    void addDataSource(BusDataBaseAddVO dataBaseAddVO) throws Exception;

    boolean testDataSourceConnection(BusDataBaseAddVO dataBaseAddVO) throws SQLException;

    boolean initDataSource(String dataSourceID) throws Exception;

    void addBusDataColumn(List<BusDataColumn> list) throws Exception;

    void addBusDataTable(List<BusDataTable> list) throws Exception;

    void createSoftTable(List<BusDataTable> list,BusDataSource dataSource) throws Exception;

    Pager queryBusDataTableByPage(BusDataTablePageQueryVo vo);

    void  addBusDataTable(BusDataTableAddVo vo) throws Exception;

    void changeTableStatus(BusDataTableStatusChangeVo vo) throws Exception;
}
