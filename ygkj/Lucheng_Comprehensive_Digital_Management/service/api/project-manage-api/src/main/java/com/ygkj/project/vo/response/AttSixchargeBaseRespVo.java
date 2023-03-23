package com.ygkj.project.vo.response;

import com.ygkj.project.model.AttSixchargeBase;
import lombok.Data;

import java.util.Date;

@Data
public class AttSixchargeBaseRespVo extends AttSixchargeBase {
    private String projectName;

    private String projectScale;

    private String cityAdnm;

    private String countryAdnm;

    private String townAdnm;

    private String updateTime;
}
