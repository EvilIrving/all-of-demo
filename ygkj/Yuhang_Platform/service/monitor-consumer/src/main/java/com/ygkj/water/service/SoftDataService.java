package com.ygkj.water.service;

import com.alibaba.fastjson.JSONObject;
import com.ygkj.auth.api.department.model.SysDept;
import com.ygkj.soft.database.api.VO.BusinessDataQueryVO;
import com.ygkj.soft.database.api.model.BusDataColumn;
import com.ygkj.water.client.AuthenticationClient;
import com.ygkj.water.client.BusinessDataClient;
import com.ygkj.water.model.CommonResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ygkj.com.assistant.pager.Pager;

import java.net.URI;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Huang.zh
 * @date 2020/9/16 16:24
 * @Description: 数据中心服务service层，可以在此组装一些来源于不同子模块又存在关联关系的数据
 */
@Service("softDataService")
public class SoftDataService {
    @Autowired
    private BusinessDataClient businessDataClient;

    @Autowired
    private AuthenticationClient authenticationClient;

    public CommonResult getBussinessDataWithDepartment(BusinessDataQueryVO businessDataQueryVO){
        CommonResult result = businessDataClient.queryBusinessDataByPage(businessDataQueryVO);
        LinkedHashMap resultData = (LinkedHashMap) result.getData();
        LinkedHashMap pager = (LinkedHashMap) resultData.get("page");
        List<BusDataColumn> column = (List<BusDataColumn>)resultData.get("column");
        BusDataColumn departmentNameColumn = new BusDataColumn();
        departmentNameColumn.setRemarks("责任部门");
        departmentNameColumn.setName("dept_name");
        column.add(departmentNameColumn);
        BusDataColumn divideStatusColumn = new BusDataColumn();
        divideStatusColumn.setRemarks("划分状态");
        divideStatusColumn.setName("divide_status");
        column.add(divideStatusColumn);
        column.add(departmentNameColumn);
        List<LinkedHashMap> list = (List<LinkedHashMap>) authenticationClient.departments().get("data");
        Map<String, List<LinkedHashMap>> map = list.stream().collect(Collectors.groupingBy(linkedHashMap ->String.valueOf(linkedHashMap.get("id"))));
        for (Object object : (List)pager.get("list")) {
            LinkedHashMap jsonObject = (LinkedHashMap) object;
            String deptId = String.valueOf(jsonObject.get("dts_cmf_dept"));
            if (StringUtils.isNotBlank(deptId) && !"null".equals(deptId)){
                jsonObject.put("divide_status",1);
            }else{
                jsonObject.put("divide_status",0);
            }
            List<LinkedHashMap> sysDepts = map.get(deptId);
            if (sysDepts != null){
                jsonObject.put("dept_name",sysDepts.get(0).get("name"));
            }else{
                jsonObject.put("dept_name","");
            }
        }
        return result;
    }
}
