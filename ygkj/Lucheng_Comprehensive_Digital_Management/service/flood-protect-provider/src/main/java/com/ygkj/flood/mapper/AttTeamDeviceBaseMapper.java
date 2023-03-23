package com.ygkj.flood.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ygkj.flood.model.AttTeamDeviceBase;
import com.ygkj.flood.vo.request.AttTeamDeviceBaseReqVo;
import com.ygkj.flood.vo.response.AttTeamDeviceBaseRespVo;

import java.util.List;

/**
 * @author paf
 * @DESCRIPTION:
 * @create 2021-10-22 11:00
 */
public interface AttTeamDeviceBaseMapper {
    List<AttTeamDeviceBaseRespVo> selectteamDevice(AttTeamDeviceBaseReqVo vo);

    Integer insertteamDevice(AttTeamDeviceBase attTeamDeviceBase);

    Integer updateteamDevice(AttTeamDeviceBase attTeamDeviceBase);

    Boolean deleteteamDevice(List<AttTeamDeviceBase> list);
}
