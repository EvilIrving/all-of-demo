package com.ygkj.gragh.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.ygkj.gragh.feign.UserFeignClient;
import com.ygkj.gragh.mapper.AttRiverBaseMapper;
import com.ygkj.gragh.model.AttRiverBase;
import com.ygkj.gragh.model.SysUser;
import com.ygkj.gragh.service.RiverService;
import com.ygkj.gragh.vo.request.RiverQueryVo;
import com.ygkj.utils.PageUtils;
import com.ygkj.vo.PageVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class RiverServiceImpl implements RiverService {

    @Autowired
    private AttRiverBaseMapper riverBaseMapper;
    @Autowired
    private UserFeignClient userFeignClient;

    @Override
    public PageVO<AttRiverBase> rivers(RiverQueryVo riverQueryVo) {
        PageHelper.startPage(riverQueryVo.getPageNum(), riverQueryVo.getPageSize());
        List<AttRiverBase> attRiverBases = riverBaseMapper.selectByCondition(riverQueryVo);
        if (StringUtils.isNotBlank(riverQueryVo.getId()) && attRiverBases.size() == 1) {
            //单条记录查询，需要补全责任人信息
            List<String> list = new ArrayList<>();
            AttRiverBase river = attRiverBases.get(0);
            try {
                String govermentChargeId = river.getGovermentChargeId();
                list.add(govermentChargeId);
                String waterDepartmentChargeId = river.getWaterDepartmentChargeId();
                list.add(waterDepartmentChargeId);
                String competentDepartmentChargeId = river.getCompetentDepartmentChargeId();
                list.add(competentDepartmentChargeId);
                String manageDepartmentChargeId = river.getManageDepartmentChargeId();
                list.add(manageDepartmentChargeId);
                //筛一下，不可以为空
                list = list.stream().filter(s -> StringUtils.isNotBlank(s) && !s.equals("null")).collect(Collectors.toList());
                String userCodes = String.join(",", list);
                JSONArray charges = new JSONArray();
                //判断一下，避免无谓的IO
                if (StringUtils.isNotBlank(userCodes)) {
                    List<SysUser> users = userFeignClient.users(userCodes);
                    for (SysUser user : users) {
                        String id = user.getId();
                        JSONObject jsonObject = (JSONObject) JSONObject.toJSON(user);
                        if (id.equals(govermentChargeId)) {
                            jsonObject.put("type", "goverment");
                            jsonObject.put("description", "政府负责人");
                        } else if (id.equals(waterDepartmentChargeId)) {
                            jsonObject.put("type", "waterDepartment");
                            jsonObject.put("description", "水行政部门负责人");
                        } else if (id.equals(competentDepartmentChargeId)) {
                            jsonObject.put("type", "competentDepartment");
                            jsonObject.put("description", "主管部门负责人");
                        } else if (id.equals(manageDepartmentChargeId)) {
                            jsonObject.put("type", "manageDepartment");
                            jsonObject.put("description", "管理单位责任人");
                        }
                        charges.add(jsonObject);
                    }
                    river.setCharges(charges);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return PageUtils.getPageVO(attRiverBases);
    }

//    @Override
//    public Map<String, Object> rivers2(RiverQueryVo riverQueryVo) {
//        List<AttRiverBase> rivers = rivers(riverQueryVo);
//        int P = riverQueryVo.getP();
//        int G = riverQueryVo.getG();
//        int num = riverQueryVo.getNum();
//        //生成一个随机数
//        int B = (int) (Math.random() * 6 + 1);
//        System.out.println(B);
//        long num1 = AesUtil.getNum(G, P, B);
//        String key = AesUtil.getKey(P, num, B);
//        System.out.println(key);
//        rivers.forEach(attRiverBase -> {
//            String geom = attRiverBase.getGeom();
//            if (!StringUtils.isEmpty(geom)) {
//                try {
//                    //先解密数据库
//                    String decrypt = AesUtil.decrypt(attRiverBase.getGeom());
//                    //根据新的key重新加密
//                    decrypt = AesUtil.encrypt(decrypt, key);
//                    attRiverBase.setGeom(decrypt);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//        Map<String, Object> result = new HashMap<>();
//        result.put("num", num1);
//        result.put("rows", rivers);
//        return result;
//    }

    /**
     * 统计温州市每年的水域面积
     *
     * @return
     */
    @Override
    public List<JSONObject> countYearlyArea() {
        List<AttRiverBase> rivers = riverBaseMapper.selectByCondition(new RiverQueryVo());
        Map<String, List<AttRiverBase>> map = rivers.stream().collect(Collectors.groupingBy(AttRiverBase::getCxsd));
        List<JSONObject> result = new ArrayList<>();
        map.forEach((year, list) -> {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(year, list.stream().mapToDouble(river -> river.getRiverArea().doubleValue()).sum());
            result.add(jsonObject);
        });
        return result;
    }


}
