package com.ygkj.gragh.controller;

import com.ygkj.gragh.TorrentDeviceControllerApi;
import com.ygkj.gragh.service.TorrentDeviceService;
import com.ygkj.water.model.CommonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author lxl
 * @create 2022-04-08 16:41
 * @description 山洪-体感设备 数据推送  提供外部单位进行数据推送同步使用！
 *
 *  声光电系统数据推送账号密码： lucheng   密码： Ygkj0818.
 */
@RestController
@RequestMapping("/torrentDevice")
public class TorrentDeviceController implements TorrentDeviceControllerApi {

    private TorrentDeviceService torrentDeviceService;

    public TorrentDeviceController(@Autowired TorrentDeviceService torrentDeviceService) {
        this.torrentDeviceService = torrentDeviceService;
    }


    @Override
    @PostMapping("/push")
    public CommonResult pushData(String timingData) {

        return CommonResult.success(torrentDeviceService.pushData(timingData));
    }





}
