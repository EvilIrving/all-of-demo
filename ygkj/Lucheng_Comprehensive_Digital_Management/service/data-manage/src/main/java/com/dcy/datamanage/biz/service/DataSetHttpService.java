package com.dcy.datamanage.biz.service;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dcy.datamanage.api.model.BusDataSetHttp;
import com.dcy.datamanage.api.model.BusDataSetParam;
import com.dcy.datamanage.api.model.BusDataSetResField;
import com.dcy.datamanage.biz.constant.DataSetFieldType;
import com.dcy.datamanage.biz.dto.input.BusDataSetPreviewInputDTO;
import com.dcy.datamanage.biz.dto.output.BusDataSetPreviewOutputDTO;
import com.dcy.datamanage.biz.mapper.DataSetHttpMapper;
import com.dcy.datamanage.biz.util.DataSetUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class DataSetHttpService extends ServiceImpl<DataSetHttpMapper, BusDataSetHttp> {

    private Pattern pattern = Pattern.compile("[#$]\\{\\w+}");

    @Autowired
    private RestTemplate restTemplate;

    public BusDataSetPreviewOutputDTO preview(BusDataSetPreviewInputDTO dto) throws Exception {
        JSONObject setDetail = dto.getSetDetail();
        BusDataSetHttp setHttp = setDetail.toJavaObject(BusDataSetHttp.class);
        Map<String, Object> paramMap = dto.getParam();
        if (paramMap == null) {
            paramMap = Collections.emptyMap();
        }
        List<BusDataSetParam> paramFieldList = dto.getParamList();
        Map<String, BusDataSetParam> paramFieldMap = CollectionUtils.isEmpty(paramFieldList) ? new HashMap<>(0):
                paramFieldList.stream().collect(Collectors.toMap(BusDataSetParam::getParamName, Function.identity()));
        String url = setHttp.getRequestUrl();
        String requestBody = setHttp.getRequestBody();
        String requestHeader = setHttp.getRequestHeader();
        //占位符替换
        if (!CollectionUtils.isEmpty(paramFieldMap)) {
            url = replacePlaceholder(url, paramMap, paramFieldMap, "");

            if (!StringUtils.isEmpty(requestBody)) {
                requestBody = replacePlaceholder(requestBody, paramMap, paramFieldMap, "null");
            }

            if (!StringUtils.isEmpty(requestHeader)) {
                requestHeader = replacePlaceholder(requestHeader, paramMap, paramFieldMap, "null");
            }
        }

        //组装请求头和请求体
        HttpEntity httpEntity = initHttpEntity(setHttp.getContentType(), requestHeader, requestBody);

        ResponseEntity<JSONObject> res =
                restTemplate.exchange(url, HttpMethod.resolve(setHttp.getRequestMethod()), httpEntity, JSONObject.class);

        //返回字段处理
        return dealWithRes(res.getBody(), dto, setHttp);
    }

    private BusDataSetPreviewOutputDTO dealWithRes(JSONObject resBody, BusDataSetPreviewInputDTO dto, BusDataSetHttp setHttp) throws Exception {
        try {
            BusDataSetPreviewOutputDTO result = new BusDataSetPreviewOutputDTO();
            if (resBody != null) {
                Map<String, BusDataSetResField> oldMap = DataSetUtil.getOldFieldMap(dto);
                String responsePath = setHttp.getResponsePath();
                Object resObj;
                JSONArray jsonArray = null;
                if (!StringUtils.isEmpty(responsePath)) {
                    String[] paths = responsePath.split("\\.");
                    for (String path : paths) {
                        resObj = resBody.get(path);
                        if (resObj != null) {
                            Class<?> aClass = resObj.getClass();
                            if (aClass == LinkedHashMap.class) {
                                resBody = resBody.getJSONObject(path);
                            } else if (aClass == ArrayList.class) {
                                jsonArray = resBody.getJSONArray(path);
                                resBody = null;
                            }else {
                                throw new Exception("请确保返回值为对象或者对象集合");
                            }
                        }else {
                            return result;
                        }
                    }
                }

                List<BusDataSetResField> fieldList = new ArrayList<>();
                Map<String, BusDataSetResField> completeMap = new HashMap<>();
                Map<String, BusDataSetResField> pendingMap = new HashMap<>();
                if (resBody != null) {
                    if (dto.isFieldUpdate()) {
                        //都用新的字段
                        for (Map.Entry<String, Object> entry : resBody.entrySet()) {
                            DataSetUtil.busDataSetResField(entry.getKey(), entry.getValue(), oldMap,
                                    fieldList, completeMap, pendingMap, valToType);
                        }
                    } else {
                        //删除不需要的字段
                        removeRedundant(resBody, oldMap);
                    }
                    result.setRes(resBody);
                } else if (jsonArray != null && jsonArray.size() > 0) {
                    if(jsonArray.get(0).getClass() != LinkedHashMap.class){
                        throw new Exception("请确保返回值为对象或者对象集合");
                    }
                    if (dto.isFieldUpdate()) {
                        //都用新的字段
                        for (int i = 0; i < jsonArray.size(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            if (jsonObject == null) {
                                continue;
                            }
                            //所有字段均已判断过
                            if (completeMap.size() == jsonObject.size()) {
                                break;
                            }
                            for (Map.Entry<String, Object> entry : jsonObject.entrySet()) {
                                DataSetUtil.busDataSetResField(entry.getKey(), entry.getValue(), oldMap,
                                        fieldList, completeMap, pendingMap, valToType);
                            }
                        }
                    } else {
                        //删除不需要的字段
                        for (int i = 0; i < jsonArray.size(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            if (jsonObject != null) {
                                removeRedundant(jsonObject, oldMap);
                            }
                        }
                    }
                    result.setRes(jsonArray);
                }
                result.setFieldList(fieldList);
            }
            return result;
        } catch (Exception e) {
            log.error("[http返回值解析出错]",e);
            throw new Exception("请确保返回值为对象或者对象集合");
        }
    }

    private void removeRedundant(JSONObject jsonObject, Map<String, BusDataSetResField> oldMap) {
        Iterator<Map.Entry<String, Object>> it = jsonObject.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, Object> entry = it.next();
            if (!oldMap.containsKey(entry.getKey())) {
                it.remove();
            }
        }
    }

    private HttpEntity initHttpEntity(Integer contentType, String requestHeader, String requestBody) {
        Map<String, Object> param = null;
        //请求头和请求体
        HttpHeaders headers = new HttpHeaders();
        if (contentType != null) {
            if (contentType == 1) {
                if (StringUtils.isEmpty(requestBody)) {
                    param = Collections.emptyMap();
                } else {
                    param = new HashMap<>(JSONObject.parseObject(requestBody));
                }
                headers.setContentType(MediaType.APPLICATION_JSON);
            } else {
                if (StringUtils.isEmpty(requestBody)) {
                    param = new LinkedMultiValueMap();
                } else {
                    Map<String, Object> tempParam = new LinkedMultiValueMap();
                    JSONObject.parseObject(requestBody).forEach((k, v) -> {
                        ((LinkedMultiValueMap) tempParam).add(k, v);
                    });
                    param = tempParam;
                }
                headers.setContentType(MediaType.MULTIPART_FORM_DATA);
            }
        }

        if (!StringUtils.isEmpty(requestHeader)) {
            JSONObject.parseObject(requestHeader).forEach((k, v) -> headers.add(k, String.valueOf(v)));
        }

        HttpEntity httpEntity = new HttpEntity(param, headers);
        return httpEntity;
    }

    private Function<Object, Integer> valToType = val -> {
        if (val instanceof Double) {
            return DataSetFieldType.DECIMAL;
        } else if (val instanceof Integer) {
            return DataSetFieldType.INTEGER;
        } else if (val instanceof Number) {
            return DataSetFieldType.NUMBER;
        } else if (val instanceof String) {
            return DataSetFieldType.STRING;
        } else if (val instanceof Boolean) {
            return DataSetFieldType.BOOLEAN;
        } else if (val instanceof LinkedHashMap) {
            return DataSetFieldType.OBJECT;
        } else if (val instanceof ArrayList) {
            return DataSetFieldType.ARRAY;
        }
        return DataSetFieldType.UNKNOWN;
    };

    /**
     * 占位符替换
     *
     * @param originalStr
     * @param paramMap
     * @return
     */
    private String replacePlaceholder(String originalStr, Map<String, Object> paramMap, Map<String, BusDataSetParam> paramFieldMap, String nullStr) {
        Matcher matcher = pattern.matcher(originalStr);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            String group = matcher.group();
            String key = group.substring(2, group.length() - 1);
            if (paramFieldMap.containsKey(key)) {
                Object val = paramMap.get(key);
                if (val == null) {
                    val = nullStr;
                } else {
                    BusDataSetParam busDataSetParam = paramFieldMap.get(key);
                    Integer paramType = busDataSetParam.getParamType();
                    if (DataSetFieldType.OBJECT == paramType || DataSetFieldType.ARRAY == paramType) {
                        val = JSONObject.toJSONString(val);
                    }
                    if (group.startsWith("#")) {
                        val = "\"" + val + "\"";
                    }
                }
                matcher.appendReplacement(sb, String.valueOf(val));
            }
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    public static void main(String[] args) {
        Map<Object, Object> map = Collections.emptyMap();
        System.out.println(map.size());
        System.out.println(map.containsKey("122"));
    }

}
