package com.ygkj.gragh.service.impl;

import com.ygkj.gragh.mapper.AttSlfzBaseMapper;
import com.ygkj.gragh.model.AttSlfzBase;
import com.ygkj.gragh.service.LineService;
import com.ygkj.gragh.vo.request.LineQueryVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LineServiceImpl implements LineService {

    @Autowired
    private AttSlfzBaseMapper lineMapper;

    @Override
    public List<AttSlfzBase> lines(LineQueryVo lineQueryVo) {
        return lineMapper.selectByCondition(lineQueryVo);
    }
}
