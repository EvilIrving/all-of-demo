package com.ygkj.digitization.service.impl;

import com.github.pagehelper.PageHelper;
import com.ygkj.digitization.service.WarnUserService;
import com.ygkj.digitization.mapper.WarnUserMapper;
import com.ygkj.digitization.model.WarnUser;
import com.ygkj.digitization.vo.request.WarnUserReqVo;
import com.ygkj.digitization.vo.response.WarnUserRespVo;
import com.ygkj.project.model.SysUser;
import com.ygkj.utils.PageUtils;
import com.ygkj.utils.SnowFlake;
import com.ygkj.utils.StringUtils;
import com.ygkj.vo.PageVO;
import org.apache.commons.collections4.ListUtils;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @description 预警人员表
 * @author hucong
 * @date 2021-07-26
 */
@Service
public class WarnUserServiceImpl implements WarnUserService {

    @Resource
    private WarnUserMapper warnUserMapper;
    @Resource
    private SnowFlake snowFlake;

    @Override
    public int insert(WarnUserReqVo vo) {
        WarnUser warnUser = new WarnUser();
        BeanUtils.copyProperties(vo,warnUser);
        String wenZhouWaterUserId = findWenZhouWaterUserId(vo);
        if (StringUtils.isNotBlank(wenZhouWaterUserId)) {
            warnUser.setId(wenZhouWaterUserId);
        } else {
            warnUser.setId(String.valueOf(snowFlake.nextId()));
        }
        return warnUserMapper.insert(warnUser);
    }

    /**
     * 查找温州水管理平台用户id
     * @param vo
     * @return
     */
    private String findWenZhouWaterUserId(WarnUserReqVo vo) {
        SysUser sysUser = warnUserMapper.findWenZhouWaterUser(vo.getPhone());
        if (sysUser != null) {
            return sysUser.getId();
        }
        return null;
    }

    @Override
    public int delete(String id) {
        return warnUserMapper.delete(id);
    }

    @Override
    public int update(WarnUserReqVo vo) {
        WarnUser warnUser = new WarnUser();
        BeanUtils.copyProperties(vo,warnUser);
        return warnUserMapper.update(warnUser);
    }

    @Override
    public PageVO<WarnUserRespVo> loadPage(WarnUserReqVo vo) {
        if (vo.getPageNum() != null && vo.getPageNum() > 0
                && vo.getPageSize() != null && vo.getPageSize() > 0)
            PageHelper.startPage(vo.getPageNum(), vo.getPageSize());
        return PageUtils.getPageVO(warnUserMapper.loadPage(vo));
    }

    @Override
    public void importUserExcel(MultipartFile file, String deptId) throws IOException {
        InputStream inputStream = null;
        Workbook workbook;
        List<WarnUser> warnUsers = null;

        inputStream = file.getInputStream();
        workbook = WorkbookFactory.create(inputStream);
        Sheet sheet = workbook.getSheetAt(0);
        // 获得当前sheet的结束行
        int lastRowNum = sheet.getLastRowNum();
        warnUsers = new ArrayList<>(lastRowNum - 2);
        Row row = null;
        WarnUser warnUser = null;
        for (int i = 2; i < lastRowNum + 1; i++) {
            row = sheet.getRow(i);
            Cell cellOne = row.getCell(1);
            cellOne.setCellType(CellType.STRING);
            String userName = cellOne.getStringCellValue();//人员名称
            Cell cellTwo = row.getCell(2);
            cellTwo.setCellType(CellType.STRING);
            String phone = cellTwo.getStringCellValue();//手机号码
            Cell cellThree = row.getCell(3);
            cellThree.setCellType(CellType.STRING);
            String duty = cellThree.getStringCellValue();//职务

            warnUser = new WarnUser();
            warnUser.setId(String.valueOf(snowFlake.nextId()));
            if (StringUtils.isEmpty(userName)) {
                continue;
            }
            warnUser.setUserName(userName);
            warnUser.setPhone(phone);
            warnUser.setDuty(duty);
            warnUser.setDeptId(deptId);
            warnUsers.add(warnUser);
        }
        inputStream.close();

        // 批量保存
        if (!warnUsers.isEmpty()) {
            warnUsers = warnUsers.stream().filter(e-> !StringUtils.isEmpty(e.getUserName())).collect(Collectors.toList());
            if (warnUsers.size() > 1000) {
                ListUtils.partition(warnUsers,1000).stream().forEach(k->warnUserMapper.batchInsert(k));
            } else {
                warnUserMapper.batchInsert(warnUsers);
            }
        }
    }

}
