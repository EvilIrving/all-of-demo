package com.ygkj.data.management.dto;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 面向与外界进行数据交互的界限上下文，原则上一旦定义，在外部不允许再修改
 */
public class ColumnRulePool {

    //由外界输送的规则数据
    private List<BusDataRuleDto> rulesCollection;

    //内部根据外界输送的规则数据处理的汇聚子集
    @Getter
    private List<BusDataColumnRuleDto> rulesGroup;

    @Getter
    private Map<String, List<BusDataRuleDto>> ruleMap;

    public ColumnRulePool(List<BusDataRuleDto> collection) {
        this.rulesCollection = collection;
        if (!collection.isEmpty()) {
            //聚合数据
            ruleMap = rulesCollection.stream().collect(Collectors.groupingBy(BusDataRuleDto::getColumnId));
            rulesGroup = new ArrayList<>(ruleMap.size());
            rulesCollection.forEach(busDataRuleDto -> {
                BusDataColumnRuleDto ruleDto = new BusDataColumnRuleDto(busDataRuleDto.getColumnId(), ruleMap.get(busDataRuleDto.getColumnId()));
                rulesGroup.add(ruleDto);
            });

        }
    }
}
