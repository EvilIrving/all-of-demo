---
title: 输入框数字大小限制
date: 2022-11-15
updated: 2022-11-15
tags:
- JavaScript
- 经验
categories:
- JavaScript
---

## 输入框数字大小限制

方式一：max="100" min="10"，限制的是数字输入框右边的增减箭头按钮，最大值和最小值，不能对手动输入的数进行限制。

方式二：oninput

```javascript
// 只限制长度：输入四位数的数字
<input type="number" oninput="if(value.length>4)value=value.slice(0,4)" />

// 只限制最大值100：最大为100，即使手动输入大于100的数也会自动变成100
<input type="number" oninput="if(value>100)value=100" />

// 只限制最小值0：最小为0，即使手动输入小于0的负数也会自动变成0
<input type="number" oninput="if(value<0)value=0" />

// 均作限制：长度4 最大值100 最小值0
<input type="number" oninput="if(value>100)value=100;if(value.length>4)value=value.slice(0,4);if(value<0)value=0" />
```
