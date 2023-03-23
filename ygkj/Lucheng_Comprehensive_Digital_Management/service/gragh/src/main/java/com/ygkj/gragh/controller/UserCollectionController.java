package com.ygkj.gragh.controller;


import com.ygkj.gragh.UserCollectionControllerApi;
import com.ygkj.gragh.service.UserCollectionService;
import com.ygkj.gragh.vo.request.UserCollectionAddVo;
import com.ygkj.gragh.vo.request.UserCollectionDelVo;
import com.ygkj.water.model.CommonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/userCollection")
public class UserCollectionController implements UserCollectionControllerApi {

    private UserCollectionService userCollectionService;

    public UserCollectionController(@Autowired UserCollectionService userCollectionService) {
        this.userCollectionService = userCollectionService;
    }

    @PostMapping("/follow")
    @Override
    public CommonResult follow(UserCollectionAddVo addVo) {
        if (userCollectionService.add(addVo)) {
            return CommonResult.success("操作成功");
        }
        return CommonResult.failed("操作失败");
    }

    @PostMapping("/unFollow")
    @Override
    public CommonResult unFollow(UserCollectionDelVo delVo) {
        if (userCollectionService.delete(delVo)) {
            return CommonResult.success("操作成功");
        }
        return CommonResult.failed("操作失败");
    }
}
