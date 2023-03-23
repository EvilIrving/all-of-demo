package com.ygkj.utils.loginUtils;

import com.ygkj.constants.ZlbConstants;
import com.ygkj.utils.loginUtils.IrsSignRes;
import lombok.SneakyThrows;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;


public class IrsUtils {


    @SneakyThrows
    public static IrsSignRes sign(String url, String method) {
        UriComponents uriComponents = UriComponentsBuilder.fromHttpUrl(url).build();
        uriComponents = uriComponents.encode();
        List<String> queryArr = new ArrayList<>();
        MultiValueMap<String, String> queryParams = uriComponents.getQueryParams();
        for (Map.Entry<String, List<String>> next : queryParams.entrySet()) {
            for (String va : next.getValue()) {
                if (va == null) {
                    queryArr.add(next.getKey() + "=");
                } else {
                    queryArr.add(next.getKey() + "=" + va);
                }
            }
        }
        //按照字典排序
        Collections.sort(queryArr);
        ///Tue, 09 Nov 2021 08:49:20 GMT
        DateFormat dateFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.US);
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        String dateTime = dateFormat.format(new Date());

        String signStr = method.toUpperCase() + "\n" +
                //拼接url path
                uriComponents.getPath() + "\n" +
                //拼接url query
                String.join("&", queryArr) + "\n" +
                ZlbConstants.IRS_AK + "\n" +
                dateTime + "\n";

        String sign = hmacSha256Base64(signStr, ZlbConstants.IRS_SK);

        IrsSignRes res = new IrsSignRes();

        res.setSignature(sign);
        res.setAccessKey(ZlbConstants.IRS_AK);
        res.setDateTime(dateTime);
        res.setAlgorithm(ZlbConstants.DEFAULT_HMAC_SIGNATURE);
        return res;
    }


    @SneakyThrows
    private static String hmacSha256Base64(String content, String key) {
        Mac hmacSHA256 = Mac.getInstance("HmacSHA256");
        SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
        hmacSHA256.init(secretKey);
        byte[] bytes = hmacSHA256.doFinal(content.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(bytes);
    }

    public static void main(String[] args) {

        System.out.println(sign("https://bcdsg.zj.gov.cn:8443/restapi/prod/IC33000020220329000007/uc/sso/getUserInfo", "POST"));
    }

}
