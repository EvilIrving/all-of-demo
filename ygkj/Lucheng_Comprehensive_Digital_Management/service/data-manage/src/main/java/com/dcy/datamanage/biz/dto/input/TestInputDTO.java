package com.dcy.datamanage.biz.dto.input;

import com.dcy.datamanage.api.model.BusDataSetParam;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
public class TestInputDTO {

    private String fieldString;

    private Double fieldDouble;

    private Float fieldFloat;

    private BigDecimal fieldDecimal;

    private Integer fieldInteger;

    private Short fieldShort;

    private Byte fieldByte;

    private Boolean fieldBoolean;

    private List<BusDataSetParam> paramList;

    private BusDataSetParam param;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date fieldDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date fieldDateDiy;

    private String[] strings;

    private List<String> stringList;
}
