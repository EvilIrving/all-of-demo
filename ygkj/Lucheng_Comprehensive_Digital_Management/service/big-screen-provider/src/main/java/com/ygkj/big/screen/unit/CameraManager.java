package com.ygkj.big.screen.unit;

import com.alibaba.fastjson.JSONObject;
import com.ygkj.big.screen.feign.ProjectManageClient;
import com.ygkj.big.screen.vo.request.*;
import com.ygkj.project.vo.request.HikCameraQueryReqVo;
import com.ygkj.utils.Builder;
import com.ygkj.utils.CollectionUtils;
import com.ygkj.vo.PageVO;
import com.ygkj.water.model.CommonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author xq
 * @Description 视频组件
 * @Date 2021/7/15
 */
@Component
public class CameraManager extends AbstractUnitManager {

    @Autowired
    ProjectManageClient projectManageClient;


    public CameraManager(@Autowired ApplicationContext context) {
        super(context);
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public void doSubscribe() {

    }

    @Override
    public void doPublish() {

    }

    @Override
    public Object getData(UnitDataReqVo reqVo) {
        switch (reqVo.getStrategy()) {
            case "listCamera": {
                HikCameraQueryReqVo param = reqVo.getParam("reqVo", HikCameraQueryReqVo.class);
                return listCamera(param);
            }
            default:
                break;
        }
        return null;

    }

    @Override
    public void buildData(UnitDataBuilderReqVo reqVo) {

    }

    public CommonResult listCamera(HikCameraQueryReqVo reqVo) {
        if (reqVo != null) {
            reqVo.setCoordinateNotNull(false);
        }
        CommonResult<List<LinkedHashMap>> resp = projectManageClient.selectCameras(reqVo);
        List<LinkedHashMap> list = Collections.emptyList();
        int online = 0;
        int offline = 0;
        Integer onlineStatus = new Integer(1);
        if (CollectionUtils.isNotBlank(resp.getData())) {
            list = resp.getData();
            for (LinkedHashMap data : list) {
                Object status = data.get("status");
                if (status != null) {
                    if (onlineStatus.equals(status)) {
                        online++;
                    } else {
                        offline++;
                    }
                }
            }
        }
        JSONObject data = new JSONObject();
        data.put("list", list);
        data.put("total", list.size());
        data.put("online", online);
        data.put("offline", offline);
        return CommonResult.success(data);
    }
}
