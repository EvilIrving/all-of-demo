package com.ygkj.soft.database.dao;

import com.ygkj.soft.database.service.impl.DynamicDataSourceJDBCTemplateRegister;
import com.ygkj.soft.database.api.VO.*;
import com.ygkj.soft.database.api.model.DataAssessResultInfo;
import com.ygkj.soft.database.api.model.DataAssessScoreRule;
import com.ygkj.soft.database.api.model.DataAssessUpdFrequency;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;
import ygkj.com.assistant.pager.EasyuiPager;
import ygkj.com.assistant.pager.Pager;
import ygkj.com.assistant.utils.MysqlPageUtil;
import ygkj.com.util.StringUtils;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author fml
 * @date 2021-05-20 16:53
 * @description 数据考核信息管理
 */
@Repository
@Slf4j
public class DataAssessDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private DynamicDataSourceJDBCTemplateRegister register;

    public int saveRuleInfo(DataAssessScoreRule rule) {

        String SQL = "insert into data_assess_score_rule (id,fre_id,column1,column2,column3,column4,column5) values (?,?,?,?,?,?,?)";
        return jdbcTemplate.update(SQL,
                rule.getId(),
                rule.getFreId(),
                rule.getColumn1(),
                rule.getColumn2(),
                rule.getColumn3(),
                rule.getColumn4(),
                rule.getColumn5());
    }

    public int saveFreInfo(DataAssessUpdFrequency frequency) {

        String SQL = "insert into data_assess_upd_frequency (id,table_id,frequency,unit,specific_time,dept_ids,dept_names) values (?,?,?,?,?,?,?)";
        return jdbcTemplate.update(SQL,
                frequency.getId(),
                frequency.getTableId(),
                frequency.getFrequency(),
                frequency.getUnit(),
                frequency.getSpecificTime(),
                frequency.getDeptIds(),
                frequency.getDeptNames());
    }

    public int delFreInfos(String ids) {

        String SQL = "update data_assess_upd_frequency set del_flag = 1 where find_in_set(id, ?)";
        return jdbcTemplate.update(SQL, ids);
    }

    public int updRuleInfo(DataAssessScoreRule rule) {

        String SQL = "update data_assess_score_rule set column1=?,column2=?,column3=?,column4=?,column5=? where fre_id=?";
        return jdbcTemplate.update(SQL,
                rule.getColumn1(),
                rule.getColumn2(),
                rule.getColumn3(),
                rule.getColumn4(),
                rule.getColumn5(),
                rule.getFreId());
    }

    public int updFreInfo(DataAssessUpdFrequency frequency) {

        String SQL = "update data_assess_upd_frequency set table_id=?,frequency=?,unit=?,specific_time=?,dept_ids=?,dept_names=? where id=?";
        return jdbcTemplate.update(SQL,
                frequency.getTableId(),
                frequency.getFrequency(),
                frequency.getUnit(),
                frequency.getSpecificTime(),
                frequency.getDeptIds(),
                frequency.getDeptNames(),
                frequency.getId());
    }

    public Pager freInfos(DataAssessUpdFreQueryVo vo, EasyuiPager easyuiPager) {

        // 获取频次信息
        String SQL = "select a.*, b.remarks table_name from data_assess_upd_frequency a " +
                "left join bus_data_table b on a.table_id = b.id " +
                "where a.del_flag = 0";

        List<Object> params = new ArrayList<>();

        if (StringUtils.isNotEmpty(vo.getId())) {
            SQL += "and a.id = ?";
            params.add(vo.getId());
        }
        if (StringUtils.isNotEmpty(vo.getTableId())) {
            SQL += "and a.table_id = ?";
            params.add(vo.getTableId());
        }
        if (StringUtils.isNotEmpty(vo.getTableName())) {
            SQL += "and b.remarks like ?";
            params.add("%" + vo.getTableName() + "%");
        }
        if (vo.getFrequency() != null) {
            SQL += "and a.frequency = ?";
            params.add(vo.getFrequency());
        }
        if (StringUtils.isNotEmpty(vo.getUnit())) {
            SQL += "and a.unit = ?";
            params.add(vo.getUnit());
        }
        if (StringUtils.isNotEmpty(vo.getDeptId())) {
            SQL += "and a.find_in_set(?, dept_ids)";
            params.add(vo.getDeptId());
        }
        if (StringUtils.isNotEmpty(vo.getDeptName())) {
            SQL += "and a.dept_names like ?";
            params.add("%" + vo.getDeptName() + "%");
        }

        // 查总数
        Integer total = jdbcTemplate.queryForObject(MysqlPageUtil.getCountSql(SQL), params.toArray(), Integer.class);

        // 分页，pager为空就不分页
        if (null != easyuiPager && !easyuiPager.isFetchAll()) {
            int start = MysqlPageUtil.getStart(easyuiPager.getPage(), easyuiPager.getRows());
            SQL = MysqlPageUtil.pageSqlForMysql(SQL, start, easyuiPager.getRows());
        }

        Pager pager = new Pager();
        pager.setItemsTotal(total);
        List<DataAssessUpdFrequency> list = Collections.emptyList();
        if (total > 0) {
            list = jdbcTemplate.query(SQL, params.toArray(), new BeanPropertyRowMapper<>(DataAssessUpdFrequency.class));

            if (StringUtils.isNotEmpty(vo.getId())) {
                // 获取评分规则信息
                String ruleSql = "select * from data_assess_score_rule where fre_id = " + vo.getId();
                DataAssessScoreRule rule = jdbcTemplate.queryForObject(ruleSql, DataAssessScoreRule.class);
                list.get(0).setDataAssessScoreRule(rule);
            }
        }
        pager.setList(list);
        return pager;
    }

    public Pager queryAssessResults(DataAssessResultQueryVo vo, EasyuiPager easyuiPager) {

        String SQL = "select t.* from (select a.*, b.remarks table_name, d.frequency from data_assess_result_info a \n" +
                "left join bus_data_table b on a.table_id = b.id \n" +
                "left join data_assess_upd_frequency d on a.table_id = d.table_id \n" +
                "where a.del_flag = 0";

        List<Object> params = new ArrayList<>();
        if (StringUtils.isNotEmpty(vo.getId())) {
            SQL += " and a.id = ?";
            params.add(vo.getId());
        }
        if (StringUtils.isNotEmpty(vo.getDeptName())) {
            SQL += " and a.dept_name like ?";
            params.add("%" + vo.getDeptName() + "%");
        }
        if (StringUtils.isNotEmpty(vo.getAssessMonth())) {
            SQL += " and a.assess_month = ?";
            params.add(vo.getAssessMonth());
        }
        if (vo.getScoreBegin() != null) {
            SQL += " and a.score > ?";
            params.add(vo.getScoreBegin());
        }
        if (vo.getScoreEnd() != null) {
            SQL += " and a.score < ?";
            params.add(vo.getScoreEnd());
        }
        if (vo.getScoreType() != null) {
            SQL += " and a.score_type = ?";
            params.add(vo.getScoreType());
        }
        if (StringUtils.isNotEmpty(vo.getTableName())) {
            SQL += " and b.remarks like ?";
            params.add("%" + vo.getTableName() + "%");
        }
        if (vo.getScoreType() != null && vo.getScoreType() == 2){
            SQL += " having 1 order by a.assess_month desc) t group by t.table_id, t.score_type order by t.score desc";
        }else {
            SQL += " having 1 order by a.assess_month desc) t group by t.dept_id, t.score_type order by t.score desc";
        }

        // 查总数
        Integer total = jdbcTemplate.queryForObject(MysqlPageUtil.getCountSql(SQL), params.toArray(), Integer.class);

        // 分页，pager为空就不分页
        if (null != easyuiPager && !easyuiPager.isFetchAll()) {
            int start = MysqlPageUtil.getStart(easyuiPager.getPage(), easyuiPager.getRows());
            SQL = MysqlPageUtil.pageSqlForMysql(SQL, start, easyuiPager.getRows());
        }

        Pager pager = new Pager();
        pager.setItemsTotal(total);
        List<DataAssessResultInfo> list = Collections.emptyList();
        if (total > 0) {
            list = jdbcTemplate.query(SQL, params.toArray(), new BeanPropertyRowMapper<>(DataAssessResultInfo.class));
            if(vo.getScoreType() == null || vo.getScoreType() == 1){
                //补上两个科室
                fill(list);
            }
        }
        pager.setList(list);
        return pager;
    }

    private void fill(List<DataAssessResultInfo> list) {
        DataAssessResultInfo dataAssessResultInfo = new DataAssessResultInfo();
        dataAssessResultInfo.setDeptId("GO_b76efadb64824402883dcd8cac736fb5");
        dataAssessResultInfo.setDeptName("水政水资源水保科");
        dataAssessResultInfo.setTableNum(0);
        dataAssessResultInfo.setScore(0);
        list.add(dataAssessResultInfo);

        DataAssessResultInfo dataAssessResultInfo1 = new DataAssessResultInfo();
        dataAssessResultInfo1.setDeptId("GO_e7055cb35e3f471ca85fab669c83dd4d");
        dataAssessResultInfo1.setDeptName("水行政执法队");
        dataAssessResultInfo1.setTableNum(0);
        dataAssessResultInfo1.setScore(0);
        list.add(dataAssessResultInfo1);
    }

    public List<DataAssessInfo> getDataAssessInfos() {

        String SQL = "select a.*, b.column1, b.column2, b.column3, b.column4, b.column5, d.table_name, e.data_base_name \n" +
                "from data_assess_upd_frequency a\n" +
                "left join data_assess_score_rule b on a.id = b.fre_id\n" +
                "left join bus_data_table d on a.table_id = d.id\n" +
                "left join bus_data_source e on d.data_source_id = e.id\n" +
                "where a.del_flag = 0";

        List<DataAssessInfo> assessInfos = jdbcTemplate.query(SQL, new BeanPropertyRowMapper<>(DataAssessInfo.class));
        return assessInfos;
    }

    public List<DataAssessResultInfo> calculateScore(String key, List<DataAssessInfo> dataAssessInfos) {

        JdbcTemplate customJDBCTemplate = register.getCustomJDBCTemplate(key);

        List<DataAssessResultInfo> resultInfos = new ArrayList<>();

        for (DataAssessInfo dataAssessInfo : dataAssessInfos) {

            String unit = dataAssessInfo.getUnit();
            Integer column1 = dataAssessInfo.getColumn1();
            Integer column2 = dataAssessInfo.getColumn2();
            Integer column3 = dataAssessInfo.getColumn3();
            Integer column4 = dataAssessInfo.getColumn4();
            Integer column5 = dataAssessInfo.getColumn5();
            String tableName = dataAssessInfo.getTableName();
           // String deptNames = dataAssessInfo.getDeptNames();
           // String deptIds = dataAssessInfo.getDeptIds();

           /* if (StringUtils.isEmpty(deptIds) || StringUtils.isEmpty(tableName)) {
                continue;
            }

            List<String> deptIdList = Arrays.asList(deptIds.split(","));

            // 科室名
            List<String> deptNameList = null;
            if (StringUtils.isNotEmpty(deptNames)) {
                deptNameList = Arrays.asList(deptNames.split(","));
            }

            String sql = "select dts_cmf_dept dept_id, max(dts_cmf_update_time) update_time \n" +
                    "from " + tableName + " where dts_cmf_deleted = 0 group by dts_cmf_dept";

            // 相关表数据最新更新时间
            List<DataUpdInfo> deptUpds = customJDBCTemplate.query(sql, new BeanPropertyRowMapper<>(DataUpdInfo.class));
            Map<String, String> collect = new ConcurrentHashMap<>();
            if (!CollectionUtils.isEmpty(deptUpds)) {
                collect = deptUpds.stream().filter(d -> StringUtils.isNotEmpty(d.getDeptId()))
                        .collect(Collectors.toMap(DataUpdInfo::getDeptId, DataUpdInfo::getUpdateTime));
            }

            for (int i = 0; i < deptIdList.size(); i++) {

                DataAssessResultInfo resultInfo = new DataAssessResultInfo();

                resultInfo.setId(UUID.randomUUID().toString().replaceAll("-", ""));
                resultInfo.setTableName(tableName);

                String deptId = deptIdList.get(i); // 科室id
                String deptName = null;
                if (!CollectionUtils.isEmpty(deptNameList)) {
                    deptName = deptNameList.get(i); // 科室名
                }
                String tableId = dataAssessInfo.getTableId(); // 表id

                String yyyyMM = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMM")); // 评分月(yyyyMM)

                Integer scoreType = 2; // 评分类型(1科室总评分，2科室表评分)

                // 计算分数
                Integer score = 0;
                if (collect.containsKey(deptId)) {
                    String updateTime = collect.get(deptId);
                    if (StringUtils.isNotEmpty(unit)) {
                        int timeDiff = timeDiff(updateTime, unit);
                        resultInfo.setOverdue(timeDiff);
                        if (column5 != null && timeDiff <= column5) {
                            score = 80 + (int) Math.round(((double) (column5-timeDiff) / column5) * 20);
                        } else if (column4 != null && timeDiff <= column4) {
                            score = 60 + (int) Math.round(((double) (column4-timeDiff) / column4) * 20);
                        } else if (column3 != null && timeDiff <= column3) {
                            score = 40 + (int) Math.round(((double) (column3-timeDiff) / column3) * 20);
                        } else if (column2 != null && timeDiff <= column2) {
                            score = 20 + (int) Math.round(((double) (column2-timeDiff) / column2) * 20);
                        } else if (column1 != null && timeDiff <= column1) {
                            score = (int) Math.round(((double) (column1-timeDiff) / column1) * 20);
                        }
                    }
                }

                resultInfo.setDeptId(deptId);
                resultInfo.setDeptName(deptName);
                resultInfo.setTableId(tableId);
                resultInfo.setAssessMonth(yyyyMM);
                resultInfo.setScoreType(scoreType);
                resultInfo.setScore(score);
                resultInfo.setOverdueUnit(unit);

                resultInfos.add(resultInfo);
            }*/
            String sql = "select dts_cmf_dept dept_id, max(dts_cmf_update_time) update_time \n" +
                    "from " + tableName + " where dts_cmf_deleted = 0 group by dts_cmf_dept";

            // 相关表数据最新更新时间
            List<DataUpdInfo> query = customJDBCTemplate.query(sql, new BeanPropertyRowMapper<>(DataUpdInfo.class));
            DataAssessResultInfo resultInfo = new DataAssessResultInfo();

            resultInfo.setId(UUID.randomUUID().toString().replaceAll("-", ""));
            resultInfo.setTableName(tableName);

            String deptId = dataAssessInfo.getDeptIds();
            String deptName = dataAssessInfo.getDeptNames();
            String tableId = dataAssessInfo.getTableId(); // 表id

            String yyyyMM = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMM")); // 评分月(yyyyMM)

            Integer scoreType = 2; // 评分类型(1科室总评分，2科室表评分)

            // 计算分数
            Integer score = 0;
            if (StringUtils.isNotEmpty(unit) && !CollectionUtils.isEmpty(query)) {
                int timeDiff = timeDiff(query.get(0).getUpdateTime(), unit);
                resultInfo.setOverdue(timeDiff);
                if (column5 != null && timeDiff <= column5) {
                    score = 80 + (int) Math.round(((double) (column5 - timeDiff) / column5) * 20);
                } else if (column4 != null && timeDiff <= column4) {
                    score = 60 + (int) Math.round(((double) (column4 - timeDiff) / column4) * 20);
                } else if (column3 != null && timeDiff <= column3) {
                    score = 40 + (int) Math.round(((double) (column3 - timeDiff) / column3) * 20);
                } else if (column2 != null && timeDiff <= column2) {
                    score = 20 + (int) Math.round(((double) (column2 - timeDiff) / column2) * 20);
                } else if (column1 != null && timeDiff <= column1) {
                    score = (int) Math.round(((double) (column1 - timeDiff) / column1) * 20);
                }
            }
            resultInfo.setDeptId(deptId);
            resultInfo.setDeptName(deptName);
            resultInfo.setTableId(tableId);
            resultInfo.setAssessMonth(yyyyMM);
            resultInfo.setScoreType(scoreType);
            resultInfo.setScore(score);
            resultInfo.setOverdueUnit(unit);
            resultInfos.add(resultInfo);
        }


        return resultInfos;
    }

    public void saveResultInfo(List<DataAssessResultInfo> resultInfos) {

        // 保存每个科室的表评分
        List<Object[]> params = new ArrayList<>();
        for (DataAssessResultInfo resultInfo : resultInfos) {
            params.add(new Object[]{
                    resultInfo.getId(),
                    resultInfo.getDeptId(),
                    resultInfo.getDeptName(),
                    resultInfo.getTableId(),
                    resultInfo.getAssessMonth(),
                    resultInfo.getScore(),
                    resultInfo.getScoreType(),
                    resultInfo.getOverdue(),
                    resultInfo.getOverdueUnit(),
                    resultInfo.getTableNum()
            });
        }

        String sql = "replace into data_assess_result_info " +
                "(id, dept_id, dept_name, table_id, assess_month, score, score_type, overdue, overdue_unit, table_num) \n" +
                "values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        jdbcTemplate.batchUpdate(sql, params);

        // 保存每个科室的评分
        Map<String, List<DataAssessResultInfo>> collect = resultInfos.stream().filter(r -> StringUtils.isNotEmpty(r.getDeptId()))
                .collect(Collectors.groupingBy(DataAssessResultInfo::getDeptId));
        if (!CollectionUtils.isEmpty(collect)) {

            List<DataAssessResultInfo> infos = new ArrayList<>();

            collect.forEach((k, v) -> {
                DataAssessResultInfo resultInfo = new DataAssessResultInfo();

                resultInfo.setId(UUID.randomUUID().toString().replaceAll("-", ""));
                resultInfo.setDeptId(k);
                String deptName = v.stream().filter(r -> StringUtils.isNotEmpty(r.getDeptName()))
                        .map(DataAssessResultInfo::getDeptName).findFirst().orElse(null);
                resultInfo.setDeptName(deptName);
                resultInfo.setScoreType(1);
                String assessMonth = v.stream().filter(r -> StringUtils.isNotEmpty(r.getAssessMonth()))
                        .map(DataAssessResultInfo::getAssessMonth).findFirst().orElse(null);
                resultInfo.setAssessMonth(assessMonth);
                // 平均分
                double avgScore = v.stream().filter(r -> r.getScore() != null).mapToInt(DataAssessResultInfo::getScore).average().orElse(0);
                resultInfo.setScore((int) Math.round(avgScore));
                resultInfo.setTableNum(v.size());

                infos.add(resultInfo);
            });

            // 保存科室总评分
            List<Object[]> params1 = new ArrayList<>();
            for (DataAssessResultInfo resultInfo : infos) {
                params1.add(new Object[]{
                        resultInfo.getId(),
                        resultInfo.getDeptId(),
                        resultInfo.getDeptName(),
                        "",
                        resultInfo.getAssessMonth(),
                        resultInfo.getScore(),
                        resultInfo.getScoreType(),
                        resultInfo.getOverdue(),
                        resultInfo.getOverdueUnit(),
                        resultInfo.getTableNum()
                });
            }
            jdbcTemplate.batchUpdate(sql, params1);
        }
    }

    // 计算到当前时间的时间差
    private int timeDiff(String updateTime, String unit) {
        String[] split = updateTime.split("\\.");
        String s = split[0];
        LocalDateTime start = LocalDateTime.parse(s, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        LocalDateTime end = LocalDateTime.now();
        Duration duration = Duration.between(start, end);
        int diff = 0;
        if ("1".equals(unit)) {
            diff = (int) duration.toMinutes();
        }
        if ("2".equals(unit)) {
            diff = (int) duration.toHours();
        }
        if ("3".equals(unit)) {
            diff = (int) duration.toDays();
        }
        return diff;
    }

    public List<DataTableInfo> getTableInfo() {

        String sql = "select d.table_name, d.remarks, e.data_base_name \n" +
                "from data_assess_upd_frequency a \n" +
                "left join bus_data_table d on a.table_id = d.id \n" +
                "left join bus_data_source e on d.data_source_id = e.id \n" +
                "where a.del_flag = 0 group by a.table_id";

        List<DataTableInfo> tableInfos = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(DataTableInfo.class));
        return tableInfos;
    }

    public List<AssessTableUdpInfoVo> assessTable(String dataBaseName, List<DataTableInfo> dataTableInfos) {

        JdbcTemplate customJDBCTemplate = register.getCustomJDBCTemplate(dataBaseName);

        List<AssessTableUdpInfoVo> vos = new ArrayList<>();

        Date now = new Date();

        // 获取想对应的表数据的最新更新时间
        for (DataTableInfo dataTableInfo : dataTableInfos) {
            AssessTableUdpInfoVo vo = new AssessTableUdpInfoVo();
            String tableName = dataTableInfo.getTableName();
            log.info("[计算表最新更新时间]{}",tableName);
            String sql = "select max(dts_cmf_update_time) update_time \n" +
                    "from " + tableName + " where dts_cmf_deleted = 0";

            Date updTime = customJDBCTemplate.queryForObject(sql, Date.class);
            if(updTime != null){
                long days = (now.getTime() - updTime.getTime()) / (24 * 60 * 60 * 1000);
                vo.setDays((int) days);
            }
            vo.setTableName(tableName);
            vo.setRemarks(dataTableInfo.getRemarks());
            vos.add(vo);
        }

        return vos;
    }
}
