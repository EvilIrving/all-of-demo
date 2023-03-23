package com.ygkj.big.screen.utils;

/**
 * @author wgf
 * @date 2022/8/24 10:45
 * @Description:
 */

import com.google.common.base.Strings;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xddf.usermodel.chart.XDDFChartData;
import org.apache.poi.xddf.usermodel.chart.XDDFDataSource;
import org.apache.poi.xddf.usermodel.chart.XDDFDataSourcesFactory;
import org.apache.poi.xddf.usermodel.chart.XDDFNumericalDataSource;
import org.apache.poi.xwpf.usermodel.XWPFChart;

import java.util.List;

/**
 * @author : LCheng
 * @date : 2020-12-10 10:03
 * description : poi工具
 */
public class PoiUtil {

    /**
     * 根据word模板导出 针对图表（柱状图，折线图，饼图等）的处理
     *
     * @param docChart    图表对象
     * @param title       图表标题
     * @param seriesNames 系列名称数组
     * @param cats        分类信息数组
     * @param values      值信息集合 与系列数组一一对应
     * @return {@link XWPFChart}
     * @author LCheng
     * @date 2020/12/10 11:08
     */
    public static XWPFChart wordExportChar(XWPFChart docChart, String title, String[] seriesNames, String[] cats, List<Number[]> values) {
        //获取图表数据对象
        XDDFChartData chartData = docChart.getChartSeries().get(0);

        //word图表均对应一个内置的excel，用于保存图表对应的数据
        //excel中 第一列第二行开始的数据为分类信息
        //CellRangeAddress(1, categories.size(), 0, 0) 四个参数依次为 起始行 截止行 起始列 截止列。
        //excel中分类信息的范围
        String catDataRange = docChart.formatRange(new CellRangeAddress(1, cats.length, 0, 0));
        //根据分类信息的范围创建分类信息的数据源
        XDDFDataSource<?> catDataSource = XDDFDataSourcesFactory.fromArray(cats, catDataRange, 0);
        //更新数据
        for (int i = 0; i < seriesNames.length; i++) {
            //excel中各系列对应的数据的范围
            String valDataRange = docChart.formatRange(new CellRangeAddress(1, cats.length, i + 1, i + 1));
            //根据数据的范围创建值的数据源
            Number[] val = values.get(i);
            XDDFNumericalDataSource<Number> valDataSource = XDDFDataSourcesFactory.fromArray(val, valDataRange, i + 1);
            //获取图表系列的数据对象
            XDDFChartData.Series series = chartData.getSeries().get(i);
            //替换系列数据对象中的分类和值
            series.replaceData(catDataSource, valDataSource);
            //修改系列数据对象中的标题
            CellReference cellReference = docChart.setSheetTitle(seriesNames[i], 1);
            series.setTitle(seriesNames[i], cellReference);
        }
        //更新图表数据对象
        docChart.plot(chartData);
        //图表整体的标题 传空值则不替换标题
        if (!Strings.isNullOrEmpty(title)) {
            docChart.setTitleText(title);
            docChart.setTitleOverlay(false);
        }
        return docChart;
    }
}

