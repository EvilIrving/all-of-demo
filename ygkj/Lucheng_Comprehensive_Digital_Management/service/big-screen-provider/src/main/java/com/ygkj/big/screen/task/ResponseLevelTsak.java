package com.ygkj.big.screen.task;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ygkj.big.screen.model.ResponseLevel;
import com.ygkj.big.screen.service.GiveAnAlarmService;
import com.ygkj.big.screen.service.ResponseLevelService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Arrays;

/**
 * @author wgf
 * @date 2022/5/9 15:44
 * @Description: 根据应急响应等级去生成应急响应数据。
 */
/*@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("dev")*/
@Component
@Slf4j
public class ResponseLevelTsak {

    @Autowired
    private GiveAnAlarmService giveAnAlarmService;
    @Autowired
    private ResponseLevelService responseLevelService;

    //@Test
    @Scheduled(cron = "0 0 0/1 * * ?")
    public void responseLevel(){
        //查询现在的防汛等级
        QueryWrapper<ResponseLevel> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("whether_open", 1);
        ResponseLevel responseLevel = responseLevelService.getOne(queryWrapper);
        if (responseLevel != null){
            Integer level = responseLevel.getResponseLevel();
            //Ⅰ级响应期间，每1小时自动生成简报
            //Ⅱ级响应期间，于每日7时开始，每3个小时自动生成简报
            //Ⅲ级响应期间，于每日7时、15时自动生成简报
            //Ⅳ级响应期间，于每日15时自动生成简报
            //无响应期间，不自动生成简报。手动点击“实时简报”生成当前简报

            //获取现在的小时
            int hour = LocalDateTime.now().getHour();
            //可以开启任务的小时时间数组
            Integer[] openHours = null;
            switch (level){
                case 1:
                    openHours = new Integer[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23};
                    break;
                case 2:
                    openHours = new Integer[]{7, 10, 13, 16, 19, 22};
                    break;
                case 3:
                    openHours = new Integer[]{7, 15};
                    break;
                case 4:
                    openHours = new Integer[]{15};
                    break;
                case 5:
                    break;
                default:
            }
            if (openHours != null && Arrays.asList(openHours).contains(hour)){
                log.info("开始生成防台");
                giveAnAlarmService.floodPreventionBulletin();
                log.info("防台防汛简报生成结束");
            }
        }

    }
}

