package com.ygkj.soft.database.service;

import com.ygkj.soft.database.api.VO.ApiApplyQueryVo;
import com.ygkj.soft.database.api.VO.ApiApplyUpdateVo;
import com.ygkj.soft.database.api.model.ApiApplyStatusEnum;
import com.ygkj.soft.database.api.model.BusApiApply;
import ygkj.com.assistant.pager.EasyuiPager;
import ygkj.com.assistant.pager.Pager;

/**
 * @description 接口申请表
 * @author wangd
 * @date 2020-09-24
 */
public interface BusApiApplyService {

    /**
     * 新增
     */
    boolean insert(ApiApplyUpdateVo updateVo) throws Exception;

    /**
     * 删除
     */
    boolean delete(String id) throws Exception;

    /**
     * 更新
     */
    boolean update(ApiApplyUpdateVo updateVo) throws Exception;


    /**
     * 更新审批状态
     * @param applyId   审批id
     * @param remark    审批备注
     * @param status    审批状态
     */
    boolean updateState(String applyId, String remark, ApiApplyStatusEnum status) throws Exception;

    /**
     * 根据主键 id 查询
     */
    public BusApiApply load(String id) throws Exception;

    /**
     * 待我审批列表及审批记录
     */
    Pager queryMyAuditTodo(ApiApplyQueryVo queryVo, EasyuiPager easyuiPager) throws Exception;

    /**
     * 我发起的审批记录
     */
    Pager queryAuditRecordsFromMe(ApiApplyQueryVo queryVo, EasyuiPager easyuiPager) throws Exception;
}

