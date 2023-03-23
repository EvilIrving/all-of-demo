package com.ygkj.digitization.service.impl;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.ygkj.digitization.feign.NoticeClient;
import com.ygkj.digitization.mapper.WarnUserMapper;
import com.ygkj.digitization.mapper.WaterRuleConfigurationMapper;
import com.ygkj.digitization.mapper.WaterWarnInformationMapper;
import com.ygkj.digitization.model.WaterRuleConfiguration;
import com.ygkj.digitization.model.WaterWarnInformation;
import com.ygkj.digitization.service.WaterWarnInformationService;
import com.ygkj.digitization.util.CommonUtil;
import com.ygkj.digitization.vo.request.RuleDetailInfo;
import com.ygkj.digitization.vo.request.WaterWarnInformationReqVo;
import com.ygkj.digitization.vo.response.WaterWarnInformationRespVo;
import com.ygkj.jwt.JwtTokenUtil;
import com.ygkj.project.model.SysUser;
import com.ygkj.utils.DateUtils;
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
 * @description 水位预警信息表
 * @author hucong
 * @date 2021-07-13
 */
@Service
public class WaterWarnInformationServiceImpl implements WaterWarnInformationService {

    @Resource
    private WaterWarnInformationMapper waterWarnInformationMapper;
    @Resource
    private SnowFlake snowFlake;
    @Resource
    private WaterRuleConfigurationMapper waterRuleConfigurationMapper;
    @Resource
    private NoticeClient noticeClient;
    @Resource
    private WarnUserMapper warnUserMapper;

    @Override
    public int insert(WaterWarnInformationReqVo vo) {
        WaterWarnInformation entity = new WaterWarnInformation();
        BeanUtils.copyProperties(vo,entity);
        entity.setCreateTime(new Date());
        try {
            entity.setCreatorId(JwtTokenUtil.getCurrentUserId().orElse(""));
            entity.setCreateName(JwtTokenUtil.getCurrentUserName().orElse(""));
        }catch (Exception e){}
        entity.setId(String.valueOf(snowFlake.nextId()));
        if (!StringUtils.isEmpty(entity.getSendPersonIds())) {
            entity.setSendPersonNum(warnUserMapper.countPeopleNum(entity.getSendPersonIds()));
        }
        return waterWarnInformationMapper.insert(entity);
    }

    @Override
    public int delete(String id) {
        return waterWarnInformationMapper.delete(id);
    }

    @Override
    public int update(WaterWarnInformationReqVo vo) {
        WaterWarnInformation entity = new WaterWarnInformation();
        BeanUtils.copyProperties(vo,entity);
        if (!StringUtils.isEmpty(entity.getSendPersonIds())) {
            entity.setSendPersonNum(waterRuleConfigurationMapper.countPeopleNum(entity.getSendPersonIds()));
        }
        return waterWarnInformationMapper.update(entity);
    }

    @Override
    public PageVO<WaterWarnInformationRespVo> loadPage(WaterWarnInformationReqVo vo) {
        if (vo.getPageNum() != null && vo.getPageNum() > 0
                && vo.getPageSize() != null && vo.getPageSize() > 0)
            PageHelper.startPage(vo.getPageNum(), vo.getPageSize());
        List<WaterWarnInformationRespVo> list = waterWarnInformationMapper.loadPage(vo);
        for (WaterWarnInformationRespVo waterWarnInformationRespVo : list) {
            String ruleDetailInfoStr = waterWarnInformationRespVo.getRuleDetailInfoStr();
            if (!StringUtils.isEmpty(ruleDetailInfoStr)) {
                waterWarnInformationRespVo.setRuleDetailInfoList(JSON.parseArray(ruleDetailInfoStr, RuleDetailInfo.class));
            }
        }
        return PageUtils.getPageVO(list);
    }

    @Override
    public List<SysUser> findUserList(String peopleIds) {
        return waterWarnInformationMapper.findUserList(peopleIds);
    }

    @Override
    public void sendMessage(WaterWarnInformationReqVo vo) throws Exception {
        Date currentDate = new Date();
        String sendType = vo.getSendType();
        if (!StringUtils.isEmpty(sendType) && sendType.contains("1")) {//短信发送
            String peopleIds = vo.getSendPersonIds();
            vo.setSendStatus(1);
            CommonUtil.sendSms(peopleIds, vo.getWarnContent(),"水文预警",warnUserMapper,noticeClient);
        }
        if (!StringUtils.isEmpty(sendType) && sendType.contains("2")) {//短信发送
            vo.setSendStatus(1);
            String filePath = CommonUtil.uploadPdfToqiNiu("各县（市、区）水利局",vo.getLsPerson(),
                    vo.getLsTime() == null ? DateUtils.format(currentDate,"yyyy年MM月dd日HH时"): DateUtils.format(vo.getLsTime(),"yyyy年MM月dd日HH时"),
                    DateUtils.format(currentDate,"yyyy"),DateUtils.format(currentDate,"yyyy年MM月dd日HH时"), vo.getWarnContent(),vo.getType());
            vo.setPdfUrl(filePath);
        }
    }
}
