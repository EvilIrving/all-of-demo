package com.ygkj.mapper;

import com.ygkj.entity.ProcessCustomize;
import com.ygkj.vo.resp.OperatorRespVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author: fml
 * @Date: 2020/10/10 09:42
 * @Description:
 */
@Mapper
public interface ProcessCustomizeMapper {

    /**
     * 获取所有水利用户
     *
     * @param
     * @return List<OperatorRespVo>
     */
    List<OperatorRespVo> queryAllWaterUsers();

    /**
     * 获取所有企业用户
     *
     * @param
     * @return List<OperatorRespVo>
     */
    List<OperatorRespVo> queryAllWaterCompanyUsers();

    /**
     * 获取不同步骤操作人员
     *
     * @param step
     * @return String
     */
    String getAssignees(int step);

    /**
     * 获取不同步骤流程控制信息
     *
     * @param step
     * @return ProcessCustomize
     */
    ProcessCustomize getProcessCustomize(int step);

    /**
     * 新增流程控制信息
     *
     * @param processCustomize
     * @return int
     */
    int addOperator(ProcessCustomize processCustomize);

    /**
     * 更新流程控制信息
     *
     * @param processCustomize
     * @return int
     */
    int updateOperator(ProcessCustomize processCustomize);

    /**
     * 更新流程控制信息
     *
     * @param list
     * @return List<OperatorRespVo>
     */
    List<OperatorRespVo> getUsers(List<String> list);
}
