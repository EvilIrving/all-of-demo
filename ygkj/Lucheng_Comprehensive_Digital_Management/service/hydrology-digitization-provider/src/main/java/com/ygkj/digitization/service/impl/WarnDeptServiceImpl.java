package com.ygkj.digitization.service.impl;

import com.github.pagehelper.PageHelper;
import com.ygkj.digitization.service.WarnDeptService;
import com.ygkj.digitization.mapper.WarnDeptMapper;
import com.ygkj.digitization.mapper.WarnUserMapper;
import com.ygkj.digitization.model.WarnDept;
import com.ygkj.digitization.vo.request.WarnDeptReqVo;
import com.ygkj.digitization.vo.request.WarnUserReqVo;
import com.ygkj.digitization.vo.response.WarnDeptRespVo;
import com.ygkj.digitization.vo.response.WarnUserRespVo;
import com.ygkj.utils.PageUtils;
import com.ygkj.utils.SnowFlake;
import com.ygkj.vo.PageVO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author hucong
 * @description 预警部门表
 * @date 2021-07-26
 */
@Service
public class WarnDeptServiceImpl implements WarnDeptService {

    @Resource
    private WarnDeptMapper warnDeptMapper;
    @Resource
    private SnowFlake snowFlake;
    @Resource
    private WarnUserMapper warnUserMapper;

    @Override
    public int insert(WarnDeptReqVo vo) {
        WarnDept warnDept = new WarnDept();
        BeanUtils.copyProperties(vo, warnDept);
        warnDept.setId(String.valueOf(snowFlake.nextId()));
        return warnDeptMapper.insert(warnDept);
    }

    @Override
    public int delete(String id) {
        return warnDeptMapper.delete(id);
    }

    @Override
    public int update(WarnDeptReqVo vo) {
        WarnDept warnDept = new WarnDept();
        BeanUtils.copyProperties(vo, warnDept);
        return warnDeptMapper.update(warnDept);
    }

    @Override
    public PageVO<WarnDeptRespVo> loadPage(WarnDeptReqVo vo) {
        if (vo.getPageNum() != null && vo.getPageNum() > 0
                && vo.getPageSize() != null && vo.getPageSize() > 0)
            PageHelper.startPage(vo.getPageNum(), vo.getPageSize());
        return PageUtils.getPageVO(warnDeptMapper.loadPage(vo));
    }

    @Override
    public List<WarnDeptRespVo> findTree(WarnDeptReqVo vo) {
        List<WarnDeptRespVo> list = PageUtils.getPageVO(warnDeptMapper.loadPage(vo)).getList();
        if (!list.isEmpty()) {
            List<WarnDeptRespVo> parentList = list.stream().filter(e -> "-1".equals(e.getPid())).collect(Collectors.toList());
            if (!parentList.isEmpty()) {
                findDeptChile(parentList, list, vo.getUserName());
                return parentList;
            }
        }
        return null;
    }

    /**
     * 递归查找子部门
     *
     * @param parentList
     * @param allList
     */
    private void findDeptChile(List<WarnDeptRespVo> parentList, List<WarnDeptRespVo> allList, String userName) {
        WarnUserReqVo vo = null;
        for (WarnDeptRespVo warnDeptRespVo : parentList) {
            vo = new WarnUserReqVo();
            vo.setDeptId(warnDeptRespVo.getId());
            vo.setUserName(userName);
            List<WarnUserRespVo> warnUserRespVos = warnUserMapper.loadPage(vo);
            //部门下的人员list
            warnDeptRespVo.setWarnUserList(warnUserRespVos);
            List<WarnDeptRespVo> childList = allList.stream().filter(k ->
                    k.getPid() != null && k.getPid().equals(warnDeptRespVo.getId())).collect(Collectors.toList());
            if (!childList.isEmpty()) {
                warnDeptRespVo.setChildren(childList);
                findDeptChile(childList, allList, userName);
            }
        }
    }
}