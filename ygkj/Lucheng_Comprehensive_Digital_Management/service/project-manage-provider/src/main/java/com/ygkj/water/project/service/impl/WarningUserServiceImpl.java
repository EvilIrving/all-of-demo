package com.ygkj.water.project.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ygkj.project.model.WarningUser;
import com.ygkj.water.project.mapper.WarningUserMapper;
import com.ygkj.water.project.service.WarningUserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;


/**
 * @author wgf
 * @date 2021/10/21 17:00
 * @Description:
 */
@Service
public class WarningUserServiceImpl extends ServiceImpl<WarningUserMapper, WarningUser> implements WarningUserService {

    @Resource
    private WarningUserMapper warningUserMapper;

    @Override
    public void insert(WarningUser warningUser) {
        warningUser.setId(IdWorker.get32UUID());
        warningUserMapper.insert(warningUser);
    }

    @Override
    public boolean delete(String id) {
        int ret = warningUserMapper.deleteById(id);
        return ret > 0;
    }

    @Override
    public boolean update(WarningUser warningUser) {
        int ret = warningUserMapper.updateById(warningUser);
        return ret > 0;
    }

    @Override
    public List<JSONObject> addressBookList(String key) {
        return warningUserMapper.addressBookList(key);
    }

    @Override
    public Object userList(String name) {
        return warningUserMapper.userList(name);
    }
}
