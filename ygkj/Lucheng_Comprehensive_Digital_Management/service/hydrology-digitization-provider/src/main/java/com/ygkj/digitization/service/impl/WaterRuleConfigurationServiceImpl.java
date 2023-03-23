package com.ygkj.digitization.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.ygkj.digitization.service.WaterRuleConfigurationService;
import com.ygkj.digitization.mapper.WaterRuleConfigurationMapper;
import com.ygkj.digitization.model.WaterRuleConfiguration;
import com.ygkj.digitization.vo.request.RuleDetailInfo;
import com.ygkj.digitization.vo.request.WaterRuleConfigurationReqVo;
import com.ygkj.digitization.vo.response.WaterRuleConfigurationRespVo;
import com.ygkj.gragh.model.StPptnR;
import com.ygkj.gragh.model.StRiverR;
import com.ygkj.gragh.model.StTideR;
import com.ygkj.jwt.JwtTokenUtil;
import com.ygkj.utils.PageUtils;
import com.ygkj.utils.SnowFlake;
import com.ygkj.utils.StringUtils;
import com.ygkj.vo.PageVO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * @author hucong
 * @description 水位规则配置表
 * @date 2021-07-13
 */
@Service
public class WaterRuleConfigurationServiceImpl implements WaterRuleConfigurationService {

    @Resource
    private WaterRuleConfigurationMapper waterRuleConfigurationMapper;
    @Resource
    private SnowFlake snowFlake;


    @Override
    public int insert(WaterRuleConfigurationReqVo vo) {
        handleRuleContent(vo);
        WaterRuleConfiguration entity = new WaterRuleConfiguration();
        BeanUtils.copyProperties(vo, entity);
        entity.setCreateTime(new Date());
        entity.setCreatorId(JwtTokenUtil.getCurrentUserId().orElse(""));
        entity.setCreateName(JwtTokenUtil.getCurrentUserName().orElse(""));
        entity.setId(String.valueOf(snowFlake.nextId()));
        entity.setRuleDetailInfoStr(JSONObject.toJSONString(vo.getRuleDetailInfos()));
        return waterRuleConfigurationMapper.insert(entity);
    }

    @Override
    public int delete(String id) {
        return waterRuleConfigurationMapper.delete(id);
    }

    @Override
    public int update(WaterRuleConfigurationReqVo vo) {
        handleRuleContent(vo);
        WaterRuleConfiguration entity = new WaterRuleConfiguration();
        BeanUtils.copyProperties(vo, entity);
        entity.setRuleDetailInfoStr(JSONObject.toJSONString(vo.getRuleDetailInfos()));
        return waterRuleConfigurationMapper.update(entity);
    }

    @Override
    public PageVO<WaterRuleConfigurationRespVo> loadPage(WaterRuleConfigurationReqVo vo) {
        if (vo.getPageNum() != null && vo.getPageNum() > 0
                && vo.getPageSize() != null && vo.getPageSize() > 0)
            PageHelper.startPage(vo.getPageNum(), vo.getPageSize());
        List<WaterRuleConfigurationRespVo> list = waterRuleConfigurationMapper.loadPage(vo);
        for (WaterRuleConfigurationRespVo waterRuleConfigurationRespVo : list) {
            String peopleIds = waterRuleConfigurationRespVo.getPeopleIds();
            if (!StringUtils.isEmpty(peopleIds)) {
                Integer peopleNum = waterRuleConfigurationMapper.countPeopleNum(peopleIds);
                waterRuleConfigurationRespVo.setPeopleNum(peopleNum);
            }
            String ruleDetailInfoStr = waterRuleConfigurationRespVo.getRuleDetailInfoStr();
            if (!StringUtils.isEmpty(ruleDetailInfoStr)) {
                List<RuleDetailInfo> ruleDetailInfos = JSON.parseArray(ruleDetailInfoStr, RuleDetailInfo.class);
                waterRuleConfigurationRespVo.setRuleDetailInfoList(ruleDetailInfos);
            }
        }
        return PageUtils.getPageVO(list);
    }

    @Override
    public List<StPptnR> selectHourRainfall(List<String> tableNames, String startTime, String endTime, String codes) {
        return waterRuleConfigurationMapper.selectHourRainfall(tableNames, startTime, endTime, codes);
    }

    @Override
    public List<StRiverR> selectRiverByTimeCode(String table, List<String> stcds, Date dataTime) {
        return waterRuleConfigurationMapper.selectRiverByTimeCode(table, stcds, dataTime);
    }

    @Override
    public List<StTideR> selectTideByTimeCode(String table, List<String> stcds, Date dataTime) {
        return waterRuleConfigurationMapper.selectTideByTimeCode(table, stcds, dataTime);
    }

    @Override
    public List<StRiverR> selectRsvrWaterLevelByTimeCode(String table, List<String> stcds, Date dataTime) {
        return waterRuleConfigurationMapper.selectRsvrWaterLevelByTimeCode(table, stcds, dataTime);
    }

    /**
     * 处理组装预警规则内容
     *
     * @param entity
     */
    public void handleRuleContent(WaterRuleConfigurationReqVo entity) {
        String type = entity.getType();
        List<RuleDetailInfo> ruleDetailInfos = entity.getRuleDetailInfos();
        if (!StringUtils.isEmpty(type) && !ruleDetailInfos.isEmpty()) {//预警类型 1:时段雨量警报 2:日雨量警报 3:河道水情警报 4:水库水情警报 5:潮位警报
            StringBuilder sBuilder = new StringBuilder();
            for (RuleDetailInfo ruleDetailInfo : ruleDetailInfos) {
                String ruleDetailInfoType = ruleDetailInfo.getType();
                switch (type) {
                    case "1":
                        sBuilder.append(ruleDetailInfo.getHour() + "h累计降雨量超过" + ruleDetailInfo.getOverData() + "mm;");
                        break;
                    case "2":
                        if ("2".equals(ruleDetailInfoType)) {
                            sBuilder.append("每天" + ruleDetailInfo.getHour() + "时生成日雨量超过" + ruleDetailInfo.getOverData() + "毫米;");
                        } else if ("3".equals(ruleDetailInfoType)) {
                            sBuilder.append("每天" + ruleDetailInfo.getHour() + "时全市面雨量大于等于" + ruleDetailInfo.getOverData() + "毫米;");
                        }
                        break;
                    case "3":
                        if ("5".equals(ruleDetailInfoType)) {
                            sBuilder.append("当前水位超过警戒水位" + ruleDetailInfo.getOverData() + "米生成警报;");
                        } else if ("6".equals(ruleDetailInfoType)) {
                            sBuilder.append("当前水位超过保证水位" + ruleDetailInfo.getOverData() + "米生成警报;");
                        }
                        break;
                    case "4":
                        if ("7".equals(ruleDetailInfoType)) {
                            sBuilder.append("当前水位超过汛限水位" + ruleDetailInfo.getOverData() + "米生成警报;");
                        } else if ("8".equals(ruleDetailInfoType)) {
                            sBuilder.append("超过防洪高水位" + ruleDetailInfo.getOverData() + "米生成警报;");
                        }
                        break;
                    case "5":
                        if ("9".equals(ruleDetailInfoType)) {
                            sBuilder.append("当前潮位超过" + ruleDetailInfo.getOverData() + "米显示为蓝色预警;");
                        } else if ("10".equals(ruleDetailInfoType)) {
                            sBuilder.append("当前潮位超过" + ruleDetailInfo.getOverData() + "米显示为黄色预警;");
                        } else if ("11".equals(ruleDetailInfoType)) {
                            sBuilder.append("当前潮位超过" + ruleDetailInfo.getOverData() + "米显示为橙色预警;");
                        } else if ("12".equals(ruleDetailInfoType)) {
                            sBuilder.append("当前潮位超过" + ruleDetailInfo.getOverData() + "米显示为红色预警;");
                        }
                        break;
                }
            }
            entity.setRuleContent(sBuilder.toString());
        }
    }
}
