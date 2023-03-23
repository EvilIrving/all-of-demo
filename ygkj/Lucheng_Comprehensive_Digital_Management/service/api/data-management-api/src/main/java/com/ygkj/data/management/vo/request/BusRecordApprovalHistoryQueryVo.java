package com.ygkj.data.management.vo.request;

import com.ygkj.entity.PageEntity;
import lombok.Data;

@Data
public class BusRecordApprovalHistoryQueryVo extends PageEntity {

    private Long id;

    private String userId;

    private String status;

    private String recordPrimary;

    private String tableName;

    private String operationType;
}
