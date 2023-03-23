package com.ygkj.gragh.service;

import com.ygkj.gragh.model.YongjiaMetaData;
import org.springframework.stereotype.Service;

public interface YongjiaSynchronizedService {

    boolean synchronizeData(YongjiaMetaData metaData);
}
