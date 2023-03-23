package com.ygkj.utils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.OptionalDouble;
import java.util.stream.Collectors;

/**
 * @author xq
 * @Description 水文计算工具
 * @Date 2021/5/26
 */
public class HydrologyUtils {

    private static BigDecimal DEFAULT_RUNOFF_RATIO = new BigDecimal(0.80);
    private static BigDecimal ONE_HUNDRED = new BigDecimal(100);

    /**
     * 大中型水库纳蓄能力
     * 大中型水库纳蓄能力（mm）=（水库汛限库容-当前库容）÷集雨面积×10÷折算系数（水文中心取0.80）
     * @param currentCapacity 实时库容
     * @param compareCapacity 对比库容
     * @param watShedArea 集雨面积
     * @param runoffRatio  径流系数
     * @return
     */
    public static BigDecimal calculateStorageAbility(BigDecimal currentCapacity,BigDecimal compareCapacity,BigDecimal watShedArea,BigDecimal runoffRatio){
        if (currentCapacity==null||compareCapacity==null||watShedArea==null){
            return null;
        }
        if (runoffRatio==null){
            runoffRatio=DEFAULT_RUNOFF_RATIO;
        }
        BigDecimal a=compareCapacity.subtract(currentCapacity);
        BigDecimal b=a.divide(watShedArea,10,BigDecimal.ROUND_HALF_UP);
        BigDecimal c=b.multiply(BigDecimal.TEN);
        return c.divide(runoffRatio,2,BigDecimal.ROUND_HALF_UP);
    }

    /**
     * 大中型水库纳蓄能力
     * 大中型水库纳蓄能力（mm）=（水库汛限库容-当前库容）÷集雨面积×10÷折算系数（水文中心取0.80）
     * @param currentCapacity 实时库容
     * @param compareCapacity 对比库容
     * @param watShedArea 集雨面积
     * @return
     */
    public static BigDecimal calculateStorageAbility(BigDecimal currentCapacity,BigDecimal compareCapacity,BigDecimal watShedArea){
       return calculateStorageAbility(currentCapacity, compareCapacity, watShedArea,DEFAULT_RUNOFF_RATIO);
    }


    /**
     * 计算蓄水率
     * 水库蓄水率（%）=当前库容÷水库汛限库容
     * @param currentCapacity
     * @param compareCapacity
     * @return
     */
    public static BigDecimal calculateStorageRatio(BigDecimal currentCapacity,BigDecimal compareCapacity){
        BigDecimal a=currentCapacity.divide(compareCapacity,10,BigDecimal.ROUND_HALF_UP);
        return a.multiply(ONE_HUNDRED);
    }

        /**
     * 判断闸门是否打开
     *
     * @param ovs 闸门开度
     * @return
     */
    public static boolean isWagaOpenGate(String ovs, Double gateOpenThreshold) {
        if (StringUtils.isEmpty(ovs)) {
            return false;
        }
//        if (gateOpenThreshold == null) {
//            return false;
//        }
        // ovs:1.49,1.05
        // 将开度根据逗号隔开，然后转成数字，查询最大的开度值，如果大于0，则已开
        OptionalDouble opt = Arrays.stream(ovs.split(StringUtils.JOIN_DELIMITER)).map(o1 -> {
            try {
                return OptionalDouble.of(new BigDecimal(o1).doubleValue());
            } catch (Exception e) {
                return OptionalDouble.empty();
            }
        }).filter(OptionalDouble::isPresent).mapToDouble(OptionalDouble::getAsDouble).max();
        if (!opt.isPresent()) {
            return false;
        }
//        return opt.getAsDouble() >= gateOpenThreshold;
        return opt.getAsDouble() > 0;
    }

    /**
     * 判断水库是否打开
     *
     * @return
     */
    public static int isRsvrOpened(){
        return 0;
    }

    /**
     * 判断泵站是否打开
     *
     * @return
     */
    public static int isPustOpened(String num1,String num2,String num3,String num4,String num5){
        List<String> list=new ArrayList<>();
        list.add(num1);
        list.add(num2);
        list.add(num3);
        list.add(num4);
        list.add(num5);
        List<String> list1=list.stream().filter(m->"开".equals(m)).collect(Collectors.toList());
        return list1.size();
    }
}
