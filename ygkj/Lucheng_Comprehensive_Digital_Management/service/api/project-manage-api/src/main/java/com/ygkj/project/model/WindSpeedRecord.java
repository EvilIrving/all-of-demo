package com.ygkj.project.model;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.util.Date;

@Data
public class WindSpeedRecord {

    @TableId
    private Date time;

    /**
     * 风速（m/s）
     */
    private Double windSpeed;
}
