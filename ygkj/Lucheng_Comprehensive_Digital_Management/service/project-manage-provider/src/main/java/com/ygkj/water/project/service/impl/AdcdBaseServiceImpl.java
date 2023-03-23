package com.ygkj.water.project.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ygkj.gragh.model.AttAdcdBase;
import com.ygkj.water.project.mapper.AttAdcdBaseMapper;
import com.ygkj.water.project.service.AdcdBaseService;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author xq
 * @Description
 * @Date 2021/5/27
 */
@Service("adcdBaseServiceImpl")
public class AdcdBaseServiceImpl implements AdcdBaseService {
    @Resource
    private AttAdcdBaseMapper attAdcdBaseMapper;

    /**
     * 根据行政区划编码查询行政区划名称并组成以编码为key的map
     *
     * @param adcdList
     * @return
     */
    public Map<String, String> selectAdnmByAdcd(List<String> adcdList) {
        if (CollectionUtils.isEmpty(adcdList)) {
            return Collections.emptyMap();
        }
        QueryWrapper<AttAdcdBase> wrapper = new QueryWrapper();
        wrapper = wrapper.select("adcd", "adnm");
        wrapper = wrapper.in("adcd", adcdList);
        List<AttAdcdBase> records = this.attAdcdBaseMapper.selectList(wrapper);
        if (CollectionUtils.isEmpty(records)) {
            return Collections.emptyMap();
        }
        return records.stream().collect(Collectors.toMap(AttAdcdBase::getAdcd, AttAdcdBase::getAdnm));
    }
}
