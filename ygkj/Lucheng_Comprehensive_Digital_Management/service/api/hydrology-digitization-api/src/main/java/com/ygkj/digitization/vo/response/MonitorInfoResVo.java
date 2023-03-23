package com.ygkj.digitization.vo.response;

import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author xq
 * @Description 排班信息返回实体类
 * @Date 2021/1/14
 */
@Data
public class MonitorInfoResVo {

    Integer id;

    /**
     * 排班日期，精确到日
     */
    Date monitorDate;
    /**
     * 排班领导集合
     */
    List<MonitorInfoPersonResVo> dbldList;
    /**
     * 值班人员集合
     */
    List<MonitorInfoPersonResVo> zbryList;
    /**
     * 副班人员集合
     */
    List<MonitorInfoPersonResVo> fbryList;

    public MonitorInfoResVo(Date monitorDate, List<MonitorInfoPersonResVo> dbldList, List<MonitorInfoPersonResVo> zbryList, List<MonitorInfoPersonResVo> fbryList) {
        this.monitorDate = monitorDate;
        this.dbldList = dbldList;
        this.zbryList = zbryList;
        this.fbryList = fbryList;
    }

    public MonitorInfoResVo() {
    }

    public void setDbldListFromString(String idStr, String nameStr) {
        String idArray[] = idStr.split(",");
        String nameArray[] = nameStr.split(",");
        dbldList = new ArrayList<>(idArray.length);
        for (int i = 0; i < idArray.length; i++) {
            String id = idArray[i];
            String name = null;
            if (i < idArray.length) {
                name = nameArray[i];
            }
            MonitorInfoPersonResVo temp = new MonitorInfoPersonResVo();
            temp.setUserId(id);
            temp.setUsername(name);
            dbldList.add(temp);
//            dbldList.add(MonitorInfoPersonResVo.builder().userId(id).username(name).build());
        }
    }

    public void setZbryListFromString(String idStr, String nameStr) {
        String idArray[] = idStr.split(",");
        String nameArray[] = nameStr.split(",");
        zbryList = new ArrayList<>(idArray.length);
        for (int i = 0; i < idArray.length; i++) {
            String id = idArray[i];
            String name = null;
            if (i < idArray.length) {
                name = nameArray[i];
            }
            MonitorInfoPersonResVo temp = new MonitorInfoPersonResVo();
            temp.setUserId(id);
            temp.setUsername(name);
            zbryList.add(temp);
//            zbryList.add(MonitorInfoPersonResVo.builder().userId(id).username(name).build());
        }
    }

    public void setFbryListFromString(String idStr, String nameStr) {
        String idArray[] = idStr.split(",");
        String nameArray[] = nameStr.split(",");
        fbryList = new ArrayList<>(idArray.length);
        for (int i = 0; i < idArray.length; i++) {
            String id = idArray[i];
            String name = null;
            if (i < idArray.length) {
                name = nameArray[i];
            }
            MonitorInfoPersonResVo temp = new MonitorInfoPersonResVo();
            temp.setUserId(id);
            temp.setUsername(name);
            fbryList.add(temp);
//            fbryList.add(MonitorInfoPersonResVo.builder().userId(id).username(name).build());
        }
    }


}
