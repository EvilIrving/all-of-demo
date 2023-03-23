package com.ygkj.big.screen.vo.response;

import lombok.Data;

import java.io.Serializable;

@Data
public class WzAdcdInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    private String adcd;

    private String adnm;

    private Integer num = 0;

    private Double value;

    private String txt;
}
