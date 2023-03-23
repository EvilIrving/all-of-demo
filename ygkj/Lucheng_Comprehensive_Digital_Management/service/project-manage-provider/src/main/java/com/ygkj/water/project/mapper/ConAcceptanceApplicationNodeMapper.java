package com.ygkj.water.project.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ygkj.project.model.ConAcceptanceApplicationNode;
import com.ygkj.project.vo.request.ConAcceptanceApplicationNodeReqVo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConAcceptanceApplicationNodeMapper extends BaseMapper<ConAcceptanceApplicationNode> {
    List<ConAcceptanceApplicationNode> loadNodes(ConAcceptanceApplicationNodeReqVo vo);

    List<ConAcceptanceApplicationNode> findCurrentNode(@Param("applicationId") String applicationId, @Param("nodeId")Integer nodeId);

    ConAcceptanceApplicationNode getFirstNode(@Param("applicationid") String applicationid);

    ConAcceptanceApplicationNode getAfterNode(@Param("applicationid") String applicationid, @Param("nodeId") Integer nodeId);

    @Select(" select id from con_acceptance_application_node where applicationId = #{applicationid} and del_flag = 0 order by sort desc limit 1 ")
    Integer getMaxNodeId(@Param("applicationid") String applicationid);

    @Update("update con_acceptance_application_node set status = 1 where del_flag = 0 and sort > 1 and applicationId = #{applicationid}")
    void recover(@Param("applicationid") String applicationid);

    @Select("select t1.*,t2.username unitReviewerName from con_acceptance_application_node t1 left join sys_user t2 on t1.unit_reviewer_id=t2.id where t1.id=#{currentnode}")
    ConAcceptanceApplicationNode getCurrentNode(@Param("currentnode") Integer currentnode);

    List<ConAcceptanceApplicationNode> getNodesByAssociationId(String applicationid);
}
