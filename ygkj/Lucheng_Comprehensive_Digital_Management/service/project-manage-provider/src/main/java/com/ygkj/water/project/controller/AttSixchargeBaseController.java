package com.ygkj.water.project.controller;

import com.alibaba.fastjson.JSONObject;
import com.ygkj.project.AttSixchargeBaseControllerApi;
import com.ygkj.project.model.AttSixchargeBase;
import com.ygkj.project.model.AttSixchargeChangeRecord;
import com.ygkj.project.vo.request.AttSixchargeBaseReqVo;
import com.ygkj.project.vo.response.AttSixchargeBaseRespVo;
import com.ygkj.utils.StringUtils;
import com.ygkj.vo.PageVO;
import com.ygkj.water.model.CommonResult;
import com.ygkj.water.project.service.AttSixchargeService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/threepeople")
public class AttSixchargeBaseController implements AttSixchargeBaseControllerApi {
    @Autowired
    private AttSixchargeService threePeopleService;


    private static final String SPLIT = "-";

    @PostMapping ("/saveOrUpdate")
    @Override
    public CommonResult saveOrUpdate(AttSixchargeBase entity, HttpServletRequest request) {
        try{
            if(entity.getId() == null){
                threePeopleService.save(entity);
                return CommonResult.success();
            }else {
                threePeopleService.update(entity);
                return CommonResult.success();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return CommonResult.failed("新增更新失败");
    }

    @PostMapping ("/queryPage")
    @Override
    public CommonResult queryPage(AttSixchargeBaseReqVo vo) {
        try{
            return CommonResult.success(threePeopleService.queryPage(vo));
        }catch (Exception e){
            e.printStackTrace();
        }
        return CommonResult.failed("");
    }

    @PostMapping ("/queryOne")
    @Override
    public CommonResult queryOne(AttSixchargeBaseReqVo vo) {
        try{
            return CommonResult.success(threePeopleService.queryOne(vo));
        }catch (Exception e){
            e.printStackTrace();
        }
        return CommonResult.failed("");
    }

    @Override
    @PostMapping ("/queryChangeRecord")
    public CommonResult queryChangeRecord(AttSixchargeChangeRecord record) {
        try{
            return CommonResult.success(threePeopleService.queryChangeRecord(record));
        }catch (Exception e){
            e.printStackTrace();
        }
        return CommonResult.failed("");
    }

    @GetMapping("/threeChargeTree")
    @ApiOperation("三个责任人树，feign调用")
    List<JSONObject> threeChargeTree(@RequestParam(required = false) String projectName){

        AttSixchargeBaseReqVo reqVo = new AttSixchargeBaseReqVo();
        reqVo.setProjectName(projectName);
        PageVO<AttSixchargeBaseRespVo> pageVO =  threePeopleService.queryPage(reqVo);
        List<AttSixchargeBaseRespVo> list = pageVO.getList();
//        List<JSONObject> result = new ArrayList<>();
        List<JSONObject> chargeList = new ArrayList<>();
        list.forEach( l -> {
//            JSONObject json = new JSONObject();
//            json.put("projectName",l.getProjectName());

            if (StringUtils.isNotBlank(l.getManageDepartmentChargeName())){
                JSONObject charge1 = new JSONObject();
                charge1.put("name",l.getManageDepartmentChargeName() + SPLIT + l.getTownAdnm() + SPLIT + l.getProjectName());
//                charge1.put("value1","管理单位责任人");
                charge1.put("phone",l.getManageDepartmentChargePhone());
                //设置不重复的id 给前端做判断使用
                charge1.put("id", "three" + l.getId() + "1");
//                charge1.put("value2",l.getProjectName());
//                charge1.put("value3",l.getTownAdnm());
                chargeList.add(charge1);
            }

            if (StringUtils.isNotBlank(l.getTechnologyChargeName())){
                JSONObject charge2 = new JSONObject();
                charge2.put("name",l.getTechnologyChargeName() + SPLIT + l.getTownAdnm() + SPLIT + l.getProjectName());
//                charge2.put("value1","技术责任人");
                charge2.put("phone",l.getTechnologyChargePhone());
                charge2.put("id", "three" + l.getId() + "2");
//                charge2.put("value2",l.getProjectName());
//                charge2.put("value3",l.getTownAdnm());
                chargeList.add(charge2);
            }

            if (StringUtils.isNotBlank(l.getPatrolChargeName())){
                JSONObject charge3 = new JSONObject();
                charge3.put("name",l.getPatrolChargeName() + SPLIT + l.getTownAdnm() + SPLIT + l.getProjectName());
//                charge3.put("value1","巡查责任人");
                charge3.put("phone",l.getPatrolChargePhone());
                charge3.put("id", "three" + l.getId() + "3");
//                charge3.put("value2",l.getProjectName());
//                charge3.put("value3",l.getTownAdnm());
                chargeList.add(charge3);
            }
//            json.put("charge",chargeList);
//            result.add(json);
        });
        return chargeList;
    }


}

