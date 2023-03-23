package com.ygkj.project.vo.request;

import com.ygkj.project.model.DataMaintainDirectory;
import lombok.Data;

import java.util.List;

@Data
public class DataMaintainDirectoryVo extends DataMaintainDirectory {

    private List<DataMaintainDirectoryVo> list;
}
