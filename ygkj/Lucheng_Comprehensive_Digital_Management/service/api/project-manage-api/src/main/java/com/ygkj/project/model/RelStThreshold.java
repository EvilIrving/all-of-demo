package com.ygkj.project.model;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class RelStThreshold {

    private String stCode;

    private BigDecimal threshold;

    private Integer minutes;
}
