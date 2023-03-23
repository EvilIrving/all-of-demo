package com.ygkj.controller;

import com.ygkj.aop.annotation.LogAnnotation;
import com.ygkj.service.ContactsManageService;
import com.ygkj.vo.req.ContactsPageReqVO;
import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;

/**
 * @author xq
 * @Description 通讯录管理
 * @Date 2021/1/13
 */
@RestController
@RequestMapping("/contacts")
@Api(tags = "日常业务管理-通讯录管理")
public class ContactsManageController {

    @Autowired
    ContactsManageService contactsManageService;

    @PostMapping("/getContacts")
    @ApiOperation(value = "分页获取通讯录接口")
    @LogAnnotation(title = "通讯录管理", action = "分页获取通讯录列表")
    public CommonResult getContacts(ContactsPageReqVO contactsPageReqVO) {
//        ContactsPageReqVO contactsPageReqVO = new ContactsPageReqVO();
//        contactsPageReqVO.setDeptId(deptId);
//        contactsPageReqVO.setId(id);
//        contactsPageReqVO.setPageSize(pageSize == null ? 0 : pageSize.intValue());
//        contactsPageReqVO.setPageNum(pageNum == null ? 0 : pageNum.intValue());
        return CommonResult.success(contactsManageService.selectContactsByPage(contactsPageReqVO));
    }

    @GetMapping("/getOrganSchema")
    @ApiOperation(value = "以树形结构获取组织架构接口")
    @LogAnnotation(title = "通讯录管理", action = "以树形结构获取组织架构")
    public CommonResult getOrganSchema() {
        return CommonResult.success(contactsManageService.selectOrganSchemaAsTree());
    }

    @PostMapping("/exportContactsToExcel")
    @ApiOperation(value = "导出通讯录至Excel接口")
    @LogAnnotation(title = "通讯录管理", action = "导出通讯录至Excel")
    public void exportContactsToExcel(HttpServletResponse response,
                                      @RequestParam(value = "deptId", required = false) String deptId) throws IOException {
        Workbook workbook = contactsManageService.exportDataToExcel(deptId);
        //弹出一个"文件下载"的对话框
        response.setContentType("application/octet-stream");
        //文件名为中文时会乱码，需要转一下
        response.setHeader("Content-disposition", "attachment;filename=" + URLEncoder.encode("通讯录", "UTF-8") + ".xlsx");
        response.setHeader("Access-Control-Expose-Headers", "Content-disposition");
        response.flushBuffer();
        workbook.write(response.getOutputStream());
    }


}
