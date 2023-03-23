package com.ygkj.water.project.service.impl;


import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ygkj.project.model.DataMaintainInfo;
import com.ygkj.project.model.SixPeopleInfo;
import com.ygkj.project.model.ThirdPartyUnitInfo;
import com.ygkj.utils.PageUtils;
import com.ygkj.utils.StringUtils;
import com.ygkj.vo.PageVO;
import com.ygkj.water.project.mapper.DataMaintainInfoMapper;
import com.ygkj.water.project.service.DataMaintainInfoService;
import com.ygkj.water.project.utils.EasyExcelUtils;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

@Service
public class DataMaintainInfoServiceImpl extends ServiceImpl<DataMaintainInfoMapper, DataMaintainInfo> implements DataMaintainInfoService{


    @Override
    public Object queryPage(DataMaintainInfo info) {
        info.page();
        QueryWrapper<DataMaintainInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.like(StringUtils.isNotBlank(info.getDataItem()),"data_item",info.getDataItem());
        queryWrapper.eq(StringUtils.isNotBlank(info.getId()),"id",info.getId());
        queryWrapper.eq(StringUtils.isNotBlank(info.getDirectoryId()),"directory_id",info.getDirectoryId());
        queryWrapper.orderByDesc("tm");
        List<DataMaintainInfo> dataMaintainInfos = baseMapper.selectList(queryWrapper);
        PageVO<DataMaintainInfo> pageVO = PageUtils.getPageVO(dataMaintainInfos);
        List<DataMaintainInfo> list = pageVO.getList();
        for (DataMaintainInfo dataMaintainInfo : list) {
            dataMaintainInfo.setTransManageDataType("0");
            String thirdPartyUnit = dataMaintainInfo.getThirdPartyUnit();
            if (StringUtils.isNotBlank(thirdPartyUnit)){
                List<ThirdPartyUnitInfo> thirdPartyUnitInfos = JSONArray.parseArray(thirdPartyUnit, ThirdPartyUnitInfo.class);
                dataMaintainInfo.setThirdPartyUnitInfos(thirdPartyUnitInfos);
            }
            String sixPeople = dataMaintainInfo.getSixPeople();
            if (StringUtils.isNotBlank(sixPeople)){
                dataMaintainInfo.setTransManageDataType("1");
                List<SixPeopleInfo> sixPeopleInfos = JSONArray.parseArray(sixPeople, SixPeopleInfo.class);
                for (SixPeopleInfo sixPeopleInfo : sixPeopleInfos) {
                    if ("产权人责任人".equals(sixPeopleInfo.getUnitType())){
                        dataMaintainInfo.setCompetentPeople(sixPeopleInfo);
                    } else if ("管理单位责任人".equals(sixPeopleInfo.getUnitType())) {
                        dataMaintainInfo.setManagePeople(sixPeopleInfo);
                    } else if ("巡查责任人".equals(sixPeopleInfo.getUnitType())) {
                        dataMaintainInfo.setPatrolPeople(sixPeopleInfo);
                    } else if ("技术责任人".equals(sixPeopleInfo.getUnitType())) {
                        dataMaintainInfo.setTechnologyPeople(sixPeopleInfo);
                    } else if ("物业单位管理责任人".equals(sixPeopleInfo.getUnitType())) {
                        dataMaintainInfo.setPropertyPeople(sixPeopleInfo);
                    }
                }

            }
        }
        pageVO.setList(list);
        return pageVO;
    }

    @Override
    public boolean judgeAdmin(String userId) {
        return StringUtils.isEmpty(baseMapper.selectAdmin(userId));
    }

    @Override
    public void updTask(DataMaintainInfo info) {
        //todo  更新数据项
    }

    @Override
    public void exportMaintainInfo(HttpServletResponse response,DataMaintainInfo info){
        List<ThirdPartyUnitInfo> list = new ArrayList<>();
        QueryWrapper<DataMaintainInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.like(StringUtils.isNotBlank(info.getDataItem()),"data_item",info.getDataItem());
        queryWrapper.eq(StringUtils.isNotBlank(info.getId()),"id",info.getId());
        queryWrapper.eq(StringUtils.isNotBlank(info.getDirectoryId()),"directory_id",info.getDirectoryId());
        queryWrapper.orderByDesc("tm");
        List<DataMaintainInfo> dataMaintainInfos = baseMapper.selectList(queryWrapper);
        for (DataMaintainInfo dataMaintainInfo : dataMaintainInfos) {
            String thirdPartyUnit = dataMaintainInfo.getThirdPartyUnit();
            if (StringUtils.isNotBlank(thirdPartyUnit)) {
                List<ThirdPartyUnitInfo> thirdPartyUnitInfos = JSONArray.parseArray(thirdPartyUnit, ThirdPartyUnitInfo.class);
                dataMaintainInfo.setThirdPartyUnitInfos(thirdPartyUnitInfos);
                for (ThirdPartyUnitInfo thirdPartyUnitInfo : thirdPartyUnitInfos) {
                    thirdPartyUnitInfo.setDirectoryName(dataMaintainInfo.getDirectoryName());
                }
                list.addAll(thirdPartyUnitInfos);
            }else {
                ThirdPartyUnitInfo unitInfo = new ThirdPartyUnitInfo();
                unitInfo.setDirectoryName(dataMaintainInfo.getDirectoryName());
                list.add(unitInfo);
            }

        }
//        response.setContentType("application/vnd.ms-excel");
        response.setContentType("application/octet-stream");
        response.setCharacterEncoding("utf-8");
        String fileName = null;
        try {
            //设置需要合并的列
            int[] mergeColumnIndex = {0};
            // 从第一行开始合并
            int mergeRowIndex = 1;
            // 设置第0列为标杆列
            int standardColumnIndex = 0;
            fileName = URLEncoder.encode("数据维护信息表.xlsx", "UTF-8");
            response.addHeader("Access-Control-Expose-Headers", "Content-Disposition");
            response.setHeader("Content-disposition", "attachment;filename=" + fileName);
            EasyExcel.write(response.getOutputStream(), ThirdPartyUnitInfo.class).sheet("数据维护信息")
                    .registerWriteHandler(new EasyExcelUtils(mergeColumnIndex, mergeRowIndex, standardColumnIndex))
                    .doWrite(list);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
