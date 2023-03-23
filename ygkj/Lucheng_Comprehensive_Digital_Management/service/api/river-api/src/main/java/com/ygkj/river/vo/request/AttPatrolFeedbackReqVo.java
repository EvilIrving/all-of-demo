package com.ygkj.river.vo.request;

import com.ygkj.river.model.AttBlueInspectionProblem;
import lombok.Data;

import java.util.List;

@Data
public class AttPatrolFeedbackReqVo {


    private String type;

    private List<AttBlueInspectionProblem> problems;

    /**
     * 下次检查时间  判断是否为空，如果不为空的话。即有下次巡查时间，再即将开始下次巡查前一周进行提醒，需要将对应时间存储起来！
     * 下次检查时间存在的前提为： 这次检查的问题都无异常的情况下。 = 本次巡查完成后，可以再次进行再次检查。设置该时间，默认再次发起一次新的巡查！
     */
    private String nextDate;

}
