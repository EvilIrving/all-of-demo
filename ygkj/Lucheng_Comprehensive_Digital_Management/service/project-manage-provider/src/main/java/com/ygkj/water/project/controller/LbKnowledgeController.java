package com.ygkj.water.project.controller;

import com.ygkj.project.LbKnowledgeControllerApi;
import com.ygkj.project.model.LbKnowledge;
import com.ygkj.project.vo.request.LbKnowledgeReqVo;
import com.ygkj.water.model.CommonResult;
import com.ygkj.water.project.service.LbKnowledgeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author pqf
 * @DESCRIPTION:
 * @create 2021-11-12 15:24
 */
@RestController
@RequestMapping("/lbKnowledge")
public class LbKnowledgeController implements LbKnowledgeControllerApi {
    @Autowired
    private LbKnowledgeService lbKnowledgeService;

    @PutMapping("saveOrUpdate")
    @Override
    public CommonResult saveOrUpdate(LbKnowledge vo) {
        if (vo.getId() ==  null) {
            lbKnowledgeService.insert(vo);
        } else {
            lbKnowledgeService.update(vo);
        }
        return CommonResult.success("操作成功");
    }

    @DeleteMapping("del")
    @Override
    public CommonResult del(Integer id) {
        boolean flag = lbKnowledgeService.removeById(id);
        if (flag) {
            return CommonResult.success("删除成功");
        }
        return CommonResult.failed("删除失败");
    }

    @PostMapping("loadPage")
    @Override
    public CommonResult loadPage(LbKnowledgeReqVo vo) {
        return CommonResult.success(lbKnowledgeService.loadPage(vo));
    }
}
