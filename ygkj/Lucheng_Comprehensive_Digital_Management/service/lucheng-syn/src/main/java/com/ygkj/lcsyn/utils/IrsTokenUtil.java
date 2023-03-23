package com.ygkj.lcsyn.utils;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.ygkj.lcsyn.enums.IrsDataApiEnum;
import com.ygkj.lcsyn.model.IrsTokenEntity;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author lxl
 * @create 2022-06-17 13:50
 * @description
 */
@Component
@Log4j2
public class IrsTokenUtil implements InitializingBean {


    public static String APP_KEY = "A330302305471202204000005";

    private static String APP_SECRET = "c93276cb23ea44f486519a0c06afb157";

    private static IrsTokenEntity tokenEntity = new IrsTokenEntity();

    private static IrsTokenEntity getIrsTokenEntity(){
        return tokenEntity;
    };


    /**
     * 获取接口请求所需 sign
     * @return
     */
    public static String getIrsRequestSign(long currentTimeMillis){

        if (currentTimeMillis > tokenEntity.getRefreshSecretEndTime()){
            log.info("首次刷新秘钥过期，重新获取");
            //刷新秘钥过期，需要重新获取
            PopBasicInfo(APP_KEY);
        }
        //判断时间，根据时间获取对应的requestToken
        String str = currentTimeMillis > tokenEntity.getRequestSecretEndTime() ?
                PopBasicInfo(tokenEntity.getRefreshSecret()).getRequestSecret() : tokenEntity.getRequestSecret();
        log.info("使用的str为：{}",str);
        return Md5Utils.getMD5(APP_KEY + str + currentTimeMillis, "UTF-8");
    }


    private static IrsTokenEntity PopBasicInfo(String requestSecret) {

        Map<String, Object> params = new HashMap<>();
        //根据传入的 secret 判断调用首次刷新还是非首次刷新接口
        String url;
        if (APP_SECRET.equals(requestSecret)){
            url = IrsDataApiEnum.REFRESH_TOKEN_BY_KEY.getUrl();
        }else {
            url = IrsDataApiEnum.REFRESH_TOKEN_BY_SEC.getUrl();
        }
        String timestamp = String.valueOf(System.currentTimeMillis());
        String sign = Md5Utils.getMD5(APP_KEY + requestSecret + timestamp, "UTF-8");
        params.put("appKey", APP_KEY);
        params.put("sign", sign);
        params.put("requestTime", timestamp);
        try {
            String result = HttpClientUtil.sendGet(url,new HashMap<>(),params,"UTF-8");
            if (StringUtils.isNotBlank(result)){
                JSONObject json = JSONObject.parseObject(result);
                //成功为 “00”
                if ("00".equals(json.getString("code"))){
                    JSONObject datas = json.getJSONObject("datas");
                    tokenEntity.setRefreshSecret(datas.getString("refreshSecret"));
                    tokenEntity.setRefreshSecretEndTime(datas.getLong("refreshSecretEndTime"));
                    tokenEntity.setRequestSecret(datas.getString("requestSecret"));
                    tokenEntity.setRequestSecretEndTime(datas.getLong("requestSecretEndTime"));
                }
                log.info("调用获取token的接口返回为：{}",result);
            }
        }catch (Exception e){
            log.info("获取irs Token 异常");
            e.printStackTrace();
        }
        return tokenEntity;
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        log.info("进入首次获取token");
        //加载类之前行执行首次获取token的方法
        PopBasicInfo(APP_SECRET);
    }

}
