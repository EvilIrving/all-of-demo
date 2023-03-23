package com.dcy.datamanage.biz.controller;


import com.dcy.datamanage.api.model.common.R;
import com.dcy.datamanage.api.model.BusDataSetParam;
import com.dcy.datamanage.biz.dsm.DataSourceRegistry;
import com.dcy.datamanage.biz.dsm.DefaultDataSource;
import com.dcy.datamanage.biz.dto.input.TestInputDTO;
import com.dcy.datamanage.biz.task.DataAssessTask;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("test")
@Api(tags = "转发测试")
public class TestController {

    @Autowired
    private DataAssessTask dataAssessTask;

    @Autowired
    private DataSourceRegistry dataSourceRegistry;

    @GetMapping("testGetParam")
    public R<String> testGetParam(String id){
        return R.success(id);
    }

    @GetMapping("testGetPath/{id}")
    public R<String> testGetPath(@PathVariable("id") String id){
        return R.success(id);
    }

    @PostMapping("testPostJson")
    public R<TestInputDTO> testPostJson(@RequestBody TestInputDTO dto){
        return R.success(dto);
    }

    @PostMapping("testPostJson2")
    public R<List<TestInputDTO>> testPostJson2(@RequestBody TestInputDTO dto){
        List<TestInputDTO> list = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            list.add(dto);
        }
        return R.success(list);
    }

    @PostMapping("testPostJson3")
    public R<List<String>> testPostJson3(){
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            list.add(i+"");
        }
        return R.success(list);
    }

    @PostMapping("testPostForm")
    public R<TestInputDTO> testPostForm(TestInputDTO dto){
        if(CollectionUtils.isEmpty(dto.getParamList())){
            List<BusDataSetParam> list = new ArrayList<>();
            for (int i = 0; i < 3; i++) {
                BusDataSetParam busDataSetParam = new BusDataSetParam();
                busDataSetParam.setId(""+i);
                busDataSetParam.setParamName("name"+i);
                list.add(busDataSetParam);
            }
            dto.setParamList(list);
        }
        return R.success(dto);
    }

    @GetMapping("calculateScore")
    public R<Boolean> calculateScore(int frequencyUnit){
        dataAssessTask.calculateScore(frequencyUnit);
        return R.success(true);
    }

    @GetMapping("testIn")
    public R<Boolean> testIn(long num){
        DefaultDataSource dataSource = dataSourceRegistry.getDataSource("111");
        return R.success(true);
    }
}
