package com.ygkj.platform.management.vo.request;

import com.ygkj.entity.PageEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ElasticPageSearchVo extends PageEntity {

    private String index;

    private String value;

    List<String> conditions;

}
