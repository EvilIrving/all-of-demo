package com.ygkj.big.screen.utils;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.ygkj.big.screen.feign.AuthenticationClient;
import com.ygkj.big.screen.bootstrap.GisExtend;
import com.ygkj.big.screen.model.StPptnR;
import com.ygkj.big.screen.vo.response.ArealRainAttrResVo;
import com.ygkj.big.screen.vo.response.ArealRainGeoResVo;
import com.ygkj.big.screen.vo.response.ArealRainPointResVo;
import com.ygkj.entity.ByteArrayMultipartFile;
import com.ygkj.utils.Builder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author xq
 * @Description
 * @Date 2021/7/11
 */
@Component
@Slf4j
public class GisRainfallSurfaceUtils {

    @Autowired
    AuthenticationClient authenticationClient;

    @Autowired
    RestTemplate restTemplate;

    @Qualifier("gisExtend")
    @Autowired
    GisExtend gisExtend;

    // 都为0的等雨量面图片
    final static String allZeroUrl = "https://file.ygwjg.com/a757469ea98b3637384a7baef8392c5f33089c8e.png";

    public String doUpdateGisUrl(String common_result, boolean forceUpdate) {
        JSONObject commonResult = JSONObject.parseObject(common_result);
        String temp = doUpdateGisUrl(commonResult, forceUpdate).toJSONString();
        return temp;
    }

    public JSONObject doUpdateGisUrl(JSONObject commonResult, boolean forceUpdate) {
        String[] types = new String[]{"近1小时", "前1小时", "前3小时", "前6小时", "前24小时", "前72小时"};
        for (String type : types) {
            String[] keys = getKey(type);
            String urlKey = keys[0];
            String dataKey = keys[1];
            JSONObject data = commonResult.getJSONObject("data");
            String existUrl = data.getString(urlKey);
            if (!forceUpdate && StringUtils.isNotBlank(existUrl)) {
                continue;
            }
            List<StPptnR> rainData = data.getJSONArray(dataKey).toJavaList(StPptnR.class);
            String url = getGisImageUrl(rainData);
            if (StringUtils.isNotBlank(url)) {
                String tempUrl = downAndUpload(url);
                data.put(urlKey, tempUrl);
                commonResult.put("data", data);
            } else {
                log.error("gis url 请求失败：request_param : {} ", type);
            }
        }
        return commonResult;
    }

    String[] getKey(String type) {
        String urlKey = null;
        String dataKey = null;
        switch (type) {
            case "近1小时":
                urlKey = "rainfall_newsest_1_rcurl";
                dataKey = "rainfall_newsest_1_data";
                break;
            case "前1小时":
                urlKey = "rainfall_1_rcurl";
                dataKey = "rainfall_1_data";
                break;
            case "前3小时":
                urlKey = "rainfall_3_rcurl";
                dataKey = "rainfall_3_data";
                break;
            case "前6小时":
                urlKey = "rainfall_6_rcurl";
                dataKey = "rainfall_6_data";
                break;
            case "前24小时":
                urlKey = "rainfall_24_rcurl";
                dataKey = "rainfall_24_data";
                break;
            case "前72小时":
                urlKey = "rainfall_72_rcurl";
                dataKey = "rainfall_72_data";
                break;
            default:
                break;
        }
        return new String[]{urlKey, dataKey};
    }

    public String getGisImageUrl(List<StPptnR> rainData) {
        boolean containNotZero = rainData.stream().anyMatch(o -> o.getDrp() != null && o.getDrp() > 0);
        // 如果都为0，直接返回都为0的url，以节约性能
        if (!containNotZero) {
            return allZeroUrl;
        }
        // 每次请求前休息5s，尽量降低对服务器压力
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        List<ArealRainPointResVo> features = new ArrayList<>(rainData.size());
        for (StPptnR temp : rainData) {

            ArealRainAttrResVo attributes = Builder.of(ArealRainAttrResVo::new)
                    .with(ArealRainAttrResVo::setX, temp.getLongitude())
                    .with(ArealRainAttrResVo::setY, temp.getLatitude())
                    .with(ArealRainAttrResVo::setValue, temp.getDrp())
                    .build();
            ArealRainGeoResVo geometry = Builder.of(ArealRainGeoResVo::new)
                    .with(ArealRainGeoResVo::setX, temp.getLongitude())
                    .with(ArealRainGeoResVo::setY, temp.getLatitude())
                    .build();
            ArealRainPointResVo feature = Builder.of(ArealRainPointResVo::new)
                    .with(ArealRainPointResVo::setAttributes, attributes)
                    .with(ArealRainPointResVo::setGeometry, geometry)
                    .build();
            features.add(feature);
        }
//        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        // 以表单的方式提交
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        //将请求头部和参数合成一个请求
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(Builder.of(LinkedMultiValueMap<String, String>::new)
                .with(LinkedMultiValueMap::add, "station", Builder.of(JSONObject::new).with(JSONObject::put, "features", features).build().toJSONString())
                .with(LinkedMultiValueMap::add, "f", "json")
                .with(LinkedMultiValueMap::add, "Extent", gisExtend.getGisExtend())
                .build(), headers);
//        //执行HTTP请求，将返回的结构使用ResultVO类格式化
        ResponseEntity<String> resp = restTemplate.exchange("http://117.149.229.28:6080/arcgis/rest/services/gps/dengzhiPIDWReal0605/GPServer/dengzhiPIDWReal0605/submitJob",
                HttpMethod.POST,
                requestEntity,
                String.class);
        String jobId = JSONObject.parseObject(resp.getBody()).getString("jobId");
//        String respStr=HttpClientUtil.httpPost("http://117.149.229.28:6080/arcgis/rest/services/gps/dengzhiPIDWReal0605/GPServer/dengzhiPIDWReal0605/submitJob",
//                Builder.of(HashMap<String,String>::new).with(HashMap::put,"station",Builder.of(JSONObject::new).with(JSONObject::put,"features",features).build().toJSONString()).build(),
//                Builder.of(HashMap<String,String>::new).with(HashMap::put,"Content-Type", MediaType.APPLICATION_FORM_URLENCODED_VALUE).build());
//        String jobId=JSONObject.parseObject(respStr).getString("jobId");
        // 提交任务后先休息20s
        try {
            Thread.sleep(30000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        String xmax = null;
        String xmin = null;
        String ymax = null;
        String ymin = null;
        int times = 0;
        while (times <= 2) {
            times++;
            try {
                Thread.sleep(10000 * times);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            String url = "http://117.149.229.28:6080/arcgis/rest/services/gps/dengzhiPIDWReal0605/GPServer/dengzhiPIDWReal0605/jobs/" + jobId + "/results/Idwres?f=pjson";
            String jobResStr = null;
            try {
                jobResStr = restTemplate.getForObject(url, String.class);
                JSONObject extent = JSONObject.parseObject(jobResStr).getJSONObject("value").getJSONObject("mapImage").getJSONObject("extent");
                xmax = extent.getString("xmax");
                xmin = extent.getString("xmin");
                ymax = extent.getString("ymax");
                ymin = extent.getString("ymin");
            } catch (Exception e) {
                log.error("第二步第{}次请求报错:{},返回值：{},提交任务返回值{}", times, e, jobResStr, resp.getBody());
            }
            if (ymax != null && xmax != null && xmin != null && ymin != null) {
                break;
            }
        }
        if (ymax == null || xmax == null || xmin == null || ymin == null) {
            return null;
        }
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        String url = "http://117.149.229.28:6080/arcgis/rest/services/gps/dengzhiPIDWReal0605/MapServer/jobs/" + jobId + "/export?bbox=119.53839579772948%2C26.979173080444387%2C121.33689579772948%2C28.77767308044439&f=pjson&transparent=true";
        String href = null;
        try {
            String fJobResVo = restTemplate.postForObject(url, new JSONObject().toJSONString(), String.class, Builder.of(HashMap<String, Object>::new)
                    .with(HashMap::put, "bbox", xmin + "," + ymin + "," + xmax + "," + ymax)
                    .with(HashMap::put, "f", "pjson")
                    .with(HashMap::put, "transparent", true)
                    .build());
            href = JSONObject.parseObject(fJobResVo).getString("href");
        } catch (Exception e) {
            log.error("第三步请求报错:{}", e);
        }
        return href;
    }

    /**
     * 下载gis图片并上传到7牛云
     *
     * @param url
     * @return
     */
    private String downAndUpload(String url) {
        ResponseEntity<byte[]> rsp = restTemplate.getForEntity(url, byte[].class);
        //http://117.149.229.28:6080/arcgis/rest/directories/arcgisoutput/gps/dengzhiPIDWReal0605_GPServer/gps/dengzhiPIDWReal0605_MapServer/_ags_map7066a83a134e495e944525e827da4253.png
        String fileName = url.substring(url.lastIndexOf("/") + 1);
        MultipartFile multipartFile = new ByteArrayMultipartFile(rsp.getBody(), fileName, MediaType.IMAGE_PNG_VALUE);
        LinkedHashMap resp = authenticationClient.upload(multipartFile);
        JSONObject resp1 = JSONObject.parseObject(JSONObject.toJSONString(resp, SerializerFeature.WriteMapNullValue));
        return resp1.getJSONObject("data").getString("filePath");
    }
}
