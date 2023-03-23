package com.ygkj.digitization;

import com.ygkj.digitization.vo.request.*;
import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.*;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;

/**
 * @author xq
 * @Description 值班管理api
 * @Date 2021/1/13
 */
@Api(tags = "值班管理", value = "值班管理")
public interface MonitorManageControllerApi {

    @ApiOperation("获取值班人员列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "personType", value = "人员类型：1：副班人员，2：值班人员，3：带班领导"),
            @ApiImplicitParam(name = "username", value = "用户姓名")
    })
    CommonResult getMonitorPersonList(Integer pageNum, Integer pageSize,
                                      Integer personType,
                                      String username);

    @ApiOperation("添加新值班人员")
    CommonResult addMonitorPerson(MonitorPersonAddVo addMonitorPersonVo);

    @ApiOperation("根据用户id删除多个值班人员")
    CommonResult deleteMonitorPerson(@ApiParam(name = "用户id集合") String userIdList);

    @ApiOperation("将所有的值班人员信息导出至Excel文件中")
    void exportMonitorPersonToExcel(HttpServletResponse response, MonitorPersonToExcelQueryVo monitorPersonToExcelQueryVo);

    @ApiOperation("获取/查询值班常用电话列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "contactName", value = "单位名称或人员姓名"),
            @ApiImplicitParam(name = "contactType", value = "通讯类型:1为人员，2为单位")
    })
    CommonResult getMonitorContactList(Integer pageNum, Integer pageSize, String contactName, Integer contactType);

    @ApiOperation("根据id获取到常用电话记录")
    CommonResult getMonitorContactById(@ApiParam("id") Integer id);

    @ApiOperation("添加一项新值班常用电话记录")
    CommonResult addMonitorContact(MonitorContactVo monitorContactVo);

    @ApiOperation("更新一项值班常用电话记录")
    CommonResult updateMonitorContact(MonitorContactVo monitorContactVo);

    @ApiOperation("删除多项值班常用电话记录")
    CommonResult deleteMonitorContact(@ApiParam(name = "值班常用电话记录id集合,用逗号隔开") String ids);

    @ApiOperation("将查询或所有的值班常用电话记录导出至Excel文件中")
    void exportMonitorContactToExcel(HttpServletResponse httpServletResponse, MonitorContactToExcelQueryVo monitorContactToExcelQueryVo);

    @ApiOperation("添加一个排班记录")
    CommonResult addMonitorInfo(MonitorInfoVo monitorInfoVo);

    @ApiOperation("根据日期起止时间查询排班记录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "startDate", value = "开始时间", dataType = "java.util.Date"),
            @ApiImplicitParam(name = "endDate", value = "结束时间", dataType = "java.util.Date")
    })
    CommonResult getMonitorInfoByDate(Date startDate, Date endDate);

    /**
     * 原型图中没有提到，暂时写着留待以后备用
     */
    @ApiOperation("删除一个排班记录")
    CommonResult deleteOneMonitorInfo(@ApiParam("排班记录id") Integer infoId);


}
