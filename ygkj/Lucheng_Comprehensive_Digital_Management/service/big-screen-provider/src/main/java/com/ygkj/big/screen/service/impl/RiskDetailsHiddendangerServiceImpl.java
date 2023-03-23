package com.ygkj.big.screen.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ygkj.big.screen.service.RiskDetailsHiddendangerService;
import com.ygkj.big.screen.mapper.RiskDetailsHiddendangerMapper;
import com.ygkj.big.screen.vo.response.*;
import com.ygkj.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author hucong
 * @version 1.0
 * @date 2021/6/16 16:58
 */
@Service
@Slf4j
public class RiskDetailsHiddendangerServiceImpl implements RiskDetailsHiddendangerService {

    @Autowired
    private RiskDetailsHiddendangerMapper riskDetailsHiddendangerMapper;

    @Override
    public List<RiskHiddenDangerStaticResp> cancellationHiddenDangerStatic() {
        List<RiskHiddenDangerStaticResp> list = riskDetailsHiddendangerMapper.cancellationHiddenDangerStatic();
        Integer total = list.stream().mapToInt(e -> e.getNum()).sum();
        for (RiskHiddenDangerStaticResp entity : list) {
            double rate = new BigDecimal((new BigDecimal(entity.getNum() + "").divide(new BigDecimal(total + ""),
                    4, BigDecimal.ROUND_HALF_UP).doubleValue() * 100) + "").setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            entity.setRate(rate + "");
            entity.setTotal(total);
        }
        return list;
    }

    @Override
    public RiskDetailsHiddenDangerResp findDetailsHiddenDangerBySbId(String sbId) {
        RiskDetailsHiddenDangerResp entity = riskDetailsHiddendangerMapper.findDetailsHiddenDangerBySbId(sbId);
        if (!StringUtils.isEmpty(entity.getReportTime())) {
            entity.setReportTime(entity.getReportTime().split(" ")[0]);
        }
        MakeRectificationPlan makeRectificationPlan = entity.getMakeRectificationPlan();
        RectificationNodeResp rectificationNodeResp = entity.getRectificationNodeResp();//整改节点
        RectificationCompletedResp rectificationCompletedResp = entity.getRectificationCompletedResp();
        if (rectificationNodeResp == null) {
            rectificationNodeResp = new RectificationNodeResp();
        }
        String fileInfoListSb = entity.getFileInfoListSb();//文件str
        String recPlanFile = entity.getRecPlanFile();//文件str
        if (!StringUtils.isEmpty(fileInfoListSb)) {
            List<FileDetail> fileDetails = JSONObject.parseArray(fileInfoListSb, FileDetail.class);
            entity.setFileInfoList(fileDetails);
        }

        if (!StringUtils.isEmpty(recPlanFile)) {
            List<FileDetail> fileDetailList = JSONObject.parseArray(recPlanFile, FileDetail.class);
            makeRectificationPlan.setImageList(fileDetailList);
        }

        makeRectificationPlan.setRecCondition(entity.getRecCondition());
        String sbHideDangerItems = entity.getSbHideDangerItems();
        if (!StringUtils.isEmpty(sbHideDangerItems)) {
            JSONArray jsonArray = JSON.parseArray(sbHideDangerItems);
            if (jsonArray.size() > 0) {
                JSONObject jo = jsonArray.getJSONObject(0);

                String name = jo.getString("name");//节点名称
                rectificationNodeResp.setName(name);
                String content = jo.getString("content");//整改内容
                rectificationNodeResp.setContent(content);
                String plannedCompleteDate = jo.getString("plannedCompleteDate");//计划完成时间
                makeRectificationPlan.setPlannedCompleteDate(plannedCompleteDate);
                rectificationNodeResp.setPlannedCompleteDate(plannedCompleteDate);
                String completeDate = jo.getString("completeDate");//完成时间
                rectificationNodeResp.setCompleteDate(completeDate);
                rectificationCompletedResp.setCompleteDate(completeDate);

                if (!StringUtils.isEmpty(jo.getString("id"))) {
                    makeRectificationPlan.setPlanRevisionPlan("已制定");
                } else {
                    makeRectificationPlan.setPlanRevisionPlan("未制定");
                }

                String files = jo.getString("files");
                if (!StringUtils.isEmpty(files)) {
                    List<FileDetail> fileDetailList = JSONObject.parseArray(files, FileDetail.class);
                    List<FileDetail> jpgFiles = fileDetailList.stream().filter(e -> ".jpg".equals(e.getFileExt()) || ".png".equals(e.getFileExt())).collect(Collectors.toList());
                    if (!jpgFiles.isEmpty()) {
                        rectificationNodeResp.setImageList(jpgFiles);
                    }
                    List<FileDetail> fileFiles = fileDetailList.stream().filter(e -> !(".jpg".equals(e.getFileExt()) || ".png".equals(e.getFileExt()))).collect(Collectors.toList());
                    if (!fileFiles.isEmpty()) {
                        rectificationNodeResp.setFileList(fileFiles);
                    }
                }
            }
        }

        //整改完成相关图片str
        String recCompleteFile = entity.getRecCompleteFile();
        if (!StringUtils.isEmpty(recCompleteFile)) {
            List<FileDetail> fileDetailList = JSONObject.parseArray(recCompleteFile, FileDetail.class);
            List<FileDetail> jpgFiles = fileDetailList.stream().filter(e -> ".jpg".equals(e.getFileExt()) || ".png".equals(e.getFileExt())).collect(Collectors.toList());
            if (!jpgFiles.isEmpty()) {
                rectificationCompletedResp.setImageList(jpgFiles);
            }
//            List<FileDetail> fileFiles = fileDetailList.stream().filter(e -> !(".jpg".equals(e.getFileExt()) || ".png".equals(e.getFileExt()))).collect(Collectors.toList());
//            if (!fileFiles.isEmpty()) {
//                rectificationCompletedResp.setFileList(fileFiles);
//            }
        }
        return entity;
    }
}
