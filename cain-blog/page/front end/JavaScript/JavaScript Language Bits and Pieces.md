---
title: JavaScript语言零碎
date: 2020-03-20
updated: 2020-05-19
tags:
- 正则表达式
- JavaScript
categories:
- JavaScript
---


每个宏任务之后，引擎会立即执行微任务队列中的所有任务，然后再执行其他的宏任务，或渲染，或进行其他任何操作。

如果我们想要异步执行（在当前代码之后）一个函数，但是要在更改被渲染或新事件被处理之前执行，那么我们可以使用 queueMicrotask 来对其进行安排（schedule）。

好用的油猴脚本：
YouTube 广告
贴吧页面
精简jQuery API 中文文档
计时器掌控者|视频广告跳过|视频广告加速器
Pixiv 增强
阅读全文、自动展开全文、自动移除万恶弹框
AC-百度
greasyfork.org/zh-CN/scripts/14178

Bilibili-Evolved
cdn.jsdelivr.net/gh/the1812/Bilibili-Evolved@master/bilibili-evolved.user.js

Github增强
greasyfork.org/zh-CN/scripts/412245

计时器掌控者
greasyfork.org/zh-CN/scripts/372673

B站番剧跳转樱花动漫
greasyfork.org/zh-CN/scripts/429332

CSDN去广告净化阅读
greasyfork.org/zh-CN/scripts/373457

知乎增强
greasyfork.org/zh-CN/scripts/419081

在 HTTP 协议中，500 Internal Server Error 是表示服务器端错误的响应状态码，意味着所请求的服务器遇到意外的情况并阻止其执行请求。

磨刀不误砍柴工，基础没完全掌握建议先打基础，不懂的知识点要先扫盲，切忌好高骛远。

vue可以继续深入，熟练了再用react，这里没有什么先后顺序，主要是学习要专注和深入，才会有积累，沉淀下来的东西无关乎技术框架，是造诣和心得，通用的。

win10 修改hosts文件并立即让其生效

1. 打开hosts所在的目录 ：Win+R->C:\windows\System32\drivers\etc
2. 使用Notepad++或者记事本以管理员身份打开hosts
3. 输入命令让该记录不用重启而立即生效：Win+R->cmd->ipconfig /flushdns

数据可视化方案基于 d3.js 进行二次开发封装。解耦可视化代码与软件主体部分，方便任意添加自定义可视化图形（echarts/highcharts/g2 等）

## and-design

Tabs组件是否支持[自定义路由](https://github.com/ant-design/ant-design/issues/342)(使用react-router)？

```react
<TabPane tab={<Link to="path/to">链接</Link>} key="3"></TabPane>
```

## 垃圾回收的基本算法被称为 “mark-and-sweep”

定期执行以下“垃圾回收”步骤：

垃圾收集器找到所有的根，并“标记”（记住）它们。
然后它遍历并“标记”来自它们的所有引用。
然后它遍历标记的对象并标记 它们的 引用。所有被遍历到的对象都会被记住，以免将来再次遍历到同一个对象。
……如此操作，直到所有可达的（从根部）引用都被访问到。
没有被标记的对象都会被删除。

优化建议：
分代收集（Generational collection）
增量收集（Incremental collection）
闲时收集（Idle-time collection）

垃圾回收是自动完成的，我们不能强制执行或是阻止执行。

[V8 之旅：垃圾回收](http://jayconrod.com/posts/55/a-tour-of-v8-garbage-collection)
[V8 博客](http://v8project.blogspot.com/)

## 序列化（serialize）

如果一个对象具有 toJSON，那么它会被 JSON.stringify 调用。
完整格式：
let json = JSON.stringify(value[, replacer, space])
value：要编码的值。
replacer：要编码的属性数组或映射函数 function(key, value)。
space：用于格式化的空格数量

let value = JSON.parse(str, [reviver]);
str：要解析的 JSON 字符串。
reviver：可选的函数 function(key,value)，该函数将为每个 (key, value) 对调用，并可以对值进行转换。

## 雅思IELTS备考培训课程

雅思考试分为听，说，读，写四项，单项总分9分。每个单项得分相加除以四取平均值，四舍五入，最终总分满分为9分。雅思的成绩有效期为2年，成绩没有及格与不及格之分，只有高低分的区分。考生必须年满16岁，母语为非英语。相较中国的雅思考试，考生更容易在新加坡的雅思考试中取得5.0-7.5分。考生在留学新加坡期间直接获取雅思分数为后续升学的可能性提前做准备，同时新加坡的雅思成绩认可度比中国更高。

雅思考试分类：
学术类（A类）— 适合准备出国留学的同学
培训类（G类）— 适用于计划在英语国家（如澳大利亚、加拿大、新西兰及英国）参加工作或移民，或申请培训及非文凭类课程的人士。

## Settings Sync Shortcuts

1. Upload Key : Shift + Alt + U
2. Download Key : Shift + Alt + D

```js
Extensions Added:
  2077-theme v1.5.3
  auto-close-tag v0.5.11
  auto-rename-tag v0.1.7
  beautify v1.5.0
  bracket-pair-colorizer v1.0.61
  chinese-clorem v0.0.2
  code-settings-sync v3.4.3
  cssrem v2.4.3
  es7-react-js-snippets v3.1.1
  html-css-class-completion v1.20.0
  JavaScriptSnippets v1.8.0
  json-tools v1.0.2
  LiveServer v5.6.1
  material-icon-theme v4.8.0
  npm-intellisense v1.4.0
  path-intellisense v2.4.0
  prettier-vscode v8.0.1
  super-comprehensive v1.9.8
  vscode-language-babel v0.0.31
  vscode-language-pack-zh-hans v1.58.8
  vscodeintellicode v1.2.14
  
  // npm包
    "clipboard": "2.0.4",
    "codemirror": "5.45.0",
    "driver.js": "0.9.5",
    "dropzone": "5.5.1",
    "file-saver": "2.0.1",
    "fuse.js": "3.4.4",
    "js-cookie": "2.2.0",
    "jsonlint": "1.6.3",
    "jszip": "3.2.1",
    "normalize.css": "7.0.0",
    "nprogress": "0.2.0",
    "path-to-regexp": "2.4.0",
    "screenfull": "4.2.0",
    "sortablejs": "1.8.4",
    "@toast-ui/editor": "^3.1.3",
    "vue-splitpane": "1.0.4",
    "vuedraggable": "2.20.0",
    "xlsx": "0.14.1"

https://www.npmjs.com/package/driver.js
https://fusejs.io/
https://clipboardjs.com/
https://github.com/pillarjs/path-to-regexp#readme
https://github.com/sindresorhus/screenfull#readme
https://codemirror.net/

npm install dingtalk-jsapi --save

import * as dd from 'dingtalk-jsapi';
Vue.prototype.$dd = dd;

 this.$dd.ready(() => {
    this.$ding.biz.navigation.setTitle({
      title: "你的导航栏标题", 
    })
  })
  
  //在main.js中
// 提交以后禁用按钮一段时间，防止重复提交
import Vue from 'vue'
Vue.directive('noMoreClick', {
  inserted(el, binding) {
    el.addEventListener('click', e => {
      el.classList.add('is-disabled')
      el.disabled = true
      setTimeout(() => {
        el.disabled = false
        el.classList.remove('is-disabled')
      }, 2000)//我这里设置的是2000毫秒也就是2秒
    })
  }
})


<el-button v-no-more-click type="primary" @click="submitForm('ruleForm')">确 定</el-button>
```

## header

contentType
contentType是網頁要送到Server的資料型態，若沒指定則預設為'application/x-www-form-urlencoded; charset=UTF-8'

dataType
dataType是網頁預期從Server接收的資料型態，若沒指定則jQuery會根據response的MIME type來推定為xml, json, script, html, text。

## 杀死进程

1. netstat -ano，列出所有端口的情况。
2. 查看被占用端口对应的PID，输入命令：netstat -aon|findstr "8080"，回车，记下最后一位数字，即PID
3. tasklist|findstr "2668"，回车，查看是哪个进程或者程序占用了2668端口
4. 打开任务管理器，点击查看--->选择列，将PID(进程标示符)前面的勾打上
5. 在任务管理器中选中该进程点击”结束进程“按钮，或者是在cmd的命令窗口中输入：taskkill /f /t /im TIM.exe。

## 蓝屏

## Windows

「操作一」

右键系统 C 盘，然后选择：属性、磁盘清理、点击 “清理系统文件”、然后在 “以前的 Windows 安装” 及 “临时Windows安装文件” 选项打勾并进行清理。

「操作二」

1）Win + R 键打开运行窗口输入 services.msc

2）找到 WindowsUpdate 服务项，右键选择禁用。

3）打开 C:\Windows\SoftwareDistribution，可以删除 DataStore 和 Download 文件夹下的所有临时更新文件。

4）按照 1、2 的步骤重新开启 WindowsUpdate 服务项。

「操作三」

Win + I 键然后选择：系统、存储、请开启使用 “存储感知” 及进入配置页面 ，选择 “删除以前版本的 Windows” 并点击立即清理。

## 浏览器获取资源到显示的简要过程

通过 HTTP(S) 协议获取到页面资源(HTML 文档，CSS 文件)
将 HTML 文档解析，构建为 DOM 树
计算 DOM 树上的 CSS 属性
根据 CSS 属性，对 DOM 树上的元素逐个渲染，得到内存中的位图
对位图进行合成
将位图绘制到界面上

上面的过程，流程上是一个串联操作，但实际执行时，是一个类似并行的操作，即下一个步骤并不会乖乖地等待前一个步骤全部完成后再开始，而是前一个步骤完成一部分后，下一个步骤会开始进行

### Shadow DOM

是一种浏览器技术，主要用于在 web 组件中封装变量和 CSS。Virtual DOM 则是一种由 Javascript 类库基于浏览器 API 实现的概念。

## 修复ios设备上滑动时候闪屏问题

## 插件

通常用来为 Vue 添加全局功能。插件的功能范围没有严格的限制——一般有下面几种：

- 添加全局方法或者 property。如：vue-custom-element
- 添加全局资源：指令/过滤器/过渡等。如 vue-touch
- 通过全局混入来添加一些组件选项。如 vue-router
- 添加 Vue 实例方法，通过把它们添加到 Vue.prototype 上实现一个库，提供自己的 API，同时提供上面提到的一个或多个功能。如 vue-router

## 事件循环算法

从 宏任务 队列（例如 “script”）中出队（dequeue）并执行最早的任务。
执行所有 微任务：
当微任务队列非空时：
出队（dequeue）并执行最早的微任务。
执行渲染，如果有。
如果宏任务队列为空，则休眠直到出现宏任务。
转到步骤 1。
安排（schedule）一个新的 宏任务：

使用零延迟的 setTimeout(f)。
它可被用于将繁重的计算任务拆分成多个部分，以使浏览器能够对用户事件作出反应，并在任务的各部分之间显示任务进度。

此外，也被用于在事件处理程序中，将一个行为（action）安排（schedule）在事件被完全处理（冒泡完成）后。

安排一个新的 微任务：

使用 queueMicrotask(f)。
promise 处理程序也会通过微任务队列。
在微任务之间没有 UI 或网络事件的处理：它们一个立即接一个地执行。

所以，我们可以使用 queueMicrotask 来在保持环境状态一致的情况下，异步地执行一个函数。

## 属性标志

对象属性（properties），除 value 外，还有三个特殊的特性（attributes），也就是所谓的“标志”：

writable — 如果为 true，则值可以被修改，否则它是只可读的。
enumerable — 如果为 true，则会被在循环中列出，否则不会被列出。
configurable — 如果为 true，则此特性可以被删除，这些属性也可以被修改，否则不可以。

## 闭包

就是能够读取其他函数内部变量的函数，或者子函数在外调用，子函数所在的父函数的作用域不会被释放。

## 判断类型

typeof()，instanceof，Object.prototype.toString.call()等

## Vue2.x响应式数据原理/双向绑定原理

**数据变化更新视图，视图变化更新数据**。

- vue.js 则是采用数据劫持结合发布者-订阅者模式的方式。
- 劫持过程中 Observer遍历数据，通过`Object.defineProperty()来劫持各个属性的`setter`，`getter.
- Compile 解析模板，将变量替换成数据，给节点添加Watcher. Watcher是Observe与Compile之间桥梁，监听数据变化，在数据变动时触发Compile，以及相应的监听回调。
- 订阅器 Dep对Observe和Watcher进行统一管理

## WebSocket

身份验证和授权、保证消息可交付、可靠的消息排序、历史消息保留

## 高阶函数

返回一个函数；
能够解决 DRY [1] 问题。
React 高阶组件：

接收一个组件作为参数；
返回另一个组件；
返回的组件将渲染通过参数传递的组件；
能够解决 DRY [2] 问题。

## 字符串转化为驼峰格式

```js
function camelize(str) {
  return str.replace(/(?:^\w|[A-Z]|\b\w|\s+)/g, function(match, index) {
    if (+match === 0) return ""; // 或者对空格进行判断 if (/\s+/.test(match))
    return index === 0 ? match.toLowerCase() : match.toUpperCase();
  });
}
```

## 加速 Vue.js 应用的六种绝技

1. 只更新需要更新的内容 key 属性作为唯一标识帮助 Vue 去定位某个元素
2. 消除重复渲染
3. 优化事件监听 @mouseover 和 window.scroll  使用防抖函数来限制这些事件的处理次数
4. 删除或减少过慢的组件
5. 只渲染一次  v-once 指令
6. 虚拟滚动  vue-virtual-scroll-list

1、v-for 正确设置key值
2、封装复用的模块（http请求）、组件（ui库）
3、路由懒加载：component：() => import('./xxx.vue')
4、productionSourceMap: false
5、启用gzip压缩，打包体积更小
6、keep-alive 缓存不活跃组件
7、插件CDN方式引入，减小项目体积
8、图片使用CDN地址，图片懒加载

## Vue性能优化

尽量减少data中的数据，data中的数据都会增加getter和setter，会收集对应的watcherv-if和v-for不能连用如果需要使用v-for给每项元素绑定事件时使用事件代理SPA 页面采用keep-alive缓存组件在更多的情况下，使用v-if替代v-showkey保证唯一使用路由懒加载、异步组件防抖、节流第三方模块按需导入长列表滚动到可视区域动态加载图片懒加载
SEO优化
预渲染服务端渲染SSR
打包优化
压缩代码Tree Shaking/Scope Hoisting使用cdn加载第三方模块多线程打包happypacksplitChunks抽离公共文件sourceMap优化
用户体验
骨架屏PWA
还可以使用缓存(客户端缓存、服务端缓存)优化、服务端开启gzip压缩等。

## 纯函数

传入的参数相同时，函数总是返回相同的结果。
函数执行时不产生副作用。

高阶函数：
以一个或多个函数作为参数；
将一个函数作为返回值返回。

## 柯里化有什么好处？

在柯里化帮助下，你可以不用重复传入相同的参数。
它可以创建高阶函数，在处理事件时极为有用。
小段代码可以轻松地配置和复用。

```js
function curry(fn) {
    if (fn.length <= 1) return fn;
    const generator = (...args) => {
        if (fn.length === args.length) {
            return fn(...args)
        } else {
            return (...args2) => {

                return generator(...args, ...args2)
            }
        }
    }
    return generator
}
```

## HTTPS的工作原理

使用称为传输层安全（TLS）的加密通信协议，前身称为安全套接字层（SSL）。

此加密使用两个密钥，一个称为公钥，另一个称为私钥。

1、公钥：在浏览器和网站之间共享。
2、私钥:此密钥用于解密由公钥加密的信息，并且不会在服务器之外共享。
TSL使用案例是加密应用程序与服务器之间的通信、电子邮件、消息传递语音（VoIP）。

TLS握手🤝，步骤：
确定会话期间将使用的TLS版本。
通过使用TLS证书验证服务器的身份。
握手过程结束后，生成会话密钥供会话期间使用。

## 跨域资源共享

（CORS）是一种机制，它使用HTTP报头来指定哪些外源可以访问本地资源，以及如何访问它

## CSP（内容安全策略）

恶意攻击：跨站脚本（XSS），数据注入攻击，点击劫持
使用了指令概念，每个指令都必须指定可以从何处加载资源，从而防止浏览器从任何其他位置加载数据。

## Model-View-Controller

Model —— 负责应用的业务逻辑。它管理着应用的状态。这还包括读取和写入数据，持久化应用程序状态，甚至可能包括与数据管理相关的任务，例如网络和数据验证。
View —— 这部分有两个重要的任务：向用户展示数据和处理用户和应用的交互。
Controller —— view 层和 model 层经由一个或多个 controller 绑定在一起。

## Model–View–Presenter

MVP 是 MVC 设计模式的衍生品，该模式专注于改进展示逻辑。
Model —— model 代表一组描述业务逻辑和数据的类。它制定了更改和操作数据的规则。
View —— view 负责与用户进行交互，就如下图中 XML、Activity、fragments 部分。它与流程中要实现的逻辑无关。
Presenter —— presenter 从 View 获取输入，在 model 的帮助下处理数据，并在处理完成后将结果传递回 view。

## Model-View-ViewModel

MVVM 最初是由 Microsoft 提出的致力于将应用中 UI 的开发与业务逻辑的开发分离。
Model —— MVVM 中的 model 层和 MVC 中的 model 层非常相似，包含了供应用正常运转所需的基本数据。
View —— view 层是用户和设计模式之间的图形界面，类似于 MVC 中的 view 层。用来展示处理后的数据。
View-Model —— view-model 既是视图层的抽象，又提供了要访问的模型数据的包装。 也就是说，它包含一个可以被转换为视图的模型，并且还包含了一些命令，视图层可以使用这些命令更改模型。

MVVM 中的数据绑定会产生额外的过载
测试它们的兼容性时，MVVM 是其中最好的，这得益于它的数据绑定。
 对于 MVC，应用程序的入口是 controller，而对于 MVP 和 MVVM，入口是 view。

## 四种跨浏览器选项卡实时通信方法

1. 使用本地存储事件 LocalStorage
2. 使用 BroadcastChannel API 接口：允许选项卡、窗口、Frames、Iframes 和 Web Workers 之间的通信。safari 和 IE 不支持
3. 使用 Service Worker 发送消息
4. 使用 window.postMessage() 方法

## 虚拟 DOM 的工作方式

当用户 UI 发生变化时，将整个用户 UI 渲染到虚拟 DOM 中。
计算之前虚拟 DOM 和当前虚拟 DOM 表示形式之间的差异。
根据变化差异更新真实 DOM。

### 优点

高效的 diff 算法。
简单且有助于提升性能。
没有 React 也能使用。
轻量。
允许构建应用程序且不考虑状态转换。

### 缺点

使用 diff 算法，有必要检查所有步骤来识别变化。
增量 DOM 的工作方式
使用真实 DOM 来定位代码更改，真实 DOM 仅用于与新 DOM 树进行差异比较。主要思想是将每个组件编译成一组指令。然后，这些指令用于创建 DOM 树并对其进行更改。
优缺点：易于与许多其他框架结合使用。
简单的 API 使其成为强大的目标模板引擎。
适合基于移动设备的应用程序。
在大多数情况下，增量 DOM 不如虚拟 DOM 运行快。

## Cookie 的局限性

只能存储 4 KB 的数据，具体限制取决于浏览器
一个域下的 cookie 数量有限制，具体取决于浏览器（如 20 个）
跨域 cookie 的总数有限制，具体取决于浏览器（如 300 个）。一旦达到限制数量，为存储新的 cookie，最老的 cookie 将被移除。
Cookie 数据在每次请求时都被会发到服务器。这将消耗额外的带宽并影响性能。
可能被第三方读取数据（如第三方 cookie）

## 减少 Web 应用程序中 CORS 预检时间的 4 种方法

OPTIONS 请求也叫做预处理请求（pre-flight requests）

1. 使用浏览器的预检缓存
2. 使用代理、网关或负载均衡实现服务器端缓存
3. 使用代理、网关或负载均衡避免预检请求
4. 简单请求

简单请求：
请求方式应该是 GET，POST，或者 HEAD。
只允许携带几个固定的头信息，包括 Accept，Accept-Language，Content-Language，Content-Type，DPR，Downlink，Save-Data，Viewport-Width 和 Width。
尽管 Content-Type 头被允许, 它只限于application/x-www-form-urlencoded，multipart/form-data 和 text/plain。

经验法则:如果一个await函数没有使用另一个await函数调用的结果(或从结果到处东西)，你应该使用Promise.all()使它们同步执行。

## this

隐式绑定
显式绑定
new 绑定
window 绑定

### this的指向问题

（1）函数单独调用指向window
（2）new构造函数constructor 指向实例化对象
（3）对象或方法调用指向调用者
（4）事件触发指向触发事件的dom元素
（5）bind,apply,call 是自己绑定的this指向
（6）定时器指向window
（7）箭头函数 指向定义函数的上下文

## new 关键字

创建一个空的简单JavaScript对象（即{}）；
为步骤1新创建的对象添加属性__proto__，将该属性链接至构造函数的原型对象 ；
将步骤1新创建的对象作为this的上下文 ；
如果该函数没有返回对象，则返回this。

## 小程序支付流程

用户发起支付请求
后端调用统一下单接口得到 prepay_id
把支付所需参数返回前端
前端调用支付接口进行支付操作
支付结果通知
前端根据不同的支付结果给用户不同的提示

pointer-events CSS 属性指定在什么情况下 (如果有) 某个特定的图形元素可以成为鼠标事件的 target

```json
vscode设置vue代码片段

首选项  — 用户片段 — 新建vue.json片段 — 粘贴以下片段 — 在Vue文件中输入vue 按tab即可

{
    "Print to console": {
        "prefix": "vue",
        "body": [
            "<template>",
            "    <div>\n",
            "    </div>",
            "</template>\n",
            "<script>",
            "export default {",
            "    props: {\n",
            "    },",
            "    data() {",
            "        return {\n",
            "        };",
            "    },",
            "    computed: {\n",
            "    },",
            "    created() {\n",
            "    },",
            "    mounted() {\n",
            "    },",
            "    watch: {\n",
            "    },",
            "    methods: {\n",
            "    },",
            "    components: {\n",
            "    },",
            "};",
            "</script>\n",
            "<style scoped lang=\"${1:scss}\">\n",
            "</style>\n",
        ],
        "description": "Create vue template"
    }
}
```

## 当 uni-app 遇见 vscode

全局安装 vue-cli 3.x — npm install -g @vue/cli
通过 CLI 创建 uni-app 项目 — vue create -p dcloudio/uni-preset-vue my-project
提示选择项目模板，初次体验建议选择 hello uni-app 项目模板
在vscode中打开项目 安装vetur
安装uni-app组件语法提示 — npm i @dcloudio/uni-helper-json
运行 — npm run dev:%PLATFORM%
发布 — npm run build:%PLATFORM%

## Chrome长截图

Windows 为 F12 检查页面, 按键Ctrl + Shift + P，输入命令 Capture full size screenshot，点击选项即刻实现

## 安装的jdk没有jre(JDK11以上版本)

以管理员身份运行cmd  首先，跳转到jdk安装目录  输入bin\jlink.exe --module-path jmods --add-modules java.desktop --output jre,回车

## IntelliJ IDEA配置Jdk

首先打开idea
1、点击File -->Project Structure；
2、点击左侧标签页SDKs选项，再点击左上角“+”，选择JDK；
3、在弹出框选择JDK安装路径，点击OK即可配置成功。

## 查找8080端口对应的进程

netstat --help 帮助
cmd下输入netstat -ao|findstr 8080，右边列表示该进程id
打开任务管理器，找到该进程id对应的进程，右键关闭该进程即可；另，使用tasklist /fi "pid eq 2240" 命令可查看进程号对应进程信息，使用taskkill /PID 2240命令可杀掉该进程

[微信登录流程](https://abigaleyu.co/2017/08/11/miniprograms-signin/ )

## 获取WIFI密码使用方法

一、以管理员权限运行CMD
二、输入命令：
for /f "skip=9 tokens=1,2 delims=:" %i in ('netsh wlan show profiles') do @echo %j | findstr -i -v echo | netsh wlan show profiles %j key=clear

## history

history.pushState() 和 history.replaceState() 均接收三个参数（state, title, url）
参数说明如下：
state：合法的 Javascript 对象，可以用在 popstate 事件中
title：现在大多浏览器忽略这个参数，可以直接用 null 代替
url：任意有效的 URL，用于更新浏览器的地址栏

## 日期限制范围

```vue
方法一

 :picker-options="pickerOptions"

pickerOptions: {
        disabledDate(time) {
          return time.getTime() > new Date().getTime()
        }
      },//限制日期选择范围

日期限制范围 方法二

<el-date-picker
    v-model="datePick"
    type="daterange"
    value-format='yyyy-MM-dd'
    size="small"
    :picker-options="pickerOptions"
    range-separator="至"
    start-placeholder="开始日期"
    end-placeholder="结束日期">
</el-date-picker>
data() {
  let _minTime = null
  let _maxTime = null
  return {
    datePick: [new Date().format("yyyy-MM-dd") ,new Date().format("yyyy-MM-dd")], //默认选择当天，看个人需求
    pickerOptions:{
      onPick(time){
        // 如果选择了只选择了一个时间
        if (!time.maxDate) {
          let timeRange = 6*24*60*60*1000  // 6天
          _minTime = time.minDate.getTime() - timeRange // 最小时间
          _maxTime = time.minDate.getTime() + timeRange // 最大时间
        // 如果选了两个时间，那就清空本次范围判断数据，以备重选
        } else {
          _minTime = _maxTime = null
        }
      },
      disabledDate(time) {
        // onPick后触发
        // 该方法会轮询当3个月内的每一个日期，返回false表示该日期禁选
        if(_minTime && _maxTime){
          return time.getTime() < _minTime || time.getTime() > _maxTime
        }
      }
    }
  }
}
```

```js
xAxis： {
    show: true,    // 是否显示 x 轴
    position: 'top',    // x 轴的位置（'top'，'bottom'） 
    type: 'category',    // 坐标轴类型
    nameRotate: 10,    // 坐标轴名字旋转，角度值
    inverse: false,    // 是否是反向坐标轴
    boundaryGap: ['20%', '20%'],    // 坐标轴两边留白策略
    splitNumber: 5,    // 坐标轴的分割段数（预估值）
    axisLine: {
        show: true,    // 是否显示坐标轴轴线
        symbol: ['none', 'arrow'],     // 轴线两端箭头，两个值，none表示没有箭头，arrow表示有箭头
        symbolSize: [10, 15],     // 轴线两端箭头大小，数值一表示宽度，数值二表示高度
        lineStyle: {
            color: '#333',    // 坐标轴线线的颜色
            width: '5',    // 坐标轴线线宽
            type: 'solid',     // 坐标轴线线的类型（'solid'，实线类型；'dashed'，虚线类型；'dotted',点状类型）
        },
    },
    axisTick: {
        show: true,    // 是否显示坐标轴刻度
        inside: true,     // 坐标轴刻度是否朝内，默认朝外
        length: 5,    // 坐标轴刻度的长度
        lineStyle: {
            color: '#FFF',     // 刻度线的颜色
            width: 10,    // 坐标轴刻度线宽
            type: 'solid',     // 坐标轴线线的类型（'solid'，实线类型；'dashed'，虚线类型；'dotted',点状类型）
        },
    },
    axisLabel: {
        show: true,     // 是否显示刻度标签
        interval: '0',    // 坐标轴刻度标签的显示间隔，在类目轴中有效.0显示所有
        inside: true,    // 刻度标签是否朝内，默认朝外
        rotate: 90,    // 刻度标签旋转的角度，在类目轴的类目标签显示不下的时候可以通过旋转防止标签之间重叠；旋转的角度从 -90 度到 90 度
        margin: 10,    // 刻度标签与轴线之间的距离
        // formatter 刻度标签的内容格式器，支持字符串模板和回调函数两种形式
        color: '#FFF',     // 刻度标签文字的颜色
        fontStyle: 'normal',    // 文字字体的风格（'normal'，无样式；'italic'，斜体；'oblique'，倾斜字体） 
        fontWeight: 'normal',    // 文字字体的粗细（'normal'，无样式；'bold'，加粗；'bolder'，加粗的基础上再加粗；'lighter'，变细；数字定义粗细也可以，取值范围100至700）
        fontSize: '20',    // 文字字体大小
        align: 'left',     // 文字水平对齐方式，默认自动（'left'，'center'，'right'）
        verticalAlign: 'left',    // 文字垂直对齐方式，默认自动（'top'，'middle'，'bottom'
        lineHeight: '50',    // 行高 ）
        backgroundColor: 'red',    // 文字块背景色，例：'#123234', 'red', 'rgba(0,23,11,0.3)'
    },
    splitLine: {
        show: true,    // 是否显示分隔线。默认数值轴显示，类目轴不显示
        interval: '0',    // 坐标轴刻度标签的显示间隔，在类目轴中有效.0显示所有
        color: ['#ccc'],    // 分隔线颜色，可以设置成单个颜色，也可以设置成颜色数组，分隔线会按数组中颜色的顺序依次循环设置颜色
        width: 3,    // 分隔线线宽
        type: 'solid',     // 坐标轴线线的类型（'solid'，实线类型；'dashed'，虚线类型；'dotted',点状类型）
    },
    splitArea: {
        show: true,    // 是否显示分隔区域
        interval: '0',    // 坐标轴刻度标签的显示间隔，在类目轴中有效.0显示所有
        areaStyle: {
            color: ['rgba(250,250,250,0.3)','rgba(200,200,200,0.3)'],    // 分隔区域颜色。分隔区域会按数组中颜色的顺序依次循环设置颜色。默认是一个深浅的间隔色
            opacity: 1,    // 图形透明度。支持从 0 到 1 的数字，为 0 时不绘制该图形
        },
    },
    data: {
        textStyle: {
            color: '#FFF',     // 文字的颜色
            fontStyle: 'normal',    // 文字字体的风格（'normal'，无样式；'italic'，斜体；'oblique'，倾斜字体） 
            fontWeight: 'normal',    // 文字字体的粗细（'normal'，无样式；'bold'，加粗；'bolder'，加粗的基础上再加粗；'lighter'，变细；数字定义粗细也可以，取值范围100至700）
            fontSize: '20',    // 文字字体大小
            align: 'left',     // 文字水平对齐方式，默认自动（'left'，'center'，'right'）
            verticalAlign: 'left',    // 文字垂直对齐方式，默认自动（'top'，'middle'，'bottom'
            lineHeight: '50',    // 行高 ）
            backgroundColor: 'red',    // 文字块背景色，例：'#123234', 'red', 'rgba(0,23,11,0.3)'
        },
    },
}
```

## 写代码水平的几个发展阶段

懵懂
会用
会看
会写
会想
总结

空值合并操作符`??`是一个逻辑操作符，当左侧的操作数为 null 或者 undefined 时，返回其右侧操作数，否则返回左侧操作数。

## IDE下React JSX无法自动补全的设置

前往vsCode用户设置的Json文件

1. `"emmet.triggerExpansionOnTab": true`
2. `"emmet.includeLanguages": { "javascript": "javascriptreact", "vue-html": "html", "plaintext": "jade" }`
3. `"emmet.syntaxProfiles": { "javascript": "jsx" },`

## PC适配的成型方案

用rem来做字体的适配，用srcset来做图片的响应式，宽度可以用rem，flex，栅格系统等来实现响应式，然后可能还需要利用媒体查询来作为响应式布局的基础，因此综合上面的实现方案，项目中实现响应式布局需要注意下面几点：
 • 设置viewport
 • 媒体查询
 • 字体的适配（字体单位）
 • 百分比布局
 • 图片的适配（图片的响应式）
 • 结合flex，grid，BFC，栅格系统等已经成型的方案
 • 流式布局（即百分比布局）和媒体查询混合使用
使用keepalive, 页面再次加载的时候不触发created，触发activated

更改数据用`Vue.set()`方法：`Vue.set( target, key, value )`
target：要更改的数据源(可以是对象或者数组)
key：要更改的具体数据
value ：重新赋的值

## nodesass安装问题

`<https://github.com/PanJiaChen/vue-element-admin/issues/24>`

`npm i node-sass sass-loader style-loader -D`

`npm i node-sass --sass_binary_site=<https://npm.taobao.org/mirrors/node-sass/>`

## 拖拽上传

`dragover` - 被拖动的对象进入目标容器时触发
`dragleave`- 被拖动的对象离开目标容器时触发
`drop` - 被拖动的对象进入目标容器，释放鼠标键时触发

## 上传进度条

```js
async uploadFile() {
    const form = new FormData()
    form.append('name', 'file')
    form.append('file', this.file)
    const res = await axios.post('/uploadfile', form, {
      onUploadProgress: progress => {
        this.uploadProgress = Number(
          ((progress.loaded / progress.total) * 100).toFixed(2)
        )
      }
    })
},
```

## 取消上传

```js
const CancelToken = axios.CancelToken;
const source = CancelToken.source();

axios.post('/upload', form, {
  cancelToken: source.token
})

source.cancel();
```

## #npm Global npm install location on windows?

```js
These are typical npm paths if you install a package globally:

Windows XP -             %USERPROFILE%\Application Data\npm\node_modules
Newer Windows Versions - %AppData%\npm\node_modules
or -                     %AppData%\roaming\npm\node_modules

```

## tailwind

```js

npm install -D tailwindcss@latest

// 安装 postcss7
npm install tailwindcss@npm:@tailwindcss/postcss7-compat @tailwindcss/postcss7-compat postcss@^7 autoprefixer@^9

// 创建两个文件，创建
npx tailwindcss init -p

// tailwind.config.js设置文件以删除未使用的样式
// tailwind.config.js
  module.exports = {
   purge: ['./index.html', './src/**/*.{vue,js,ts,jsx,tsx}'],
    darkMode: false, // or 'media' or 'class'
    theme: {
      extend: {},
    },
    variants: {
      extend: {},
    },
    plugins: [],
  }

// 在 CSS 中创建 Tailwind 的导入
/*./src/assts/style/tailwind.css*/
@tailwind base;
@tailwind components;
@tailwind utilities;

// src/main.js设置tailwind.css导入tailwind.css文件
import './src/assts/style/tailwind.css'
```

## element UI修改组件的默认样式，方法有三种

1. 新建`global.css`, 在`main.js`中引入`import "./assets/style/global.css";`
2. 混用本地和全局样式即： 同时使用有 `scoped` 和非`scoped` 样式。
3. 通过`deep`属性去控制, `>>>`或 `/deep/` 或 `::v-deep` 操作符  

## `ShadowSocks #VPN`

1. 打开chrome的设置页面（`chrome://settings/`），向下滚动找到“高级”按钮
2. 点击展开高级设置 > 系统 > 打开代理设置
3. 点击进入Internet属性设置的“连接”页面，找到“局域网设置”按钮
4. 点击按钮打开”局域网设置”功能页面，取消勾选“为LAN使用代理服务器”，然后按“确定”保存设置

## JavaScript 前端无法获取响应header的Content-Disposition字段

这里的暴露给外部，意思是让客户端可以访问得到，既可以在Network里看到，也可以在代码里获取到他们的值。content-disposition不在其中，所以即使服务器在协议回包里加了该字段，但因没“暴露”给外部，客户端就“看得到，吃不到”。

而响应首部 Access-Control-Expose-Headers 就是控制“暴露”的开关，它列出了哪些首部可以作为响应的一部分暴露给外部。

`<https://www.nowcoder.com/ta/huawei>`  
`<https://www.nowcoder.com/paper/list?mutiTagIds=239>`
第一个链接是我在牛客网的总结的华为机考考察知识点汇总
第二个链接是华为题库仿真题训练

## bugs

h5不能显示tooltip  
echarts自带运行环境的判断，而在h5环境中，竟然有全局变量wx。所以echarts自己判断环境就为wx，和普通html不一样。  
解决方式：在引用echarts之前设置 `window.wx = undefined`

在微信 API 回调函数内 this 指向的是微信 API，所以拿不到 `this.data.*/ this.*`值。
解决方式

1. 箭头函数（ ()=>{} ）
2. 在 “外部” 提前定义变量 接收 this

## JSON

值只能是以下几种数据格式，其他任何格式都会触发报错，例如 JavaScript 中的 undefined。

- 数字，包含浮点数和整数
- 字符串，需要包裹在双引号中
- Bool值，true 或者 false
- 数组，需要包裹在方括号中 []
- 对象，需要包裹在大括号中 {}
- Null
  
还需要注意的是 JSON 文件中无法使用注释，试图添加注释将会引发报错

## Web搜索技巧

1. 使用引号强制进行精确匹配搜索
2. And 运算符
3. 使用 OR 操作符获取搜索词中某一个的结果
4. - 操作符将排除包含搜索词的结果
5. 可以使用(*)通配符作为占位符，它将被任何单词或短语替换
6. 在一个单一的网站搜索site:freecodecamp.org
7. 查找特定的文件类型 filetype:pdf learn css
8. 搜索一个数字范围 ecmascript 2016..2018

## nvm

1. 管理员身份运行`cmd`，输入`where node`查看当前的node安装在哪个文件夹下，打开文件夹后将`node.exe`所在的文件夹内容全部清空
2. 安装`nvm`管理工具，打开nvm[下载](https://link.juejin.cn/?target=https%3A%2F%2Fgithub.com%2Fcoreybutler%2Fnvm-windows%2Freleases)之后，下载压缩包安装nvm
3. 输入`nvm v`查看当前版本号，如果成功出现版本号，则代表安装成功
4. 输入`nvm ls available`查看当前版本号
5. 安装对应版本：`nvm install 12.17.0`；使用命令：`nvm use 12.17.0`；`node -v` 查看是否成功
6. `nvm ls`查看已经安装的版本

可以通过`nvm install xxx`的操作，安装不同版本的node，再通过上述命令查看本地已经安装过的node版本，再`nvm use xxx`就可以快速切换node版本啦

## 文章

[CSS 思维模式](https://juejin.cn/post/6844903888836001806)  
[CSS3 Animation & Transitions Crash Course](https://www.youtube.com/watch?v=zHUpx90NerM)  
[基于 react 的移动端适配方案](https://blog.csdn.net/Greenhand_BN/article/details/104638288)  
[音乐播放器](https://github.com/honeydlp/react-audio/blob/master/audioSelf/index.js)  
[PDF转图片，PDF转JPG/PNG，完全由JS实现](https://developer.aliyun.com/article/716460)  
[PDF转图片示例](https://oktools.net/pdf2img?spm=a2c6h.12873639.0.0.1f9256dcoetcxp)  
[表单校验](https://xuezenghui.com/posts/vuelidate/)
[You-need-to-know-css](https://lhammer.cn/You-need-to-know-css/#/)
[可视化教程](https://www.yuque.com/mo-college)
[vue 组件缓存清除实践](https://www.jianshu.com/p/8cedd68d30ad)  
[git in Idea](https://mp.weixin.qq.com/s/v59o0Cj75nNyiifh2jPXbg)
[git命令](https://mp.weixin.qq.com/s/nwYKWNTv8SiX_hviWdxvDA)  
[经纬度算距离](https://itbilu.com/javascript/js/NJWhVH3Z-.html )  
[Element-UI的Switch 开关修改为字在按钮上](https://blog.csdn.net/sleepwalker_1992/article/details/85763951)

跨域文章：

- [跨域](<https://ningyu1.github.io/site/post/92-cors-ajax/>)
- [跨域](<https://blog.csdn.net/qq_31122833/article/details/109807367>)
- [跨域](<https://developer.aliyun.com/article/980740>)
- [跨域](<https://blog.csdn.net/weixin_44987346/article/details/122674108>)
- [跨域](<https://juejin.cn/post/7006216841353920519>)
- [跨域](<https://rualc.com/>)
- [跨域](<https://ts.yayujs.com/handbook/TheBasics.html#%E5%9F%BA%E7%A1%80-the-basics>)
- [跨域](<https://tech.meituan.com/2019/08/01/eslint-application-practice-in-medium-and-large-teams.html>)
- [跨域](<https://juejin.cn/post/7034692170351116296>)
- [跨域](<https://juejin.cn/post/6844903891675381768>)
