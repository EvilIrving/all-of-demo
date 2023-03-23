package com.ygkj.big.screen.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.ygkj.big.screen.model.BsScene;
import com.ygkj.big.screen.vo.request.BsSceneQueryReqVo;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * @author xq
 * @description 大屏组件表
 * @date 2021-06-28
 */
@DS("meta")
public interface BsSceneMapper {

    /**
     * 新增
     *
     * @author xq
     * @date 2021/06/28
     **/
    int insert(BsScene bsScene);

    /**
     * 刪除
     *
     * @author xq
     * @date 2021/06/28
     **/
    int delete(String id);

    /**
     * 更新
     *
     * @author xq
     * @date 2021/06/28
     **/
    int update(BsScene bsScene);

    /**
     * 查询 根据主键 id 查询
     *
     * @author xq
     * @date 2021/06/28
     **/
    List<BsScene> load(String id);

    List<BsScene> customScene();

    /**
     * 查询 分页查询
     *
     * @author xq
     * @date 2021/06/28
     **/
    List<BsScene> pageList(int offset, int pagesize);

    /**
     * 查询 分页查询 count
     *
     * @author xq
     * @date 2021/06/28
     **/
    int pageListCount(int offset, int pagesize);

    List<BsScene> selectByCondition(BsSceneQueryReqVo reqVo);

    @Select("select 1 from lc_meta_db.bs_scene where id=#{id} and editable=1")
    @ResultType(Integer.class)
    Integer checkSceneEditable(String id);

    int replaceSceneDefault(String userId, String bsName, String sceneId);

    String loadDefaultSenceId(String userId, String bsName);
}
