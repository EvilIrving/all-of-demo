package com.ygkj.big.screen.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.ygkj.big.screen.model.MessageRecord;
import com.ygkj.big.screen.vo.request.MessageRecordQueryVo;

import java.util.List;

/**
 * @author Huangzh
 * @description 短信发送记录
 * @date 2021-06-19
 */
@DS("business")
public interface MessageRecordMapper {

    /**
     * 新增
     *
     * @author Huangzh
     * @date 2021/06/19
     **/
    int insert(MessageRecord messageRecord);

    /**
     * 刪除
     *
     * @author Huangzh
     * @date 2021/06/19
     **/
    int delete(int id);

    /**
     * 更新
     *
     * @author Huangzh
     * @date 2021/06/19
     **/
    int update(MessageRecord messageRecord);

    /**
     * 查询 根据主键 id 查询
     *
     * @author Huangzh
     * @date 2021/06/19
     **/
    MessageRecord load(int id);

    /**
     * 查询 分页查询
     *
     * @author Huangzh
     * @date 2021/06/19
     **/
    List<MessageRecord> pageList(int offset, int pagesize);

    /**
     * 查询 分页查询 count
     *
     * @author Huangzh
     * @date 2021/06/19
     **/
    int pageListCount(int offset, int pagesize);

    List<MessageRecord> selectByCondition(MessageRecordQueryVo queryVo);

    int batchInsert(List<MessageRecord> list);

}

