package com.ygkj.service;

import com.ygkj.entity.Contacts;
import com.ygkj.entity.OrganSchema;
import com.ygkj.entity.SysDept;
import com.ygkj.vo.req.ContactsPageReqVO;
import com.ygkj.vo.resp.PageVO;
import org.apache.poi.ss.usermodel.Workbook;

import java.util.List;

/**
 * @author Lenovo
 * 
 * @Date 2021/1/13
 */
public interface ContactsManageService {
    PageVO<Contacts> selectContactsByPage(ContactsPageReqVO contactsPageReqVO);

    List<OrganSchema> selectOrganSchemaAsTree();

    Workbook exportDataToExcel(String deptId);

}
