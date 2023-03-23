package com.ygkj.big.screen.vo.request;

import lombok.Data;

import java.io.Serializable;

/**
 * @author xq
 * @Description
 * @Date 2021/7/12
 */
@Data
public class UnitDataBuilderReqVo implements Serializable {

    private static final long serialVersionUID = 2039771215060815213L;

    boolean publishSelf = false;

    String buildStrategy;

}
