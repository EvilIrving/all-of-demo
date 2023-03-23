package com.ygkj.river.utils;

import com.alibaba.fastjson.JSONObject;
import com.hikvision.artemis.sdk.ArtemisHttpUtil;
import com.hikvision.artemis.sdk.config.ArtemisConfig;

import java.util.HashMap;
import java.util.Map;

public class HikvisionUtils {


    /**
     * 根据区域编号获取下级监控点列表
     *
     * @return
     */
    public static JSONObject doCapture(String cameraIndexCode) {

        /**
         * STEP1：设置平台参数，根据实际情况,设置host appkey appsecret 三个参数.
         */
        ArtemisConfig.host = "10.33.158.145:6443";
        // 秘钥Appkey
        ArtemisConfig.appKey = "22734686";
        // 秘钥AppSecret
        ArtemisConfig.appSecret = "HFWhm5BuRjWAEVMgqXJS";


        /**
         * STEP2：设置OpenAPI接口的上下文
         */
        final String ARTEMIS_PATH = "/artemis";

        /**
         * STEP3：设置接口的URI地址
         */
        String getRootApi = ARTEMIS_PATH + "/api/video/v1/manualCapture";
        Map<String, String> path = new HashMap<String, String>(2) {
            {
                put("https://", getRootApi);//根据现场环境部署确认是http还是https
            }
        };

        /**
         * STEP4：设置参数提交方式
         */
        String contentType = "application/json";

        /**
         * STEP5：组装请求参数
         */
        JSONObject jsonBody = new JSONObject();
        jsonBody.put("cameraIndexCode", cameraIndexCode);
        String body = jsonBody.toJSONString();

        /**
         * STEP6：调用接口
         */
        String result = ArtemisHttpUtil.doPostStringArtemis(path, body, null, null, contentType);

        return JSONObject.parseObject(result);
    }

}
