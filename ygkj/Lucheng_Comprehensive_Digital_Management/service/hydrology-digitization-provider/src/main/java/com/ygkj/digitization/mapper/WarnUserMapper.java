package com.ygkj.digitization.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.ygkj.digitization.model.WarnUser;
import com.ygkj.digitization.vo.request.WarnUserReqVo;
import com.ygkj.digitization.vo.response.WarnUserRespVo;
import com.ygkj.project.model.SysUser;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @description 预警人员表
 * @author hucong
 * @date 2021-07-26
 */
public interface WarnUserMapper {

    /**
     * 新增
     * @author hucong
     * @date 2021/07/26
     **/
    int insert(WarnUser warnUser);

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
    int update(WarnUser warnUser);

    /**
     * list数据查找
     * @param vo
     * @return
     */
    List<WarnUserRespVo> loadPage(WarnUserReqVo vo);

    /**
     * 批量保存
     * @param warnUsers
     */
    void batchInsert(@Param("list") List<WarnUser> warnUsers);

    /**
     * 根据用户ids，查找用户数据
     * @param peopleIds
     * @return
     */
    List<WarnUserRespVo> findUserList(@Param("peopleIds") String peopleIds);

    /**
     * 统计人员数量
     * @param sendPersonIds
     * @return
     */
    Integer countPeopleNum(@Param("peopleIds") String sendPersonIds);

    @DS("main")
    SysUser findWenZhouWaterUser(@Param("phone") String phone);
}
