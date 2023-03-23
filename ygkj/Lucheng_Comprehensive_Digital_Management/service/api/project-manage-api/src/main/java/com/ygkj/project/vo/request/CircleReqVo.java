package com.ygkj.project.vo.request;

import com.ygkj.entity.PointEntity;
import lombok.Data;

/**
 * @author xq
 * @Description
 * @Date 2021/6/7
 */
@Data
public class CircleReqVo {
    PointEntity circleCenter;
    Double radius;
}
