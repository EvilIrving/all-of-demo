---
title: Express
date: 2020-04-25
update: 2020-04-28
tags: 
- Node
categories: 
- Node
---
## 路由处理方法

**Express 应用** 对象还提供了为其它所有 HTTP 动词定义路由处理器的方法，大多数处理器的使用方式完全一致：

`checkout()`, `copy()`, **`delete()`**, **`get()`**, `head()`, `lock()`, `merge()`, `mkactivity()`, `mkcol()`, `move()`, `m-search()`, `notify()`, `options()`, `patch()`, **`post()`**, `purge()`, **`put()`**, `report()`, `search()`, `subscribe()`, `trace()`, `unlock()`, `unsubscribe()`.

有一个特殊的路由方法 `app.all()`，它可以在响应任意 HTTP 方法时调用。用于在特定路径上为所有请求方法加载中间件函数。

路由处理器方法有2个参数：

1. 路由
   1. 字符串
   2. 正则表达式
2. 回调函数

响应方法如下：

- res.download()
- res.end()
- res.json()
- res.jsonp()
- res.redirect()
- res.render()
- res.send()
- res.sendFile()
- res.sendStatus()

## 中间件MiddleWare

中间件在 Express 应用中得到了广泛使用，从提供错误处理静态文件、到压缩 HTTP 响应等等。路由函数可以通过向 HTTP 客户端返回一些响应来结束 HTTP “请求 - 响应”周期，而中间件函数*通常是*对请求或响应执行某些操作，然后调用“栈”里的下一个函数，可能是其它中间件或路由处理器。中间件的调用顺序由应用开发者决定。

> **注：**中间件可以执行任何操作，运行任何代码，更改请求和响应对象，也可以**结束“请求 - 响应”周期**。如果它没有结束循环，则必须调用 `next()` 将控制传递给下一个中间件函数（否则请求将成为悬挂请求）。

大多数应用会使用**第三方**中间件来简化常见的 web 开发任务，比如 cookie、会话、用户身份验证、访问请求 `POST` 和 JSON 数据，日志记录等。
