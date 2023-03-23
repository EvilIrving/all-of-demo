package com.dcy.datamanage.biz.util;

/**
 * 雪花算法
 */
public class SnowFlake {

    public static SnowFlake zero = new SnowFlake(0, 0);

    /**
     * 起始时间
     */
    private final static long START_TIME = 1647591531518L;

    /**
     * 每一部分占用的位数
     */
    private final static long SEQUENCE_BIT = 12;
    private final static long MACHINE_BIT = 5;
    private final static long DATA_CENTER_BIT = 5;

    /**
     * 每一部分的最大值
     */
    private final static long MAX_DATA_CENTER_NUM = -1L ^ (-1L << DATA_CENTER_BIT);
    private final static long MAX_MACHINE_NUM = -1L ^ (-1L << MACHINE_BIT);
    private final static long MAX_SEQUENCE = -1L ^ (-1L << SEQUENCE_BIT);

    /**
     * 每一部分向左的位移
     */
    private final static long MACHINE_LEFT = SEQUENCE_BIT;
    private final static long DATA_CENTER_LEFT = SEQUENCE_BIT + MACHINE_BIT;
    private final static long TIMESTAMP_LEFT = DATA_CENTER_LEFT + DATA_CENTER_BIT;
    private long dataCenterId;
    private long machineId;
    private long sequence = 0L;
    private long lastStamp = -1L;

    public SnowFlake(long dataCenterId, long machineId) {
        if (dataCenterId > MAX_DATA_CENTER_NUM || dataCenterId < 0) {
            throw new IllegalArgumentException("dataCenterId out");
        }
        if (machineId > MAX_MACHINE_NUM || machineId < 0) {
            throw new IllegalArgumentException("machineId out");
        }
        this.dataCenterId = dataCenterId;
        this.machineId = machineId;
    }

    public synchronized String nextId() {
        long nowTime = getNowTime();
        //时间戳倒退了，报错
        if (nowTime < lastStamp) {
            throw new RuntimeException("Clock moved backwards.");
        }

        if (nowTime == lastStamp) {
            //相同毫秒内，序列号自增
            sequence = (sequence + 1) & MAX_SEQUENCE;
            //同一毫秒的序列数已经达到最大,重新获取时间
            if (sequence == 0L) {
                nowTime = getNextMill();
            }
        } else {
            //不同毫秒内，序列号置为0
            sequence = 0L;
        }

        lastStamp = nowTime;
        //时间戳部分
        return String.valueOf((nowTime - START_TIME) << TIMESTAMP_LEFT
                //数据中心部分
                | dataCenterId << DATA_CENTER_LEFT
                //机器标识部分
                | machineId << MACHINE_LEFT
                //序列号部分
                | sequence);
    }

    private long getNextMill() {
        long mill = getNowTime();
        while (mill <= lastStamp) {
            mill = getNowTime();
        }
        return lastStamp + 1;
    }

    private long getNowTime() {
        return System.currentTimeMillis();
    }
}
