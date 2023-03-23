package com.ygkj.project.vo.response;


import com.ygkj.project.model.DeptCustom;
import lombok.Data;

import java.util.List;

/**
 * @author wgf
 * @date 2021/10/21 20:45
 * @Description:
 */
@Data
public class DeptCustomVo extends DeptCustom {

    private List<DeptCustomVo> list;
}
