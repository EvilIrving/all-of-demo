package com.ygkj.digitization.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class PptnRAssistReq implements Serializable {
    private String stcd;
    private String drp;
    private String tm;
}
