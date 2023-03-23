package com.ygkj.water.project.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ygkj.project.model.DutyManagement;
import com.ygkj.project.vo.request.DutyReqVo;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DutyManagementMapper extends BaseMapper<DutyManagement> {
    /**
     * 批量保存
     * @param dutyManagements
     */
    void batchInsert(@Param("list") List<DutyManagement> dutyManagements);

    @Delete("delete from duty_management where duty_date like concat(#{yearMonth},'%') ")
    void deleteByYearAndMonth(@Param("yearMonth") String yearMonth);

    List<DutyManagement> loadPage(DutyReqVo reqVo);
}
