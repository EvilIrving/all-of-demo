---
title: 使用Cookie与Storage刷新页面或关闭页面后验证登录
date: 2022-12-31 17:46:38
update:  2023-1-07 17:36:34
tags: 
- JavaScript
- 存储管理
- 验证登录
categories: 
- JavaScript
---

```JavaScript
//刷新页面时，首先进入onbeforeunload 方法
window.onbeforeunload = () => {
  let numbers = window.localStorage.getItem("numbers");
  const sesTime = window.sessionStorage.getItem("sesTime");
  const localTime = window.localStorage.getItem("localTime"); //当localTime 没有值，并且sesTime === localTime，减去当前页。
  if (localTime != "NaN" && localTime != null && sesTime === localTime) {
    numbers = parseInt(numbers) - 1;
    window.localStorage.setItem("numbers", numbers);
  }
};

window.onload = () => {
  let time = "";
  let numbers = window.localStorage.getItem("numbers"); //计算打开的标签页数量
  const sesTime = window.sessionStorage.getItem("sesTime");
  const localTime = window.localStorage.getItem("localTime"); //首次登录的时间
  //当numbers无值或者小于0时，为numbers 赋值0
  if (numbers == "NaN" || numbers === null || parseInt(numbers) < 0) {
    numbers = 0;
  }
  //sesTime || localTime 没有值，并且numbers为0时，清除localStorage里的登录状态，退出到登录页面
  if (
    (sesTime === null ||
      sesTime === "NaN" ||
      localTime === null ||
      localTime === "NaN") &&
    parseInt(numbers) === 0
  ) {
    Cookies.remove("Admin-Token");
    Cookies.remove("UserName");
    Cookies.remove("UserInfo");
    // localStorage.clear();
    // sessionStorage.clear();
    //为localTime和sesTime赋值，记录第一个标签页的时间
    time = new Date().getTime();
    window.sessionStorage.setItem("sesTime", time);
    window.localStorage.setItem("localTime", time);
    router.push("/login");
  }
  //当time ，localTime 有值，并且sesTime != localTime时，为sesTime赋值
  if (
    !time &&
    localTime != "NaN" &&
    localTime != null &&
    sesTime != localTime
  ) {
    window.sessionStorage.setItem("sesTime", localTime);
  }
  //最后保存当前是第几个标签页
  numbers = parseInt(numbers) + 1;
  window.localStorage.setItem("numbers", numbers);
};
```
