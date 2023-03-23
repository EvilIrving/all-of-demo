package com.ygkj.project.vo.request;

import com.ygkj.project.model.SysDept;
import lombok.Data;

import java.util.List;

/**
 * @author wgf
 * @date 2021/11/19 14:23
 * @Description:
 */
@Data
public class SysDeptVo extends SysDept {

    private List<SysDeptVo> list;
}
