---
title: 跨源资源共享（CORS）经验总结
date: 2022-12-26 23:57:00
tags:
- JavaScript
- 跨域
categories:
- JavaScript
---

## 同源策略

### 定义

[如果两个 `URL` 的 `protocol`、`port`和 `host` 都相同的话，则这两个 `URL` 是同源。这个方案也被称为“协议/主机/端口元组”，或者直接是“元组”。][1]  
以下三种情况任一不同的URL之间请求即可产生跨域限制。

* 域
* 协议
* 端口  

### 方法

* `JsonP`：前端通过`Script`脚本进行伪装请求,且只能允许Get请求
* `CORS`：服务端设置
* `WebSocket`:特殊场景时使用，不属于常规跨域操作
* 代理服务（`Nginx`）服务端`CORS`配置，推荐使用

[1]: https://developer.mozilla.org/zh-CN/docs/Web/Security/Same-origin_policy  "MDN"
