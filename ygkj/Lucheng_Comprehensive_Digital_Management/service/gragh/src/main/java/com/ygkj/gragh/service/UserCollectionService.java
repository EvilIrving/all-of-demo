package com.ygkj.gragh.service;

import com.ygkj.gragh.vo.request.UserCollectionAddVo;
import com.ygkj.gragh.vo.request.UserCollectionDelVo;

public interface UserCollectionService {

    boolean add(UserCollectionAddVo addVo);

    boolean delete(UserCollectionDelVo delVo);
}
