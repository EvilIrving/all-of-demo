package com.ygkj.lcsyn.task;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.ygkj.lcsyn.LcSynApplication;
import com.ygkj.lcsyn.config.DingDingConfig;
import com.ygkj.lcsyn.enums.ProvinceDataEnum;
import com.ygkj.lcsyn.enums.SysUserEnum;
import com.ygkj.lcsyn.mapper.SynZzdDeptMapper;
import com.ygkj.lcsyn.mapper.SynZzdUserMapper;
import com.ygkj.lcsyn.model.ModifyInfo;
import com.ygkj.lcsyn.model.SynZzdDept;
import com.ygkj.lcsyn.model.SynZzdUser;
import com.ygkj.lcsyn.service.DingService;
import com.ygkj.lcsyn.utils.EncryUtil;
import com.ygkj.lcsyn.utils.HttpClientUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author lxl
 * @create 2022-05-26 10:17
 * @description
 */
@Slf4j
@Component
@RunWith(SpringRunner.class)
@ActiveProfiles("dev")
@SpringBootTest(classes = LcSynApplication.class)
public class SynUserTask {

    @Resource
    private DingService dingService;

    @Resource
    private SynZzdDeptMapper synZzdDeptMapper;

    @Resource
    private SynZzdUserMapper synZzdUserMapper;



    @Scheduled(cron = " 0 0 1 * * ?")
    @Test
    public void getOrgAuth() {

        Map<String, Object> map = new HashMap<>();

        //获取授权部门
        JSONObject res = dingService.post(ProvinceDataEnum.ORG_SCOPE, map);
        log.info(res.toJSONString());
        JSONObject message = res.getJSONObject("message");
        JSONObject authOrgScopes = message.getJSONObject("authOrgScopes");
        JSONArray authedDept = authOrgScopes.getJSONArray("authedDept");
        List<String> deptIds = authedDept.toJavaList(String.class);
        Set<String> subDeptId = getSubDeptId(deptIds);

        List<String> list1 = new ArrayList<>(subDeptId);
        //查询授权部门进行更新插入
        getDeptInfo(list1);

    }


    public Set<String> getSubDeptId(List<String> pIds){

        Set<String> resSet = new HashSet<>();
        for (String pId : pIds) {
            Map<String, Object> map = new HashMap<>();
            map.put("parentId",pId);
            JSONObject res = dingService.post(ProvinceDataEnum.DEPT_LIST, map);
            if (res.getInteger("status") == 0){
                JSONArray message = res.getJSONArray("message");
                List<String> list = message.toJavaList(String.class);
                list.add(pId);
                resSet.addAll(list);
            }
        }
        return resSet;
    }



    public void getDeptInfo(List<String> deptIds) {

        int add = 0, update = 0;
        for (String deptId : deptIds) {
            Map<String, Object> map = new HashMap<>();
            map.put("deptId",deptId);
            //获取授权部门
            try {
                JSONObject deptInfoJson = dingService.post(ProvinceDataEnum.DEPT_INFO, map);
                if (deptInfoJson.getInteger("status") == 0){
                    JSONObject message = deptInfoJson.getJSONObject("message");

                    SynZzdDept synZzdDept = message.toJavaObject(SynZzdDept.class);
                    // 查询是否存在判断是更新或者新增
                    if (StringUtils.isBlank(synZzdDeptMapper.selectSysDept(synZzdDept.getOrganizationId()))){
                        int insert = synZzdDeptMapper.insertSysDept(synZzdDept);
                        add += insert;
                    }else {
                        int up = synZzdDeptMapper.updateSysDept(synZzdDept);
                        update += up;
                    }
                }
            }catch (Exception e){

                log.error("同步 {} 部门发生异常： {}",deptId ,e.getMessage());
            }

            //更新用户
            getUserInfo(deptId);

        }
        log.info("本次新增部门 {} 个，更新部门 {} 个",add,update);
    }

//    @Test
    public void getUserInfo(String dept){

//        String dept = "67994241";
        Map<String, Object> map = new HashMap<>();
        map.put("deptId",dept);
        List<SynZzdUser> synZzdUsers;
        //获取授权部门
        try {
            JSONObject res = dingService.post(ProvinceDataEnum.DEPT_USER, map);
            JSONArray message = res.getJSONArray("message");
            synZzdUsers = message.toJavaList(SynZzdUser.class);
            int i = 0, u = 0;
            Integer deleteNum = 0;

            //进行删除！如果不存在该部门下的用户进行删除。他们的人员更新接口有问题无法正确进行人员变更删除
            String ids = synZzdUsers.stream().map(s -> s.getUserCode()).collect(Collectors.joining(","));
            if (StringUtils.isNotBlank(ids)){
                deleteNum = synZzdUserMapper.deletedUserByIds(dept, ids);
            }

            for (SynZzdUser synZzdUser : synZzdUsers) {
                //手机号码解密
                if (StringUtils.isNotBlank(synZzdUser.getMobile())){
                    String phone = aesDecrypt(synZzdUser.getMobile(), DingDingConfig.APP_SECRET);
                    synZzdUser.setMobile(phone);
                }
                //email解密
                if (StringUtils.isNotBlank(synZzdUser.getMail())){
                    synZzdUser.setMail(aesDecrypt(synZzdUser.getMail(), DingDingConfig.APP_SECRET));
                }
                synZzdUser.setDept(dept);
                if (StringUtils.isBlank(synZzdUserMapper.findByUserCode(synZzdUser.getUserCode()))){
                    synZzdUser.setSalt(SysUserEnum.SALT.getValue());
                    synZzdUser.setPassword(SysUserEnum.PASSWORD.getValue());
                    synZzdUser.setUserType(SysUserEnum.USER_TYPE.getValue());
                    int insert = synZzdUserMapper.insertSysUser(synZzdUser);
                    i += insert;
                }else {
                    int up = synZzdUserMapper.updateSysUser(synZzdUser);
                    u += up;
                }
            }
            log.info("同步了 {} 部门用户， 更新 {} 条记录， 新增 {} 条记录 ，删除 {} 条记录",dept,u,i,deleteNum);
        }catch (Exception e){
            log.error("同步 {} 部门用户发生异常： {}", dept ,e.getMessage());
        }
    }

    @Test
    public void main() {

        getUserInfo("67004483");

    }



    /**
     * 获取期限内 失效用户 code
     * @throws Exception
     */
    @Test
//    @Scheduled(cron = "0 30 9 * * ?")
    public void synModifyInfoByTime() throws Exception {

        Map<String, Object> param = new HashMap<>();

        Integer pageSize = 100;
        Integer pageNumber = 1;
        Calendar calendar = Calendar.getInstance();
        calendar.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String endTime = sdf.format(calendar.getTime());
        calendar.add(Calendar.DAY_OF_MONTH,-100);
        String startTime = sdf.format(calendar.getTime());

        param.put("startTime",startTime);
        param.put("endTime",endTime);
        param.put("pageSize",pageSize);
        param.put("pageNumber",pageNumber);
        //queryType 传 1 为查询失效用户， queryType 不传为查询新增或者变更用户
        param.put("queryType","1");

        JSONObject message = dingService.post(ProvinceDataEnum.CHANGE_USER, param).getJSONObject("message");
        JSONArray rows = message.getJSONArray("rows");
        Integer total = (Integer) message.get("total");
        List<ModifyInfo> modifyInfos = rows.toJavaList(ModifyInfo.class);

        while (total >= pageSize * pageNumber){

            pageNumber ++;
            param.put("pageNumber",pageNumber);
            JSONObject message2 = dingService.post(ProvinceDataEnum.CHANGE_USER, param).getJSONObject("message");
            JSONArray rows2 = message2.getJSONArray("rows");
            List<ModifyInfo> modifyInfos2 = rows2.toJavaList(ModifyInfo.class);
            modifyInfos.addAll(modifyInfos2);
        }
        Integer flag = 0;
        for (ModifyInfo modifyInfo : modifyInfos) {
            Integer integer = synZzdUserMapper.deleteByUserCode(modifyInfo.getUserCode());
            flag += integer;
        }
        log.info("查询时间内失效数据量为：" + total);
        log.info("本次失效数据更新量为：" + flag);
    }


    @Test
//    @Scheduled(cron = "0 30 9 * * ?")
    public void synModifyDeptInfoByTime() throws Exception {

        Map<String, Object> param = new HashMap<>();

        Integer pageSize = 100;
        Integer pageNumber = 1;
        Calendar calendar = Calendar.getInstance();
        calendar.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String endTime = sdf.format(calendar.getTime());
        calendar.add(Calendar.DAY_OF_MONTH,-100);
        String startTime = sdf.format(calendar.getTime());

        param.put("startTime",startTime);
        param.put("endTime",endTime);
        param.put("pageSize",pageSize);
        param.put("pageNumber",pageNumber);
        //queryType 传 1 为查询失效记录， queryType 不传为查询新增或者变更
        param.put("queryType","1");

        JSONObject message = dingService.post(ProvinceDataEnum.CHANGE_DEPT, param).getJSONObject("message");

        JSONArray rows = message.getJSONArray("rows");
        Integer total = message.getInteger("total");
        List<ModifyInfo> modifyInfos = rows.toJavaList(ModifyInfo.class);
//
        while (total >= pageSize * pageNumber){

            pageNumber ++;
            param.put("pageNumber",pageNumber);
            JSONObject message2 = dingService.post(ProvinceDataEnum.CHANGE_DEPT, param).getJSONObject("message");
            JSONArray rows2 = message2.getJSONArray("rows");
            List<ModifyInfo> modifyInfos2 = rows2.toJavaList(ModifyInfo.class);

            modifyInfos.addAll(modifyInfos2);
        }
        Integer flag = 0;
        for (ModifyInfo modifyInfo : modifyInfos) {

            Integer integer = synZzdDeptMapper.deleteDeptWaterByCode(modifyInfo.getOrganizationId());
            flag += integer;
        }
        log.info("查询时间内失效数据量为：" + total);
        log.info("本次失效数据更新量为：" + flag);
    }






    private String aesDecrypt(String cliperText, String secretKey) throws Exception {

        byte[] raw = secretKey.getBytes();
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        //Cipher 对象实际完成加密操作
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, skeySpec);
        // 先用 base64 解码
        byte[] encrypted1 = Base64.getDecoder().decode(cliperText);
        byte[] original = cipher.doFinal(encrypted1);
        String originalString = new String(original, "UTF-8");
        return originalString;

    }


}
