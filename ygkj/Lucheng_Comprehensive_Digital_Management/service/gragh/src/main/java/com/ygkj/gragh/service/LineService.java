package com.ygkj.gragh.service;

import com.ygkj.gragh.model.AttSlfzBase;
import com.ygkj.gragh.vo.request.LineQueryVo;

import java.util.List;

public interface LineService {

    List<AttSlfzBase> lines(LineQueryVo lineQueryVo);
}
