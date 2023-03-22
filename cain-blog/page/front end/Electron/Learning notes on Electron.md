---
title: Electron学习笔记
date: 2023-03-08
updated: 2023-03-08
tags:
- Electron
- 基础
categories:
- Electron
cover: https://file.crazywong.com/gh/jerryc127/CDN@latest/cover/default_bg.png
---

## 快速入门

- 安装 Node.js
- 使用脚手架创建
  - `mkdir my-electron-app && cd my-electron-app && npm init`
  > entry point 应为 main.js.
      author 与 description 可为任意值，但对于应用打包是必填项。
- 安装Electron  `npm install --save-dev electron` -- [高级安装](https://www.electronjs.org/zh/docs/latest/tutorial/installation)
- 添加指令

    ```js
    {
    "scripts": {
        "start": "electron ."
    }
    }
    ```

- `npm start`
  - 运行主进程
    - 入口 main 文件控制了主进程，运行在Node.js环境中，控制应用生命周期，显示原生界面，执行特殊操作并管理渲染器进程
  - 创建页面 `index.html`
  - 在`main.js`中加载窗口
    - `const { app, BrowserWindow } = require('electron')`
      - app 模块，它控制应用程序的事件生命周期。
      - BrowserWindow 模块，它创建和管理应用程序 窗口。
    - 创建 `createWindow()`方法并调用

    ```js
    const createWindow = () => {
        const win = new BrowserWindow({
            width: 800,
            height: 600
        })
        win.loadFile('index.html')
    }
    ```

    - ready 事件被激发后创建浏览器窗口

    ```js
    app.whenReady().then(() => {
        createWindow()
    })
    ```

    - 管理窗口的生命周期
      - 使用 进程 全局的 platform 属性来专门为某些操作系统运行代码
      - `window-all-closed`
  - 打包 - `Electron Forge`
    - `npm install --save-dev @electron-forge/cli && npx electron-forge import`
  - 分发-`Forge` 的 `make`
    - `npm run make`

## VSCode调试

新建 `.vscode` 文件夹, 并新建launch.json填写:

```json
.vscode/launch.json
{
  "version": "0.2.0",
  "compounds": [
    {
      "name": "Main + renderer",
      "configurations": ["Main", "Renderer"],
      "stopAll": true
    }
  ],
  "configurations": [
    {
      "name": "Renderer",
      "port": 9222,
      "request": "attach",
      "type": "chrome",
      "webRoot": "${workspaceFolder}"
    },
    {
      "name": "Main",
      "type": "node",
      "request": "launch",
      "cwd": "${workspaceFolder}",
      "runtimeExecutable": "${workspaceFolder}/node_modules/.bin/electron",
      "windows": {
        "runtimeExecutable": "${workspaceFolder}/node_modules/.bin/electron.cmd"
      },
      "args": [".", "--remote-debugging-port=9222"],
      "outputCapture": "std",
      "console": "integratedTerminal"
    }
  ]
}
```

Electron是一个使用 JavaScript、HTML 和 CSS 构建桌面应用程序的框架
