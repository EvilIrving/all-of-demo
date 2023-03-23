package com.ygkj.digitization.service.impl;

import com.github.pagehelper.PageHelper;
import com.ygkj.digitization.mapper.*;
import com.ygkj.digitization.service.MonitorManageService;
import com.ygkj.digitization.enums.MonitorContactType;
import com.ygkj.digitization.enums.MonitorPersonTypeEnum;
import com.ygkj.digitization.enums.SexEnum;
import com.ygkj.digitization.mapper.*;
import com.ygkj.digitization.model.MonitorContact;
import com.ygkj.digitization.model.MonitorInfo;
import com.ygkj.digitization.model.MonitorPerson;
import com.ygkj.digitization.vo.request.MonitorContactVo;
import com.ygkj.digitization.vo.request.MonitorInfoVo;
import com.ygkj.digitization.vo.request.MonitorPersonAddVo;
import com.ygkj.digitization.vo.response.MonitorInfoResVo;
import com.ygkj.digitization.vo.response.MonitorPersonResVo;
import com.ygkj.gragh.model.AttAdcdBase;
import com.ygkj.utils.DateUtils;
import com.ygkj.utils.PageUtils;
import com.ygkj.vo.PageVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author xq
 * @Description 值班管理service实现
 * @Date 2021/1/13
 */
@Service
public class MonitorManageServiceImpl implements MonitorManageService {

    @Resource
    MonitorPersonMapper monitorPersonMapper;

    @Resource
    MonitorContactMapper monitorContactMapper;

    @Resource
    MonitorInfoMapper monitorInfoMapper;

    @Resource
    SysUserMapper sysUserMapper;

    @Resource
    AttAdcdBaseMapper attAdcdBaseMapper;

    @Override
    public PageVO<MonitorPersonResVo> selectMonitorPersonPage(int pageNum, int pageSize,
                                                              Integer type, String userName) {
        PageVO<MonitorPersonResVo> result = new PageVO<>();
        if (userName != null && StringUtils.isBlank(userName)) {
            /**
             * 如果userName空白字符串，则设置为空，不予查询
             */
            userName = null;
        }
        PageHelper.startPage(pageNum, pageSize);
        PageVO<MonitorPerson> tempPage = PageUtils.getPageVO(monitorPersonMapper.selectByPersonTypeAndUsername(type, userName));
        result.setTotalRows(tempPage.getTotalRows());
        result.setTotalPages(tempPage.getCurPageSize());
        result.setPageNum(tempPage.getPageNum());
        result.setCurPageSize(tempPage.getCurPageSize());
        result.setPageSize(tempPage.getPageSize());
        List<MonitorPersonResVo> tempList = fillDataForPersonResVo(tempPage.getList());
        result.setList(tempList);
        return result;
    }

    private List<MonitorPersonResVo> fillDataForPersonResVo(List<MonitorPerson> personList) {
        if (personList != null && !personList.isEmpty()) {
            /**
             * 根据user_id 去sys_user表中拿主要数据
             * 注意维持原来的顺序
             */
            Map<String, MonitorPersonResVo> userMap = new HashMap<>(personList.size());
            List<MonitorPersonResVo> resultList = new ArrayList<>(personList.size());
            for (MonitorPerson monitorPerson : personList) {

                MonitorPersonResVo temp = new MonitorPersonResVo();
                temp.setPersonType(monitorPerson.getPersonType());
                temp.setUserId(monitorPerson.getUserId());
                userMap.put(monitorPerson.getUserId(), temp);
                resultList.add(temp);
            }
            List<MonitorPersonResVo> userInfo = sysUserMapper.selectByUserIdList(new ArrayList<>(userMap.keySet()));
            if (userInfo != null && !userInfo.isEmpty()) {
                Set<String> adcdSet = new HashSet<>(personList.size());
                for (MonitorPersonResVo temp : userInfo) {
                    /**
                     * 由于要维持最初的查询顺序，所以需要一个个set
                     */
                    MonitorPersonResVo resultTemp = userMap.get(temp.getUserId());
                    if (resultTemp != null) {
                        resultTemp.setUsername(temp.getUsername());
                        resultTemp.setSex(temp.getSex());
                        resultTemp.setPhone(temp.getPhone());
                        resultTemp.setDeptId(temp.getDeptId());
                        resultTemp.setDeptName(temp.getDeptName());
                        resultTemp.setEmail(temp.getEmail());
                        resultTemp.setAdcd(temp.getAdcd());
                        resultTemp.setAdnm(temp.getAdnm());
                        resultTemp.setJob(temp.getJob());
                    }
                    adcdSet.add(temp.getAdcd());
                }
                List<AttAdcdBase> adcdList = attAdcdBaseMapper.selectAdnmByAdcdList(new ArrayList<>(adcdSet));
                adcdList=adcdList==null?Collections.emptyList():adcdList;
                Map<String, String> adcdMap = adcdList.stream().collect(Collectors.toMap(AttAdcdBase::getAdcd,AttAdcdBase::getAdnm));
                for (MonitorPersonResVo monitorPersonResVo : resultList) {
                    monitorPersonResVo.setAdnm(adcdMap.get(monitorPersonResVo.getAdcd()));
                }
            }
            return resultList;
        } else {
            return null;
        }
    }


    @Override
    public int addMonitorPerson(MonitorPersonAddVo addMonitorPersonVo) {
        List<String> paramUserId = addMonitorPersonVo.getUserIdList();
        if (paramUserId != null && !paramUserId.isEmpty()) {
            List<String> existUserIds = monitorPersonMapper.selectAllUserId();
            List<String> commonUserIds = null;
            if (existUserIds != null && !existUserIds.isEmpty()) {
                commonUserIds = new ArrayList<>(paramUserId);
                //得到公共的id，公共的id，予以更新
                commonUserIds.retainAll(existUserIds);
                //移除剩余的id，就是新增的id，
                paramUserId.removeAll(commonUserIds);
            } else {
                commonUserIds = Collections.emptyList();
            }
            int sum = 0;
            if (!paramUserId.isEmpty()) {
                Map<String, String> userIdNameMap = selectUserIdNameAsMap(paramUserId);
                for (String userId : paramUserId) {
                    MonitorPerson temp = new MonitorPerson();
                    temp.setUserId(userId);
                    temp.setUsername(userIdNameMap.get(userId));
                    temp.setPersonType(addMonitorPersonVo.getPersonType());
                    sum += monitorPersonMapper.insert(temp);
                }
            }
            if (!commonUserIds.isEmpty()) {
                sum += monitorPersonMapper.updatePersonTypeByUserId(commonUserIds, addMonitorPersonVo.getPersonType());
            }
            return sum;
        } else {
            return 0;
        }
    }

    @Override
    public int deleteMonitorPerson(List<String> userIdList) {
        if (userIdList != null && !userIdList.isEmpty()) {
            return monitorPersonMapper.deleteManyByUserIdList(userIdList);
        } else {
            return 0;
        }
    }

    @Override
    public List<MonitorPersonResVo> getMonitorPersonToExcel(Integer type, String userName) {
        if (userName != null && StringUtils.isBlank(userName)) {
            /**
             * 如果userName空白字符串，则设置为空，不予查询
             */
            userName = null;
        }
        List<MonitorPerson> tempList = monitorPersonMapper.selectByPersonTypeAndUsername(type, userName);
        List<MonitorPersonResVo> result = fillDataForPersonResVo(tempList);
        if (result != null && !result.isEmpty()) {
            for (MonitorPersonResVo monitorPersonResVo : result) {
                // 设置性别
                if (SexEnum.MALE.getSex().equals(monitorPersonResVo.getSex())) {
                    monitorPersonResVo.setSexName(SexEnum.MALE.getSexName());
                } else if (SexEnum.FEMALE.getSex().equals(monitorPersonResVo.getSex())) {
                    monitorPersonResVo.setSexName(SexEnum.FEMALE.getSexName());
                }
                //设置人员类型
                if (MonitorPersonTypeEnum.ZBRY.getPersonType().equals(monitorPersonResVo.getPersonType())) {
                    monitorPersonResVo.setPersonTypeName(MonitorPersonTypeEnum.ZBRY.getPersonTypeName());
                } else if (MonitorPersonTypeEnum.FBRY.getPersonType().equals(monitorPersonResVo.getPersonType())) {
                    monitorPersonResVo.setPersonTypeName(MonitorPersonTypeEnum.FBRY.getPersonTypeName());
                } else if (MonitorPersonTypeEnum.DBLD.getPersonType().equals(monitorPersonResVo.getPersonType())) {
                    monitorPersonResVo.setPersonTypeName(MonitorPersonTypeEnum.DBLD.getPersonTypeName());
                }
            }
        }
        return result;
    }

    @Override
    public PageVO<MonitorContact> queryMonitorContactPage(int pageNum, int pageSize,
                                                          String contactName,
                                                          Integer contactType) {
        PageHelper.startPage(pageNum, pageSize);
        return PageUtils.getPageVO(monitorContactMapper.queryByNameAndType(contactName, contactType));
    }

    @Override
    public MonitorContactVo queryMonitorContactById(Integer id) {
        if (id != null) {
            MonitorContact contact = monitorContactMapper.selectById(id);
            MonitorContactVo result = new MonitorContactVo();
            BeanUtils.copyProperties(contact, result);
            return result;
        } else {
            return null;
        }
    }

    @Override
    public MonitorContactVo addMonitorContact(MonitorContactVo monitorContactVo) {
        MonitorContact monitorContact = new MonitorContact();
        BeanUtils.copyProperties(monitorContactVo, monitorContact);
        // id自动增长
        monitorContact.setId(null);
        monitorContactMapper.insert(monitorContact);
        monitorContactVo.setId(monitorContact.getId());
        return monitorContactVo;
    }

    @Override
    public MonitorContactVo updateMonitorContact(MonitorContactVo monitorContactVo) {
        MonitorContact monitorContact = new MonitorContact();
        BeanUtils.copyProperties(monitorContactVo, monitorContact);
        monitorContactMapper.update(monitorContact);
        monitorContactVo.setId(monitorContact.getId());
        return monitorContactVo;
    }

    @Override
    public Integer deleteManyMonitorContact(List<Integer> monitorContactIdList) {
        if (monitorContactIdList != null && !monitorContactIdList.isEmpty()) {
            return monitorContactMapper.deleteManyByIdList(monitorContactIdList);
        } else {
            return 0;
        }
    }

    @Override
    public List<MonitorContact> getMonitorContactForExcel(String contactName, Integer contactType) {
        List<MonitorContact> tempList;
//        if (contactName != null && !contactName.isEmpty()) {
        tempList = monitorContactMapper.queryByNameAndType(contactName, contactType);
//        } else {
//            tempList = monitorContactMapper.selectAll();
//        }
        if (tempList != null && !tempList.isEmpty()) {
            for (MonitorContact monitorContact : tempList) {
                monitorContact.setContactTypeName(MonitorContactType.typeValueToName(monitorContact.getContactType()));
            }
        }
        return tempList;
    }

    @Override
    public int addMonitorInfo(MonitorInfoVo monitorInfoVo) {
        List<String> monitorLeaderIdList = monitorInfoVo.getDbldIdList();
        List<String> monitorPersonIdList = monitorInfoVo.getZbryIdList();
        List<String> viceMonitorPersonIdList = monitorInfoVo.getFbryIdList();
        // 去空，去重
        List<String> allUserIdList = new ArrayList<>();
        if (monitorLeaderIdList != null) {
            monitorLeaderIdList = new ArrayList<>(new HashSet<>(monitorLeaderIdList));
            monitorLeaderIdList.removeAll(Collections.singleton(null));
            allUserIdList.addAll(monitorLeaderIdList);
        }
        if (monitorPersonIdList != null) {
            monitorPersonIdList = new ArrayList<>(new HashSet<>(monitorPersonIdList));
            monitorPersonIdList.removeAll(Collections.singleton(null));
            allUserIdList.addAll(monitorPersonIdList);
        }
        if (viceMonitorPersonIdList != null) {
            viceMonitorPersonIdList = new ArrayList<>(new HashSet<>(viceMonitorPersonIdList));
            viceMonitorPersonIdList.removeAll(Collections.singleton(null));
            allUserIdList.addAll(viceMonitorPersonIdList);
        }
        //都为空则不予操作
        if (allUserIdList.isEmpty()) {
            return 0;
        }
        Map<String, String> userIdNameMap = selectUserIdNameAsMap(allUserIdList);
        if (userIdNameMap != null && !userIdNameMap.isEmpty()) {
        } else {
            return 0;
        }
        MonitorInfo monitorInfo = new MonitorInfo();
        monitorInfo.setMonitorDate(monitorInfoVo.getMonitorDate());
        String comma = ",";

        List<String> toStrIdList = new ArrayList<>();
        List<String> toStrNameList = new ArrayList<>();
        // 带班领导
        if (monitorLeaderIdList != null && !monitorLeaderIdList.isEmpty()) {
            for (String s : monitorLeaderIdList) {
                toStrIdList.add(s);
                toStrNameList.add(String.valueOf(userIdNameMap.getOrDefault(s, null)));
            }
            monitorInfo.setDbldId(String.join(comma, toStrIdList));
            monitorInfo.setDbldName(String.join(comma, toStrNameList));
            toStrIdList.clear();
            toStrNameList.clear();
        }
        // 值班人员
        if (monitorPersonIdList != null && !monitorPersonIdList.isEmpty()) {
            for (String s : monitorPersonIdList) {
                toStrIdList.add(s);
                toStrNameList.add(String.valueOf(userIdNameMap.getOrDefault(s, null)));
            }
            monitorInfo.setZbryId(String.join(comma, toStrIdList));
            monitorInfo.setZbryName(String.join(comma, toStrNameList));
            toStrIdList.clear();
            toStrNameList.clear();
        }
        // 副班人员
        if (viceMonitorPersonIdList != null && !viceMonitorPersonIdList.isEmpty()) {
            for (String s : viceMonitorPersonIdList) {
                toStrIdList.add(s);
                toStrNameList.add(String.valueOf(userIdNameMap.getOrDefault(s, null)));
            }
            monitorInfo.setFbryId(String.join(comma, toStrIdList));
            monitorInfo.setFbryName(String.join(comma, toStrNameList));
        }
        if(monitorInfoVo.getId()!=null) {
            monitorInfo.setId(monitorInfoVo.getId());
            return monitorInfoMapper.update(monitorInfo);
        }else{
            return monitorInfoMapper.insert(monitorInfo);
        }
    }

    @Override
    public List<MonitorInfoResVo> selectMonitorInfoByDate(Date startDate, Date endDate) {
        if (startDate != null && endDate != null) {
            startDate = DateUtils.clearDateTimePrecision(startDate, Calendar.HOUR, Calendar.MINUTE, Calendar.SECOND, Calendar.MILLISECOND);
            endDate = DateUtils.clearDateTimePrecision(endDate, Calendar.HOUR, Calendar.MINUTE, Calendar.SECOND, Calendar.MILLISECOND);
            // 加1秒使endDate在between里面
            endDate = DateUtils.addValForDateTime(endDate, Calendar.SECOND, 1);
            List<MonitorInfo> monitorInfos = monitorInfoMapper.selectByMonitorDateStartEnd(startDate, endDate);
            List<MonitorInfoResVo> monitorInfoResVos = null;
            if (monitorInfos != null && !monitorInfos.isEmpty()) {
                monitorInfoResVos = new ArrayList<>(monitorInfos.size());
                for (MonitorInfo monitorInfo : monitorInfos) {
                    MonitorInfoResVo resultTemp = new MonitorInfoResVo();
                    resultTemp.setId(monitorInfo.getId());
                    resultTemp.setMonitorDate(monitorInfo.getMonitorDate());
                    resultTemp.setDbldListFromString(monitorInfo.getDbldId(), monitorInfo.getDbldName());
                    resultTemp.setZbryListFromString(monitorInfo.getZbryId(), monitorInfo.getZbryName());
                    resultTemp.setFbryListFromString(monitorInfo.getFbryId(), monitorInfo.getFbryName());
                    monitorInfoResVos.add(resultTemp);
                }
            }
            return monitorInfoResVos;
        } else {
            return null;
        }
    }

    @Override
    public int deleteOneMonitorInfo(Integer monitorInfoId) {
        if (monitorInfoId != null) {
            return monitorInfoMapper.deleteById(monitorInfoId);
        } else {
            return 0;
        }
    }

    @Override
    public boolean checkMonitorInfoExistByMoniterDate(Date monitorDate) {
        if (monitorDate == null) {
            return false;
        } else {
            return monitorInfoMapper.checkRecordExistByDate(monitorDate) != null;
        }
    }


    private Map<String, String> selectUserIdNameAsMap(List<String> userIdList) {
        if (userIdList != null && !userIdList.isEmpty()) {
            String userIdStr = String.join(",", userIdList);
            List<MonitorPersonResVo> userIdNameList = sysUserMapper.selectUserNameByUserIdList(userIdStr);
            if (userIdNameList != null && !userIdNameList.isEmpty()) {
                return userIdNameList.stream().collect(Collectors.toMap(MonitorPersonResVo::getUserId, MonitorPersonResVo::getUsername));
            } else {
                return Collections.emptyMap();
            }
        } else {
            return Collections.emptyMap();
        }
    }
}
