package com.ygkj.digitization.controller;

import com.ygkj.digitization.MonitorManageControllerApi;
import com.ygkj.digitization.enums.MonitorPersonTypeEnum;
import com.ygkj.digitization.model.MonitorContact;
import com.ygkj.digitization.service.MonitorManageService;
import com.ygkj.digitization.vo.request.*;
import com.ygkj.digitization.vo.response.MonitorPersonResVo;
import com.ygkj.utils.ExportUtils;
import com.ygkj.water.model.CommonResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author xq
 * @Description 值班管理控制层
 * @Date 2021/1/13
 */
@RestController
@RequestMapping("/monitorManage")
public class MonitorManageController implements MonitorManageControllerApi {

    @Autowired
    MonitorManageService monitorManageService;


    @GetMapping("/person/getMonitorPersonList")
    @Override
    public CommonResult getMonitorPersonList(@RequestParam("pageNum") Integer pageNum,
                                             @RequestParam("pageSize") Integer pageSize,
                                             @RequestParam(value = "personType", required = false) Integer personType,
                                             @RequestParam(value = "username", required = false) String username) {
        if (pageNum == null) {
            pageNum = 0;
        }
        if (pageSize == null) {
            pageSize = 0;
        }
        return CommonResult.success(monitorManageService.selectMonitorPersonPage(pageNum, pageSize, personType, username));
    }

    @PostMapping("/person/addMonitorPerson")
    @Override
    public CommonResult addMonitorPerson(@RequestBody MonitorPersonAddVo addMonitorPersonVo) {
        if (addMonitorPersonVo == null) {
            return CommonResult.failed("参数为空");
        }
        if (addMonitorPersonVo.getPersonType() == null) {
            return CommonResult.failed("人员类型为空");
        }
        List<String> userIds = addMonitorPersonVo.getUserIdList();
        if (userIds == null) {
            return CommonResult.failed("人员id为空");
        }
        // 去空
        userIds.removeAll(Collections.singletonList(null));
        // 去重
        userIds = new ArrayList<>(new HashSet<>(userIds));
        if (userIds.isEmpty()) {
            return CommonResult.failed("人员id为空");
        }
        addMonitorPersonVo.setUserIdList(userIds);
        if (addMonitorPersonVo.getPersonType() == null) {
            return CommonResult.failed("人员类型为空");
        }
        if (addMonitorPersonVo.getPersonType() == null) {
            return CommonResult.failed("人员类型为空");
        } else if (!MonitorPersonTypeEnum.DBLD.getPersonType().equals(addMonitorPersonVo.getPersonType()) &&
                !MonitorPersonTypeEnum.ZBRY.getPersonType().equals(addMonitorPersonVo.getPersonType()) &&
                !MonitorPersonTypeEnum.FBRY.getPersonType().equals(addMonitorPersonVo.getPersonType())) {
            return CommonResult.failed("非法人员类型");
        }
        return CommonResult.success(monitorManageService.addMonitorPerson(addMonitorPersonVo));
    }

    @DeleteMapping("/person/deleteMonitorPerson")
    @Override
    public CommonResult deleteMonitorPerson(String userIdList) {
        if (StringUtils.isBlank(userIdList)) {
            return CommonResult.failed("参数为空");
        } else {
            List<String> userIds = Arrays.asList(userIdList.trim().split(","));
            if (userIds != null && !userIds.isEmpty()) {
                // 去空
                userIds.removeAll(Collections.singletonList(null));
                // 去重
                userIds = new ArrayList<>(new HashSet<>(userIds));
                return CommonResult.success(monitorManageService.deleteMonitorPerson(userIds));
            } else {
                return CommonResult.failed("参数为空");
            }
        }
    }

    @PostMapping("/person/exportMonitorPersonToExcel")
    @Override
    public void exportMonitorPersonToExcel(HttpServletResponse response,
                                           @RequestBody MonitorPersonToExcelQueryVo monitorPersonToExcelQueryVo) {
//        List<MonitorPersonResVo> monitorContactList = monitorManageService.getMonitorPersonToExcel(monitorPersonToExcelQueryVo.getPersonType(),
//                monitorPersonToExcelQueryVo.getUsername());
        // 2021.08.24 改为导出全部
        List<MonitorPersonResVo> monitorContactList = monitorManageService.getMonitorPersonToExcel(null,null);
        ExportUtils.exportExcel(monitorContactList, "值班人员",
                new String[]{"姓名", "性别", "移动电话", "电子邮件", "行政区划", "部门", "职务", "类型"},
                new String[]{"username", "sexName", "phone", "email", "adnm", "deptName", "job", "personTypeName"}, response);

    }

    @GetMapping("/contact/getMonitorContactList")
    @Override
    public CommonResult getMonitorContactList(@RequestParam("pageNum") Integer pageNum,
                                              @RequestParam("pageSize") Integer pageSize,
                                              @RequestParam(value = "contactName", required = false) String contactName,
                                              @RequestParam(value = "contactType", required = false) Integer contactType) {
        if (pageNum == null) {
            pageNum = 0;
        }
        if (pageSize == null) {
            pageSize = 0;
        }
        if (contactName != null && !(contactName = contactName.trim()).isEmpty()) {

        } else {
            contactName = null;
        }
        return CommonResult.success(monitorManageService.queryMonitorContactPage(pageNum, pageSize, contactName, contactType));
    }

    @GetMapping("/contact/getMonitorContactById")
    @Override
    public CommonResult getMonitorContactById(@RequestParam("id") Integer id) {
        if (id == null) {
            return CommonResult.failed("参数为空");
        } else {
            return CommonResult.success(monitorManageService.queryMonitorContactById(id));

        }
    }

    @PostMapping("/contact/addMonitorContact")
    @Override
    public CommonResult addMonitorContact(MonitorContactVo monitorContactVo) {
        CommonResult judgeResult = monitorContactAddUpdateParamJudge(monitorContactVo);
        if (judgeResult == null) {
            return CommonResult.success(monitorManageService.addMonitorContact(monitorContactVo));
        } else {
            return judgeResult;
        }
    }

    private CommonResult monitorContactAddUpdateParamJudge(MonitorContactVo monitorContactVo) {
        if (monitorContactVo == null) {
            return CommonResult.failed("参数为空");
        }
        if (monitorContactVo.getContactName() == null) {
            return CommonResult.failed("名称/姓名为空");
        }
        if (monitorContactVo.getContactType() == null) {
            return CommonResult.failed("类型为空");
        }
        if (monitorContactVo.getPhone() == null) {
            return CommonResult.failed("电话为空");
        }
        return null;
    }

    @PostMapping("/contact/updateMonitorContact")
    @Override
    public CommonResult updateMonitorContact(MonitorContactVo monitorContactVo) {
        CommonResult judgeResult = monitorContactAddUpdateParamJudge(monitorContactVo);
        if (judgeResult == null) {
            if (monitorContactVo.getId() == null) {
                return CommonResult.failed();
            } else {
                return CommonResult.success(monitorManageService.updateMonitorContact(monitorContactVo));
            }
        } else {
            return judgeResult;
        }
    }

    @DeleteMapping("/contact/deleteMonitorContact")
    @Override
    public CommonResult deleteMonitorContact(String ids) {
        if (StringUtils.isBlank(ids)) {
            return CommonResult.failed("参数为空");
        } else {
            List<String> idStrList = Arrays.asList(ids.trim().split(","));
            if (idStrList != null && !idStrList.isEmpty()) {
                List<Integer> idList = idStrList.stream().map(Integer::valueOf).collect(Collectors.toList());
                return CommonResult.success(monitorManageService.deleteManyMonitorContact(idList));
            } else {
                return CommonResult.failed("参数为空");
            }
        }
    }

    @PostMapping("/contact/exportMonitorContactToExcel")
    @Override
    public void exportMonitorContactToExcel(HttpServletResponse response,
                                            @RequestBody MonitorContactToExcelQueryVo monitorContactToExcelQueryVo) {
        List<MonitorContact> monitorContactList = monitorManageService.getMonitorContactForExcel(monitorContactToExcelQueryVo.getContactName(),
                monitorContactToExcelQueryVo.getContactType());
        ExportUtils.exportExcel(monitorContactList, "常用联系人电话", new String[]{"名称/姓名", "电话", "传真", "类型"}, new String[]{"contactName", "phone", "fax", "contactTypeName"}, response);
    }

    @PostMapping("/info/addMonitorInfo")
    @Override
    public CommonResult addMonitorInfo(@RequestBody MonitorInfoVo monitorInfoVo) {
        if (monitorInfoVo == null) {
            return CommonResult.failed("参数为空");
        }
        if (monitorInfoVo.getMonitorDate() == null) {
            return CommonResult.failed("排班日期为空");
        } else if (monitorInfoVo.getId() == null && monitorManageService.checkMonitorInfoExistByMoniterDate(monitorInfoVo.getMonitorDate())) {
            return CommonResult.failed("已存在该排班日期的记录");
        }
        if (monitorInfoVo.getDbldIdList() != null && !monitorInfoVo.getDbldIdList().isEmpty()) {

        } else {
            return CommonResult.failed("带班领导为空");
        }
        if (monitorInfoVo.getZbryIdList() != null && !monitorInfoVo.getZbryIdList().isEmpty()) {

        } else {
            return CommonResult.failed("值班人员为空");
        }
        if (monitorInfoVo.getFbryIdList() != null && !monitorInfoVo.getFbryIdList().isEmpty()) {

        } else {
            return CommonResult.failed("副班人员为空");
        }
        return CommonResult.success(monitorManageService.addMonitorInfo(monitorInfoVo));

    }

    @GetMapping("/info/getMonitorInfoByDate")
    @Override
    public CommonResult getMonitorInfoByDate(@RequestParam("startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
                                             @RequestParam("endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {
        return CommonResult.success(monitorManageService.selectMonitorInfoByDate(startDate, endDate));
    }

    @DeleteMapping("/info/deleteOneMonitorInfo")
    @Override
    public CommonResult deleteOneMonitorInfo(@RequestParam("infoId") Integer infoId) {
        if (infoId == null) {
            return CommonResult.failed("带班领导为空");
        } else {
            return CommonResult.success(monitorManageService.deleteOneMonitorInfo(infoId));
        }
    }
}