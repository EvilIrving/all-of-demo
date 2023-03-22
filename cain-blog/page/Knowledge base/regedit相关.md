---
title: regedit相关
date: 2022-01-08 12:59:52
update: 2020-01-08 12:59:52
tags:
- Windows
categories: 
- 知识库
---
## regedit更改Window时间显示秒

1. 在键盘上按下Win+R组合键打开运行，输入regedit
2. 在打开的注册表编辑器窗口中，依次展开到HKEY_CURRENT_USER\SOFTWARE\Microsoft\Windows\CurrentVersion\Explorer\Advanced
3. 在右侧新建一个DWORD(32位)值，并命名为ShowSecondsInSystemClock，双击打开将数值数据修改为1，确定保存
4. 重启文件资源管理器，之后就可以看到时间显示到秒了。
