package test;

import com.alibaba.fastjson.JSONObject;
import com.ygkj.gragh.mapper.AttRivQuaStBaseMapper;
import com.ygkj.gragh.model.AttRivQuaStBase;
import com.ygkj.service.GenerateDistributionIDService;
import com.ygkj.utils.Builder;
import com.ygkj.utils.HttpClientUtil;
import org.junit.Test;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;

/**
 * @author xq
 * @Description
 * @Date 2021/8/4
 */
//@RunWith(SpringRunner.class)
//@ActiveProfiles("dev")
//@SpringBootTest(classes = GraphApplication.class)
public class SynRiverWaterQualityTask {

    @Resource
    GenerateDistributionIDService idService;

    @Resource
    AttRivQuaStBaseMapper rivQuaStBaseMapper;

    String token = "5UjGsHkO50E12f7H";

    String host = "https://wzhz.wenzhou.gov.cn/";

    /**
     * 同步河道水质测站信息
     */
    @Test
    public void syncRiverWaterQualityStationInfo() {

        Integer pageSize = 100000;

        Integer currentPage = 1;
//        while (true) {
        //https://wzhz.wenzhou.gov.cn//riverOpenApi/openRiverChief?currentPage=2&pageSize=1000
        String respStr = HttpClientUtil.sendGet(host + "/riverOpenApi/openSite", Builder.of(HashMap<String, String>::new).with(HashMap::put, "token", token).build(), Builder.of(HashMap<String, Object>::new).with(HashMap::put, "currentPage", currentPage).with(HashMap::put, "pageSize", pageSize).build(), StandardCharsets.UTF_8.name());
        try {
            JSONObject resp = JSONObject.parseObject(respStr);
            List<AttRivQuaStBase> insertList = resp.getJSONArray("data").toJavaList(AttRivQuaStBase.class);
            for (AttRivQuaStBase temp : insertList) {
                try {
                    this.rivQuaStBaseMapper.insert(temp);
                } catch (Exception e) {
                    e.printStackTrace();
                    return;
                }
            }
//                JSONObject pageInfo = resp.getJSONObject("page");
//                int totalPage = pageInfo.getIntValue("totalPage");
//                if (currentPage < totalPage) {
//                    currentPage++;
//                } else {
//                    break;
//                }
        } catch (Exception e) {
            e.printStackTrace();
//                break;
        }
//        }
    }

}
