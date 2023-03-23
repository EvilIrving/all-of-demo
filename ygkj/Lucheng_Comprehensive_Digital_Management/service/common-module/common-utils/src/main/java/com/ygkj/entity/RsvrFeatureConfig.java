package com.ygkj.entity;

import lombok.Data;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * @author xq
 * @Description
 * @Date 2021/8/2
 */
@Component
@ConfigurationProperties(prefix = "rsvr.limitrange")
@Slf4j
@Data
@ToString
public class RsvrFeatureConfig implements InitializingBean {

    /**
     * 水库汛限水位
     */
    private String meiLimitRange = "04-15,07-14";

    private String typhoonLimitRange = "07-15,10-15";

    private LocalDate meiLimitStart;
    private LocalDate meiLimitEnd;
    private LocalDate typhoonLimitStart;
    private LocalDate typhoonLimitEnd;

    @Override
    public void afterPropertiesSet() throws Exception {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate now = LocalDate.now();
        try {
            String[] meiRanges = meiLimitRange.split(com.ygkj.utils.StringUtils.JOIN_DELIMITER);
            meiLimitStart = LocalDate.parse(now.getYear() + "-" + meiRanges[0], dtf).withYear(now.getYear());
            meiLimitEnd = LocalDate.parse(now.getYear() + "-" + meiRanges[1], dtf).withYear(now.getYear());
        } catch (Exception e) {
            meiLimitStart = LocalDate.now().withYear(2021).withMonth(4).withDayOfMonth(15);
            meiLimitStart = LocalDate.now().withYear(2021).withMonth(7).withDayOfMonth(15);
        }
        try {
            String[] typhoonRanges = typhoonLimitRange.split(com.ygkj.utils.StringUtils.JOIN_DELIMITER);
            typhoonLimitStart = LocalDate.parse(now.getYear() + "-" + typhoonRanges[0], dtf).withYear(now.getYear());
            typhoonLimitEnd = LocalDate.parse(now.getYear() + "-" + typhoonRanges[1], dtf).withYear(now.getYear());
        } catch (Exception e) {
            typhoonLimitStart = LocalDate.now().withYear(2021).withMonth(7).withDayOfMonth(15);
            typhoonLimitEnd = LocalDate.now().withYear(2021).withMonth(10).withDayOfMonth(15);
        }
    }
}
