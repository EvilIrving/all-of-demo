package com.ygkj.mapper;

import com.ygkj.entity.Contacts;
import com.ygkj.entity.DeptIdUserNum;
import com.ygkj.entity.OrganSchema;
import com.ygkj.vo.req.ContactsPageReqVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author xq
 * @Description
 * @Date 2021/1/13
 */
public interface ContactsManageMapper {
    List<Contacts> selectAllContacts();

    List<Contacts> selectContactByParam(ContactsPageReqVO reqVO);

    List<Contacts> selectContactsByDeptId(@Param("deptId") String deptId);

    List<OrganSchema> selectAllDeptIdNamePid();

    List<DeptIdUserNum> selectDeptUserNum();
}
