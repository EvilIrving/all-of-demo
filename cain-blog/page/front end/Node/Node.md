---
title: Node总结
date: 2020-04-02
update: 2020-04-12
tags: 
- Node
- 教程
categories: 
- Node
---
## 介绍

Node.js 是一个基于 Chrome V8 引擎的 JavaScript 运行时。NodeJs使用事件驱动，非阻塞式的I/O，Nodejs的包管理生态式NPM，是世界上最大的开源程序包库

Node 的运行环境核心是V8引擎，基于C++编写，V8将JS编写的代码直接编译成机器码，保证了JS的运行效率，并同时负责在JS代码运行时进行内存的垃圾回收。

### REPL

尝试输入 JavaScript 类的名称，例如 `Number`，添加一个点号并按下 `tab`，REPL 会打印可以在该类上访问的所有属性和方法

输入 `global.` 并按下 `tab`，可以检查可以访问的全局变量

如果在某些代码之后输入 `_`，则会打印最后一次操作的结果。

REPL 有一些特殊的命令，所有这些命令都以点号 `.` 开头。它们是：

- `.help`: 显示点命令的帮助。
- `.editor`: 启用编辑器模式，可以轻松地编写多行 JavaScript 代码。当处于此模式时，按下 ctrl-D 可以运行编写的代码。
- `.break`: 当输入多行的表达式时，输入 `.break` 命令可以中止进一步的输入。相当于按下 ctrl-C。
- `.clear`: 将 REPL 上下文重置为空对象，并清除当前正在输入的任何多行的表达式。
- `.load`: 加载 JavaScript 文件（相对于当前工作目录）。
- `.save`: 将在 REPL 会话中输入的所有内容保存到文件（需指定文件名）。
- `.exit`: 退出 REPL（相当于按下两次 ctrl-C）。

### 输出到命令行

`console.log('我的%s已经%d岁', '猫', 2)`

- `%s` 会格式化变量为字符串
- `%d` 会格式化变量为数字
- `%i` 会格式化变量为其整数部分
- `%o` 会格式化变量为对象

`console.clear()` 会清除控制台（其行为可能取决于所使用的控制台）。

`console.count()` 元素计数

 `console.trace()` 实现打印堆栈踪迹

 `time()` 和 `timeEnd()` 轻松地计算函数运行所需的时间

`console.error` 会打印到 `stderr` 流。console.log称为标准输出（或称为 `stdout`）

## NPM 使用

### 自定义包

模块的本质是文件，在引入模块的时候得到的就是对应文件里的module.exports对象，有些时候，一个模块可能会依赖于其它模块，然后将这些所有的模块统一放进一个文件夹中，就是 package (包)。

一旦有了这个文件夹，引入时，我们会通过该文件夹的名字进行引入，但是一个文件里有好多模块，如果我们引入的名字是文件夹名，node如何确定该去获取的module.exports对象呢？

CommonJs规定，如果我们要创建一个包，我们需要保证每个包里有一个package.json文件，该文件规定了包里的所有配置。

在对应包的路径下，打开终端输入`npm init(交互式) -y(默认全部yes)`创建package.json文件，不需要我们亲自创建和书写内容。

### 使用第三方包

`npm install  <package-name>@version (-g)`

`npm install  <package-name>`

> `--save` 安装并添加条目到 `package.json` 文件的 dependencies。
>
> `--save-dev` 安装并添加条目到 `package.json` 文件的 devDependencies。

npm install  根据`package.json`记录重装所有插件

> 引入时只需要`var express = require('express')`;
>
> install 简写 i; uninstall 简写 uni

`npm update <package-name>`

`npm run <task-name>` 支持一种用于指定命令行任务（可通过使用以下方式运行）的格式

`npm search express` 搜索模块

`npm publish` 发布模块

`npm ls -g` 查看全局安装的模块

`npm uninstall packname` 连带其依赖文件一同删除

`npm view` 包名 version

- 本地安装（无-g）
  1. 将安装包放在 ./node_modules 下（运行 npm 命令时所在的目录），如果没有 node_modules 目录，会在当前执行 npm 命令的目录下生成 node_modules 目录。
  2. 可以通过 require() 来引入本地安装的包。
- 全局安装（有-g）
  1. 将安装包放在 /usr/local 下或者你 node 的安装目录。
  2. 可以直接在命令行里使用。

#### package.json 属性说明

- name - 包名。
- version - 包的版本号。
- description - 包的描述。
- homepage - 包的官网 url 。
- author - 包的作者姓名。
- contributors - 包的其他贡献者姓名。
- dependencies - 依赖包列表。如果依赖包没有安装，npm 会自动将依赖包安装在 node_module 目录下。
- repository - 包代码存放的地方的类型，可以是 git 或 svn，git 可在 Github 上。
- main - main 字段是一个模块ID，它是一个指向你程序的主要项目。就是说，如果你包的名字叫express，然后用户安装它，然后require("express")。
- keywords - 关键字

#### package-lock.json文件

该文件旨在跟踪被安装的每个软件包的确切版本，以便产品可以以相同的方式被 100％ 复制（即使软件包的维护者更新了软件包）。

## 模块

Node.js 提供了exports 和 require 两个对象，其中 exports 是模块公开的接口，require 用于从外部获取一个模块的接口，即所获取模块的 exports 对象。如果是只想把一个对象封装为一个模块，使用module.exports

引入自定义模块，require 需要模块的路径，且路径以`./` 开头，不需要写文件后缀。

区别：

- exports 是对module.exports 的引用，exports = module.exports = {}
- 外界在require时，永远得到的是对应模块里的module.exports代指的值

通过URL pathname的差别，以区别不同的网络请求，类似的，通过地址栏内容的部分内容的不同，区分请求的思想，称为路由

POST请求如果传递了参数，参数不会拼接到URL网址的后面，相应的，node获取POST参数的要通过事件监听。步骤如下：

1. 让req对象去监听两个系统自带的事件，分别是end和data事件
2. data事件在整个POST传递数据的过程中会触发多次，每次触发都会携带一段数据片段，我们需要在该回调内做数据片段的拼接操作；end事件在传递数据结束后只触发一次，该回调内做完整数据的处理。

在发起网络请求时，如果地址写成了相对路径，系统会依据当前发起请求的地址ip+端口+相对路径，最终发起网络请求。

## shell命令

```plain
node 空格 某个js文件         // 调用 node 程序，运行某个js文件

clear  或者  cls            // 清空界面

ls/dir                      // 查看列表(list)

cd 目录名                   // 进入到目录中去

cd ..                      // 返回上一级目录

cd \                     // 直接回到根目录
                   
Ctrl+C                     // 停止 Node 程序

输入部分文件名后按下 Tab 键  // 补全文件名 或 目录名， 多次tab会进行切换

↑ ↓ 上下箭头                // 切换历史输入

在小黑窗中复制内容：选中内容，再点鼠标右键
把粘贴板中的内容复制到小黑窗： 点鼠标右键
```

## tips

### content-type的作用

在http协议中，content-type用来告诉对方本次传输的数据的类型是什么。

- 在请求头中设置content-type来告诉服务器，本次请求携带的数据是什么类型的
- 在响应头中设置content-type来告诉服务器，本次返回的数据是什么类型的

通过使用res对象中的setHeader方法，我们可以**设置content-type这个响应头**。这个响应头的作用是告诉浏览器，本次响应的内容是什么格式的内容，以方便浏览器进行处理。

在通过HTML form提交生成的POST请求中，请求头的Content-Type由<form>元素上的enctype属性指定

- app.use(....)之后，在res.body中就会多出一个属性res.body。
- `extended: false`：表示使用系统模块querystring来处理传入的参数，也是官方推荐的
- `extended: true`：表示使用第三方模块qs来处理传入的参数.

## 事件循环

### 阻塞事件循环

任何花费太长时间才能将控制权返回给事件循环的 JavaScript 代码，都会阻塞页面中任何 JavaScript 代码的执行，甚至阻塞 UI 线程，并且用户无法单击浏览、滚动页面等。

### 调用堆栈

调用堆栈是一个 LIFO 队列（后进先出）。事件循环不断地检查调用堆栈，以查看是否需要运行任何函数。当执行时，它会将找到的所有函数调用添加到调用堆栈中，并按顺序执行每个函数。

重要概念：入队函数执行、消息队列、ES6 作业队列

消息队列与作业队列：有个游乐园中过山车的比喻很好：消息队列将你排在队列的后面（在所有其他人的后面），你不得不等待你的回合，而工作队列则是快速通道票，这样你就可以在完成上一次乘车后立即乘坐另一趟车。

## process.nextTick()

**每当事件循环进行一次完整的行程时，我们都将其称为一个滴答。**

当将一个函数传给 `process.nextTick()` 时，则指示引擎在当前操作结束（在下一个事件循环滴答开始之前）时调用此函数：

```javascript
process.nextTick(() => {
  //做些事情
})
```

事件循环正在忙于处理当前的函数代码。

调用 `setTimeout(() => {}, 0)` 会在**下一个滴答结束时**执行该函数，比使用 `nextTick()`（其会优先执行该调用并在下一个滴答开始之前执行该函数）晚得多。

当要确保在下一个事件循环迭代中代码已被执行，则使用 `nextTick()`。

## setImmediate()

```javascript
setImmediate(() => {
  //运行一些东西
})
```

作为 setImmediate() 参数传入的任何函数都是在事件循环的下一个迭代中执行的回调。

### `setImmediate()` 与 `setTimeout(() => {}, 0)`（传入 0 毫秒的超时）、`process.nextTick()` 有何不同？

传给 `process.nextTick()` 的函数会在事件循环的**当前迭代中**（当前操作结束之后）被执行。 这意味着它会始终在 `setTimeout` 和 `setImmediate` 之前执行。

延迟 0 毫秒的 `setTimeout()` 回调与 `setImmediate()` 非常相似。 执行顺序取决于各种因素，但是它们都会在事件循环的下一个迭代中运行。

*（IE 和 Edge）实现的*也有 `setImmediate()` ，是非标准函数，其它浏览器不可用。

## 定时器

通常在 `setInterval` 回调函数中调用 `clearInterval`，以使其自行判断是否应该再次运行或停止。

```javascript
const interval = setInterval(() => {
  if (App.somethingIWait === 'arrived') {
    clearInterval(interval)
    return
  }
  // 否则做些事情
}, 100)
```

`setInterval` 每 n 毫秒启动一个函数，而无需考虑函数何时完成执行。函数可能需要不同的执行时间，这具体取决于网络条件，也许一个较长时间的执行会与下一次执行重叠，为了避免这种情况，可以在回调函数完成时安排要被调用的递归的 setTimeout：

```javascript
const myFunction = () => {
  // 做些事情
  setTimeout(myFunction, 1000)
}
setTimeout(myFunction, 1000)
```

## 异步编程与回调

### 异步编程

计算机在设计上是异步的，就是说，程序是异步的且会暂停执行直到需要关注，这使得计算机可以同时执行其他操作。 当程序正在等待来自网络的响应时，则它无法在请求完成之前停止处理器。，C、Java、C＃、PHP、Go、Ruby、Swift 和 Python 都是同步的。 其中一些语言通过使用线程（衍生新的进程）来处理异步操作。

### 回调中的错误

如何处理回调的错误？ 一种非常常见的策略是使用 Node.js 所采用的方式：任何回调函数中的第一个参数为错误对象（即错误优先的回调）。

但是回调只适用于简单的场景，复杂场景下的嵌套回调，会导致**回调地狱**，因此引入了新的特性。

## Promise

### 来由和创建

Promise 通常被定义为**最终会变为可用值的代理**。是一种处理异步代码（而不会陷入回调地狱）的方式。

当 promise 被调用后，它会以**处理中状态**开始。 这意味着**调用的函数会继续执行**，而 promise 仍处于处理中直到解决为止，从而为调用的函数提供所请求的任何数据。

在创建和使用Promise之间，有一种称为 Promisifying 的技术。 这项技术能够使用经典的 JavaScript 函数来接受回调并使其返回 promise。示例如下：

```javascript
const fs = require('fs')

const getFile = (fileName) => {
  return new Promise((resolve, reject) => {
    fs.readFile(fileName, (err, data) => {
      if (err) {
        reject(err)  // 调用 `reject` 会导致 promise 失败，无论是否传入错误作为参数，
        return        // 且不再进行下去。
      }
      resolve(data)
    })
  })
}

getFile('/etc/passwd')
.then(data => console.log(data))
.catch(err => console.error(err))
```

getFile函数是普通函数，但它的结果返回了一个Promise，因此getFile函数执行时可以使用消费函数。在NodeJs中 util 模块中有一个 promisifying 函数可以完成此操作。

### 消费或使用

```javascript
const isItDoneYet = new Promise(/* ... 如上所述 ... */)
//...

const checkIfItsDone = () => {
  isItDoneYet
    .then(ok => {
      console.log(ok)
    })
    .catch(err => {
      console.error(err)
    })
}
```

运行 `checkIfItsDone()` 会**指定**当 `isItDoneYet` promise 被解决（在 `then` 调用中）或被拒绝（在 `catch` 调用中）**时执行的函数**。

### 链式Promise

Promise可以被消费和返回，Promise调用时执行的函数也可以返回Promise，就可以形成一个链式，产生回调函数嵌套的效果但不是嵌套。

### 处理错误

当 promise 链中的任何内容失败并引发错误或拒绝 promise 时，则控制权会转到链中最近的 `catch()` 语句。

```javascript
new Promise((resolve, reject) => {
  throw new Error('错误')
}).catch(err => {
  console.error(err)
})

// 或

new Promise((resolve, reject) => {
  reject('错误')
}).catch(err => {
  console.error(err)
})
```

### 级联错误

如果在 `catch()` 内部引发错误，则可以附加第二个 `catch()`来处理，依此类推。

```javascript
new Promise((resolve, reject) => {
  throw new Error('错误')
})
  .catch(err => {
    throw new Error('错误')
  })
  .catch(err => {
    console.error(err)
  })
```

### 编排 promise

#### promise.all()

如果需要同步不同的 promise，则 `Promise.all()` 可以帮助定义 promise 列表，并在所有 promise 都被解决后执行一些操作。

```javascript
const f1 = fetch('/something.json')
const f2 = fetch('/something2.json')

Promise.all([f1, f2])
  .then(res => {
    console.log('结果的数组', res)
  })
  .catch(err => {
    console.error(err)
  })
```

ES2015 解构赋值语法也可以执行：

```javascript
Promise.all([f1, f2]).then(([res1, res2]) => {
  console.log('结果', res1, res2)
})
```

当然，不限于使用 `fetch`，任何 promise 都可以以这种方式使用。

#### promise.rece()

当传给其的首个 promise 被解决时，则 `Promise.race()` 开始运行，并且只运行一次附加的回调（传入第一个被解决的 promise 的结果）。

```javascript
const first = new Promise((resolve, reject) => {
  setTimeout(resolve, 500, '第一个')
})
const second = new Promise((resolve, reject) => {
  setTimeout(resolve, 100, '第二个')
})

Promise.race([first, second]).then(result => {
  console.log(result) // 第二个
})
```

### 常见错误

Uncaught TypeError: undefined is not a promise

如果在控制台中收到 `Uncaught TypeError: undefined is not a promise` 错误，则请确保使用 `new Promise()` 而不是 `Promise()`。

UnhandledPromiseRejectionWarning

这意味着调用的 promise 被拒绝，但是没有用于处理错误的 `catch`。 在 `then` 之后添加 `catch` 则可以正确地处理。

## Async和Await

### 来由和原理

异步函数是 promise 和生成器的组合，基本上，它们是 promise 的更高级别的抽象。 而 async/await 建立在 promise 之上。它们减少了 promises 的样板，且减少了 promise 链的“不破坏链条”的限制。Promise 被引入了用于解决著名的回调地狱问题，但是它们自身引入了复杂性以及语法复杂性。它们使代码看起来**像是同步**的，但它是异步的并且在后台无阻塞。**异步函数会返回 promise:**

```javascript
const doSomethingAsync = () => {
  return new Promise(resolve => {
    setTimeout(() => resolve('做些事情'), 3000)
  })
}

const doSomething = async () => {
  console.log(await doSomethingAsync())
}

console.log('之前')
doSomething()
console.log('之后')
```

在任何函数之前加上 `async` 关键字意味着该函数会返回 promise。也就可以用then等消费方法。多个异步函数串联也变得易读，

## 事件触发器(events)

events 模块只提供了一个对象： events.EventEmitter，EventEmitter 的核心就是事件触发与事件监听器功能的封装。

```javascript
const EventEmitter = require('events')
const eventEmitter = new EventEmitter()
```

该对象公开了 `on` 和 `emit` 方法。

- `emit` 用于触发事件。
- `on` 用于添加回调函数（会在事件被触发时执行）。

```javascript
const EventEmitter = require('events')
const door = new EventEmitter()

//event.js 文件
var EventEmitter = require('events').EventEmitter;
var event = new EventEmitter();
event.on('some_event', function() {
 console.log('some_event 事件触发');
});
setTimeout(function() {
 event.emit('some_event');
}, 1000);
```

事件监听器返回及使用以下事件：

- 当监听器被添加时返回 `newListener`。
- 当监听器被移除时返回 `removeListener`。

| 常用方法                                         | 描述                                                         |
| :----------------------------------------------- | :----------------------------------------------------------- |
| **addListener**(event, listener)                 | 为指定事件添加一个监听器到监听器数组的尾部。                 |
| **once**(event, listener)                        | 为指定事件注册一个监听器，接受一个字符串 event 和一个回调函数。 |
| **removeListener**(All)(event, listener) / off() | 移除指定事件的某个监听器，监听器 必须是该事件已经注册过的监听器。 |

## HTTP服务

### 搭建服务器

```javascript
const http = require('http')

const port = 3000

const server = http.createServer((req, res) => {
  res.statusCode = 200
  res.setHeader('Content-Type', 'text/plain')
  res.end('你好世界\n')
})

server.listen(port, () => {
  console.log(`服务器运行在 http://${hostname}:${port}/`)
})
//或者
var http = require('http');

http.createServer(function (request, response) {

 // 发送 HTTP 头部
 // HTTP 状态值: 200 : OK
 // 内容类型: text/plain
 response.writeHead(200, {'Content-Type': 'text/plain'});

 // 发送响应数据 "Hello World"
 response.end('Hello World\n');
}).listen(8888);

// 终端打印如下信息
console.log('Server running at http://127.0.0.1:8888/');
```

传入的回调函数会在每次接收到请求时被执行。http.createServer(option,requestListener(request, response));`requestListener` 是一个函数，会被自动添加到request事件。 每当接收到新的请求时，request 事件会被调用，并提供两个对象：一个请求（http.IncomingMessage 对象）和一个响应（http.ServerResponse 对象）。

- request 提供了请求的详细信息。 通过它可以访问请求头和请求的数据。

- response 用于构造要返回给客户端的数据。

常用属性：

`request.url` 获取到端口号往后，哈希值之前的一段url，借助URL模块对其解析之后使用。

`request.method` 请求方式

`response` : 用于对前端的请求作出回应

常用方法：

`end`(内容)：string 或 buffer 只写一次

`write`(内容)：string 或 buffer 可以写多次

`writeHead`(状态码，配置对象)：向客户端发送响应头，不设置则默认浏览器自带响应头，响应头告知我们返回数据格式以及编码

### 发送HTTP请求

#### 执行 GET 请求

```javascript
const https = require('https')
const options = {
  hostname: 'nodejs.cn',
  port: 443,
  path: '/todos',
  method: 'GET'
}

const req = https.request(options, res => {
  console.log(`状态码: ${res.statusCode}`)

  res.on('data', d => {
    process.stdout.write(d)
  })
})

req.on('error', error => {
  console.error(error)
})

req.end()
```

### 执行 POST 请求

有多种方式可以执行 HTTP POST 请求，具体取决于要使用的抽象级别。

```javascript
const https = require('https')

const data = JSON.stringify({
  todo: '做点事情'
})

const options = {
  hostname: 'nodejs.cn',
  port: 443,
  path: '/todos',
  method: 'POST',
  headers: {
    'Content-Type': 'application/json',
    'Content-Length': data.length
  }
}

const req = https.request(options, res => {
  console.log(`状态码: ${res.statusCode}`)

  res.on('data', d => {
    process.stdout.write(d)
  })
})

req.on('error', error => {
  console.error(error)
})

req.write(data)
req.end()
```

最简单的是使用 Axios 库

```javascript
const axios = require('axios')

axios
  .post('http://nodejs.cn/todos', {
    todo: '做点事情'
  })
  .then(res => {
    console.log(`状态码: ${res.statusCode}`)
    console.log(res)
  })
  .catch(error => {
    console.error(error)
  })
```

PUT 和 DELETE 请求使用相同的 POST 请求格式，只需更改 `options.method` 的值即可。

### 获取HTTP请求正文

当使用 `http.createServer()` 初始化 HTTP 服务器时，服务器会在获得所有 HTTP 请求头（而不是请求正文时）时调用回调。在连接回调中传入的 `request` 对象是一个流。因此，必须监听要处理的主体内容，并且其是按数据块处理的。首先，通过监听流的 `data` 事件来获取数据，然后在数据结束时调用一次流的 `end` 事件：

```javascript
const server = http.createServer((req, res) => {
  // 可以访问 HTTP 请求头
  req.on('data', chunk => {
    console.log(`可用的数据块: ${chunk}`)
  })
  req.on('end', () => {
    //数据结束
  })
})
//因此，若要访问数据（假设期望接收到字符串），则必须将其放入数组中：
const server = http.createServer((req, res) => {
  let data = '';
  req.on('data', chunk => {
    data += chunk;
  })
  req.on('end', () => {
    JSON.parse(data).todo // '做点事情'
  })
})
```

## 文件模块

`fs` 模块提供了许多非常实用的函数来访问文件系统并与文件系统进行交互。 `fs` 模块的特殊之处是，所有的方法默认情况下都是异步的，但是通过在前面加上 `Sync` 也可以同步地工作。

### 文件描述符

文件描述符是使用 `fs` 模块提供的 `open()` 方法打开文件后返回的：

```javascript
const fs = require('fs')

fs.open('/Users/joe/test.txt', 'r', (err, fd) => {
  //fd 是文件描述符。
})
```

注意，将 `r` 作为 `fs.open()` 调用的第二个参数。该标志意味着打开文件用于读取。其他常用的标志有：

- `r+` 打开文件用于读写。
- `w+` 打开文件用于读写，将流定位到文件的开头。如果文件不存在则创建文件。
- `a` 打开文件用于写入，将流定位到文件的末尾。如果文件不存在则创建文件。
- `a+` 打开文件用于读写，将流定位到文件的末尾。如果文件不存在则创建文件。

也可以使用 `fs.openSync` 方法打开文件，该方法会返回文件描述符（而不是在回调中提供）：

```javascript
const fs = require('fs')

try {
  const fd = fs.openSync('/Users/joe/test.txt', 'r')
} catch (err) {
  console.error(err)
}
```

一旦获得文件描述符，就可以以任何方式执行所有需要它的操作，例如调用 `fs.open()` 以及许多与文件系统交互的其他操作。

### 文件属性

具体地说，使用 `fs` 模块提供的 `stat()` 方法。调用时传入文件的路径，一旦 Node.js 获得文件的详细信息，则会调用传入的回调函数，并带上两个参数：错误消息和文件属性：

```javascript
const fs = require('fs')
fs.stat('/Users/joe/test.txt', (err, stats) => {
  if (err) {
    console.error(err)
    return
  }
  //可以访问 `stats` 中的文件属性
  stats.isFile() //true
  stats.isDirectory() //false
  stats.isSymbolicLink() //false
  stats.size //1024000 //= 1MB
})

//Node.js 也提供了同步的方法，该方法会阻塞线程，直到文件属性准备就绪为止：

const fs = require('fs')
try {
  const stats = fs.statSync('/Users/joe/test.txt')
} catch (err) {
  console.error(err)
}
```

### 文件路径

`path` 模块提供了许多非常实用的函数来访问文件系统并与文件系统进行交互。在 Linux 和 macOS 上，路径可能类似于：

```plain
/users/joe/file.txt
```

在 Windows 上则有所不同，具有类似以下的结构：

```plain
C:\users\joe\file.txt
```

系统中的每个文件都有路径。而在不同的系统中，路径的表现方式有所不同，因此可引入path模块。以下的路径方法，其解析和规范化都不会检查路径是否存在。 其只是根据获得的信息来计算路径。

```javascript
const path = require('path')
const notes = '/users/joe/notes.txt'

path.dirname(notes) // /users/joe 获取文件的父文件夹。
path.basename(notes) // notes.txt  获取文件名部分。
path.extname(notes) // .txt 获取文件的扩展名。

//通过为 basename 指定第二个参数来获取不带扩展名的文件名
path.basename(notes, path.extname(notes)) //notes

//path.join() 连接路径的两个或多个片段
const name = 'joe'
path.join('/', 'users', name, 'notes.txt') //'/users/joe/notes.txt'

//path.resolve() 获得相对路径的绝对路径计算：
path.resolve('joe.txt') //'/Users/joe/joe.txt' 如果从主文件夹运行。

//Node.js 只是简单地将 /joe.txt 附加到当前工作目录。 如果指定第二个文件夹参数，则 resolve 会使用第一个作为第二个的基础
path.resolve('tmp', 'joe.txt') //'/Users/joe/tmp/joe.txt' 如果从主文件夹运行。

//第一个参数以斜杠开头，则表示它是绝对路径
path.resolve('/etc', 'joe.txt') //'/etc/joe.txt'

//path.normalize() 是另一个有用的函数，当包含诸如 .、.. 或双斜杠之类的相对说明符时，其会尝试计算实际的路径：
path.normalize('/users/joe/..//test.txt') //'/users/test.txt'

require('path').parse('/users/test.txt')
//输出是
{
  root: '/',
  dir: '/users',
  base: 'test.txt',
  ext: '.txt',
  name: 'test'
}
```

### 读取文件

使用`fs.readFile()` 方法，向其传入文件路径、编码、以及会带上文件数据（以及错误）进行调用的回调函数：

```javascript
const fs = require('fs')

fs.readFile('/Users/joe/test.txt', 'utf8' , (err, data) => {
  if (err) {
    console.error(err)
    return
  }
  console.log(data)
})
```

也可以使用同步的版本 `fs.readFileSync()`

```javascript
const fs = require('fs')

try {
  const data = fs.readFileSync('/Users/joe/test.txt', 'utf8')
  console.log(data)
} catch (err) {
  console.error(err)
}
```

`fs.readFile()` 和 `fs.readFileSync()` **都会在返回数据之前将文件的全部内容读取到内存中。**这意味着大文件会对内存的消耗和程序执行的速度产生重大的影响。在这种情况下，**更好的选择是使用流来读取文件的内容。**

### 写入文件

使用 `fs.writeFile()` API

```javascript
const fs = require('fs')

const content = '一些内容'

fs.writeFile('/Users/joe/test.txt', content, err => {
  if (err) {
    console.error(err)
    return
  }
  //文件写入成功。
})
```

也可以使用同步的版本 `fs.writeFileSync()`：

```javascript
const fs = require('fs')

const content = '一些内容'

try {
  const data = fs.writeFileSync('/Users/joe/test.txt', content)
  //文件写入成功。
} catch (err) {
  console.error(err)
}
```

默认情况下，此 API 会替换文件的内容（如果文件已经存在）。可以通过指定标志来修改默认的行为：

```javascript
fs.writeFile('/Users/joe/test.txt', content, { flag: 'a+' }, err => {})
```

将内容追加到文件末尾的便捷方法是 `fs.appendFile()`（及其对应的 `fs.appendFileSync()`）：

```javascript
const content = '一些内容'

fs.appendFile('file.log', content, err => {
  if (err) {
    console.error(err)
    return
  }
  //完成！
})
```

所有这些方法都是在将全部内容写入文件之后才会将控制权返回给程序（在异步的版本中，这意味着执行回调）。

### 使用文件夹

使用 `fs.access()` 检查文件夹是否存在以及 Node.js 是否具有访问权限。

使用 `fs.mkdir()` 或 `fs.mkdirSync()` 可以创建新的文件夹。

```javascript
const fs = require('fs')

const folderName = '/Users/joe/test'

try {
  if (!fs.existsSync(folderName)) {
    fs.mkdirSync(folderName)
  }
} catch (err) {
  console.error(err)
}
```

使用 `fs.readdir()` 或 `fs.readdirSync()` 可以读取目录的内容。

```javascript
const fs = require('fs')
const path = require('path')
const folderPath = '/Users/joe'
fs.readdirSync(folderPath)
//获取完整的路径：
fs.readdirSync(folderPath).map(fileName => {
  return path.join(folderPath, fileName)
})
//也可以过滤结果以仅返回文件（排除文件夹）：
const isFile = fileName => {
  return fs.lstatSync(fileName).isFile()
}

fs.readdirSync(folderPath).map(fileName => {
  return path.join(folderPath, fileName)
})
.filter(isFile)
```

使用 `fs.rename()` 或 `fs.renameSync()` 可以重命名文件夹。 第一个参数是当前的路径，第二个参数是新的路径：

```javascript
const fs = require('fs')

fs.rename('/Users/joe', '/Users/roger', err => {
  if (err) {
    console.error(err)
    return
  }
  //完成
})
```

`fs.renameSync()` 是同步的版本：

```javascript
const fs = require('fs')

try {
  fs.renameSync('/Users/joe', '/Users/roger')
} catch (err) {
  console.error(err)
}
```

使用 `fs.rmdir()` 或 `fs.rmdirSync()` 可以删除文件夹。删除包含内容的文件夹可能会更复杂。安装 `fs-extra`模块，在此示例中，需要的是 `remove()` 方法。

```bash
npm install fs-extra
```

```javascript
const fs = require('fs-extra')

const folder = '/Users/joe'

fs.remove(folder, err => {
  console.error(err)
})
```

也可以与 promise 一起使用：

```javascript
fs.remove(folder)
  .then(() => {
    //完成
  })
  .catch(err => {
    console.error(err)
  })
```

或使用 async/await：

```javascript
async function removeFolder(folder) {
  try {
    await fs.remove(folder)
    //完成
  } catch (err) {
    console.error(err)
  }
}

const folder = '/Users/joe'
removeFolder(folder)
```

## Buffer

Buffer 被引入用以帮助开发者处理二进制数据，在此生态系统中传统上只处理字符串而不是二进制数据。Buffer 与流紧密相连。 当流处理器接收数据的速度快于其消化的速度时，则会将数据放入 buffer 中。

使用 Buffer.from()、Buffer.alloc() 和 Buffer.allocUnsafe() 方法可以创建 buffer，Buffer（字节数组）可以像数组一样被访问

```javascript
const buf = Buffer.from('Hey!')
//const buf = Buffer.alloc(1024)
//使用buffer
buf[2]
//输出为字符串
buf.toString()
//输出长度
buf.length

forEach 或 for..of迭代buffer内容
write() //写入buffer
copy() //复制buffer
slice(beginPos,endPos) //切片buffer

//Buffer.from(array)
//Buffer.from(arrayBuffer[, byteOffset[, length]])
//Buffer.from(buffer)
//Buffer.from(string[, encoding])
```

## Stream

### 来由和使用

例如，在传统的方式中，当告诉程序读取文件时，这会将文件从头到尾读入内存，然后进行处理。使用流，则可以逐个片段地读取并处理（而无需全部保存在内存中）。**Node中， 所有的流都是 EventEmitter 的实例。**

相对于使用其他的数据处理方法，流基本上提供了两个主要优点：

- **内存效率**: 无需加载大量的数据到内存中即可进行处理。
- **时间效率**: 当获得数据之后即可立即开始处理数据，这样所需的时间更少，而不必等到整个数据有效负载可用才开始。

```javascript
const http = require('http')
const fs = require('fs')

const server = http.createServer((req, res) => {
  const stream = fs.createReadStream(__dirname + '/data.txt')
  stream.pipe(res)
})
server.listen(3000)
```

在文件流上调用 `pipe()` 方法。它获取来源流，并将其通过管道传输到目标流。在该示例中，文件流通过管道传输到 HTTP 响应。`pipe()` 方法的返回值是目标流，因此可以链接多个 `pipe()` 调用

```javascript
src.pipe(dest1).pipe(dest2)
//等同于
src.pipe(dest1)
dest1.pipe(dest2)
```

### 不同类型的流

- `Readable`: 可以通过管道读取、但不能通过管道写入的流（可以接收数据，但不能向其发送数据）。 当推送数据到可读流中时，会对其进行缓冲，直到使用者开始读取数据为止。
- `Writable`: 可以通过管道写入、但不能通过管道读取的流（可以发送数据，但不能从中接收数据）。
- `Duplex`: 可以通过管道写入和读取的流，基本上相对于是可读流和可写流的组合。
- `Transform`: 类似于双工流、但其输出是其输入的转换的转换流。

## 开发环境与生产环境

Node.js 假定其始终运行在开发环境中。 可以通过设置 `NODE_ENV=production` 环境变量来向 Node.js 发出正在生产环境中运行的信号。

通过在 shell 中执行命令，export NODE_ENV=production，最好将其放在的 shell 配置文件中（例如，使用 Bash shell 的 `.bash_profile`），否则当系统重启时，该设置不会被保留。

设置环境为 `production` 通常可以确保：

- 日志记录保持在最低水平。
- 更高的缓存级别以优化性能。

可以使用条件语句在不同的环境中执行代码：

```javascript
if (process.env.NODE_ENV === "development") {
  //...
}
if (process.env.NODE_ENV === "production") {
  //...
}
if(['production', 'staging'].indexOf(process.env.NODE_ENV) >= 0) {
  //...
})
```

## 异常处理

在 Node.js 中，我们不抛出字符串，而仅抛出 Error 对象。错误对象是 Error 对象的实例、或者继承自 Error 类

```javascript
throw new Error('错误信息')
```

异常处理是几种常规情况的总结，处理方法伪代码：

```javascript
try {
  //代码行
} catch (e) {}

//捕获未捕获的异常
process.on('uncaughtException', err => {
  console.error('有一个未捕获的错误', err)
  process.exit(1) //强制性的（根据 Node.js 文档）
})

// Promise错误：处理所调用的每个函数（doSomethingX）中的错误（使用try..catch），并且在错误处理程序内部抛出新的错误，这就会调用外部的 catch 处理程序

// 为了能够在本地（而不是在调用的函数中）处理错误，则可以断开链条，在每个 then() 函数中创建函数并处理异常

async/await 的错误处理:
async function someFunction() {
  try {
    await someOtherFunction()
  } catch (err) {
    console.error(err.message)
  }
}
```

## Node中常用的插件及其作用

- jquery
- formidable
- express
- body-parser
- cors
- mongoose
- nodemon
- progress
- cowsay
- axios
- nrm
- chalk
- multer
- cookie-parser
- debug
- jade
- http-errors
- morgan
- koa
- qs
- classnames
  
## 面试题

- 什么是模版引擎
- 什么是内存泄漏，原因及解决
- node程序间交互
