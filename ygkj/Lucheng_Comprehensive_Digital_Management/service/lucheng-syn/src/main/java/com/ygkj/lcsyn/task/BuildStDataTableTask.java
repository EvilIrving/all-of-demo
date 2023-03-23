package com.ygkj.lcsyn.task;

import com.ygkj.lcsyn.mapper.BusinessMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * @author xq
 * @description 构建数据表定时任务
 * @Date 2021/3/19
 */
@Component
@Slf4j
public class BuildStDataTableTask {

    @Resource
    private BusinessMapper businessMapper;

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMM");

    @Scheduled(cron = "0 0 1 28 * ?")
    @Async("asyncTaskExecutor")
    public void buildTable() {
        LocalDate nextMonth = LocalDate.now().plusMonths(1);
        String yearMonthStr = formatter.format(nextMonth);

        pptn(yearMonthStr);
        river(yearMonthStr);
        rsvr(yearMonthStr);
        tide(yearMonthStr);
        was(yearMonthStr);
    }

    private void was(String yearMonthStr) {
        try {
            String tableName = "st_was_r_" + yearMonthStr;
            if (businessMapper.checkTableExist(tableName) == null) {
                String createDataTableSql = "CREATE TABLE `" + tableName + "` (\n" +
                        "  `MGSTCD` varchar(25) NOT NULL COMMENT '合并测站编码',\n" +
                        "  `STCD` varchar(15) NOT NULL COMMENT '测站编码',\n" +
                        "  `TM` datetime NOT NULL COMMENT '检测时间',\n" +
                        "  `UPZ` double DEFAULT NULL COMMENT '闸上水位',\n" +
                        "  `DWZ` double DEFAULT NULL COMMENT '闸下水位',\n" +
                        "  `MXGTQ` varchar(50) DEFAULT NULL COMMENT '最大过闸流量',\n" +
                        "  `OVS` varchar(255) DEFAULT NULL COMMENT '开度',\n" +
                        "  `TGTQ` double(9,3) DEFAULT NULL,\n" +
                        "  `SWCHRCD` varchar(1) DEFAULT NULL,\n" +
                        "  `SUPWPTN` varchar(1) DEFAULT NULL,\n" +
                        "  `SDWWPTN` varchar(1) DEFAULT NULL,\n" +
                        "  `MSQMT` varchar(1) DEFAULT NULL,\n" +
                        "  `MODIT` datetime DEFAULT NULL,\n" +
                        "  `TAG` tinyint(1) DEFAULT NULL,\n" +
                        "  `UPLOAD_TIME` datetime(6) DEFAULT CURRENT_TIMESTAMP(6),\n" +
                        "  `LL` varchar(255) DEFAULT NULL COMMENT '流量',\n" +
                        "  `FLAG` varchar(255) DEFAULT '0' COMMENT '判断是否更新过',\n" +
                        "  UNIQUE KEY `index_mgstcd_stcd_tm` (`MGSTCD`,`STCD`,`TM`)  USING BTREE, \n" +
                        "  KEY `index_sort_mgstcd` (`MGSTCD`) USING BTREE,\n" +
                        "  KEY `index_sort_stcd` (`STCD`) USING BTREE,\n" +
                        "  KEY `index_sort_tm` (`TM`) USING BTREE,\n" +
                        "  KEY `index_sort_upz` (`UPZ`) USING BTREE,\n" +
                        "  KEY `index_sort_dwz` (`DWZ`) USING BTREE,\n" +
                        "  KEY `index_sort_mxgtq` (`MXGTQ`) USING BTREE,\n" +
                        "  KEY `index_sort_ovs` (`OVS`(191)) USING BTREE,\n" +
                        "  KEY `index_sort_ll` (`LL`(191)) USING BTREE \n" +
                        ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='堰闸水情表';";
//                String watBusRecordSql = "insert into `wz_soft_data_db`.`bus_data_divide` (system_type,table_name) values('水灾害','" + tableName + "')";
                businessMapper.createTable(createDataTableSql);
                log.info("堰闸水情表-{}构建成功", tableName);
            } else {
                log.info("已存在堰闸水情表-{}", tableName);
            }
        } catch (Exception e) {
            log.error("堰闸水情表-构建失败{}", e.getStackTrace());
        }
    }


    private void pptn(String yearMonthStr) {
        try {
            String tableName = "st_pptn_r_" + yearMonthStr;
            if (businessMapper.checkTableExist(tableName) == null) {
                String createDataTableSql = "CREATE TABLE `" + tableName + "` (\n" +
                        "  `mgstcd` varchar(25) NOT NULL COMMENT '合并测站编码',\n" +
                        "  `stcd` varchar(15) NOT NULL COMMENT '测站编码',\n" +
                        "  `tm` datetime NOT NULL COMMENT '测量时间',\n" +
                        "  `drp` double(5,1) DEFAULT NULL COMMENT '时段降水量',\n" +
                        "  `intv` double(5,2) DEFAULT NULL COMMENT '时段长',\n" +
                        "  `upload_time` datetime DEFAULT NULL COMMENT '上报时间',\n" +
                        "  UNIQUE KEY `index_mgstcd_stcd_tm` (`MGSTCD`,`STCD`,`TM`)  USING BTREE, \n" +
                        "KEY `index_sort_mgstcd` (`mgstcd`) USING BTREE,\n" +
                        "  KEY `index_sort_stcd` (`stcd`) USING BTREE,\n" +
                        "  KEY `index_sort_tm` (`tm`) USING BTREE,\n" +
                        "  KEY `index_sort_drp` (`drp`) USING BTREE\n" +
                        ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='降雨量信息表';";
                businessMapper.createTable(createDataTableSql);
//                String watBusRecordSql = "insert into `wz_soft_data_db`.`bus_data_divide` (system_type,table_name) values('水灾害','" + tableName + "')";
                log.info("降雨量信息表-{}构建成功", tableName);
            } else {
                log.info("已存在降雨量信息表-{}", tableName);
            }
        } catch (Exception e) {
            log.error("河道水情表-构建失败{}", e.getStackTrace());
        }
    }

    private void river(String yearMonthStr) {
        try {
            String tableName = "st_river_r_" + yearMonthStr;
            if (businessMapper.checkTableExist(tableName) == null) {
                String createDataTableSql = "CREATE TABLE `" + tableName + "` (\n" +
                        "  `mgstcd` varchar(25) NOT NULL COMMENT '合并测站编码',\n" +
                        "  `stcd` varchar(15) NOT NULL COMMENT '测站编码',\n" +
                        "  `tm` datetime NOT NULL COMMENT '监测时间',\n" +
                        "  `z` double(7,3) DEFAULT NULL COMMENT '水位',\n" +
                        "  `tag` tinyint(1) DEFAULT NULL,\n" +
                        "  `upload_time` datetime(6) DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(6) COMMENT '上报时间',\n" +
                        "   UNIQUE KEY `index_mgstcd_stcd_tm` (`MGSTCD`,`STCD`,`TM`)  USING BTREE, \n" +
                        "  KEY `index_sort_mgstcd` (`mgstcd`) USING BTREE,\n" +
                        "  KEY `index_sort_stcd` (`stcd`) USING BTREE,\n" +
                        "  KEY `index_sort_tm` (`tm`) USING BTREE,\n" +
                        "  KEY `index_sort_z` (`z`) USING BTREE\n" +
                        ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='河道水情表';";
//                String watBusRecordSql = "insert into `wz_soft_data_db`.`bus_data_divide` (system_type,table_name) values('水灾害','" + tableName + "')";
                businessMapper.createTable(createDataTableSql);
                log.info("河道水情表-{}构建成功", tableName);
            } else {
                log.info("已存在河道水情表-{}", tableName);
            }
        } catch (Exception e) {
            log.error("河道水情表-构建失败{}", e.getStackTrace());
        }
    }

    private void rsvr(String yearMonthStr) {
        try {
            String tableName = "st_rsvr_r_" + yearMonthStr;
            if (businessMapper.checkTableExist(tableName) == null) {
                String createDataTableSql = "CREATE TABLE `" + tableName + "` (\n" +
                        "  `mgstcd` varchar(25) NOT NULL COMMENT '合并测站编码',\n" +
                        "  `stcd` varchar(15) NOT NULL COMMENT '测站编码',\n" +
                        "  `tm` datetime NOT NULL COMMENT '监测时间',\n" +
                        "  `rz` double(7,3) DEFAULT NULL COMMENT '水位',\n" +
                        "  `tag` tinyint(1) DEFAULT NULL,\n" +
                        "  `upload_time` datetime(6) DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(6) COMMENT '上报时间',\n" +
                        "  UNIQUE KEY `index_mgstcd_stcd_tm` (`MGSTCD`,`STCD`,`TM`)  USING BTREE, \n" +
                        "  KEY `index_sort_mgstcd` (`mgstcd`) USING BTREE,\n" +
                        "  KEY `index_sort_stcd` (`stcd`) USING BTREE,\n" +
                        "  KEY `index_sort_tm` (`tm`) USING BTREE,\n" +
                        "  KEY `index_sort_rz` (`rz`) USING BTREE\n" +
                        ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='水库水情表';";
//                String watBusRecordSql = "insert into `wz_soft_data_db`.`bus_data_divide` (system_type,table_name) values('水灾害','" + tableName + "')";
                businessMapper.createTable(createDataTableSql);
                log.info("水库水情表-{}构建成功", tableName);
            } else {
                log.info("已存在水库水情表-{}", tableName);
            }
        } catch (Exception e) {
            log.error("水库水情表-构建失败{}", e.getStackTrace());
        }
    }

    private void tide(String yearMonthStr) {
        try {
            String tableName = "st_tide_r_" + yearMonthStr;
            if (businessMapper.checkTableExist(tableName) == null) {
                String createDataTableSql = "CREATE TABLE `" + tableName + "` (\n" +
                        "  `MGSTCD` varchar(25) NOT NULL COMMENT '合并测站编码',\n" +
                        "  `STCD` varchar(15) NOT NULL COMMENT '测站编码',\n" +
                        "  `TM` datetime NOT NULL COMMENT '检测时间',\n" +
                        "  `TDZ` double(7,3) DEFAULT NULL COMMENT '潮位',\n" +
                        "  `TAG` tinyint(1) DEFAULT NULL,\n" +
                        "  `UPLOAD_TIME` datetime(6) DEFAULT CURRENT_TIMESTAMP(6) COMMENT '上报时间',\n" +
                        "  UNIQUE KEY `index_mgstcd_stcd_tm` (`MGSTCD`,`STCD`,`TM`)  USING BTREE, \n" +
                        "  KEY `index_sort_mgstcd` (`MGSTCD`) USING BTREE,\n" +
                        "  KEY `index_sort_stcd` (`STCD`) USING BTREE,\n" +
                        "  KEY `index_sort_tm` (`TM`) USING BTREE,\n" +
                        "  KEY `index_sort_tdz` (`TDZ`) USING BTREE\n" +
                        ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='潮汐水情表';";
//                String watBusRecordSql = "insert into `wz_soft_data_db`.`bus_data_divide` (system_type,table_name) values('水灾害','" + tableName + "')";
                businessMapper.createTable(createDataTableSql);
//                businessMapper.createTable(watBusRecordSql);
                log.info("潮汐水情表-{}构建成功", tableName);
            } else {
                log.info("已存在潮汐水情表-{}", tableName);
            }
        } catch (Exception e) {
            log.error("潮汐水情表-构建失败{}", e.getStackTrace());
        }
    }

}
