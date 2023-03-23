package com.ygkj.digitization.service;

import com.ygkj.digitization.model.StPptnRAssist;

public interface StPptnRAssistService {
    /**
     * 新增
     */
    public int insert(StPptnRAssist stPptnRAssist);

    /**
     * 删除
     */
    public int delete(int id);

    /**
     * 更新
     */
    public int update(StPptnRAssist stPptnRAssist);

    /**
     * 根据主键 id 查询
     */
    public StPptnRAssist load(int id);



}
