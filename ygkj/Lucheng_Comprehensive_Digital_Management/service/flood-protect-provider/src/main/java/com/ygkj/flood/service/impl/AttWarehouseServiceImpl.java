package com.ygkj.flood.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.nacos.common.utils.UuidUtils;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.github.pagehelper.PageHelper;
import com.ygkj.flood.feign.NoticeClient;
import com.ygkj.flood.feign.SendFeignClient;
import com.ygkj.flood.mapper.AttDictBaseMapper;
import com.ygkj.flood.mapper.AttWarehouseBaseMapper;
import com.ygkj.flood.mapper.AttWarehouseDeviceBaseMapper;
import com.ygkj.flood.model.AttWarehouseAdd;
import com.ygkj.flood.model.AttWarehouseBase;
import com.ygkj.flood.model.AttWarehouseDeviceBase;
import com.ygkj.flood.model.AttWarehouseOut;
import com.ygkj.flood.service.AttWarehouseService;
import com.ygkj.flood.vo.request.AttWarehouseAddVo;
import com.ygkj.flood.vo.request.AttWarehouseDeviceBaseReqVo;
import com.ygkj.flood.vo.request.AttWarehousePageQueryVo;
import com.ygkj.flood.vo.response.AttWarehouseDeviceBaseRespVo;
import com.ygkj.flood.vo.response.AttWarehouseResVo;
import com.ygkj.flood.vo.response.ChartResVo;
import com.ygkj.jwt.JwtTokenUtil;
import com.ygkj.utils.ExportUtils;
import com.ygkj.utils.PageUtils;
import com.ygkj.utils.SnowFlake;
import com.ygkj.utils.StringUtils;
import com.ygkj.vo.PageVO;
import com.ygkj.warning.model.MsgSendRecord;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service("attWarehouseService")
public class AttWarehouseServiceImpl implements AttWarehouseService {

    @Autowired
    private SnowFlake snowFlake;

    @Autowired
    private AttWarehouseBaseMapper attWarehouseBaseMapper;
    @Resource
    private NoticeClient noticeClient;

    @Resource
    private AttDictBaseMapper dictBaseMapper;

    @Autowired
    private AttWarehouseDeviceBaseMapper attWarehouseDeviceBaseMapper;

    @Autowired
    private SendFeignClient sendFeignClient;


    @Override
    public PageVO<AttWarehouseResVo> attWarehouses(AttWarehousePageQueryVo queryVo) {
        if (queryVo.getPageSize() != null && queryVo.getPageNum() != null) {
            PageHelper.startPage(queryVo.getPageNum(), queryVo.getPageSize());
        }
        List<AttWarehouseResVo> list = attWarehouseBaseMapper.selectWarehouse(queryVo);

//        PageVO<AttWarehouseResVo> pageVO = PageUtils.getPageVO(list);
//        List<AttWarehouseResVo> list1 = pageVO.getList();
//        for (AttWarehouseResVo vo : list1) {
//            String str = vo.getWarehouseMaterials();
//            if (str != null) {
//                String[] strs = str.split(",");
//                List<String> list2 = new ArrayList<>();
//                for (int i = 0; i <= strs.length - 1; i++) {
//                    list2.add(strs[i]);
//                }
//                List<AttWarehouseDeviceBaseRespVo> deviceList = attWarehouseDeviceBaseMapper.batchSelect(list2);
//                String string = "";
//                for (AttWarehouseDeviceBaseRespVo respVo : deviceList) {
//                    string = string + respVo.getName() + ",";
//                }
//                vo.setWarehouseMaterials(string.substring(0, string.length() - 1));
//            }
//        }

//        List<AttWarehouseDeviceBaseRespVo> devicelist=
//        if (org.apache.commons.lang3.StringUtils.isNotBlank(queryVo.getOrderColumn())) {
//            Comparator<AttWarehouseResVo> comparator = new BeanComparator<>(queryVo.getOrderColumn());
//            if (queryVo.getAscOrDesc() != null && queryVo.getAscOrDesc()) {
//                comparator = comparator.reversed();
//            }
//            list = list.stream().sorted(comparator).collect(Collectors.toList());
//        }
        return PageUtils.getPageVO(list);
    }

    /**
     * 统计防汛仓库--用于组件详表表头
     *
     * @param queryVo
     * @return
     */
    @Override
    public JSONObject stats4PagedWareHouses(AttWarehousePageQueryVo queryVo) {
        List<AttWarehouseResVo> list = this.attWarehouses(queryVo).getList();
//        Map<String, Long> map = Collections.emptyMap();
//        if (CollectionUtils.isNotBlank(list)) {
//            map = list.stream().filter(o -> o.getType()!=null).collect(Collectors.groupingBy(AttWarehouseResVo::getType, Collectors.counting()));
//        }
//        List<AttDictResVo> dictList = dictBaseMapper.selectDict(Builder.of(AttDictRequestVo::new).with(AttDictRequestVo::setDictCode,"warehouseType").build());
//        dictList=dictList==null?Collections.emptyList():dictList;
//        List<ChartResVo<String, Long>> stats = new ArrayList<>();
//        for (AttDictResVo dict : dictList) {
//            stats.add(Builder.of(ChartResVo<String, Long>::new)
//                    .with(ChartResVo::setKey, dict.getDictName())
//                    .with(ChartResVo::setValue, map.getOrDefault(dict.getId(), 0L))
//                    .build());
//        }
        Double totalmaterialPrice = 0.0;
        Double totalAcreage = 0.0;
        for (AttWarehouseResVo vo : list) {
            totalmaterialPrice += Double.parseDouble(vo.getMaterialPrice());
            totalAcreage += vo.getAcreage().doubleValue();
        }
        JSONObject result = new JSONObject();
        result.put("totalmaterialPrice", totalmaterialPrice);
        result.put("totalAcreage", totalAcreage);
        result.put("total", list.size());//仓库总数
        return result;
    }

    /**
     * 导出防汛仓库
     *
     * @param queryVo
     * @param response
     */
    @Override
    public void exportWareHouses(AttWarehousePageQueryVo queryVo, HttpServletResponse response) {
        queryVo.noPage();
        if (queryVo.getExportStrategy() == null) {
            queryVo.setExportStrategy(0);
        }
        List<AttWarehouseResVo> cameraList = this.attWarehouses(queryVo).getList();
//        switch (queryVo.getExportStrategy()){
//            case 0:{
//                ExportUtils.exportExcel(cameraList, "防汛仓库",
//                        new String[]{"物资点名称", "行政区划", "仓库类型", "地址", "物资价值(万元)", "负责人", "联系电话", "仓库面积(km²)"},
//                        new String[]{"name", "areaName", "type", "address", "materialPrice", "chargePerson", "chargePhone", "acreage"}, response,AttWarehouseResVo.class, Collections.emptyMap());
//
//            }break;
//        }
        switch (queryVo.getExportStrategy()) {
            case 0: {
                ExportUtils.exportExcel(cameraList, "救援仓库",
                        new String[]{"仓库名称", "仓库类型", "仓库面积", "物资总价值", "详细地址", "联系人", "联系方式"},
                        new String[]{"name", "type", "acreage", "materialPrice", "address", "chargePerson", "chargePhone"}, response, AttWarehouseResVo.class, Collections.emptyMap());

            }
            break;
        }
    }

    @Override
    public boolean insert(AttWarehouseAddVo attWarehouseAddVo) {
        AttWarehouseBase attWarehouseBase = new AttWarehouseBase();
        BeanUtils.copyProperties(attWarehouseAddVo, attWarehouseBase);
        attWarehouseBase.setId(String.valueOf(snowFlake.nextId()));
        if (attWarehouseBaseMapper.insert(attWarehouseBase) > 0) {
            return true;
        }
        return false;
    }

    @Override
    public boolean update(AttWarehouseAddVo attWarehouseAddVo) {
        AttWarehouseBase attWarehouseBase = new AttWarehouseBase();
        BeanUtils.copyProperties(attWarehouseAddVo, attWarehouseBase);
        if (attWarehouseBaseMapper.update(attWarehouseBase) > 0) {
            return true;
        }
        return false;
    }

    @Override
    public boolean delete(String ids) {
        if (attWarehouseBaseMapper.delete(ids) > 0) {
            return true;
        }
        return false;
    }

    @Override
    public JSONArray warehousesGroupByArea() {
        List<AttWarehouseResVo> list = attWarehouseBaseMapper.selectWarehouse(new AttWarehousePageQueryVo());
        Map<String, List<AttWarehouseResVo>> map = list.stream().filter(vo -> vo.getAreaName() != null && vo.getAreaName() != "").collect(Collectors.groupingBy(AttWarehouseResVo::getAreaName));
        JSONArray result = new JSONArray();
        map.forEach((areaName, warehouses) -> {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("areaName", areaName);
            jsonObject.put("areaCode", warehouses.get(0).getAreaCode());
            jsonObject.put("number", warehouses.size());
            jsonObject.put("list", warehouses);
            result.add(jsonObject);
        });
        result.sort(Comparator.comparing(obj -> ((JSONObject) obj).getInteger("number")));

        return result;
    }

    @Override
    public Map<String, Object> warehousesGroupByType() {
        List<JSONObject> joList = attWarehouseBaseMapper.warehousesGroupByType();
        Map<String, List<Map<String, Object>>> map = joList.stream().collect(Collectors.groupingBy(c -> (String) c.get("type")));
        Map<String, Object> result = new HashMap<>();
        result.put("sgxsNum", map.get("省供销社") != null && map.get("省供销社").size() > 0 ? map.get("省供销社").get(0).get("number") : 0);
        result.put("shcjNum", map.get("社会厂家") != null && map.get("社会厂家").size() > 0 ? map.get("社会厂家").get(0).get("number") : 0);
        result.put("xjckNum", map.get("县级仓库") != null && map.get("县级仓库").size() > 0 ? map.get("省供销社").get(0).get("number") : 0);
        result.put("sjck1Num", map.get("市级仓库") != null && map.get("市级仓库").size() > 0 ? map.get("市级仓库").get(0).get("number") : 0);
        result.put("sjck2Num", map.get("省级仓库") != null && map.get("省级仓库").size() > 0 ? map.get("省级仓库").get(0).get("number") : 0);
        return result;
    }

    @Override
    public JSONArray staticAcreageByArea() {
        List<AttWarehouseResVo> list = attWarehouseBaseMapper.selectWarehouse(new AttWarehousePageQueryVo());
        Map<String, List<AttWarehouseResVo>> map = list.stream().collect(Collectors.groupingBy(AttWarehouseResVo::getAreaName));
        JSONArray result = new JSONArray();
        map.forEach((areaName, warehouses) -> {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("areaName", areaName);
            jsonObject.put("areaCode", warehouses.get(0).getAreaCode());
            double sum = warehouses.stream().filter(e -> e.getAcreage() != null).mapToDouble(k ->
                    k.getAcreage().setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue()).sum();
            jsonObject.put("acreageData", sum);
            jsonObject.put("list", warehouses);
            result.add(jsonObject);
        });
        return result;
    }

    @Override
    public List<JSONObject> staticAcreageByType() {
        List<JSONObject> joList = attWarehouseBaseMapper.staticAcreageByType();
        return joList;
    }

    @Override
    public JSONArray staticMaterialPriceByArea() {
        List<AttWarehouseResVo> list = attWarehouseBaseMapper.selectWarehouse(new AttWarehousePageQueryVo());
        Map<String, List<AttWarehouseResVo>> map = list.stream().collect(Collectors.groupingBy(AttWarehouseResVo::getAreaName));
        JSONArray result = new JSONArray();
        map.forEach((areaName, warehouses) -> {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("areaName", areaName);
            jsonObject.put("areaCode", warehouses.get(0).getAreaCode());
            double sum = warehouses.stream().filter(e -> !StringUtils.isEmpty(e.getMaterialPrice())).mapToDouble(k ->
                    Double.parseDouble(k.getMaterialPrice())).sum();
            jsonObject.put("materialPriceData", new BigDecimal(sum + "").setScale(2, BigDecimal.ROUND_HALF_UP));
            jsonObject.put("list", warehouses);
            result.add(jsonObject);
        });
        return result;
    }

    @Override
    public Map<String, Object> staticMaterialPriceByType() {
        List<JSONObject> joList = attWarehouseBaseMapper.staticMaterialPriceByType();
        Map<String, List<Map<String, Object>>> map = joList.stream().collect(Collectors.groupingBy(c -> (String) c.get("type")));
        Map<String, Object> result = new HashMap<>();
        result.put("sgxsPrice", map.get("省供销社") != null && map.get("省供销社").size() > 0 ? map.get("省供销社").get(0).get("materialPriceData") : 0);
        result.put("shcjPrice", map.get("社会厂家") != null && map.get("社会厂家").size() > 0 ? map.get("社会厂家").get(0).get("materialPriceData") : 0);
        result.put("xjckPrice", map.get("县级仓库") != null && map.get("县级仓库").size() > 0 ? map.get("省供销社").get(0).get("materialPriceData") : 0);
        result.put("sjck1Price", map.get("市级仓库") != null && map.get("市级仓库").size() > 0 ? map.get("市级仓库").get(0).get("materialPriceData") : 0);
        result.put("sjck2Price", map.get("省级仓库") != null && map.get("省级仓库").size() > 0 ? map.get("省级仓库").get(0).get("materialPriceData") : 0);
        return result;
    }

    @Override
    public void wareHouseSendSms(String phone, String userName,String content) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
        MsgSendRecord messageLog = new MsgSendRecord();
        messageLog.setSendType(1);
        content = "【鹿城区水利局】时间"+ sdf.format(new Date()) + "，" + content + "。";
        messageLog.setContext(content);
        messageLog.setReceivePhone(phone);
        messageLog.setSendName(JwtTokenUtil.getCurrentUserName().orElse(""));
        messageLog.setSendId(JwtTokenUtil.getCurrentUserId().orElse(""));
        messageLog.setBusinessType(4);
        sendFeignClient.send(messageLog);

    }


    @Override
    public Map<String, Object> warehousesCount() {
        Map<String, Object> result = new HashMap<>();
        Integer total = attWarehouseBaseMapper.countWarehousesNum();
        result.put("total", total);
        BigDecimal totalArea = attWarehouseBaseMapper.countTotalArea();
        result.put("totalArea", totalArea);
        BigDecimal materialPrice = attWarehouseBaseMapper.countMaterialPrice().setScale(4, BigDecimal.ROUND_HALF_UP);
        result.put("materialPrice", materialPrice);
        Map<String, Object> map1 = this.warehousesGroupByType();
        Map<String, Object> map2 = this.staticMaterialPriceByType();
        List<ChartResVo> list1 = new LinkedList<>();
        List<ChartResVo> list2 = new LinkedList<>();
        String[] str = {"sgxs", "shcj", "xjck", "sjck1", "sjck2"};
        String[] str1 = {"sgxsNum", "shcjNum", "xjckNum", "sjck1Num", "sjck2Num"};
        String[] str2 = {"sgxsPrice", "shcjPrice", "xjckPrice", "sjck1Price", "sjck2Price"};
        for (int i = 0; i <= str.length - 1; i++) {
            ChartResVo vo = new ChartResVo();
            vo.setKey(str[i]);
            vo.setValue(map1.get(str1[i]));
            list1.add(vo);
            ChartResVo resVo = new ChartResVo();
            resVo.setKey(str[i]);
            resVo.setValue(map2.get(str2[i]));
            list2.add(resVo);
        }
        result.put("totalNumByType", list1);
        result.put("totalPriceByType", list2);
        return result;
    }

    @Override
    public PageVO<AttWarehouseDeviceBaseRespVo> selectDevice(AttWarehouseDeviceBaseReqVo vo) {
        if (vo.getPageNum() != null && vo.getPageNum() > 0
                && vo.getPageSize() != null && vo.getPageSize() > 0)
            PageHelper.startPage(vo.getPageNum(), vo.getPageSize());
        PageVO<AttWarehouseDeviceBaseRespVo> page = PageUtils.getPageVO(attWarehouseDeviceBaseMapper.selectDevice(vo));
        return page;
    }

    @Override
    public int insertOrUpdateDevice(AttWarehouseDeviceBase base, String warehouseId) {
        int count = 0;
        if (base.getId() == null) {
            count = attWarehouseDeviceBaseMapper.insertDevice(base);
        } else {
            count = attWarehouseDeviceBaseMapper.updateDevice(base);
        }
        if (count > 0) {
            AttWarehouseDeviceBaseReqVo reqVo = new AttWarehouseDeviceBaseReqVo();
            reqVo.setWarehouseId(warehouseId);
            List<AttWarehouseDeviceBaseRespVo> list = attWarehouseDeviceBaseMapper.selectDevice(reqVo);
            AttWarehouseBase attWarehouseBase = new AttWarehouseBase();
            Double value = 0.0;
            for (AttWarehouseDeviceBaseRespVo respVo : list) {
                value += Double.parseDouble(respVo.getMaterialPrice());
            }
            attWarehouseBase.setMaterialPrice(String.valueOf(value));
            attWarehouseBase.setId(warehouseId);
            count = attWarehouseBaseMapper.update(attWarehouseBase);
        }
        return count;
    }

    @Override
    public int deleteDevice(AttWarehouseDeviceBaseReqVo vo) {
        List<AttWarehouseDeviceBaseRespVo> attWarehouseDeviceBaseRespVos = attWarehouseDeviceBaseMapper.selectDevice(vo);
        int count = attWarehouseDeviceBaseMapper.deleteDevice(vo);
        if (count > 0) {
            AttWarehouseDeviceBaseReqVo reqVo = new AttWarehouseDeviceBaseReqVo();
            reqVo.setWarehouseId(attWarehouseDeviceBaseRespVos.get(0).getWarehouseId());
            List<AttWarehouseDeviceBaseRespVo> list = attWarehouseDeviceBaseMapper.selectDevice(reqVo);
            AttWarehouseBase attWarehouseBase = new AttWarehouseBase();
            Double value = 0.0;
            for (AttWarehouseDeviceBaseRespVo respVo : list) {
                value += Double.parseDouble(respVo.getMaterialPrice());
            }
            attWarehouseBase.setMaterialPrice(String.valueOf(value));
            attWarehouseBase.setId(attWarehouseDeviceBaseRespVos.get(0).getWarehouseId());
            count = attWarehouseBaseMapper.update(attWarehouseBase);
        }
        return count;
    }

    @Override
    public void exportDevice(AttWarehouseDeviceBaseReqVo vo, HttpServletResponse response) {
        List<AttWarehouseDeviceBaseRespVo> attWarehouseDeviceBaseRespVos = attWarehouseDeviceBaseMapper.selectDevice(vo);
        ExportUtils.exportExcel(attWarehouseDeviceBaseRespVos, "仓库物资",
                new String[]{"物资名称", "物资类型", "数量", "规格", "物资价值"},
                new String[]{"name", "type", "num", "measure", "materialPrice"}, response, AttWarehouseDeviceBaseRespVo.class, Collections.emptyMap());
    }


    @Override
    public List<JSONObject> warehouseMaterialStandardList() {

        return attWarehouseDeviceBaseMapper.warehouseMaterialStandardList();
    }


    @Override
    public boolean addToDevice(AttWarehouseAdd attWarehouseAdd) {
        //根据仓库id和物资名称查询是否存在该物资记录于仓库，如果存在进行增加，否则进行新增
        AttWarehouseDeviceBase base = new AttWarehouseDeviceBase();
        base.setWarehouseId(attWarehouseAdd.getWarehouseId());
        base.setName(attWarehouseAdd.getMaterialName());
        AttWarehouseDeviceBase deviceBase = attWarehouseDeviceBaseMapper.selectOneByCondition(base);
        Integer flag = 0;
        if (ObjectUtils.isNotEmpty(deviceBase)){
            //更新数量
            deviceBase.setNum(deviceBase.getNum() + attWarehouseAdd.getNum());
            flag = attWarehouseDeviceBaseMapper.updateDevice(deviceBase);
        }else {
            deviceBase.setWarehouseId(attWarehouseAdd.getWarehouseId());
            deviceBase.setName(attWarehouseAdd.getMaterialName());
            deviceBase.setNum(attWarehouseAdd.getNum());
            flag = attWarehouseDeviceBaseMapper.insertDevice(deviceBase);
        }
        return flag > 0;
    }


    @Override
    public boolean outToDevice(AttWarehouseOut attWarehouseOut) {

        //根据仓库id和物资名称查询是否存在该物资记录于仓库，如果存在进行减少，否则进行新增
        AttWarehouseDeviceBase base = new AttWarehouseDeviceBase();
        base.setWarehouseId(attWarehouseOut.getWarehouseId());
        base.setName(attWarehouseOut.getMaterialName());
        AttWarehouseDeviceBase deviceBase = attWarehouseDeviceBaseMapper.selectOneByCondition(base);
        Integer flag = 0;
        if (ObjectUtils.isNotEmpty(deviceBase)){
            //更新数量
            deviceBase.setNum(deviceBase.getNum() - attWarehouseOut.getNum());
            flag = attWarehouseDeviceBaseMapper.updateDevice(deviceBase);
        }else {
            return false;
        }
        return flag > 0;
    }
}
