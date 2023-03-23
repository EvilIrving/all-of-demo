package com.ygkj.digitization.mapper;

import com.ygkj.digitization.model.WarnDept;
import com.ygkj.digitization.vo.request.WarnDeptReqVo;
import com.ygkj.digitization.vo.response.WarnDeptRespVo;

import java.util.List;

/**
 * @description 预警部门表
 * @author hucong
 * @date 2021-07-26
 */
public interface WarnDeptMapper {

    /**
     * 新增
     * @author hucong
     * @date 2021/07/26
     **/
    int insert(WarnDept warnDept);

    /**
     * 刪除
     * @author hucong
     * @date 2021/07/26
     **/
    int delete(String id);

    /**
     * 更新
     * @author hucong
     * @date 2021/07/26
     **/
    int update(WarnDept warnDept);


    List<WarnDeptRespVo> loadPage(WarnDeptReqVo vo);
}
