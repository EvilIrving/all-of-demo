---
title: 历史开发报错收集
date: 2023-02-24
updated: 2023-02-25
tags:
  - JavaScript
categories: 教程
keywords: '常见开发报错,EasyUI,DataGrid'
description: 一些值得记录的或不太好解决的bug
cover: https://s2.loli.net/2023/02/22/PcT2Eo7QaskyS3Y.jpg
---

## EasyUI--DataGrid数据与表头错位

![4604AE61-A1D8-4dd0-B4F5-01D2EA57EC79.png](https://s2.loli.net/2023/02/25/uTi3eDfPOhyKcvZ.png)
页面展示有错位，控制台无报错信息。也没有查询到靠谱的解决方法。

### 可能原因

1. datagrid重新渲染导致的错位[<sup>1</sup>](#refer-anchor-1)
2. 推测表头或者单元格的class名应该是用字段名拼出来的，如果含有特殊字符可能违反了css的命名规则[<sup>2</sup>](#refer-anchor-2)
3. 当有多个datagrid同时存在并且有人初始化查询过，就可能导致另外的datagrid查询后不再设置宽度，从而导致列挤在一起，只需将这里的if判断注释掉，改成全部都会设置宽度就不会再出现这种情况[<sup>3</sup>](#refer-anchor-3)

### 方法

#### 思路

上述推测和解决方案未能解决笔者遇到的错位问题。死马当作活马医的情况下，笔者咨询了Chat-GPT，答案如下：

没头没脑的，给笔者一点想法，是否只是表格数据宽度不一致而导致的？

#### 解决方案  

**项目中实际设置给定的宽高，将固定值修改为百分比并且，百分比总值超过100，即便是超过1%错位问题也没有了。**

## 参考

<div id="refer-anchor-1"></div>

- [1] [datagrid重新渲染导致的错位](https://blog.csdn.net/BtWangZhi/article/details/79463152)

<div id="refer-anchor-2"></div>

- [2] [easyui使用datagrid时列名包含特殊字符导致表头与数据错位的问题](https://www.cnblogs.com/liqipeng/p/4609105.html)

<div id="refer-anchor-3"></div>

- [3] [easyUI版本问题](https://blog.csdn.net/q394895302/article/details/89631519?utm_medium=distribute.pc_relevant.none-task-blog-2~default~baidujs_baidulandingword~default-1-89631519-blog-79463152.pc_relevant_3mothn_strategy_and_data_recovery&spm=1001.2101.3001.4242.2&utm_relevant_index=4)
