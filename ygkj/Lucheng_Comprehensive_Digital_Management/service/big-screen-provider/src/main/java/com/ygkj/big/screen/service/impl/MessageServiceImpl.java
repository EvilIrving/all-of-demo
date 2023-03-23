package com.ygkj.big.screen.service.impl;

import com.github.pagehelper.PageHelper;
import com.ygkj.big.screen.service.MessageService;
import com.ygkj.big.screen.mapper.MessageRecordMapper;
import com.ygkj.big.screen.model.MessageRecord;
import com.ygkj.big.screen.vo.request.MessageRecordQueryVo;
import com.ygkj.utils.PageUtils;
import com.ygkj.vo.PageVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageServiceImpl implements MessageService {

    private MessageRecordMapper messageRecordMapper;

    public MessageServiceImpl(@Autowired MessageRecordMapper messageRecordMapper) {
        this.messageRecordMapper = messageRecordMapper;
    }

    @Override
    public PageVO<MessageRecord> selectByCondition(MessageRecordQueryVo queryVo) {
        if (queryVo.getPageNum() != 0 && queryVo.getPageSize() != 0) {
            PageHelper.startPage(queryVo.getPageNum(), queryVo.getPageSize());
        }
        List<MessageRecord> list = messageRecordMapper.selectByCondition(queryVo);
        return PageUtils.getPageVO(list);
    }
}
