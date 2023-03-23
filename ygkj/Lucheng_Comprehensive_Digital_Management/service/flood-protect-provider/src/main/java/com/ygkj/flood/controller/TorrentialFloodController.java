package com.ygkj.flood.controller;

import com.ygkj.flood.TorrentialFloodControllerApi;
import com.ygkj.flood.service.TorrentialFloodService;
import com.ygkj.flood.vo.request.AttPrevTfBaseVo;
import com.ygkj.flood.vo.request.PrevTfInfoReqVo;
import com.ygkj.flood.vo.response.PvPchargeResVo;
import com.ygkj.utils.StringUtils;
import com.ygkj.vo.PageVO;
import com.ygkj.water.model.CommonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 * @author xq
 * @Description
 * @Date 2021/6/8
 */
@RestController
@RequestMapping("torrentialFlood")
public class TorrentialFloodController implements TorrentialFloodControllerApi {

    @Autowired
    TorrentialFloodService torrentialFloodService;

//    @Autowired




    @GetMapping("listPrevInfo")
    @Override
    public CommonResult listPrevInfo() {
        return CommonResult.success(torrentialFloodService.listPrevInfo());
    }

    @GetMapping("listPrevTfInfo")
    @Override
    public CommonResult listPrevTfInfo(PrevTfInfoReqVo reqVo) {
        return CommonResult.success(torrentialFloodService.listPrevTfInfo(reqVo));
    }


    @Override
    @PostMapping("/listPrevResponsibilities")
    public CommonResult listPrevTfResponsibilities(PrevTfInfoReqVo reqVo) {
        return CommonResult.success(torrentialFloodService.listPrevTfResponsibilities(reqVo));
    }

    @Override
    @PostMapping("/sendTfMsg")
    public CommonResult sendTfMsg(@RequestBody PrevTfInfoReqVo reqVo) {
        torrentialFloodService.sendTfMsg(reqVo);
        return CommonResult.success();
    }


    @Override
    @PostMapping("/pvPchargeList")
    public CommonResult pvPchargeList(PrevTfInfoReqVo reqVo) {

        PageVO<PvPchargeResVo> list = torrentialFloodService.pvPchargeList(reqVo);
        return CommonResult.success(list);
    }


    @Override
    @PostMapping("/pchargeSaveOrUpdate")
    public CommonResult pchargeSaveOrUpdate(PvPchargeResVo resVo) {

        if (StringUtils.isNotBlank(resVo.getPchargeCode())){
            if (torrentialFloodService.updatePcharge(resVo)){
                return CommonResult.success("操作成功");
            }else {
                return CommonResult.failed("操作失败");
            }
        }else {
            if (torrentialFloodService.insertPcharge(resVo)){
                return CommonResult.success("操作成功");
            }else {
                return CommonResult.failed("操作失败");
            }
        }
    }

    @Override
    @GetMapping("/pchargeDelete")
    public CommonResult pchargeDelete(String code) {

        if (torrentialFloodService.deletePcharge(code)){
            return CommonResult.success("操作成功");
        }else {
            return CommonResult.failed("操作失败");
        }
    }


    @Override
    @GetMapping("/listPrevObj")
    public CommonResult listPrevObj(AttPrevTfBaseVo vo) {
        return CommonResult.success(torrentialFloodService.listPrevObj(vo));
    }

    @Override
    @GetMapping("/prevObjCount")
    public CommonResult prevObjCount(AttPrevTfBaseVo vo) {
        return CommonResult.success(torrentialFloodService.listPrevObj(vo).size());
    }


    @Override
    @PostMapping("/affectPopulationList")
    public CommonResult bigScreenAffectPopulationList(PrevTfInfoReqVo reqVo) {
        return CommonResult.success(torrentialFloodService.bigScreenAffectPopulationList(reqVo));
    }
}
