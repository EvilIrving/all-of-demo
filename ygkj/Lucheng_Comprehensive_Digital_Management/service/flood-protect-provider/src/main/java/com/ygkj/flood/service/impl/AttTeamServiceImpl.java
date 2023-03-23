package com.ygkj.flood.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.ygkj.flood.mapper.*;
import com.ygkj.flood.model.AttAdcdBase;
import com.ygkj.flood.model.AttTeamBase;
import com.ygkj.flood.model.AttTeamDeviceBase;
import com.ygkj.flood.model.AttTeamPersonBase;
import com.ygkj.flood.service.AttTeamService;
import com.ygkj.flood.vo.request.*;
import com.ygkj.flood.vo.response.*;
import com.ygkj.utils.*;
import com.ygkj.vo.PageVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.stream.Collectors;

@Service("attTeamService")
public class AttTeamServiceImpl implements AttTeamService {

    @Autowired
    private SnowFlake snowFlake;

    @Autowired
    private AttTeamBaseMapper attTeamBaseMapper;

    @Autowired
    private AttTeamPersonBaseMapper teamPersonBaseMapper;

    @Resource
    private AttDictBaseMapper dictBaseMapper;

    @Resource
    private AttAdcdBaseMapper adcdBaseMapper;

    @Autowired
    private AttTeamDeviceBaseMapper attTeamDeviceBaseMapper;

    @Override
    public PageVO<AttTeamBaseResVo> selectByCondition(AttTeamBasePageQueryVo queryVo) {
        if (queryVo.getPageSize() != null && queryVo.getPageNum() != null) {
            PageHelper.startPage(queryVo.getPageNum(), queryVo.getPageSize());
        }
        List<AttTeamBaseResVo> list = attTeamBaseMapper.selectByCondition(queryVo);
//        if (org.apache.commons.lang3.StringUtils.isNotBlank(queryVo.getOrderColumn())) {
//            Comparator<AttTeamBaseResVo> comparator = new BeanComparator<>(queryVo.getOrderColumn());
//            if (queryVo.getAscOrDesc() != null && queryVo.getAscOrDesc()) {
//                comparator = comparator.reversed();
//            }
//            list = list.stream().sorted(comparator).collect(Collectors.toList());
//        }
        return PageUtils.getPageVO(list);
    }

    /**
     * 统计防汛队伍--用于组件详表表头
     *
     * @param queryVo
     * @return
     */
    @Override
    public JSONObject stats4PagedTeams(AttTeamBasePageQueryVo queryVo) {
        queryVo.setPageNum(null);
        queryVo.setPageSize(null);
        List<AttTeamBaseResVo> list = this.selectByCondition(queryVo).getList();
        Map<String, Long> map = Collections.emptyMap();
        if (CollectionUtils.isNotBlank(list)) {
            map = list.stream().filter(o -> o.getTypeId() != null).collect(Collectors.groupingBy(AttTeamBaseResVo::getTypeId, Collectors.counting()));
        }
        List<AttDictResVo> dictList = dictBaseMapper.selectDict(Builder.of(AttDictRequestVo::new).with(AttDictRequestVo::setDictCode, "teamType").build());
        dictList = dictList == null ? Collections.emptyList() : dictList;
        List<ChartResVo<String, Long>> stats = new ArrayList<>();
        for (AttDictResVo dict : dictList) {
            stats.add(Builder.of(ChartResVo<String, Long>::new)
                    .with(ChartResVo::setKey, dict.getDictName())
                    .with(ChartResVo::setValue, map.getOrDefault(dict.getId(), 0L))
                    .build());
        }
        JSONObject result = new JSONObject();
        result.put("stats", stats);
        result.put("total", list.size());
        return result;
    }

    /**
     * 导出防汛队伍
     *
     * @param queryVo
     * @param response
     */
    @Override
    public void exportTeams(AttTeamBasePageQueryVo queryVo, HttpServletResponse response) {
        queryVo.noPage();
        if (queryVo.getExportStrategy() == null) {
            queryVo.setExportStrategy(0);
        }
        List<AttTeamBaseResVo> cameraList = this.selectByCondition(queryVo).getList();
//        switch (queryVo.getExportStrategy()) {
//            case 0: {
//                ExportUtils.exportExcel(cameraList, "防汛仓库",
//                        new String[]{"抢险队伍名称", "行政区划", "队伍类型", "地址", "负责人", "联系电话", "人数(万人)"},
//                        new String[]{"name", "areaName", "typeName", "address", "chargePerson", "chargePhone", "existingNumber"}, response, AttTeamBaseResVo.class, Collections.emptyMap());
//
//            }
//            break;
//        }
        switch (queryVo.getExportStrategy()) {
            case 0: {
                ExportUtils.exportExcel(cameraList, "救援队伍",
                        new String[]{"名称", "队伍类型","抢险专业", "单位性质", "编制人数", "现有人数", "详细地址", "负责人","联系方式"},
                        new String[]{"name", "typeName","rescueSpecialty", "unitNature", "organizationNumber", "existingNumber", "address", "areaName","chargePhone"}, response, AttTeamBaseResVo.class, Collections.emptyMap());

            }
            break;
        }
    }

    @Override
    public boolean insertTeam(AttTeamBaseAddVo attTeamBaseAddVo) {
        AttTeamBase attTeamBase = new AttTeamBase();
        BeanUtils.copyProperties(attTeamBaseAddVo, attTeamBase);
        attTeamBase.setId(String.valueOf(snowFlake.nextId()));
        if (attTeamBaseMapper.insert(attTeamBase) > 0) {
            return true;
        }
        return false;
    }

    @Override
    public boolean updateTeam(AttTeamBaseAddVo attTeamBaseAddVo) {
        AttTeamBase attTeamBase = new AttTeamBase();
        BeanUtils.copyProperties(attTeamBaseAddVo, attTeamBase);
        if (attTeamBaseMapper.update(attTeamBase) > 0) {
            return true;
        }
        return false;
    }

    @Override
    public boolean deleteTeam(String ids) {
        if (attTeamBaseMapper.delete(ids) > 0) {
            return true;
        }
        return false;
    }

    @Override
    public  PageVO<AttTeamPersonResVo> teamPersons(AttTeamPersonQueryVo queryVo,Integer pageSize, Integer pageNum) {
        if (pageSize != null && pageNum != null) {
            PageHelper.startPage(pageNum,pageSize);
        }
        PageVO<AttTeamPersonResVo> page=PageUtils.getPageVO(teamPersonBaseMapper.selectByCondition(queryVo));
        return page;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean insertTeamPerson(List<AttTeamPersonAddVo> list) {
        if (!list.isEmpty()) {
//            String teamId = list.get(0).getTeamId();
//            teamPersonBaseMapper.deleteByTeamId(teamId);
            if(list.get(0).getId()==null||"".equals(list.get(0).getId())){
                for (AttTeamPersonAddVo attTeamPersonAddVo : list) {
                    attTeamPersonAddVo.setId(String.valueOf(snowFlake.nextId()));
                }
                if (teamPersonBaseMapper.batchInsert(list) > 0) {
                    return true;
                }
            }else{
                AttTeamPersonBase attTeamPersonBase = new AttTeamPersonBase();
                BeanUtils.copyProperties(list.get(0), attTeamPersonBase);
                teamPersonBaseMapper.update(attTeamPersonBase);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean deletePersons(List<AttTeamPersonBase> list) {
        if (!list.isEmpty()) {
            if(teamPersonBaseMapper.deletePersons(list)>0){
                return true;
            };
        }
        return false;
    }

    @Override
    public JSONArray teamGroupByArea() {
        JSONArray result = new JSONArray();
        List<AttTeamBaseResVo> list = attTeamBaseMapper.selectByCondition(new AttTeamBasePageQueryVo()).stream()
                .filter(attTeamBaseResVo -> StringUtils.isNotBlank(attTeamBaseResVo.getAreaCode()))
                .collect(Collectors.toList());
        Map<String, List<AttTeamBaseResVo>> map = list.stream().collect(Collectors.groupingBy(o -> o.getAreaCode().substring(0, 6).concat("000000")));
        List<AttAdcdBase> attAdcdBaseList = adcdBaseMapper.selectCountyOfWz();
        if (CollectionUtils.isNotBlank(attAdcdBaseList)) {
            for (AttAdcdBase attAdcdBase : attAdcdBaseList) {
                String areaName = attAdcdBase.getAdnm();
                String areaCode = attAdcdBase.getAdcd();
                List<AttTeamBaseResVo> teams = map.getOrDefault(areaCode, Collections.emptyList());
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("areaName", areaName);
                jsonObject.put("list", teams);
                jsonObject.put("areaCode", areaCode);
                jsonObject.put("number", teams.size());
                result.add(jsonObject);
            }
        }
        result.sort(Comparator.comparing(obj -> ((JSONObject) obj).getInteger("number")));
        return result;
    }

    @Override
    public List<JSONObject> teamGroupByType() {
        List<JSONObject> joList = attTeamBaseMapper.teamGroupByType();
        return joList;
    }

    @Override
    public JSONArray staticPeopleNumByArea() {
        JSONArray result = new JSONArray();
        List<AttTeamBaseResVo> list = attTeamBaseMapper.selectByCondition(new AttTeamBasePageQueryVo()).stream()
                .filter(attTeamBaseResVo -> StringUtils.isNotBlank(attTeamBaseResVo.getAreaCode()))
                .collect(Collectors.toList());
        Map<String, List<AttTeamBaseResVo>> map = list.stream().collect(Collectors.groupingBy(AttTeamBaseResVo::getAreaName));
        map.forEach((areaName, teams) -> {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("areaName", areaName);
            jsonObject.put("list", teams);
            jsonObject.put("areaCode", teams.get(0).getAreaCode());
            int sum = teams.stream().mapToInt(e -> e.getExistingNumber()).sum();
            jsonObject.put("peopleNum", sum);
            result.add(jsonObject);
        });
        return result;
    }

    @Override
    public List<JSONObject> staticPeopleNumByType() {
        List<JSONObject> joList = attTeamBaseMapper.staticPeopleNumByType();
        return joList;
    }

    @Override
    public Map<String, Object> teamCount() {
        Map<String, Object> result = new HashMap<>();
        //统计项（队伍总数，队伍总人数）
        Integer total = attTeamBaseMapper.countTeamNum();
        result.put("total", total);
        Integer existingNumber = attTeamBaseMapper.countExistingNumber();
        result.put("existingNumber", existingNumber);
        return result;
    }

    @Override
    public JSONObject teamCountList() {
        JSONObject result=new JSONObject();
        Integer totalTeam = attTeamBaseMapper.countTeamNum();
        result.put("totalTeam", totalTeam);
        AttTeamBasePageQueryVo queryVo=new AttTeamBasePageQueryVo();
        List<AttTeamBaseResVo> list=attTeamBaseMapper.selectByCondition(queryVo);
        result.put("maxExistingId",list.get(0).getId());
        result.put("maxExistingNum",list.get(0).getExistingNumber());
        result.put("maxExistingName",list.get(0).getName());
        result.put("minExistingId",list.get(list.size()-1).getId());
        result.put("minExistingNum",list.get(list.size()-1).getExistingNumber());
        result.put("minExistingName",list.get(list.size()-1).getName());
        return result;
    }

    @Override
    public List<AttTeamDeviceBaseRespVo> teamDeviceList(AttTeamDeviceBaseReqVo vo) {
        return attTeamBaseMapper.teamDeviceList(vo);
    }

    @Override
    public JSONObject teamDeviceByType() {
        List<Map<String,Object>> list=attTeamBaseMapper.teamDeviceByType();
        Map<String,List<Map<String,Object>>> map=list.stream().collect(Collectors.groupingBy(c->String.valueOf(c.get("type"))));
        JSONObject result = new JSONObject();
//        Set<Map.Entry<String,List<Map<String,Object>>>> entries=map.entrySet();
//        for(Map.Entry entry :entries){
//            List<Map<String,Object>> list1= (List<Map<String,Object>>)entry.getValue();
//            result.put((String) entry.getKey(),list1.get(0).get("num"));
//        }
        result.put("lightingEquipment",map.get("照明设备")!=null&&map.get("照明设备").size()>0?map.get("照明设备").get(0).get("num"):0);
        result.put("floodControEquipment",map.get("防汛设备")!=null&&map.get("防汛设备").size()>0?map.get("防汛设备").get(0).get("num"):0);
        result.put("pumpingEquipment",map.get("抽水设备")!=null&&map.get("抽水设备").size()>0?map.get("抽水设备").get(0).get("num"):0);
        return result;
    }

    @Override
    public List<AttTeamDeviceBaseRespVo> selectteamDevice(AttTeamDeviceBaseReqVo vo) {
        return attTeamDeviceBaseMapper.selectteamDevice(vo);
    }

    @Override
    public Integer insertteamDevice(AttTeamDeviceBase attTeamDeviceBase) {
        return attTeamDeviceBaseMapper.insertteamDevice(attTeamDeviceBase);
    }

    @Override
    public Integer updateteamDevice(AttTeamDeviceBase attTeamDeviceBase) {
        return attTeamDeviceBaseMapper.updateteamDevice(attTeamDeviceBase);
    }

    @Override
    public boolean deleteteamDevice(List<AttTeamDeviceBase> list) {
        return attTeamDeviceBaseMapper.deleteteamDevice(list);
    }

    @Override
    public void exportTeamPersons(String teamId,String personName,HttpServletResponse response) {
        AttTeamPersonQueryVo queryVo = new AttTeamPersonQueryVo();
        queryVo.setTeamId(teamId);
        queryVo.setPersonName(personName);
        List<AttTeamPersonResVo> cameraList = teamPersonBaseMapper.selectByCondition(queryVo);
        ExportUtils.exportExcel(cameraList, "救援队伍人员",
                new String[]{"人员名称", "职务", "办公电话", "移动电话"},
                new String[]{"personName", "personJob", "officePhone", "personPhone"}, response, AttTeamPersonResVo.class, Collections.emptyMap());

    }

    @Override
    public void exportTeamDevice(AttTeamDeviceBaseReqVo vo,HttpServletResponse response) {
        List<AttTeamDeviceBaseRespVo> cameraList = this.teamDeviceList(vo);
        ExportUtils.exportExcel(cameraList, "救援队伍设备",
                new String[]{"设备名称", "设备类型", "设备数量","单位"},
                new String[]{"name", "type", "num","measure"}, response);

    }
}
