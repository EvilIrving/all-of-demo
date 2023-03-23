package com.ygkj.service.impl;

import com.github.pagehelper.PageHelper;
import com.ygkj.client.AdcdClient;
import com.ygkj.entity.Contacts;
import com.ygkj.entity.DeptIdUserNum;
import com.ygkj.entity.OrganSchema;
import com.ygkj.mapper.ContactsManageMapper;
import com.ygkj.service.ContactsManageService;
import com.ygkj.utils.PageUtils;
import com.ygkj.vo.req.ContactsPageReqVO;
import com.ygkj.vo.resp.PageVO;
import com.ygkj.water.model.CommonCode;
import com.ygkj.water.model.CommonResult;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author xq
 * @Date 2021/1/13
 */
@Service
public class ContactsManageServiceImpl implements ContactsManageService {

    @Resource
    ContactsManageMapper contactsManageMapper;

    @Autowired
    AdcdClient adcdClient;

    @Override
    public PageVO<Contacts> selectContactsByPage(ContactsPageReqVO reqVO) {
        List<Contacts> list = null;
        PageVO<Contacts> results = null;
//        if (contactsPageReqVO.getDeptId() != null && !contactsPageReqVO.getDeptId().isEmpty()) {
//            PageHelper.startPage(contactsPageReqVO.getPageNum(), contactsPageReqVO.getPageSize());
//            list = contactsManageMapper.selectContactsByDeptId(contactsPageReqVO.getDeptId());
//            results = PageUtils.getPageVO(list);
//        } else {
//            PageHelper.startPage(contactsPageReqVO.getPageNum(), contactsPageReqVO.getPageSize());
//            list = contactsManageMapper.selectAllContacts();
//            results = PageUtils.getPageVO(list);
//        }
        PageHelper.startPage(reqVO.getPageNum(), reqVO.getPageSize());
        list = contactsManageMapper.selectContactByParam(reqVO);
        results = PageUtils.getPageVO(list);
        fillAdnm(results.getList());
        return results;
    }

    @Override
    public List<OrganSchema> selectOrganSchemaAsTree() {
        List<OrganSchema> organSchemas = contactsManageMapper.selectAllDeptIdNamePid();
        List<OrganSchema> result = null;
        if (organSchemas != null && !organSchemas.isEmpty()) {
            List<DeptIdUserNum> deptIdUserNums = contactsManageMapper.selectDeptUserNum();
            Map<String, Integer> numMap = null;
            if (deptIdUserNums != null && !deptIdUserNums.isEmpty()) {
                numMap = new HashMap<>(deptIdUserNums.size());
                for (DeptIdUserNum deptIdUserNum : deptIdUserNums) {
                    numMap.put(deptIdUserNum.getDeptId(), deptIdUserNum.getNumOrDefault(0));
                }
            } else {
                numMap = new HashMap<>(1);
            }
            Map<String, OrganSchema> idMap = new HashMap<>(organSchemas.size());
            for (OrganSchema organSchema : organSchemas) {
                int num = numMap.getOrDefault(organSchema.getId(), 0);
                organSchema.setNum(num);
                idMap.put(organSchema.getId(), organSchema);
            }
            result = new ArrayList<>(organSchemas.size());
            for (OrganSchema organSchema : organSchemas) {
                OrganSchema parentObject = idMap.get(organSchema.getPid());
                if (parentObject != null) {
                    if (parentObject.getChildren() == null) {
                        parentObject.setChildren(new ArrayList<>());
                    }
                    parentObject.getChildren().add(organSchema);
                    parentObject.setNum(parentObject.getNum() + organSchema.getNum());
                } else {
                    result.add(organSchema);
                }
            }
        }
        if (result.size() == 1) {
            OrganSchema organSchema = result.get(0);
            organSchema.setChildren(organSchema.getChildren().stream().filter(OrganSchema::isCounty).collect(Collectors.toList()));
        }
        return result;
    }

    private void fillAdnm(List<Contacts> contacts) {
        if (contacts != null && !contacts.isEmpty()) {
            HashSet<String> adcdSet = new HashSet<>(contacts.size());
            for (Contacts contact : contacts) {
                adcdSet.add(contact.getAdcd());
            }
            CommonResult commonResult = adcdClient.selectAdnmByAdcdList(new ArrayList<>(adcdSet));
            if (CommonCode.SUCCESS.code() == commonResult.getCode()) {
                List<LinkedHashMap> attAdcdList = (List<LinkedHashMap>) commonResult.getData();
                if (attAdcdList != null && !attAdcdList.isEmpty()) {
                    Map<String, String> map = new HashMap<>(attAdcdList.size());
                    for (LinkedHashMap attAdcd : attAdcdList) {
                        map.put((String) attAdcd.get("adcd"), (String) attAdcd.get("adnm"));
                    }
                    for (Contacts contact : contacts) {
                        contact.setAdnm(map.getOrDefault(contact.getAdcd(), null));
                    }
                }
            }
        }
    }

    @Override
    public Workbook exportDataToExcel(String deptId) {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet();
        Row titleRow = sheet.createRow(0);
        titleRow.createCell(0).setCellValue("序号");
        titleRow.createCell(1).setCellValue("姓名");
        titleRow.createCell(2).setCellValue("性别");
        titleRow.createCell(3).setCellValue("移动电话");
        titleRow.createCell(4).setCellValue("电子邮件");
        titleRow.createCell(5).setCellValue("行政区划");
        titleRow.createCell(6).setCellValue("部门");
        titleRow.createCell(7).setCellValue("职务");
        List<Contacts> results = null;
        if (deptId != null && !deptId.isEmpty()) {
            results = contactsManageMapper.selectContactsByDeptId(deptId);
        } else {
            results = contactsManageMapper.selectAllContacts();
        }
        fillAdnm(results);
        int rowNum = 1;
        Row curRow = null;
        for (Contacts result : results) {
            curRow = sheet.createRow(rowNum);
            curRow.createCell(0).setCellValue(rowNum);
            curRow.createCell(1).setCellValue(result.getUsername());
            if (result.getSex() == 1) {
                curRow.createCell(2).setCellValue("男");
            } else if (result.getSex() == 2) {
                curRow.createCell(2).setCellValue("女");
            }
            curRow.createCell(3).setCellValue(result.getPhone());
            curRow.createCell(4).setCellValue(result.getEmail());
            curRow.createCell(5).setCellValue(result.getAdnm());
            curRow.createCell(6).setCellValue(result.getDeptName());
            curRow.createCell(7).setCellValue(result.getJob());
            rowNum++;
        }
        return workbook;
    }
}
