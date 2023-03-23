package com.ygkj.gragh.service.impl;

import com.ygkj.gragh.mapper.UserCollectionMapper;
import com.ygkj.gragh.model.UserCollection;
import com.ygkj.gragh.service.UserCollectionService;
import com.ygkj.gragh.vo.request.UserCollectionAddVo;
import com.ygkj.gragh.vo.request.UserCollectionDelVo;
import com.ygkj.jwt.JwtTokenUtil;
import com.ygkj.service.GenerateDistributionIDService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@Service("userCollectionService")
public class UserCollectionServiceImpl implements UserCollectionService {

    private UserCollectionMapper userCollectionMapper;

    private GenerateDistributionIDService generateDistributionIDService;

    public UserCollectionServiceImpl(@Autowired UserCollectionMapper userCollectionMapper,
                                     @Autowired GenerateDistributionIDService generateDistributionIDService) {
        this.userCollectionMapper = userCollectionMapper;
        this.generateDistributionIDService = generateDistributionIDService;
    }

    @Override
    public boolean add(UserCollectionAddVo addVo) {
        UserCollection userCollection = new UserCollection();
        BeanUtils.copyProperties(addVo, userCollection);
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String token = request.getHeader("authorization");
        if (StringUtils.isNotBlank(token)) {
            userCollection.setUserId(JwtTokenUtil.getUserId(token));
        }
        userCollection.setId(generateDistributionIDService.nextID());
        if (userCollectionMapper.insert(userCollection) > 0) {
            return true;
        }
        return false;
    }

    @Override
    public boolean delete(UserCollectionDelVo delVo) {
        if (userCollectionMapper.delete(delVo) > 0) {
            return true;
        }
        return false;
    }
}
