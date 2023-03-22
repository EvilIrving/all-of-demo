---
title: Vue中封装Echarts柱形图
date: 2023-02-10
tags:
  - Vue
  - Echarts
categories: Vue
keywords: 'Vue,Echarts,教程,文檔'
description: 在Vue中封装Echarts图表的默认Option及传入的data，合并数据后在页面展示
# cover: https://file.crazywong.com/gh/jerryc127/CDN/img/butterfly-docs-01-cover.png
---

### 默认配置

新建一个`defaul_option.js`文件，定义基础option与颜色数据

```js
// 这里的数据会被深度合并
const BASIC_OPTION = {
    grid: {
        top: 30,
        left: 28,
        right: 10,
        bottom: 45
    },
    xAxis: {
        type: 'category',
        data: ['2017', '2018', '2019', '2020', '2021', '2022', '2023'],
        axisLabel: {
            interval: 0,
            fontSize: "16",
            color: "#fff",
        },
        axisLine: {
            color: "#fff",
            lineStyle: {
                color: "#fff",
                opacity: 0.5,
            },
        }
    },
    yAxis: {
        type: 'value',
        axisLine: {
            show: false,
        },
        axisLabel: {
            color: "#fff",
            fontSize: "16",
        },
        splitLine: {
            show: false,
            lineStyle: {
                color: "#fff",
                opacity: 0.5,
            },
        },
    },
    legend:{
        show:true,
        right:0,
        top:0,
    },
    series: []
};

const COLOR_ARRAY = ["#2886c6", "#50bfda", "#89e3ec"];

export { BASIC_OPTION, COLOR_ARRAY };
```
