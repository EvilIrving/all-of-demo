package com.ygkj.water.project.utils;
import com.ygkj.project.model.WaveHeight;


/**
 * @author wgf
 * @date 2022/5/13 17:04
 * @Description: 计算风暴潮波高
 */
public class WaveHeightUtils {

    private static double g = 9.81;

    /**
     * 计算平均波高H
     * @param V 风速
     * @param d 水深
     * @param F 风区长度
     * @return
     */
    public static double height(double V, double d, double F) {
        double h1, h2, h3, H;
        h1 = 0.0018 * Math.pow((g * F) / Math.pow(V, 2), 0.45);
        h2 = 0.7 * Math.pow((g * d) / Math.pow(V, 2), 0.7);
        h3 = Math.tanh(h1 / (0.13 * Math.tanh(h2)));
        H = Math.pow(V, 2) * 0.13 * h2 * h3 / g;
        return H;
    }

    /**
     * 计算平均波周期
     * @param V 标准风速
     * @param H 平均波高
     * @return
     */
    public static double time(double V, double H) {
        double T;
        T = 13.9 * V * Math.pow((g * H) / Math.pow(V, 2), 0.5) / g;
        return T;
    }

    /**
     * 计算波长
     * @param T 平均波周期
     * @param d 水深
     * @return
     */
    public static double length(double T, double d) {
        double L = 5, tol = Math.pow(10, -6), D, DL;
        do {
            D = L;
            L = (g * Math.pow(T, 2)) / (2 * Math.PI) * Math.tanh(2 * Math.PI * d / L);
            DL = L - D;
        } while (Math.abs(DL) > tol);
        return L;
    }

    /**
     * 函数通过输入累积频率F和平均波高H来计算不同频率时的波高
     *
     * @param Fi 累积频率
     * @param H 平均波高
     * @param d 水深
     * @return
     */
    public static double Height(double Fi, double H, double d) {
        double Hf;
        Hf = H * Math.pow((-4 / Math.PI * (1 + H / (d * Math.pow(2 * Math.PI, 0.5))) * Math.log(Fi)), (1 - H / d) / 2);
        return Hf;
    }

    /**
     * \
     * 计算波高
     *
     * @param speed        风速
     * @param height       测量高度
     * @param fetchLength  风区长度
     * @param averageDepth 水深
     * @return
     */
    public static WaveHeight WaveHeight(double speed, double height, double fetchLength, double averageDepth) {
        //V为修正风速，Kz为高度换算系数,H为平均波高T为平均波周期
        double V, Kz, H, T;
        Kz = 2.523 / Math.log10(height / 0.03);
        //修正风速，V为修正后的风速
        V = speed * Kz;
        //计算平均波高
        H = height(V, averageDepth, fetchLength);
        //计算平均波周期
        T = time(V, H);
        //计算并波长
        double length = length(T, averageDepth);
        //计算并输出1% ， 2% ， 13%频率时的波高
        double H1 = Height(0.01, H, averageDepth);
        double H2 = Height(0.02, H, averageDepth);
        double H3 = Height(0.13, H, averageDepth);
        WaveHeight waveHeight = new WaveHeight();
        waveHeight.setLength(length);
        waveHeight.setH1(H1);
        waveHeight.setH2(H2);
        waveHeight.setH13(H3);
        return waveHeight;
    }

}


