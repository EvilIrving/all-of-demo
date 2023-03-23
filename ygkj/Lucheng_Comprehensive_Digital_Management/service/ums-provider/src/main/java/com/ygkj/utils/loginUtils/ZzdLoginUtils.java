package com.ygkj.utils.loginUtils;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.xxpt.gateway.shared.api.request.OapiOpenplatformAppcenterGetAppUsedRangeRequest;
import com.alibaba.xxpt.gateway.shared.api.response.OapiOpenplatformAppcenterGetAppUsedRangeResponse;
import com.alibaba.xxpt.gateway.shared.client.http.ExecutableClient;
import com.alibaba.xxpt.gateway.shared.client.http.GetClient;
import com.alibaba.xxpt.gateway.shared.client.http.IntelligentGetClient;
import com.alibaba.xxpt.gateway.shared.client.http.PostClient;
import com.ygkj.water.model.CommonCode;

public class ZzdLoginUtils {
    public static Boolean isPermission = false;

    private static ExecutableClient executableClient;

    private static String APP_KEY = "slgc-42Rs5MVaxcLXs7987N9niMJZ3";

    private static String APP_SECRET = "fTuT13tQQGGP9CJ6ZM0Ers05X83UuLu1gom48Yiv";

//    private static String APP_KEY = "slgc-lucheng-kZWX0BK7i6UUJVfaa";

//    private static String APP_SECRET = "9YRO56ZjPgEIbtwxaqp1MB1c14W4FXtO305WyZ69";

    static {
        executableClient = ExecutableClient.getInstance();
        //浙里办网关地址
//        executableClient.setDomainName("interface.zjzwfw.gov.cn/gateway/component/routing/agent.htm");
        //浙政钉环境
        executableClient.setDomainName("openplatform-pro.ding.zj.gov.cn");
        //专用钉钉环境
//        executableClient.setDomainName("openplatform.dg-work.cn");
        executableClient.setProtocal("https");


        executableClient.setAccessKey(APP_KEY);
        executableClient.setSecretKey(APP_SECRET);
        executableClient.init();
    }


    public static String getToken() {

        String api = "/gettoken.json";
        GetClient getClient = executableClient.newGetClient(api);
        //设置参数
        getClient.addParameter("appkey", APP_KEY);
        getClient.addParameter("appsecret", APP_SECRET);
        //调用API
        String apiResult = getClient.get();
//        System.out.println("getToken:" + apiResult);
        String token = JSON.parseObject(apiResult).getJSONObject("content")
                .getJSONObject("data").getString("accessToken");
        return token;
    }

    public static JSONObject getDingTalkAppUser(String accessToken, String authCode) throws Exception {
        String api = "/rpc/oauth2/dingtalk_app_user.json";
        PostClient postClient = executableClient.newPostClient(api);
        //设置参数
//        System.out.println("authCode:" + authCode);
        postClient.addParameter("access_token", accessToken);
        postClient.addParameter("auth_code", authCode);
        //调用API
        String apiResult = postClient.post();
//        System.out.println("DingTalkAppUser:" + apiResult);
        JSONObject object = JSON.parseObject(apiResult);
        if (object.getBoolean("success").equals(true)){
            JSONObject content = object.getJSONObject("content");
            if ("240132".equals(content.getString("responseCode"))){
                throw new Exception(CommonCode.FORBIDDEN.message());
            }
            if (content.getBoolean("success").equals(true)){
                JSONObject data = content.getJSONObject("data");
                return data;
            }
        }
        return null;
    }

    public static void getAppUsedRange(Long tenantId,Long currentPage,String accountId,Long totalPage){
        if (currentPage > totalPage){
            isPermission = false;
            return;
        }
        //executableClient保证单例
        IntelligentGetClient intelligentGetClient = executableClient.newIntelligentGetClient("/openplatform/appcenter/getAppUsedRange");
        OapiOpenplatformAppcenterGetAppUsedRangeRequest oapiOpenplatformAppcenterGetAppUsedRangeRequest = new OapiOpenplatformAppcenterGetAppUsedRangeRequest();
        //租户ID
        oapiOpenplatformAppcenterGetAppUsedRangeRequest.setTenantId(tenantId);
        //当前页
        oapiOpenplatformAppcenterGetAppUsedRangeRequest.setCurrentPage(currentPage);
        //获取结果
        OapiOpenplatformAppcenterGetAppUsedRangeResponse apiResult = intelligentGetClient.get(oapiOpenplatformAppcenterGetAppUsedRangeRequest);
        if (apiResult.getSuccess().equals(true)){
            String contentStr = apiResult.getContent();
            JSONObject object = JSON.parseObject(contentStr);
            JSONObject content = object.getJSONObject("content");
            totalPage = content.getLong("totalPage");
            if ("slgc".equals(content.getString("appName"))){
//                JSONArray accountIdList = content.getJSONArray("accountIdList");
//                List<String> strings = accountIdList.toJavaList(String.class);
//                if (strings.contains(accountId)){
                    isPermission = true;
                    return;
//                }
//                if (currentPage < totalPage){
//                    getAppUsedRange(tenantId,currentPage + 1,accountId,totalPage);
//                }
            }else {
                if (currentPage < totalPage){
                    getAppUsedRange(tenantId,currentPage + 1,accountId,totalPage);
                }
            }
        }
    }

    public static void main(String[] args) {
        getAppUsedRange(42002L,1L,"766090",1L);
        System.out.println(ZzdLoginUtils.isPermission);
    }
}
