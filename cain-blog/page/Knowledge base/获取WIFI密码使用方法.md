---
title: 获取WIFI密码使用方法
date: 2022-01-08 12:59:52
update: 2020-01-08 12:59:52
tags:
- Windows
categories: 
- 知识库
---
1. 以管理员权限运行CMD

2. 输入命令：

```txt
for /f "skip=9 tokens=1,2 delims=:" %i in ('netsh wlan show profiles') do @echo %j | findstr -i -v echo | netsh wlan show profiles %j key=clear
```
