package com.ygkj.lcsyn.task;

import com.ygkj.lcsyn.mapper.ExchangeMapper;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Component
public class TableTask {

    @Resource
    private ExchangeMapper exchangeMapper;

    /**
     * 增加公共字段
     */
    public void addCol() {
        //查询所有表
        List<String> tableNameList = exchangeMapper.listTableName();

        //遍历所有表
        for (String tableName : tableNameList) {
//            //增加公共字段
//            exchangeMapper.addCol(tableName);
//
//            //查询原主键
//            String pri = exchangeMapper.selectPri(tableName);
//
//            if(StringUtils.isEmpty(pri)){
//                //没有主键的话给每个dts_cmf_id赋唯一值
//                exchangeMapper.initId(tableName);
//
//            }else {
//                //有主键的话把dts_cmf_id使用原主键的值
//                exchangeMapper.updateId(tableName,pri);
//                //取消原主键
//                exchangeMapper.delPri(tableName);
//            }
            //将dts_cmf_id放在表头
            exchangeMapper.firstDtsId(tableName);
//
//            //设置新主键
//            exchangeMapper.setPri(tableName);
        }
    }

    /**
     * 重命名表
     */
    public void renameTable() {
        //查询所有表
        List<String> tableNameList = exchangeMapper.listTableName();

        //所有表重命名
        for (String tableName:tableNameList){
            exchangeMapper.renameTable(tableName);
        }
    }
}
