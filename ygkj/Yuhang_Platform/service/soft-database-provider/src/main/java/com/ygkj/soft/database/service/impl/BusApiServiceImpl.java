package com.ygkj.soft.database.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.ygkj.soft.database.dao.*;
import com.ygkj.jwt.JwtTokenUtil;
import com.ygkj.jwt.constants.Constants;
import com.ygkj.soft.database.api.VO.ApiCategoryUpdateVo;
import com.ygkj.soft.database.api.VO.BusApiParamUpdateVo;
import com.ygkj.soft.database.api.VO.BusApiQueryVo;
import com.ygkj.soft.database.api.VO.BusApiUpdateVo;
import com.ygkj.soft.database.api.model.*;
import com.ygkj.soft.database.dao.*;
import com.ygkj.soft.database.service.BusApiService;
import com.ygkj.water.model.CommonResult;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import ygkj.com.assistant.pager.EasyuiPager;
import ygkj.com.assistant.pager.Pager;
import ygkj.com.util.StringUtils;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
import java.lang.reflect.Field;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.*;

/**
 *@描述 接口服务
 *@参数
 *@返回值
 *@创建人  wd
 *@创建时间  2020/9/18 15:49
 *@修改人和其它信息
 */

@Service("busApiService")
public class BusApiServiceImpl implements BusApiService {

    @Autowired
    private BusApiDao busApiDao;

    @Autowired
    private BusApiParamDao busApiParamDao;

    @Autowired
    private ApiCategoryDao apiCategoryDao;

    @Resource
    private RestTemplate restTemplate;

    @Resource
    private BusApiInvokeDetailDao busApiInvokeDetailDao;

    @Resource
    private BusApiInvokeLogDao busApiInvokeLogDao;

    @Autowired
    private SchemaDao schemaDao;




    @Override
    public void insert(BusApiUpdateVo updateVo) throws Exception {
        BusApi api = new BusApi();
        BeanUtils.copyProperties(updateVo,api);
        api.setId(UUID.randomUUID().toString());
        api.setCreatedTime(new Date());
        busApiDao.add(api);
    }

    @Override
    public void delete(String id) throws Exception {
        busApiDao.delete(id);
    }

    @Override
    public void update(BusApiUpdateVo updateVo) throws Exception {
        BusApi api = new BusApi();
        BeanUtils.copyProperties(updateVo,api);
        api.setUpdatedTime(new Date());
        busApiDao.update(api);
    }

    @Override
    public BusApi load(String id) throws Exception {
        return busApiDao.findById(id);
    }

    @Override
    public Pager pageList(BusApiQueryVo queryVo, EasyuiPager easyuiPager) throws Exception {
        return busApiDao.pageList(queryVo,easyuiPager);
    }

    @Override
    public void saveApiParams(List<BusApiParamUpdateVo> paramList) throws Exception {
        List<BusApiParam> saveList = new ArrayList<>();
        List<BusApiParam> updateList = new ArrayList<>();

        for (BusApiParamUpdateVo busApiParamUpdateVo : paramList){

            if(StringUtils.isNullOrEmpty(busApiParamUpdateVo.getApiId())){
                throw new Exception("接口ID不能为空");
            }

            BusApiParam param = new BusApiParam();
            BeanUtils.copyProperties(busApiParamUpdateVo,param);

            if(StringUtils.isNullOrEmpty(param.getId())){
                param.setId(UUID.randomUUID().toString());
                saveList.add(param);
            }else{
                updateList.add(param);
            }
        }

        if(!saveList.isEmpty()){
            busApiParamDao.batchSave(saveList);
        }

        if(!updateList.isEmpty()){
            busApiParamDao.batchUpdate(updateList);
        }
    }

    @Override
    public List<BusApiParam> queryApiParams(String apiId, Integer bus_type) throws Exception {
        return busApiParamDao.findAllList(apiId,bus_type);
    }

    @Override
    public void delApiParam(String apiParamId) throws Exception {
        busApiParamDao.delete(apiParamId);
    }

    @Override
    public List<ApiCategory> apiCategoryTree() throws Exception {
        // 获取一级分类
        List<ApiCategory> allList = apiCategoryDao.findAllList(null);

        if(null!=allList && allList.size()>0){
            allList.stream().forEach(apiCategory->{
                try {
                    // 获取二级分类
                    List<ApiCategory> subList = apiCategoryDao.findAllList(apiCategory.getId());
                    if(null!=subList && subList.size()>0){
                        apiCategory.setChildren(subList);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
        return allList;
    }

    @Override
    public void saveApiCategory(ApiCategoryUpdateVo updateVo) throws Exception {
        ApiCategory category = new ApiCategory();
        BeanUtils.copyProperties(updateVo,category);
        category.setId(UUID.randomUUID().toString());
        category.setCreateTime(new Date());
        apiCategoryDao.add(category);
    }

    @Override
    public void updateApiCategory(ApiCategoryUpdateVo updateVo) throws Exception {
        ApiCategory category = new ApiCategory();
        BeanUtils.copyProperties(updateVo,category);
        category.setUpdateTime(new Date());
        apiCategoryDao.update(category);
    }

    @Override
    public void delApiCategory(String apiCategoryId) throws Exception {
        apiCategoryDao.delete(apiCategoryId);
    }

    /**
     * 外部调用
     *
     * @param outUrl
     * @return
     */
    @Override
    public CommonResult externalCall(HttpServletRequest request, String outUrl) {
        //根据outUrl查询api详细信息
        BusApi busApi = busApiDao.findByOutUrl(outUrl);
        if(busApi == null){
            return CommonResult.failed("接口不存在");
        }
        String enable = "1";
        if(!enable.equals(busApi.getApiStatus())){
            return CommonResult.failed("接口已停用");
        }

        CommonResult commonResult = CommonResult.failed("调用失败");
        boolean flag = false;
        MultiValueMap multiValueMap = null;

        Date now = new Date();
        //无论哪种请求方式都把请求头转发
        try {
            HttpHeaders headers = forwardingHead(request);
            multiValueMap = getBody(request);
            HttpEntity<Map<String, Object>> requestEntity = new HttpEntity(multiValueMap, headers);
            ResponseEntity<CommonResult> responseEntity = restTemplate.postForEntity(busApi.getApiUrl(), requestEntity, CommonResult.class);
            if(responseEntity != null){
                flag = true;
                commonResult = responseEntity.getBody();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        long end = System.currentTimeMillis();

        String logId = UUID.randomUUID().toString();
        //保存invokeDetail
        BusApiInvokeDetail busApiInvokeDetail = new BusApiInvokeDetail();
        busApiInvokeDetail.setId(UUID.randomUUID().toString());
        busApiInvokeDetail.setLogId(logId);
        busApiInvokeDetail.setInvokeParam(multiValueMap == null ? "": multiValueMap.toString());
        busApiInvokeDetail.setInvokeResult(JSONObject.toJSONString(commonResult));
        busApiInvokeDetailDao.save(busApiInvokeDetail);

        //保存invokeLog
        BusApiInvokeLog busApiInvokeLog = new BusApiInvokeLog();
        busApiInvokeLog.setId(logId);
        busApiInvokeLog.setApiId(busApi.getId());
        busApiInvokeLog.setInvokeIp(getIp(request));
        busApiInvokeLog.setInvokeSatus(flag);
        busApiInvokeLog.setInvokeTime(now);
        busApiInvokeLog.setInvokeSpend((int)(end - now.getTime()));
        busApiInvokeLog.setDataSize(request.getContentLength());
        busApiInvokeLog.setInvokeUserId(JwtTokenUtil.getUserId(request.getHeader(Constants.ACCESS_TOKEN)));
        busApiInvokeLogDao.save(busApiInvokeLog);

        return commonResult;
    }

    private String getIp(HttpServletRequest request) {
        String ipAddress = request.getHeader("x-forwarded-for");
        if(ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("Proxy-Client-IP");
        }
        if(ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("WL-Proxy-Client-IP");
        }
        if(ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getRemoteAddr();
            if("127.0.0.1".equals(ipAddress) || "0:0:0:0:0:0:0:1".equals(ipAddress)){
                //根据网卡取本机配置的IP
                InetAddress inet=null;
                try {
                    inet = InetAddress.getLocalHost();
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }
                ipAddress= inet.getHostAddress();
            }
        }
        //对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
        if(ipAddress!=null && ipAddress.length()>15){
            if(ipAddress.indexOf(",")>0){
                ipAddress = ipAddress.substring(0,ipAddress.indexOf(","));
            }
        }
        return ipAddress;

    }

    private MultiValueMap getBody(HttpServletRequest request) {
        MultiValueMap ret = new LinkedMultiValueMap();
        Map<String,String[]> map = request.getParameterMap();
        for(Iterator<Map.Entry<String, String[]>> itr = map.entrySet().iterator(); itr.hasNext();){
            Map.Entry<String, String[]> entry = itr.next();
            String key = entry.getKey();
            String[] value = entry.getValue();
            ret.put(key, new ArrayList(Arrays.asList(value[0])));
        }
        return ret;
    }

    private HttpHeaders forwardingHead(HttpServletRequest request) {
        HttpHeaders headers = new HttpHeaders();
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String name = headerNames.nextElement();
            String value = request.getHeader(name);
            headers.set(name, value);
        }
        return headers;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public CommonResult generateApi(String dataSourceName, String tableName,String desc) {
        BusApi busApi = new BusApi();
        String id = UUID.randomUUID().toString();
        busApi.setId(id);
        busApi.setApiStatus("1");
        busApi.setRequestType(1);//POST
        busApi.setApiType(2);
        busApi.setApiRemark(tableName.concat("表记录查询"));
        busApi.setApiName(desc);
        String url = "http://127.0.0.1:8899/businessData/queryBusinessDataByPage/".
                concat("{").concat(dataSourceName).concat("}/"). concat("{").concat(tableName).concat("}");
        busApi.setApiUrl(url);
        busApi.setOutUrl("/queryBusinessDataByPage/".concat(id));
        busApiDao.add(busApi);

        List<BusApiParam> saveList = new ArrayList<>();
//        saveList.add(new BusApiParam(UUID.randomUUID().toString(),1,id,"dataBaseName","数据库名称","string",
//                true,null));
//        saveList.add(new BusApiParam(UUID.randomUUID().toString(),1,id,"tableName","表名称","string",
//                true,null));
//        saveList.add(new BusApiParam(UUID.randomUUID().toString(),1,id,"page","第几页","string",
//                false,null));
//        saveList.add(new BusApiParam(UUID.randomUUID().toString(),1,id,"rows","每页限定记录数","string",
//                false,null));
        BusDataTable table = new BusDataTable();
        table.setTableName(tableName);
        table = schemaDao.getBusDataTable(table).get(0);
        //组装表头
        List<BusDataColumn> column = schemaDao.getBusDataColumn(new BusDataColumn(table.getId()));
        column.sort(Comparator.comparingInt(BusDataColumn::getOrderColumn));
        for (BusDataColumn dataColumn : column) {
            saveList.add(new BusApiParam(UUID.randomUUID().toString(),2,id,dataColumn.getName(),dataColumn.getRemarks(),"string"));
        }
        try {
            busApiParamDao.batchSave(saveList);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return CommonResult.success("操作成功");
    }


    @Transactional(rollbackFor = Exception.class)
    public void test(){
        List<BusDataSource> busDataSource = schemaDao.getBusDataSource(new BusDataSource());
        for (BusDataSource dataSource : busDataSource) {
            String dataBaseName = dataSource.getDataBaseName();
            List<BusDataTable> tables = schemaDao.getBusDataTable(new BusDataTable(dataSource.getId()));
            for (BusDataTable table : tables) {
                generateApi(dataBaseName, table.getTableName(),table.getRemarks());
            }
        }
    }
}
