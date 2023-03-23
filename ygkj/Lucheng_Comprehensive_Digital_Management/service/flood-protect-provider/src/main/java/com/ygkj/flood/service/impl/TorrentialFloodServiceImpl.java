package com.ygkj.flood.service.impl;

import com.alibaba.nacos.common.utils.UuidUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.ygkj.flood.feign.SendFeignClient;
import com.ygkj.flood.mapper.*;
import com.ygkj.flood.model.AttPrevBase;
import com.ygkj.flood.model.AttPrevTfBase;
import com.ygkj.flood.model.AttPrevTfBaseResponsibilities;
import com.ygkj.flood.service.TorrentialFloodService;
import com.ygkj.flood.vo.request.AttPrevTfBaseVo;
import com.ygkj.flood.vo.request.PrevTfInfoReqVo;
import com.ygkj.flood.vo.response.PvFloodAffectPopulationResVo;
import com.ygkj.flood.vo.response.PvPchargeResVo;
import com.ygkj.jwt.JwtTokenUtil;
import com.ygkj.utils.CollectionUtils;
import com.ygkj.utils.PageUtils;
import com.ygkj.utils.StringUtils;
import com.ygkj.vo.PageVO;
import com.ygkj.warning.model.MsgSendRecord;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toCollection;

/**
 * @author xq
 * @Description
 * @Date 2021/6/8
 */
@Service("torrentialFloodService")
public class TorrentialFloodServiceImpl implements TorrentialFloodService {

    @Resource
    AttPrevBaseMapper attPrevBaseMapper;
    @Resource
    AttPrevTfBaseMapper attPrevTfBaseMapper;
    @Resource
    private SendFeignClient sendFeignClient;

    @Resource
    AttPrevTfBaseResponsibilitiesMapper responsibilitiesMapper;

    @Resource
    private PvPchargeMapper pvPchargeMapper;

    @Resource
    private PvPrevObjMapper pvPrevObjMapper;

    /**
     * 查询防治对象
     *
     * @return
     */
    @Override
    public List<AttPrevBase> listPrevInfo() {
        return attPrevBaseMapper.selectByCondition();
    }

    /**
     * 查询防治对象
     *
     * @return
     */
    @Override
    public List<AttPrevTfBase> listPrevTfInfo(PrevTfInfoReqVo reqVo) {
        List<AttPrevTfBase> result = attPrevTfBaseMapper.selectByCondition(reqVo);

        if (CollectionUtils.isBlank(result)) {
            return Collections.emptyList();
        }
        // 去重
        result = result.stream().collect(
                collectingAndThen(toCollection(() -> new TreeSet<>(Comparator.comparing(this::getUnitKey))), ArrayList::new)
        );
        return result;
    }



    @Override
    public List<AttPrevTfBase> listPrevObj(AttPrevTfBaseVo vo) {

        List<AttPrevTfBase> list = pvPrevObjMapper.selectPrevObj(vo);
        return list;
    }



    public String getUnitKey(AttPrevTfBase village) {
        String lngLatKey = String.valueOf(village
                .getNvi())
                .concat(StringUtils.JOIN_DELIMITER)
                .concat(village.getAvi())
                .concat(StringUtils.JOIN_DELIMITER)
                .concat(String.valueOf(village.getLon()))
                .concat(StringUtils.JOIN_DELIMITER)
                .concat(String.valueOf(village.getLat()));
        return lngLatKey;
    }


    @Override
    public PageVO<AttPrevTfBaseResponsibilities> listPrevTfResponsibilities(PrevTfInfoReqVo reqVo) {

        reqVo.page();
        List<AttPrevTfBaseResponsibilities> responsibilities = responsibilitiesMapper.selectList(Wrappers.lambdaQuery(AttPrevTfBaseResponsibilities.class)
                .eq(AttPrevTfBaseResponsibilities::getDelFlag,0)
                .eq(StringUtils.isNotBlank(reqVo.getBaseId()), AttPrevTfBaseResponsibilities::getBaseId, reqVo.getBaseId()));
        return PageUtils.getPageVO(responsibilities);
    }


    @Override
    public void sendTfMsg(PrevTfInfoReqVo reqVo) {

        MsgSendRecord messageLog = new MsgSendRecord();
        messageLog.setSendType(1);
        String content = "【鹿城区水利局】"+ reqVo.getContent() ;
        messageLog.setContext(content);
        List<String> phoneList = Arrays.asList(reqVo.getPhones().split(","));
//        List<JSONObject> recInfo = reqVo.getRecInfo();
        Set<String> phoneSet = new HashSet<>(phoneList);
        for (String phone : phoneSet) {
            messageLog.setReceivePhone(phone);
//            messageLog.setReceiveName(StringUtils.isNotBlank(json.getString("workerName"))? json.getString("workerName"): null);
            messageLog.setSendName(JwtTokenUtil.getCurrentUserName().orElse(""));
            messageLog.setSendId(JwtTokenUtil.getCurrentUserId().orElse(""));
            messageLog.setBusinessType(4);
            sendFeignClient.send(messageLog);
        }
    }

    @Override
    public PageVO<PvPchargeResVo> pvPchargeList(PrevTfInfoReqVo reqVo) {

        reqVo.page();
        List<PvPchargeResVo> list = pvPchargeMapper.pvPchargeListByConfition(reqVo);

        return PageUtils.getPageVO(list);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean updatePcharge(PvPchargeResVo resVo) {

        if (pvPchargeMapper.updatePchargeBase(resVo) > 0 && pvPchargeMapper.updatePchargeInfo(resVo) > 0){
            return true;
        }else {
            return false;
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean insertPcharge(PvPchargeResVo resVo) {

        resVo.setPchargeCode(UuidUtils.generateUuid());
//        resVo.setPchargeCode(idService.nextID());
        if (pvPchargeMapper.insertPvPchargeInfo(resVo) > 0 && pvPchargeMapper.insertPvPchargeBase(resVo) > 0){
            return true;
        }else {
            return false;
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deletePcharge(String code) {

        if (pvPchargeMapper.deletePvPchargeInfo(code) > 0 && pvPchargeMapper.deletePvPchargeBase(code) > 0){
            return true;
        }else {
            return false;
        }
    }


    @Override
    public PageVO<PvFloodAffectPopulationResVo> bigScreenAffectPopulationList(PrevTfInfoReqVo reqVo) {

        reqVo.page();
        List<PvFloodAffectPopulationResVo> list = pvPrevObjMapper.findAffectPopulationList(reqVo);

        list.forEach( l -> {
            l.setChargerName(reqVo.getChargerName());
            l.setChargerPhone(reqVo.getChargerPhone());
        });
        PageVO<PvFloodAffectPopulationResVo> pageVO = PageUtils.getPageVO(list);

        //获取总影响人口数量
        if (StringUtils.isNotBlank(reqVo.getBaseId())){
            Integer sum = pvPrevObjMapper.findSumPeople(reqVo.getBaseId());
            pageVO.setTotalPages(sum);
        }
        return pageVO;
    }
}
