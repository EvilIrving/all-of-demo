package com.ygkj.data.management.vo.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.Date;

@Data
@JsonIgnoreProperties(value = {"hibernateLazyInitializer", "handler"})
public class BusApiBaseResVo {

    /**
     * id
     */
    private String id;

    /**
     * 服务接口名称
     */
    private String apiName;

    /**
     * 请求方式
     */
    private String requestMethod;

    /**
     * 接口分类
     */
    private String apiCategory;

    /**
     * 接口类别
     */
    private String apiState;

    /**
     * 接口状态（待上线/下线/正常）
     */
    private String status;

    /**
     * 责任人id
     */
    private String responsibleId;

    /**
     * 责任人名称
     */
    private String responsible;

    /**
     * 责任人电话
     */
    private String responsibleMobile;

    /**
     * 申请次数
     */
    private Integer applyNumber;

    /**
     * 总调用次数
     */
    private Integer totalInvokeNumber;

    /**
     * 今日调用次数
     */
    private Integer todayInvokeNumber;

    private Date createTime;
}
