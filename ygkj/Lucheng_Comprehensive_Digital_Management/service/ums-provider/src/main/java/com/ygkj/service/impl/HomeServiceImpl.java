package com.ygkj.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ygkj.client.DataManagementClient;
import com.ygkj.constants.Constant;
import com.ygkj.data.management.model.BusDataTableCategory;
import com.ygkj.entity.SysDept;
import com.ygkj.entity.SysUser;
import com.ygkj.jwt.JwtTokenUtil;
import com.ygkj.mapper.AssistMapper;
import com.ygkj.platform.management.model.SysLoginLog;
import com.ygkj.service.*;
import com.ygkj.vo.resp.HomeRespVO;
import com.ygkj.vo.resp.PermissionRespNode;
import com.ygkj.vo.resp.UserInfoRespVO;
import com.ygkj.warning.model.SysSendMessageLog;
import com.ygkj.water.model.CommonResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @ClassName: HomeServiceImpl
 * TODO:类文件简单描述
 * @Author: Huang.zh
 * @CreateDate: 2019/10/25 21:27
 * @UpdateUser: Huang.zh
 * @UpdateDate: 2019/10/25 21:27
 * @Version: 0.0.1
 */
@Service
public class HomeServiceImpl implements HomeService {
    @Autowired
    private UserService userService;
    @Autowired
    private DeptService deptService;
    @Autowired
    private PermissionService permissionService;
    @Autowired
    private AssistMapper assistMapper;
    @Autowired
    private DataManagementClient dataManagementClient;
    @Autowired
    private RedisService redisService;


    @Override
    public HomeRespVO getHomeInfo(String userId, String system) {
        SysUser sysUser = userService.detailInfo(userId);
        UserInfoRespVO vo = new UserInfoRespVO();
        if (sysUser != null) {
            BeanUtils.copyProperties(sysUser, vo);
            SysDept sysDept = deptService.detailInfo(sysUser.getDeptId());
            if (sysDept != null) {
                vo.setDeptId(sysDept.getId());
                vo.setDeptName(sysDept.getName());
            }
        }
        List<PermissionRespNode> menus = permissionService.permissionTreeList(userId, system);
        HomeRespVO respVO = new HomeRespVO();
        respVO.setMenus(menus);
        respVO.setUserInfo(vo);
        return respVO;
    }

    @Override
    public JSONObject myTrajectory() {
        JSONObject result = new JSONObject();
        String userId = JwtTokenUtil.getCurrentUserId().orElse(StringUtils.EMPTY);
        String deptId = JwtTokenUtil.getCurrentUserDepartmentId().orElse(StringUtils.EMPTY);
        JSONArray array = permissionService.polymerizationRoles();
        //角色
        result.put("roles", array.stream()
                .map(o -> ((JSONObject) o).getJSONArray("roles").stream()
                        .map(o1 -> ((JSONObject) o1).getString("NAME")).collect(Collectors.joining(","))).collect(Collectors.joining(",")));
        //个人消息
        result.put("message", combineLogs(userId));
        //收藏资料数量
        result.put("collectionNumbers", assistMapper.userCollectionNumbers(userId));
        //数据维护
        result.put("dataUpdate", dataUpdate(userId, deptId));
        //数据责任
        result.put("dataResponsibility", dataResponsibility());
        //最常访问
        Map<String, Object> map = assistMapper.frequentlyVisited(userId);
        result.putAll(map == null ? Collections.emptyMap() : map);
        //登录信息
        result.put("loginInfo", loginInfo(userId, deptId));
        return result;
    }

    /**
     * 个人消息
     *
     * @return
     */
    private JSONObject combineLogs(String userId) {
        JSONObject result = new JSONObject();
        result.put("description", "个人消息");
        if (StringUtils.isNotBlank(userId)) {
            List<SysSendMessageLog> logs = assistMapper.mineReceiveAndSend(userId).stream()
                    .filter(sysSendMessageLog -> StringUtils.isNotBlank(sysSendMessageLog.getDestinationUserId()) &&
                            StringUtils.isNotBlank(sysSendMessageLog.getFromUserId())).collect(Collectors.toList());
            if (!logs.isEmpty()) {
                //总接收消息数
                result.put("myTotalReceive", logs.stream().filter(log -> log.getDestinationUserId().equals(userId)).count());
                //总消息发送数
                result.put("myTotalSend", logs.stream().filter(log -> log.getFromUserId().equals(userId)).count());
                //最多消息接收
                Map<String, Long> map = logs.stream()
                        .filter(sysSendMessageLog -> sysSendMessageLog.getDestinationUserId().equals(userId))
                        .collect(Collectors.groupingBy(SysSendMessageLog::getFromUserName, Collectors.counting()));
                map.entrySet().stream().max(Comparator.comparing(Map.Entry::getValue)).ifPresent(entry -> {
                    result.put("maxReceiveFromUserName", entry.getKey());
                    result.put("maxReceive", entry.getValue().intValue());
                });
                //最多消息发送
                map = logs.stream()
                        .filter(sysSendMessageLog -> sysSendMessageLog.getFromUserId().equals(userId))
                        .collect(Collectors.groupingBy(SysSendMessageLog::getDestinationUserName, Collectors.counting()));
                map.entrySet().stream().max(Comparator.comparing(Map.Entry::getValue)).ifPresent(entry -> {
                    result.put("maxSendToUserName", entry.getKey());
                    result.put("maxSend", entry.getValue().intValue());
                });
            }
        }
        return result;
    }

    /**
     * 数据责任
     *
     * @return
     */
    private JSONObject dataResponsibility() {
        JSONObject result = new JSONObject();
        CommonResult<List<BusDataTableCategory>> category = dataManagementClient.getMyCategory("实时数据");
        List<BusDataTableCategory> list = category.getData();
        list.addAll(dataManagementClient.getMyCategory("基础数据").getData());
        int typeNumber = 0, dataNumber = 0;
        for (BusDataTableCategory busDataTableCategory : list) {
            dataNumber += busDataTableCategory.getCount().intValue();
            typeNumber += busDataTableCategory.getChildren().size();
        }
        result.put("dataNumber", dataNumber);
        result.put("typeNumber", typeNumber);
        result.put("description", "数据责任");
        return result;
    }

    /**
     * 登录信息
     *
     * @param userId
     * @param deptId
     * @return
     */
    private JSONObject loginInfo(String userId, String deptId) {
        JSONObject result = new JSONObject();
        result.put("description", "登录信息");
        //总登录次数
        Double zscore = redisService.zscore(Constant.LOGIN_RANK_KEY, userId);
        result.put("totalLoginNumber", zscore == null ? 0 : zscore.intValue());
        SysLoginLog log = assistMapper.lastLoginLog(userId);
        if (log != null) {
            result.put("lastLoginTime", log.getLoginTime());
            result.put("lastLoginIp", log.getLoginIp());
        } else {
            result.put("lastLoginTime", Constant.NO_DATA_STRING);
            result.put("lastLoginIp", Constant.NO_DATA_STRING);
        }
        Set<Object> setTotal = redisService.zrevrangeByScore(Constant.LOGIN_RANK_KEY, 0, Double.MAX_VALUE);
        Set<Object> setDepartment = redisService.zrevrangeByScore(Constant.LOGIN_RANK_KEY.concat("_").concat(deptId), 0, Double.MAX_VALUE);
        int totalRank = 1, departmentRank = 1;
        for (Object obj : setDepartment) {
            if (userId.equals(obj)) {
                break;
            }
            departmentRank += 1;
        }
        for (Object obj : setTotal) {
            if (userId.equals(obj)) {
                break;
            }
            totalRank += 1;
        }
        result.put("totalRank", totalRank);
        result.put("departmentRank", departmentRank);
        return result;
    }

    /**
     * 数据维护
     *
     * @param userId
     * @param deptId
     * @return
     */
    private JSONObject dataUpdate(String userId, String deptId) {
        JSONObject result = new JSONObject();
        result.put("description", "数据维护");
        Set<Object> setTotal = redisService.zrevrangeByScore(Constant.DATA_UPDATE_KEY, 0, 1d);
        Set<Object> setDepartment = redisService.zrevrangeByScore(Constant.DATA_UPDATE_KEY.concat("_").concat(deptId), 0, 1d);
        if (setTotal.contains(userId)) {
            int totalRank = 1;
            for (Object obj : setTotal) {
                if (userId.equals(obj)) {
                    break;
                }
                totalRank += 1;
            }
            result.put("totalRank", totalRank);
        } else {
            result.put("totalRank", Constant.NO_DATA_STRING);
        }
        if (setDepartment.contains(userId)) {
            int departmentRank = 1;
            for (Object obj : setDepartment) {
                if (userId.equals(obj)) {
                    break;
                }
                departmentRank += 1;
            }
            result.put("departmentRank", departmentRank);
        } else {
            result.put("departmentRank", Constant.NO_DATA_STRING);
        }
        Double zscore = redisService.zscore(Constant.DATA_UPDATE_KEY, userId);
        result.put("rate", zscore == null ? 1d : zscore.doubleValue());
        return result;
    }

}
