package com.ygkj.consumer.auth.controller;

import com.ygkj.auth.VO.request.SysArchiveCollectionReqVO;
import com.ygkj.auth.VO.response.PageVO;
import com.ygkj.auth.api.archivecollection.SysArchiveCollectionApi;
import com.ygkj.auth.api.archivecollection.model.SysArchiveCollection;
import com.ygkj.consumer.auth.client.authentication.AuthenticationClient;
import com.ygkj.water.model.CommonResult;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author hucong
 * @version 1.0
 * @date 2021/1/19 18:36
 */
public class SysArchiveCollectionController implements SysArchiveCollectionApi {

    @Autowired
    private AuthenticationClient authenticationClient;


    @Override
    public CommonResult detailInfo(String id) {
        return authenticationClient.findArchiveCollectionById(Integer.parseInt(id));
    }

    @Override
    public PageVO<SysArchiveCollection> pageInfo(SysArchiveCollectionReqVO vo) {
        return authenticationClient.pageArchiveCollectionList(vo);
    }

    @Override
    public CommonResult addSysArchiveCollection(SysArchiveCollection entity) {
        return authenticationClient.insertArchiveCollection(entity);
    }

    @Override
    public CommonResult deleteSysArchiveCollection(String id) {
        return authenticationClient.deleteArchiveCollection(Integer.parseInt(id));
    }

    @Override
    public CommonResult updateSysArchiveCollection(SysArchiveCollection entity) {
        return authenticationClient.updateArchiveCollection(entity);
    }
}
