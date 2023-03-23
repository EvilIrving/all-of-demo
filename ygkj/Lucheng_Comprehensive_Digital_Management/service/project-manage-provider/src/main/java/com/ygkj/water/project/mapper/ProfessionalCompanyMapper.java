package com.ygkj.water.project.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ygkj.project.model.ProfessionalCompany;
import com.ygkj.project.model.SysUser;
import com.ygkj.project.vo.request.WyhUserVo;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * 物业化公司 Mapper 接口
 * </p>
 *
 * @author wgf
 * @since 2021-11-23
 */
@Repository
public interface ProfessionalCompanyMapper extends BaseMapper<ProfessionalCompany> {

    @Select(" SELECT id, username, phone, dept_id deptId  FROM `sys_user` WHERE deleted = 1 AND find_in_set(dept_id ,#{deptId})")
    List<WyhUserVo> getWyhUserByDeptId(WyhUserVo wyhUserVo);

    @Select("select `name` companyName , id from sys_dept_water_manage where `name` like concat('%',#{key},'%') ")
    List<ProfessionalCompany> getWaterManageDeptList(String key);

    @Select(" SELECT su.id,su.username,su.user_type userType FROM professional_company pc \n" +
            " LEFT JOIN sys_user su\n" +
            " on pc.id = su.dept_id\n" +
            " where su.id = #{currentUserId} and su.deleted=1")
    SysUser selectWyLeader(String currentUserId);
}
