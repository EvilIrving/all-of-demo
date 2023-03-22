---
title: 金额格式化
date: 2022-10-29
updated: 2022-10-29
tags:
- JavaScript
- 刷题
categories:
- JavaScript
---
```JavaScript
// 1 
const num = String(1234567890);
let result = '';

for (let i = num.length - 1; i >= 0; i--) {
  if (i !== num.length - 1 && i % 3 === 0) {
    result = num[i] + ',' + result;
  } else {
    result = num[i] + result;
  }
}
// 2
String(1234567890).split('').reverse().reduce((prev, next, index) => (index % 3) === 0 ? next + ',' + prev : next + prev)
// 3
(1234567890).toLocaleString('en-US')
// 4
String(1234567890).replace(/\B(?=(\d{3})+(?!\d))/g, ',');
```
