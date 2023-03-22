---
title: element-resize-detector监听页面元素
date: 2023-02-22
updated: 2023-02-2
tags:
  - Vue
  - npm
categories: Vue
keywords: 'Vue,npm,element-resize-detector,simple-element-resize-detect,监听页面元素'
description: 在Vue中封装Echarts图表的默认Option及传入的data，合并数据后在页面展示
# cover: https://file.crazywong.com/gh/jerryc127/CDN/img/butterfly-docs-01-cover.png
---

```js
const CSS = `position:absolute;left:0;top:-100%;width:100%;height:100%;margin:1px 0 0;border:none;opacity:0;pointer-events:none;`;

export default (element, handler) => {
 const frame = document.createElement('iframe');
 const supportsPE = document.documentMode < 11 && 'pointerEvents' in frame.style;

 frame.style.cssText = `${CSS}${supportsPE ? '' : 'visibility:hidden;'}`;
 frame.onload = () => {
  frame.contentWindow.onresize = () => {
   handler(element);
  };
 };
 element.appendChild(frame);
 return frame;
};
```
