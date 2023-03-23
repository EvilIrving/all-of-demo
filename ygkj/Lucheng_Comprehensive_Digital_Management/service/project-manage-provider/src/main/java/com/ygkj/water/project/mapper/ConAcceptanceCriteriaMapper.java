package com.ygkj.water.project.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ygkj.project.model.ConAcceptanceCriteria;
import com.ygkj.project.vo.request.ConAcceptanceCriteriaReqVo;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConAcceptanceCriteriaMapper extends BaseMapper<ConAcceptanceCriteria> {
    @Select("<script>select tbl.*,t1.company_name deptName" +
            " from con_acceptance_criteria tbl " +
            "left join professional_company t1 " +
            "on tbl.dept_id = t1.id " +
            "where 1 = 1" +
            "<if test=\"id !=null and id !=''\">and id =#{id}</if>" +
            "<if test=\"items !=null and items !=''\">and items =#{items}</if>" +
            "<if test=\"deptId !=null and deptId !=''\">and dept_id =#{deptId}</if> " +
            "and del_flag='0' and dept_id is not null order by create_time desc </script>")
    List<ConAcceptanceCriteria> list(ConAcceptanceCriteriaReqVo vo);
}
