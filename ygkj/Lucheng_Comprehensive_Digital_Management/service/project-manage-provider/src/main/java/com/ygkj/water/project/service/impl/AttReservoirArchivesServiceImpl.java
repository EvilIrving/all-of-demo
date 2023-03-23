package com.ygkj.water.project.service.impl;

import cn.hutool.core.map.MapUtil;
import com.alibaba.fastjson.JSONObject;
import com.ygkj.project.model.SysFile;
import com.ygkj.project.vo.response.AttReservoirArchivesResVo;
import com.ygkj.water.project.mapper.AttReservoirArchivesMapper;
import com.ygkj.water.project.service.AttReservoirArchivesService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @author lxl
 * @description 大中型水库档案
 * @date 2021-05-04
 */
@Service
public class AttReservoirArchivesServiceImpl implements AttReservoirArchivesService {

    @Resource
    private AttReservoirArchivesMapper attReservoirArchivesMapper;

    @Override
    public JSONObject reservoirArchivesCount() {
        Integer integer = attReservoirArchivesMapper.reservoirArchivesFileCount();
        List<SysFile> sysFiles = attReservoirArchivesMapper.reservoirArchivesFileList();
        List<AttReservoirArchivesResVo> attReservoirArchivesResVos = attReservoirArchivesMapper.reservoirArchivesAndResBase();
        attReservoirArchivesResVos.forEach(resVo -> {
            if (StringUtils.isNotEmpty(resVo.getEmergencyFileId())) {
                for (SysFile sysFile : sysFiles) {
                    if (resVo.getEmergencyFileId().equals(sysFile.getId())) {
                        resVo.setSysFile(sysFile);
                        break;
                    }
                }
            }
        });
        Map<String, Object> map = MapUtil.newHashMap();
        map.put("fileCount", integer);
        map.put("attReservoirArchivesList", attReservoirArchivesResVos);
        return new JSONObject(map);
    }
}
