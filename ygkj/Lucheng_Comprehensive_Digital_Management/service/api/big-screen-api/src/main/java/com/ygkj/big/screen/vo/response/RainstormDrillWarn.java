package com.ygkj.big.screen.vo.response;

import lombok.Data;

import java.io.Serializable;

/**
 * @author hucong
 * @version 1.0
 * @date 2021/6/19 14:43
 */
@Data
public class RainstormDrillWarn implements Serializable {
    private String lat;
    private String lng;
    private String name;
}
