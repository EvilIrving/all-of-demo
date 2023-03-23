package com.ygkj.water.project.service;

import com.alibaba.fastjson.JSONObject;
import com.ygkj.project.model.AttServiceItem;
import com.ygkj.project.vo.request.AttServiceItemReqVo;
import com.ygkj.vo.PageVO;

/**
 * @author lxl
 * @create 2022-04-12 11:27
 * @description
 */
public interface AttServiceItemService {

    JSONObject loadSkPage(AttServiceItemReqVo vo);

    int insert(AttServiceItemReqVo vo);

    int update(AttServiceItemReqVo vo);

    int delete(String id);

    PageVO<AttServiceItem> loadPage(AttServiceItemReqVo vo);
}
