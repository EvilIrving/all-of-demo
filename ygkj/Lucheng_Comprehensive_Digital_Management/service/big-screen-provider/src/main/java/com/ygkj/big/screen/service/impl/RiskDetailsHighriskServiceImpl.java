package com.ygkj.big.screen.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.ygkj.big.screen.service.RiskDetailsHighriskService;
import com.ygkj.big.screen.mapper.RiskDetailsHighriskMapper;
import com.ygkj.big.screen.vo.response.FileDetail;
import com.ygkj.big.screen.vo.response.RiskDetailsHighriskResp;
import com.ygkj.big.screen.vo.response.RiskHiddenDangerStaticResp;
import com.ygkj.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author hucong
 * @description 风险-高风险点详情表
 * @date 2021-06-17
 */
@Service
@Slf4j
public class RiskDetailsHighriskServiceImpl implements RiskDetailsHighriskService {

    @Resource
    private RiskDetailsHighriskMapper riskDetailsHighriskMapper;


    @Override
    public RiskDetailsHighriskResp findDetailsHighriskBySbId(String sbId) {
        RiskDetailsHighriskResp entity = riskDetailsHighriskMapper.findDetailsHighriskBySbId(sbId);
        if (entity == null) {
            return new RiskDetailsHighriskResp();
        }
        String fileInfoListSb = entity.getFileInfoListSb();
        if (!StringUtils.isEmpty(fileInfoListSb)) {
            List<FileDetail> fileDetailList = JSONObject.parseArray(fileInfoListSb, FileDetail.class);
            if (!fileDetailList.isEmpty()) {
                entity.setFileDetails(fileDetailList);
            }
        }
        String recCompleteFile = entity.getRecCompleteFile();
        if (!StringUtils.isEmpty(recCompleteFile)) {
            List<FileDetail> fileDetailList = JSONObject.parseArray(recCompleteFile, FileDetail.class);
            List<FileDetail> jpgFiles = fileDetailList.stream().filter(e -> ".jpg".equals(e.getFileExt()) || ".png".equals(e.getFileExt())).collect(Collectors.toList());
            if (!jpgFiles.isEmpty()) {
                entity.setNumberUploadImages(jpgFiles);
            }
            List<FileDetail> fileFiles = fileDetailList.stream().filter(e -> !(".jpg".equals(e.getFileExt()) || ".png".equals(e.getFileExt()))).collect(Collectors.toList());
            if (!fileFiles.isEmpty()) {
                entity.setNumberUploadFiles(fileFiles);
            }
        }
        return entity;
    }

    @Override
    public List<RiskHiddenDangerStaticResp> cancellationWeakPointsStatic() {
        List<RiskHiddenDangerStaticResp> list = riskDetailsHighriskMapper.cancellationHiddenDangerStatic();
        if (list.stream().filter(e -> "未整改".equals(e.getReccondition())).count() == 0) {
            RiskHiddenDangerStaticResp resp = new RiskHiddenDangerStaticResp();
            resp.setNum(0);
            resp.setReccondition("未整改");
            list.add(resp);
        }
        if (list.stream().filter(e -> "整改中".equals(e.getReccondition())).count() == 0) {
            RiskHiddenDangerStaticResp resp = new RiskHiddenDangerStaticResp();
            resp.setNum(0);
            resp.setReccondition("整改中");
            list.add(resp);
        }
        if (list.stream().filter(e -> "已落实措施".equals(e.getReccondition())).count() == 0) {
            RiskHiddenDangerStaticResp resp = new RiskHiddenDangerStaticResp();
            resp.setNum(0);
            resp.setReccondition("已落实措施");
            list.add(resp);
        }
        if (list.stream().filter(e -> "完成整改".equals(e.getReccondition())).count() == 0) {
            RiskHiddenDangerStaticResp resp = new RiskHiddenDangerStaticResp();
            resp.setNum(0);
            resp.setReccondition("完成整改");
            list.add(resp);
        }
        Integer total = list.stream().mapToInt(e -> e.getNum()).sum();
        for (RiskHiddenDangerStaticResp entity : list) {
                double rate = total == null || total == 0 ? 0 : new BigDecimal((new BigDecimal(entity.getNum() + "").divide(new BigDecimal(total + ""),
                        4, BigDecimal.ROUND_HALF_UP).doubleValue() * 100) + "").setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            entity.setRate(rate + "");
            entity.setTotal(total);
        }
        return list;
    }
}
