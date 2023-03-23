package com.ygkj.water.project.controller;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.metadata.fill.FillConfig;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.ygkj.project.model.EvaluationItemData;
import com.ygkj.project.model.EvaluationItemTemplate;
import com.ygkj.project.model.RegularReview;
import com.ygkj.project.vo.request.EvaluationItemDataVo;
import com.ygkj.project.vo.request.RegularReviewVo;
import com.ygkj.utils.StringUtils;
import com.ygkj.vo.PageVO;
import com.ygkj.water.model.CommonResult;
import com.ygkj.water.project.service.EvaluationItemDataService;
import com.ygkj.water.project.service.EvaluationItemTemplateService;
import com.ygkj.water.project.service.RegularReviewService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author wgf
 * @date 2022/4/25 10:07
 * @Description: 定期考评
 */

@Api(tags = "定期考评")
@RestController
@RequestMapping("/regularReview")
public class RegularReviewController {

    @Autowired
    private RegularReviewService regularReviewService;
    @Autowired
    private EvaluationItemTemplateService evaluationItemTemplateService;
    @Autowired
    private EvaluationItemDataService evaluationItemDataService;

    @ApiOperation(value = "定期考评新增或修改")
    @PostMapping("/saveOrUpdate")
    @Transactional(rollbackFor = Exception.class)
    public CommonResult saveOrUpdate(@RequestBody RegularReviewVo regularReviewVo) {
        String id = regularReviewVo.getId();
        RegularReview regularReview = new RegularReview();
        BeanUtils.copyProperties(regularReviewVo, regularReview);
        if (StringUtils.isNotBlank(id)) {
            regularReviewService.updateById(regularReview);
            ArrayList<EvaluationItemData> arrayList = new ArrayList<>();
            List<EvaluationItemDataVo> list = regularReviewVo.getList();
            for (int i = 0; i < list.size(); i++) {
                EvaluationItemDataVo evaluationItemDataVo = list.get(i);
                EvaluationItemData evaluationItemData = new EvaluationItemData();
                evaluationItemData.setId(evaluationItemDataVo.getKpId());
                evaluationItemData.setReviewId(id);
                evaluationItemData.setTemplateId(evaluationItemDataVo.getId());
                evaluationItemData.setMarkPrinciple(evaluationItemDataVo.getMarkPrinciple());
                evaluationItemData.setScore(evaluationItemDataVo.getScore());
                arrayList.add(evaluationItemData);
            }
            evaluationItemDataService.updateBatchById(arrayList);
            return CommonResult.success();
        } else {
            String uuid = IdWorker.get32UUID();
            regularReview.setId(uuid);
            regularReviewService.save(regularReview);
            ArrayList<EvaluationItemData> arrayList = new ArrayList<>();
            List<EvaluationItemDataVo> list = regularReviewVo.getList();
            for (int i = 0; i < list.size(); i++) {
                EvaluationItemDataVo evaluationItemDataVo = list.get(i);
                EvaluationItemData evaluationItemData = new EvaluationItemData();
                evaluationItemData.setId(IdWorker.get32UUID());
                evaluationItemData.setMarkPrinciple(evaluationItemDataVo.getMarkPrinciple());
                evaluationItemData.setReviewId(uuid);
                evaluationItemData.setScore(evaluationItemDataVo.getScore());
                evaluationItemData.setTemplateId(evaluationItemDataVo.getId());
                arrayList.add(evaluationItemData);
            }
            evaluationItemDataService.saveBatch(arrayList);
            return CommonResult.success();
        }
    }

    @ApiOperation(value = "定期考评列表")
    @PostMapping("/list")
    public CommonResult list(RegularReview regularReview) {
        PageVO<RegularReview> pageVO =  regularReviewService.getList(regularReview);
        return CommonResult.success(pageVO);
    }

    @ApiOperation(value = "定期考评数据详情")
    @GetMapping("/getById")
    public CommonResult getById(String id) {
        RegularReview review = regularReviewService.getById(id);
        List<EvaluationItemDataVo> edListById = regularReviewService.getEdListById(id);
        RegularReviewVo regularReviewVo = new RegularReviewVo();
        BeanUtils.copyProperties(review, regularReviewVo);
        regularReviewVo.setList(edListById);
        return CommonResult.success(regularReviewVo);
    }

    @ApiOperation(value = "定期考评删除")
    @GetMapping("/del")
    @Transactional(rollbackFor = Exception.class)
    public CommonResult del(RegularReview regularReview) {
        regularReview.setDelFlag(1);
        if (regularReviewService.updateById(regularReview)) {
            UpdateWrapper<EvaluationItemData> updateWrapper = new UpdateWrapper<>();
            updateWrapper.set("del_flag", 1);
            updateWrapper.eq(StringUtils.isNotBlank(regularReview.getId()), "review_id", regularReview.getId());
            evaluationItemDataService.update(updateWrapper);
            return CommonResult.success();
        } else {
            return CommonResult.failed("刪除失敗");
        }
    }

    @ApiOperation(value = "查询模板")
    @PostMapping("/mbList")
    public CommonResult mbList() {
        QueryWrapper<EvaluationItemTemplate> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByAsc("sort");
        List<EvaluationItemTemplate> list = evaluationItemTemplateService.list(queryWrapper);
        return CommonResult.success(list);
    }

    @ApiOperation(value = "定期考评单条数据的导出")
    @GetMapping("/getByIdToExcel")
    public void getByIdToExcel(String id, HttpServletResponse response) {
        RegularReview review = regularReviewService.getById(id);
        List<EvaluationItemDataVo> list = regularReviewService.getEdListById(id);
        try {
            response.setContentType("application/octet-stream");
            String fileName = URLEncoder.encode("鹿城区海塘物业化管理日常检查、定期考评", "UTF-8");
            response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
            response.setHeader("Access-Control-Expose-Headers", "Content-disposition");
            InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("office/鹿城区海塘物业化管理日常检查、定期考评.xlsx");
            ServletOutputStream out = response.getOutputStream();
            ExcelWriter excelWriter = EasyExcel.write(out).withTemplate(inputStream).build();
            WriteSheet writeSheet = EasyExcel.writerSheet().build();
            FillConfig fillConfig = FillConfig.builder().forceNewRow(Boolean.TRUE).build();
            excelWriter.fill(list, fillConfig, writeSheet);
            Map<String, Object> map = new HashMap<>();
            map.put("synthesisScore", review.getSynthesisScore());
            excelWriter.fill(map, writeSheet);
            excelWriter.finish();
            Workbook workbook = new XSSFWorkbook();
            response.flushBuffer();
            excelWriter.writeContext().writeWorkbookHolder().getOutputStream();
            workbook.write(excelWriter.writeContext().writeWorkbookHolder().getOutputStream());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @ApiOperation(value = "年度考评统计的列表")
    @PostMapping("/yearList")
    public CommonResult yearList(RegularReview regularReview) {
        PageVO<RegularReview> pageVO =  regularReviewService.yearList(regularReview);
        return CommonResult.success(pageVO);
    }
}
