package com.ygkj.water.project.controller;

import com.ygkj.project.WagaControllerApi;
import com.ygkj.project.vo.request.WrpWagaQueryReqVo;
import com.ygkj.water.project.service.WagaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

/**
 * @author xq
 * @Description
 * @Date 2021/9/14
 */
@RestController
@RequestMapping("waga")
public class WagaController implements WagaControllerApi {

    @Autowired
    WagaService wagaService;

    @PostMapping("exportWrpWagaUnitTable")
    @Override
    public void exportWrpWagaUnitTable(@RequestBody WrpWagaQueryReqVo reqVo, HttpServletResponse response) {
        wagaService.exportWrpWagaUnitTable(reqVo,response);
    }
}
