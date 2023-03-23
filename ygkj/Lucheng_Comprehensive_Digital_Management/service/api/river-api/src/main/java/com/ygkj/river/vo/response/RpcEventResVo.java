package com.ygkj.river.vo.response;

import com.ygkj.river.model.RiverPatrolCameraEvent;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author xq
 * @Description
 * @Date 2021/4/8
 */
@Data
@ApiModel("视频巡河-事件回参")
public class RpcEventResVo extends RiverPatrolCameraEvent {
    /**
     * 河长级别
     */
    @ApiModelProperty("河长级别")
    private String riverManagerGrade;

    /**
     * 河长姓名
     */
    @ApiModelProperty("河长姓名")
    private String riverManagerName;

    /**
     * 河长职务
     */
    @ApiModelProperty("河长职务")
    private String riverManagerPhone;

//    /**
//     * 摄像头经度
//     */
//    @ApiModelProperty("摄像头经度")
//    private String cameraLng;
//    /**
//     * 摄像头纬度
//     */
//    @ApiModelProperty("摄像头纬度")
//    private String cameraLat;
}
