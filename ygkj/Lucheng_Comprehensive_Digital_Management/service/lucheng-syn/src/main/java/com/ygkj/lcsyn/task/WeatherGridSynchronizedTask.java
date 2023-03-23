package com.ygkj.lcsyn.task;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ygkj.lcsyn.utils.HttpClientUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
//@RestController
//@RequestMapping("/grid")
public class WeatherGridSynchronizedTask {

    @Value("${weather.syn-rain-path}")
    private String path;

    @Autowired
    private RestTemplate restTemplate;

    public static void main(String[] args) {
        System.out.println(TimeUnit.HOURS.toMillis(1));
    }

    private void checkPath(){
        String system = System.getProperty("os.name").toLowerCase();
        String oldPath = system.contains("windows")?path.concat("\\oldData"):path.concat("/oldData");
        String currentPath = system.contains("windows")?path.concat("\\currentData"):path.concat("/currentData");
        File file = new File(oldPath);
        if (!file.exists()){
            file.mkdirs();
        }
        file = new File(currentPath);
        if (!file.exists()){
            file.mkdirs();
        }
    }

    private static JSONObject GetToken(){
        String sendUrl= "https://api-dshare.wenzhou.gov.cn/webapi2/get_token";
        Map<String, String> parameters = new HashMap<String, String>();
        parameters.put("username", "jhq.slqy");
        parameters.put("password", "b263539a-c5cf-11ec-a019-00163e010604");
        JSONObject result= HttpClientUtil.doPost(sendUrl,parameters);
        System.out.println(result);
        return result;
    }
    @Scheduled(cron = "0 0 0/1 * * ? ")
//    @GetMapping("")
    public void execute(){
        checkPath();
        /*****获得带有dat文件下载url的json串*****/
        JSONObject object = GetToken();
        String token = object.getString("token");
        System.out.println(token);
        String str_token = "jwt "+token;
        String headerkey = "Authorization";
        Map<String, String> parameters = new HashMap<>();
        parameters.put("powermatters", ""); //事项编码，具体编码需参照业务要求填写
        parameters.put("subpowermatters", ""); //事项子编码，具体编码需参照业务要求填写
        parameters.put("p_left_limit", "0");
        parameters.put("p_right_limit","3");
        JSONObject jsonObject = HttpClientUtil.doPost("https://api-dshare.wenzhou.gov.cn/webapi2/LC-adm_shl_pt_1glwgjsybxx_d", parameters,headerkey,str_token);
        System.out.println(jsonObject.toJSONString());
        JSONArray datas = jsonObject.getJSONArray("datas");
        List<Object> list = datas.subList(datas.size() - 3, datas.size());
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmm");
        String oldPath = path.concat("\\oldData");
        String currentPath = path.concat("\\currentData");
        //清空最新数据
        File currentFilePath = new File(currentPath);
        if (currentFilePath.isDirectory()){
            File[] files = currentFilePath.listFiles();
            for (File f : files) {
                f.delete();
            }
        }
        log.info("-----开始同步大数据局未来1/3/6小时数据-----");
        //保存历史数据和最新数据
        for (Object o : list) {
            JSONObject data = (JSONObject)o;
            String name = format.format(data.getDate("qbsj")).concat("_").concat(data.getString("ljjssz"));
            String filePath = oldPath.concat("\\").concat(name).concat(".dat");
            File file = new File(filePath);
            String url = data.getString("url");
            if (!file.exists()){
                /******历史*****/
                try (FileOutputStream stream = new FileOutputStream(filePath)){
                    byte[] imgBytes = restTemplate.getForObject(url, byte[].class);
                    stream.write(imgBytes);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
            /******最新*****/
            filePath = currentPath.concat("\\").concat(name).concat(".dat");
            file = new File(filePath);
            if (file.exists()){
                continue;
            }
            try (FileOutputStream stream = new FileOutputStream(filePath)){
                byte[] imgBytes = restTemplate.getForObject(url, byte[].class);
                stream.write(imgBytes);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        log.info("-----同步大数据局未来1/3/6小时数据已结束！！！-----");
    }



}
