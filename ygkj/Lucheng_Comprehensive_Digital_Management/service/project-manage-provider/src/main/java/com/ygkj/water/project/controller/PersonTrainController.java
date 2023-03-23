package com.ygkj.water.project.controller;

import com.ygkj.project.PersonTrainControllerApi;
import com.ygkj.project.vo.request.BhDepPersontrainReqVo;
import com.ygkj.utils.StringUtils;
import com.ygkj.water.model.CommonResult;
import com.ygkj.water.project.service.BhDepPersontrainService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/persontrain")
public class PersonTrainController implements PersonTrainControllerApi {
    @Resource
    private BhDepPersontrainService persontrainService;

    @Override
    @PostMapping("/saveOrUpdatePersonTrain")
    public CommonResult saveOrUpdatePersonTrain(BhDepPersontrainReqVo vo) {
        try{
            if (StringUtils.isBlank(vo.getId())){
                persontrainService.insert(vo);
            }else {
                persontrainService.update(vo);
            }
        }catch (Exception e){
            return CommonResult.failed(e.getMessage());
        }
        return CommonResult.success();
    }

    @Override
    @PostMapping("/loadPagePersonTrain")
    public CommonResult loadPagePersonTrain(BhDepPersontrainReqVo vo) {
        try{
            return CommonResult.success(persontrainService.load(vo));
        }catch (Exception e){
            e.printStackTrace();
        }
        return CommonResult.failed();
    }

    @Override
    @PostMapping("/delPersonTrain")
    public CommonResult deletePersonTrain(String id) {
        try{
            if (StringUtils.isBlank(id)){
                throw new RuntimeException("参数缺失");
            }
            persontrainService.delete(id);

        }catch (Exception e){
            return CommonResult.failed(e.getMessage());
        }
        return CommonResult.success();
    }
}
