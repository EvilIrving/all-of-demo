package com.ygkj.big.screen.controller;

import com.ygkj.big.screen.WorkingConditionsControllerApi;
import com.ygkj.big.screen.service.WorkingConditionsService;
import com.ygkj.water.model.CommonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

/**
 * @author pqf
 * @DESCRIPTION:
 * @create 2021-10-18 15:35
 */
@RestController
@RequestMapping("/workingConditions")
public class WorkingConditionsController implements WorkingConditionsControllerApi {

    @Autowired
    private WorkingConditionsService workingConditionsService;

    @GetMapping("/workingConditionsTotal")
    @Override
    public CommonResult workingConditionsTotal() {
        return  CommonResult.success(this.workingConditionsService.workingConditionsTotal());
    }

    @PostMapping("/workingConditionsList")
    @Override
    public CommonResult workingConditionsList(String code,String eng_scal, String pjnm,String pjtp,String openStatus,Integer pageNum,Integer pageSize) {
        return this.workingConditionsService.workingConditionsList(code,eng_scal,pjnm,pjtp,openStatus,pageNum,pageSize);
    }

    @Override
    public void exportProject(HttpServletResponse httpServletResponse) {

    }

}
