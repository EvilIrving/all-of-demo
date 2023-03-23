package com.ygkj.soft.database.task;

import com.ygkj.soft.database.api.VO.DataAssessInfo;
import com.ygkj.soft.database.api.model.DataAssessResultInfo;
import com.ygkj.soft.database.dao.DataAssessDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ygkj.com.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author fml
 * @date 2021-05-21 11:21
 * @description 数据考核
 */
@Component
 @RestController
 @RequestMapping("/test")
public class DataAssessResultTask {

    @Autowired
    private DataAssessDao dataAssessDao;

    @Scheduled(cron = "0 0 0/4 * * ?")
    @GetMapping("/assessResult")
    public void assessResult() {

        // 获取频次信息(关联获取规则、表名以及数据源)
        List<DataAssessInfo> assessInfos = dataAssessDao.getDataAssessInfos();
        if (CollectionUtils.isEmpty(assessInfos)) {
            return;
        }

        // 根据数据源分组，获取相关数据
        Map<String, List<DataAssessInfo>> collect = assessInfos.stream().filter(a -> StringUtils.isNotBlank(a.getDataBaseName()))
                .collect(Collectors.groupingBy(DataAssessInfo::getDataBaseName));
        if (!CollectionUtils.isEmpty(collect)) {
            List<DataAssessResultInfo> resultInfos = new ArrayList<>();

            // 遍历map,计算相应的考核结果信息
            for (String key : collect.keySet()){
                resultInfos.addAll(dataAssessDao.calculateScore(key, collect.get(key)));
            }

            // 考核结果信息
            dataAssessDao.saveResultInfo(resultInfos);
        }

    }
}
