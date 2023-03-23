package com.ygkj.water.project.task;

import com.alibaba.fastjson.JSON;
import com.ygkj.project.model.UnitPositionSet;
import com.ygkj.project.model.UnitPositionSetBean;
import com.ygkj.project.vo.request.AttSixchargeBaseReqVo;
import com.ygkj.project.vo.response.AttSixchargeBaseRespVo;
import com.ygkj.utils.CollectionUtils;
import com.ygkj.utils.StringUtils;
import com.ygkj.water.project.mapper.AttSixchargeBaseMapper;
import com.ygkj.water.project.mapper.UnitPositionSetMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

//@SpringBootTest(classes = ProjectManageApplication.class)
//@RunWith(SpringRunner.class)
//@ActiveProfiles("dev")
@Component
public class UnitJobResponsibilityTask {
    @Autowired
    private AttSixchargeBaseMapper attSixchargeBaseMapper;
    @Autowired
    private UnitPositionSetMapper unitPositionSetMapper;

//    @Test
    @Scheduled(cron = "0 30 9 * * ?")
    public void task(){
        AttSixchargeBaseReqVo vo = new AttSixchargeBaseReqVo();
        List<AttSixchargeBaseRespVo> attSixchargeBaseRespVos = attSixchargeBaseMapper.queryPage(vo);
        List<UnitPositionSet> positionSetList = new ArrayList<>();
        for (AttSixchargeBaseRespVo respVo : attSixchargeBaseRespVos) {
            UnitPositionSet unitPositionSet = new UnitPositionSet();
            List<UnitPositionSetBean> list = new ArrayList<>();
            if (StringUtils.isNotBlank(respVo.getManageDepartmentChargeName())){
                UnitPositionSetBean bean1 = new UnitPositionSetBean();
                bean1.setLeader(respVo.getManageDepartmentChargeName());
                bean1.setLeaderPhone(respVo.getManageDepartmentChargePhone());
                bean1.setLeaderPost("单位负责人");
                list.add(bean1);
            }
            if (StringUtils.isNotBlank(respVo.getTechnologyChargeName())){
                UnitPositionSetBean bean2 = new UnitPositionSetBean();
                bean2.setLeader(respVo.getTechnologyChargeName());
                bean2.setLeaderPhone(respVo.getTechnologyChargePhone());
                bean2.setLeaderPost("技术负责人");
                list.add(bean2);
            }
            if (StringUtils.isNotBlank(respVo.getPatrolChargeName())){
                UnitPositionSetBean bean3 = new UnitPositionSetBean();
                bean3.setLeader(respVo.getPatrolChargeName());
                bean3.setLeaderPhone(respVo.getPatrolChargePhone());
                bean3.setLeaderPost("巡查负责人");
                list.add(bean3);
            }

            unitPositionSet.setJsonarray(list.size()>0?JSON.toJSONString(list):null);
            unitPositionSet.setProjectCode(respVo.getProjectCode());
            positionSetList.add(unitPositionSet);
        }
        if (CollectionUtils.isNotBlank(positionSetList)){
            for (UnitPositionSet unitPositionSet : positionSetList) {
                unitPositionSetMapper.updateBatch(unitPositionSet);
            }
        }
    }
}
