package com.ygkj.gragh.feign;

import com.ygkj.project.model.SectionStorm;
import com.ygkj.project.vo.request.RsvrStationWaterReqVo;
import com.ygkj.vo.PageVO;
import com.ygkj.water.model.CommonResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author xq
 * @Description
 * @Date 2021/6/30
 */
@FeignClient(contextId = "project-manage", name = "project-manage")
public interface ProjectManageClient {
    /**
     * 水库、测站、水位查询
     *
     * @param reqVo
     * @return
     */
    @PostMapping("projectManage/rsvrWaterLevel")
    CommonResult<PageVO<LinkedHashMap>> rsvrWaterLevel(@RequestBody RsvrStationWaterReqVo reqVo);

    @PostMapping("overview/predictTideLevel")
    List<SectionStorm> predictTideLevel(@RequestParam String dmId);

}
