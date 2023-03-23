package com.ygkj.digitization.service;

import com.ygkj.digitization.model.MonitorContact;
import com.ygkj.digitization.vo.request.MonitorContactVo;
import com.ygkj.digitization.vo.request.MonitorInfoVo;
import com.ygkj.digitization.vo.request.MonitorPersonAddVo;
import com.ygkj.digitization.vo.response.MonitorInfoResVo;
import com.ygkj.digitization.vo.response.MonitorPersonResVo;
import com.ygkj.vo.PageVO;

import java.util.Date;
import java.util.List;


/**
 * @author xq
 * @Description 值班管理service
 * @Date 2021/1/13
 */
public interface MonitorManageService {
    PageVO<MonitorPersonResVo> selectMonitorPersonPage(int pageNum, int pageSize,
                                                       Integer type,String userName);

    int addMonitorPerson(MonitorPersonAddVo addMonitorPersonVo);

    int deleteMonitorPerson(List<String> userIdList);

    List<MonitorPersonResVo> getMonitorPersonToExcel(Integer type, String userName);

    PageVO<MonitorContact> queryMonitorContactPage(int pageNum,int pageSize,String contactName,Integer contactType);

    MonitorContactVo queryMonitorContactById(Integer id);

    MonitorContactVo addMonitorContact(MonitorContactVo monitorContactVo);

    MonitorContactVo updateMonitorContact(MonitorContactVo monitorContactVo);

    Integer deleteManyMonitorContact(List<Integer> monitorContactIdList);

    List<MonitorContact> getMonitorContactForExcel(String contactName,Integer contactType);

    int addMonitorInfo(MonitorInfoVo monitorInfoVo);

    List<MonitorInfoResVo> selectMonitorInfoByDate(Date startDate, Date endDate);

    int deleteOneMonitorInfo(Integer monitorInfoId);

    boolean checkMonitorInfoExistByMoniterDate(Date monitorDate);

}
