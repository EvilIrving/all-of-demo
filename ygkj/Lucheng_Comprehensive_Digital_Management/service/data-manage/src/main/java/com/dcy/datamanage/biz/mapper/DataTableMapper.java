package com.dcy.datamanage.biz.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dcy.datamanage.api.model.BusDataTable;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface DataTableMapper extends BaseMapper<BusDataTable> {

    /**
     * 表别名查询
     *
     * @param tableNameList
     * @param dataSourceId
     * @return
     */
    List<BusDataTable> listTableExtend(@Param("tableNameList") List<String> tableNameList, @Param("dataSourceId") String dataSourceId);

    /**
     * 批量替换
     *
     * @param list
     */
    void replaceAll(List<BusDataTable> list);
}
