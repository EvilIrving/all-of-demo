---
title: JavaScript面试问题
date: 2021-07-30
updated: 2021-07-30
tags:
- 面试
- JavaScript
categories:
- 面试
keywords:
description:
cover:
---

## 题目：无答案

- JavaScript
  - 数据类型
  - CommonJS、AMD、CMD、ES Modules，模块加载机制
  - 原型、原型链、原型继承、this、apply、call、New操作符
  - 设计模式：单例、工厂、观察者
  - 跨域CORS
  - 图片格式：png、jpg、gif、webp
  - git和svn
  - 网络协议
  - 本地存储、Cookie及其之间的区别
  - 服务器和浏览器之间的 cookies 是怎么传的，httponly 的 cookies 和可读写的 cookie 有什么区别，有无长度限制
  - 各种位置的区别
  - 从URL 到渲染完成的整个过程，包括 DOM 构建的过程
  - 公钥加密和私钥加密
  - 文件编码、URL 编码、Unicode编码：一个gbk编码的页面如何正确引用一个utf8的的资源
  - 移动端适配方案、移动端300毫秒延迟、移动端点击穿透
  - Async 和 Defer 的作用和区别
  - DOMContentLoaded 事件和 Load 事件的区别
  - 需要注意哪些 SEO
  - 简述作用域链、执行上下文和执行栈
  - 介绍下 Set、Map、WeakSet 和 WeakMap 的区别
  - AJAX、常见HTTP状态码
  - GET和POST的不同
  - 闭包、其特性、影响
  - 常见的内存泄露
  - 事件委托、事件(流)模型（冒泡、捕获）；什么事件不支持冒泡，捕获
  - 普通函数、箭头函数和构造函数的区别
  - 如何理解Proxy
  - 垃圾回收机制
  - 手写代码
    - 深浅拷贝
    - 节流和防抖
    - 数组去重、数组扁平化去并除其中重复部分数据
    - 排序
    - Bind、Apply、Call、 New、Promise、Async/Await、Generator、柯里化、Instanceof、Proxy + Fetch == axios
    - 判断数据是NaN
    - 轮播图
    - 页面懒加载
    - 表单验证
    - 弹窗功能
    - 分页功能
- Typescript
  - Typescript 与 JavaScript 的优势
- Node
- Vue
  - 首屏加载慢、白屏时间检测及解决
  - 双向绑定的原理
  - 常用指令和自定义指令
  - 过滤器
  - Computed、Watch原理
  - 如何理解keep-alive
  - 组件间传值方式
  - 什么是 FOUC（无样式内容闪烁）
  - 性能优化？具体哪些优化的收益较大？
  - Compile过程
- 小程序
  - 小程序的页面构成/生命周期
  - 提高小程序的首屏加载时间
  - Wxss 与 css 的区别请简述
  - 小程序的双向绑定和 vue 的异同？
  - 页面间有哪些传递数据的方法
  - 微信扫一扫二维码网页上登陆前后端过程
  - 微信支付流程

- Uni-app
- React
  - 数据传递虚拟
  - dom 与 diff 算法
  - 调用 setState 之后发生了什么
  - 生命周期函数
  - 性能优化
  - react-redux实现原理
- Electron
- Flutter
- webpack
  - 打包原理
  - Webpack 与 Vite 区别
  - 热更新原理
- 个人
  - 技术栈哪个有优势
  - 项目中难点、亮点、解决方案
  - 职业想法和规划

## 题目：含答案

### 请解释事件委托（event delegation）

事件委托是将事件监听器添加到父元素，而不是每个子元素单独设置事件监听器。当触发子元素时，事件会冒泡到父元素，监听器就会触发。这种技术的好处是：

- 内存占用减少，因为只需要一个父元素的事件处理程序，而不必为每个后代都添加事件处理程序。
- 无需从已删除的元素中解绑处理程序，也无需将处理程序绑定到新元素上。

- <https://davidwalsh.name/event-delegate>
- <https://stackoverflow.com/questions/1687296/what-is-dom-event-delegation>

### 事件流模型

事件流模型表示的是一个事件触发的完整步骤，该步骤一共有三步：

1. 事件捕获：第一阶段，某个节点触发事件后，系统从根节点开始，一直向下寻找，直至真正触发该事件的节点，沿途在直系继承树上绑定了相同事件的节点会按照顺序依次触发；即同一事件，自父元素捕获至子元素（事件源元素）。自顶向下；

2. 事件触发：真正触发事件的节点，触发事件；

3. 事件冒泡：第三阶段，真正触发事件的节点触发完毕，会沿着直系继承树一直向上传递该事件，一直传递到根节点，沿途绑定了相同事件的节点会依次触发。即同一事件，自子元素冒泡向父元素。自底向上。

触发顺序：捕获 >> 触发 >> 冒泡

### 请简述`JavaScript`中的`this`

JS 中的`this`是一个相对复杂的概念，不是简单几句能解释清楚的。粗略地讲，函数的调用方式决定了`this`的值。我阅读了网上很多关于`this`的文章，[Arnav Aggrawal](https://medium.com/@arnav_aggarwal) 写的比较清楚。`this`取值符合以下规则：

1. 在调用函数时使用`new`关键字，函数内的`this`是一个全新的对象。
1. 如果`apply`、`call`或`bind`方法用于调用、创建一个函数，函数内的 this 就是作为参数传入这些方法的对象。
1. 当函数作为对象里的方法被调用时，函数内的`this`是调用该函数的对象。比如当`obj.method()`被调用时，函数内的 this 将绑定到`obj`对象。
1. 如果调用函数不符合上述规则，那么`this`的值指向全局对象（global object）。浏览器环境下`this`的值指向`window`对象，但是在严格模式下(`'use strict'`)，`this`的值为`undefined`。
1. 如果符合上述多个规则，则较高的规则（1 号最高，4 号最低）将决定`this`的值。
1. 如果该函数是 ES2015 中的箭头函数，将忽略上面的所有规则，`this`被设置为它被创建时的上下文。

### onkeydown、 onkeyup 、onkeypress

在網頁的鍵盤事件操作有三種 keydown, keypress, keyup，針對這三者之間的差異，以下就做個簡單的分享與介紹：

1. **keydown**
   所謂的keydown就是指按下鍵盤的那個剎那，任何的鍵盤按鍵按下都可以取得對應的鍵盤代碼，也就是所謂的keyCode。例如：a -> 65、b -> 66、 c -> 67……依此類推，但不區分大小寫。也就是說大寫和小寫是一樣的keyCode。其中也包括Enter -> 13、ESC -> 27、方向鍵 -> 37~40、倒退鍵 -> 8 ......等等。此外當按下鍵盤不放時，則會不斷地連續觸發該事件。
2. **keypress**
   與keydown不同在於，keypress只會針對可以輸出文字符號的按鍵有效，也就是說ESC、方向鍵、倒退鍵......等等。這類沒有辦法輸出文字的鍵無法觸發該事件。此外也會因為大小寫的不同取得不同的keyCode，舉例來說：a -> 97、A -> 65、b -> 98、B -> 66......依此類推。也和keydown一樣當按下鍵盤不放時，則會不斷地連續觸發該事件。
3. **keyup**
   而keyup是指放開鍵盤的那個剎那，觸發該事件。因為每次只會放開鍵盤一次，所以不會有連續觸發的狀況發生。取得keyCode的部分基本上跟keydown是一樣的。不過若是想要取得input的value，也只有在keyup的事件上可以拿到目前最新的value。

這三者事件的觸發優先順序為：keydown→keypress→keyup

### NodeList与HTMLCollection之间的区别

**NodeList：**`Node.childNodes` 是实时集合。在其他情况下，`NodeList` 是一个静态集合，也就意味着随后对文档对象模型的任何改动都不会影响集合的内容，如`document.querySelectorAll`;

**HTMLCollection：**`HTMLCollection` 接口表示一个包含了元素（元素顺序为文档流中的顺序）的通用集合**；**HTML DOM 中的 `HTMLCollection` 是即时更新的（live）；当其所包含的文档结构发生改变时，它会自动更新。

### 回流与重绘

在讨论回流与重绘之前，我们要知道：

浏览器使用流式布局模型 (Flow Based Layout)。
浏览器会把HTML解析成DOM，把CSS解析成CSSOM，DOM和CSSOM合并就产生了Render Tree。
有了RenderTree，我们就知道了所有节点的样式，然后计算他们在页面上的大小和位置，最后把节点绘制到页面上。
由于浏览器使用流式布局，对Render Tree的计算通常只需要遍历一次就可以完成，但table及其内部元素除外，他们可能需要多次计算，通常要花3倍于同等元素的时间，这也是为什么要避免使用table布局的原因之一。

一句话：回流必将引起重绘，重绘不一定会引起回流。

#### 回流 (Reflow)

当Render Tree中部分或全部元素的尺寸、结构、或某些属性发生改变时，浏览器重新渲染部分或全部文档的过程称为回流。
会导致回流的操作：

页面首次渲染
浏览器窗口大小发生改变
元素尺寸或位置发生改变
元素内容变化（文字数量或图片大小等等）
元素字体大小变化
添加或者删除可见的DOM元素
激活CSS伪类（例如：:hover）
查询某些属性或调用某些方法

#### 一些常用且会导致回流的属性和方法

clientWidth、clientHeight、clientTop、clientLeft
offsetWidth、offsetHeight、offsetTop、offsetLeft
scrollWidth、scrollHeight、scrollTop、scrollLeft
scrollIntoView()、scrollIntoViewIfNeeded()
getComputedStyle()
getBoundingClientRect()
scrollTo()

#### 重绘 (Repaint)

当页面中元素样式的改变并不影响它在文档流中的位置时（例如：color、background-color、visibility等），浏览器会将新样式赋予给元素并重新绘制它，这个过程称为重绘。

##### 性能影响

回流比重绘的代价要更高。
有时即使仅仅回流一个单一的元素，它的父元素以及任何跟随它的元素也会产生回流。

优化：

浏览器会维护一个队列，把所有引起回流和重绘的操作放入队列中，如果队列中的任务数量或者时间间隔达到一个阈值的，浏览器就会将队列清空，进行一次批处理，这样可以把多次回流和重绘变成一次。
当你访问以下属性或方法时，浏览器会立刻清空队列：

- clientWidth、clientHeight、clientTop、clientLeft
- offsetWidth、offsetHeight、offsetTop、offsetLeft
- scrollWidth、scrollHeight、scrollTop、scrollLeft
- width、height
- getComputedStyle()
- getBoundingClientRect()

因为队列中可能会有影响到这些属性或方法返回值的操作，即使你希望获取的信息与队列中操作引发的改变无关，浏览器也会强行清空队列，确保你拿到的值是最精确的。

#### 如何避免

##### CSS

1. 避免使用table布局。
2. 尽可能在DOM树的最末端改变class。
3. 避免设置多层内联样式。
4. 将动画效果应用到position属性为absolute或fixed的元素上。
5. 避免使用CSS表达式（例如：calc()）。

##### JavaScript

1. 避免频繁操作样式，最好一次性重写style属性，或者将样式列表定义为class并一次性更改class属性。
2. 避免频繁操作DOM，创建一个documentFragment，在它上面应用所有DOM操作，最后再把它添加到文档中。
3. 也可以先为元素设置display: none，操作结束后再把它显示出来。因为在display属性为none的元素上进行的DOM操作不会引发回流和重绘。
4. 避免频繁读取会引发回流/重绘的属性，如果确实需要多次使用，就用一个变量缓存起来。
5. 对具有复杂动画的元素使用绝对定位，使它脱离文档流，否则会引起父元素及后续元素频繁回流

### 滚轮事件浏览器兼容性

#### mousewheel事件(兼容opera,chrome,safari,IE)

mousewheel事件对应的event对象包含一个wheelDelta属性。用户滚动鼠标滚轮时，wheelDelta的值是正负120的倍数。一般情况下，监听wheelDelta的正负值，就可以确定鼠标滚轮的滚动方向。

```js
document.addEventListener('mousewheel',function(event){
    console.log( event.wheelDelta > 0 )
},false) 
```

#### DOMMouseScroll事件(兼容FireFox)

FireFox支持一个名为DOMMouseScroll的类似事件，类比于mousewheel。不同的是，鼠标滚轮的信息保存在detail属性里面。还有一点，用户滚动鼠标滚轮时，detail的值是正负3的倍数。

```js
document.addEventListener('DOMMouseScroll',function(event){
    console.log( event.detail > 0 )
},false)
```

### 跨域问题及其解决办法

如果两个 URL 具有相同的协议，域和端口，则称它们是“同源”的。，而“同源策略”的机制规定，同源下的文件可以相互通信，非同源下的不可以，如果非要通信，JS就报错---“跨域错误”。

#### 解决

- document.domain + iframe跨域
- location.hash + iframe
- window.name + iframe跨域
- postMessage跨域
- nginx代理跨域
- nodejs中间件代理跨域
- WebSocket协议跨域

### attr() 和 prop()区别

- attr()的内部原生JS实现set get方法；prop()的原生实现是直接的点语法
- 操作自带属性用prop()，自定义属性用attr()
- 操作属性名和属性值相同的属性时，使用prop()

- 特性（attribute）— 写在 HTML 中的内容。
- 属性（property）— DOM 对象中的内容。

简略的对比：

|      | 属性                                   | 特性                         |
| :--- | :------------------------------------- | :--------------------------- |
| 类型 | 任何值，标准的属性具有规范中描述的类型 | 字符串                       |
| 名字 | 名字（name）是大小写敏感的             | 名字（name）是大小写不敏感的 |

操作特性的方法：

- `elem.hasAttribute(name)` — 检查是否存在这个特性。
- `elem.getAttribute(name)` — 获取这个特性值。
- `elem.setAttribute(name, value)` — 设置这个特性值。
- `elem.removeAttribute(name)` — 移除这个特性。
- `elem.attributes` — 所有特性的集合。

在大多数情况下，最好使用 DOM 属性。仅当 DOM 属性无法满足开发需求，并且我们真的需要特性时，才使用特性。
