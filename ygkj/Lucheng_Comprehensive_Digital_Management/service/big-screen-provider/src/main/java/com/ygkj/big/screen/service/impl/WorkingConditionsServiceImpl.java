package com.ygkj.big.screen.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ygkj.big.screen.feign.ProjectManageClient;
import com.ygkj.big.screen.service.WorkingConditionsService;
import com.ygkj.big.screen.unit.WagaManager;
import com.ygkj.big.screen.vo.request.UnitDataReqVo;
import com.ygkj.big.screen.vo.request.WrpWagaQueryReqVo;
import com.ygkj.big.screen.vo.response.RsvrStationWaterResVo;
import com.ygkj.big.screen.vo.response.WagaStationWaterResVo;
import com.ygkj.enums.StationType;
import com.ygkj.gragh.model.AttResBase;
import com.ygkj.gragh.model.StRsvrR;
import com.ygkj.gragh.rpc.GraphRpcService;
import com.ygkj.project.vo.request.PustProjectReq;
import com.ygkj.project.vo.request.RsvrStationWaterReqVo;
import com.ygkj.project.vo.response.PustProjectResp;
import com.ygkj.utils.Builder;
import com.ygkj.utils.CollectionUtils;
import com.ygkj.vo.PageVO;
import com.ygkj.water.model.CommonResult;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author pqf
 * @DESCRIPTION:
 * @create 2021-10-18 16:02
 */
@Service
public class WorkingConditionsServiceImpl implements WorkingConditionsService {

    @Autowired
    private WagaManager wagaManager;
    @Autowired
    private ProjectManageClient projectManageClient;
    @Reference
    private GraphRpcService graphRpcService;
    private DateTimeFormatter yyyyMM = DateTimeFormatter.ofPattern("yyyyMM");
    @Override
    public JSONObject workingConditionsTotal() {
        JSONObject result = new JSONObject();
        result.put("rsvrNum",0);
        result.put("rsvrOpenNum",0);
        result.put("pustNum",0);
        result.put("pustOpenNum",0);
        result.put("wagaNum",0);
        result.put("wagaOpenNum",0);
        //水库总数和水库开启
        RsvrStationWaterReqVo rsvrReqVo=new RsvrStationWaterReqVo();
        rsvrReqVo.setDrainage24H(false);
        rsvrReqVo.setDrainage48H(false);
        rsvrReqVo.setDrainage72H(false);
        CommonResult commonResult=projectManageClient.stats4RsvrUnitList(rsvrReqVo);
        LinkedHashMap<String,Object> rsvrData=(LinkedHashMap<String,Object>) commonResult.getData();
        int rsvrOpenNum=0;
        if(rsvrData!=null&& CollectionUtils.isNotBlank(rsvrData)){
            List<RsvrStationWaterResVo> data = JSONArray.parseArray(JSONArray.toJSONString(rsvrData.get("list")), RsvrStationWaterResVo.class);
            List<RsvrStationWaterResVo> openRsvrList = data.stream().filter(RsvrStationWaterResVo::isRsvrOpened).collect(Collectors.toList());
            rsvrOpenNum = openRsvrList.size();
        }

        //泵站总数和泵站开启数量
        PustProjectReq pustReqVo=new PustProjectReq();
        CommonResult commonResult1=projectManageClient.pustProject(pustReqVo);
        PageVO<PustProjectResp> pageVO=(PageVO<PustProjectResp>)commonResult1.getData();
        int pustOpenNum=0;
        if(pageVO!=null&&CollectionUtils.isNotBlank(pageVO.getList())){
            List<PustProjectResp> data = JSONArray.parseArray(JSONArray.toJSONString(pageVO.getList()), PustProjectResp.class);
            List<PustProjectResp> openPustList = data.stream().filter(PustProjectResp::isPustOpened).collect(Collectors.toList());
            pustOpenNum = openPustList.size();
        }

        //水闸总数和水闸开启数量
        WrpWagaQueryReqVo wagaReqVo=new WrpWagaQueryReqVo();
        wagaReqVo.setHours(1);
        wagaReqVo.setFilterOpenGate(false);
        JSONObject wagaData=(JSONObject) wagaManager.getData(Builder.of(UnitDataReqVo::new)
                .with(UnitDataReqVo::setStrategy, "wagaProjList")
                .with(UnitDataReqVo::putParam, "reqVo", wagaReqVo)
                .build());
        result.put("rsvrNum",rsvrData == null ? 0 : rsvrData.get("total"));
        result.put("rsvrOpenNum",rsvrOpenNum);
        result.put("pustNum",pageVO.getTotalRows());
        result.put("pustOpenNum",pustOpenNum);
        result.put("wagaNum",wagaData.get("totalNum"));
        result.put("wagaOpenNum",wagaData.get("openGateWagaNum"));
        return result;
    }

    @Override
    public CommonResult workingConditionsList(String code,String eng_scal, String pjnm, String pjtp,String openStatus,Integer pageNum,Integer pageSize) {
        JSONObject result=new JSONObject();
        if("水库".equals(pjtp)){
            RsvrStationWaterReqVo rsvrReqVo=new RsvrStationWaterReqVo();
            rsvrReqVo.setResCodes(code);
            rsvrReqVo.setEngScales(eng_scal);
//            rsvrReqVo.setResName(pjtp);
            rsvrReqVo.setResName(pjnm);
            rsvrReqVo.setPageNum(pageNum);
            rsvrReqVo.setPageSize(pageSize);
            CommonResult commonResult=projectManageClient.stats4RsvrUnitList(rsvrReqVo);
            LinkedHashMap<String,Object> rsvrData=(LinkedHashMap<String,Object>) commonResult.getData();
            if(rsvrData!=null&& CollectionUtils.isNotBlank(rsvrData)){
                List<RsvrStationWaterResVo> data = JSONArray.parseArray(JSONArray.toJSONString(rsvrData.get("list")), RsvrStationWaterResVo.class);
                result.put("list",data);
                List<String> codes = data.stream().map(RsvrStationWaterResVo::getMainStcd).collect(Collectors.toList());
                String table = StationType.RESERVOIR.table().concat(LocalDate.now().format(yyyyMM));;
                List<StRsvrR> stRsvrRS = graphRpcService.selectRsvrWaterLevelByTimeCode(table, codes, null);
                for(StRsvrR stRsvrR:stRsvrRS){
                    for(RsvrStationWaterResVo vo:data){
                        if(stRsvrR.getStcd().equals(vo.getMainStcd())){
                            vo.setWaterLevel(stRsvrR.getRz());
                            break;
                        }
                    }
                }
                List<RsvrStationWaterResVo> openRsvrList = data.stream().filter(RsvrStationWaterResVo::isRsvrOpened).collect(Collectors.toList());
                if("1".equals(openStatus)){
//                    result.put("openList",openRsvrList);
                    result.put("list",openRsvrList);
                }else if("2".equals(openStatus)){
                    data.removeAll(openRsvrList);
//                    result.put("closeList",data);
                    result.put("list",data);
                }
            }
        }else if ("泵站".equals(pjtp)){
            PustProjectReq pustReqVo=new PustProjectReq();
            pustReqVo.setAreaCode(code);
            pustReqVo.setEngScal(eng_scal);
            pustReqVo.setPustName(pjnm);
            CommonResult commonResult1=projectManageClient.pustProject(pustReqVo);
            if(commonResult1!=null&&commonResult1.getData()!=null) {
                PageVO<PustProjectResp> pageVO = (PageVO<PustProjectResp>) commonResult1.getData();
                if (pageVO != null && CollectionUtils.isNotBlank(pageVO.getList())) {
                    List<PustProjectResp> data = JSONArray.parseArray(JSONArray.toJSONString(pageVO.getList()), PustProjectResp.class);
                    result.put("list", data);
                    List<PustProjectResp> openPustList = data.stream().filter(PustProjectResp::isPustOpened).collect(Collectors.toList());
                    if ("1".equals(openStatus)) {
//                        result.put("openList", openPustList);
                        result.put("list",openPustList);

                    } else if ("2".equals(openStatus)) {
                        data.removeAll(openPustList);
//                        result.put("closeList", data);
                        result.put("list",data);
                    }
                }
            }
        }else if("水闸".equals(pjtp)){
            WrpWagaQueryReqVo wagaReqVo=new WrpWagaQueryReqVo();
            wagaReqVo.setWagaCode(code);
            wagaReqVo.setHours(1);
            wagaReqVo.setFilterOpenGate(false);
            wagaReqVo.setWagaName(pjnm);
            wagaReqVo.setEngScal(eng_scal);
            wagaReqVo.setPageNum(pageNum);
            wagaReqVo.setPageSize(pageSize);
            JSONObject wagaData=(JSONObject) wagaManager.getData(Builder.of(UnitDataReqVo::new)
                    .with(UnitDataReqVo::setStrategy, "wagaProjList")
                    .with(UnitDataReqVo::putParam, "reqVo", wagaReqVo)
                    .build());
            List<WagaStationWaterResVo> data = JSONArray.parseArray(JSONArray.toJSONString(wagaData.get("list")), WagaStationWaterResVo.class);
            result.put("list",data);
            wagaReqVo.setFilterOpenGate(true);
            JSONObject wagaOpenData=(JSONObject) wagaManager.getData(Builder.of(UnitDataReqVo::new)
                    .with(UnitDataReqVo::setStrategy, "wagaProjList")
                    .with(UnitDataReqVo::putParam, "reqVo", wagaReqVo)
                    .build());
            if(wagaOpenData!=null&&wagaOpenData.get("list")!=null){
                List<WagaStationWaterResVo> openData = JSONArray.parseArray(JSONArray.toJSONString(wagaOpenData.get("list")), WagaStationWaterResVo.class);
                if("1".equals(openStatus)){
//                result.put("openList",openData);
                    result.put("list",openData);
                }else if("2".equals(openStatus)){
                    data.removeAll(openData);
//                result.put("closeList",data);
                    result.put("list",data);
                }
            }
        }
        return CommonResult.success(result.get("list"));
    }


}
