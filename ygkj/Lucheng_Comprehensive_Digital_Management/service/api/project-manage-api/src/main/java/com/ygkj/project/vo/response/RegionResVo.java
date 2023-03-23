package com.ygkj.project.vo.response;

import com.ygkj.project.model.AttCameraRegion;
import lombok.Data;

import java.util.List;

/**
 * @author xq
 * @Description
 * @Date 2021/4/13
 */
@Data
public class RegionResVo extends AttCameraRegion {
    List<RegionResVo> children;
}
