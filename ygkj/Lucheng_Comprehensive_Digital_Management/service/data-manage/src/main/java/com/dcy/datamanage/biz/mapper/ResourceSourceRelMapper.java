package com.dcy.datamanage.biz.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dcy.datamanage.api.model.BusDataResourceSourceRel;
import com.dcy.datamanage.biz.dto.input.ResourceSourceRelSearchInputDTO;
import com.dcy.datamanage.biz.dto.output.BusDataTableOutputDTO;
import com.dcy.datamanage.biz.dto.output.ResourceSourceRelOutputDTO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ResourceSourceRelMapper extends BaseMapper<BusDataResourceSourceRel> {

    /**
     * 关联列表
     *
     * @param dto
     * @return
     */
    List<ResourceSourceRelOutputDTO> list(ResourceSourceRelSearchInputDTO dto);

    /**
     * 批量修改资源目录
     *
     * @param list
     */
    void replaceAll(List<BusDataResourceSourceRel> list);

    /**
     * 删除这些绑定关系
     *
     * @param list
     */
    void removeBySourceTable(List<BusDataResourceSourceRel> list);

    /**
     * 表的资源目录列表
     *
     * @param dataSourceId
     * @param tableNameList
     * @return
     */
    List<BusDataTableOutputDTO> list2(@Param("dataSourceId") String dataSourceId, @Param("tableNameList") List<String> tableNameList);
}
