package com.ygkj.project.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * @author pqf
 * @create 2023-02-17 10:54
 * @description
 */
@Data
public class StWaterPlantR  implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 水厂编码
     */
    private String waterPlantCode;

    /**
     * 水厂名称
     */
    private String waterPlantName;

    /**
     * 数据上传时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    private Date time;

    /**
     * 余氯
     */
    private String dpd;

    /**
     * 浊度
     */
    private String turbidity;

    /**
     * ph值
     */
    private String ph;
}
