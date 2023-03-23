package com.dcy.datamanage.biz.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dcy.datamanage.api.model.DataAssessExtendMsg;

public interface DataAssessExtendMsgMapper extends BaseMapper<DataAssessExtendMsg> {

    /**
     * 修改最新更新时间
     *
     * @param dataAssessExtendMsg
     */
    void replace(DataAssessExtendMsg dataAssessExtendMsg);
}
