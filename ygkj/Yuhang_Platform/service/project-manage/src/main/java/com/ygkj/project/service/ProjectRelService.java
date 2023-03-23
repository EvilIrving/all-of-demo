package com.ygkj.project.service;

import com.ygkj.project.api.vo.req.ProjectRelReqVo;
import com.ygkj.project.api.vo.res.ProjectRelResVo;
import com.ygkj.project.api.vo.res.RelCountResVo;
import com.ygkj.project.mapper.ProjectRelMapper;
import com.ygkj.water.utils.PageUtils;
import com.ygkj.water.utils.vo.PageVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ProjectRelService {

    @Autowired
    private ProjectRelMapper projectRelMapper;

    public PageVO<ProjectRelResVo> listRel(ProjectRelReqVo relReqVo) {
        if (CollectionUtils.isEmpty(relReqVo.getRelTypeList())) {
            return PageUtils.getPageVO(new ArrayList<ProjectRelResVo>());
        }
        return PageUtils.getPageVO(projectRelMapper.listRel(relReqVo));
    }

    public List<RelCountResVo> countRel(ProjectRelReqVo relReqVo) {
        List<RelCountResVo> list = projectRelMapper.countRel(relReqVo);
        Map<Integer, Integer> map = new HashMap<>();
        if (!CollectionUtils.isEmpty(list)) {
            for (RelCountResVo resVo : list) {
                map.put(resVo.getRelType(), resVo.getNum());
            }
        }
        List<RelCountResVo> result = new ArrayList<>();
        for (int i = 1; i < 7; i++) {
            RelCountResVo resVo = new RelCountResVo();
            resVo.setRelType(i);
            resVo.setNum(map.containsKey(i) ? map.get(i) : 0);
            result.add(resVo);
        }
        return result;
    }
}
