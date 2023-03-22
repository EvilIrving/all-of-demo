---
title: Vue面试题
date: 2020-02-02 12:59:52
update: 2020-02-15
tags: 
- Vue
- 面试
categories: 
- Vue
---

- Vue2.x响应式数据原理/双向绑定原理
  - `vue.js` 则是采用数据劫持结合发布者-订阅者模式的方式。
  - 劫持过程中 Observer遍历数据，通过`Object.defineProperty()`来劫持各个属性的`setter`，`getter`.
  - Compile 解析模板，将变量替换成数据，给节点添加Watcher. Watcher是Observe与Compile之间桥梁，监听数据变化，在数据变动时触发Compile，以及相应的监听回调。
  - 订阅器 Dep对Observe和Watcher进行统一管理
- Vue3.x响应式数据原理
- query params
  - query传值方式path字段负责路由，query字段负责数据并改写为对象，数据以键值对形式拼接，刷新页面数据不消失，且不需要修改路由配置对象
- params方式以name字段匹配路由，params字段负责参数，数据只拼接值，形成路径参数，修改路由配置对象path字段后，刷新页面不消失。
- SPA
- SSR 服务端渲染
- 闪烁问题
- v-if v-show
- 计算属性与普通函数、侦听器的区别
- public和asset文件夹的区别
- 组件的data数据源为什么不能直接写成一个对象
- 消息监听和消息发送
- nextTick()方法的作用,和这个方法能解决什么
- Vue.2X Vue.3X响应式数据原理
- axios和ajax的区别
- Vuex和单纯的全局对象有什么区别？
- 为什么 Vuex 的 mutation 中不能做异步操作？
- vuex的action有返回值吗？返回的是什么？
- Vue2.x组件通信有哪些方式？
- 为什么 v-for 和 v-if 不建议用在一起
- 为什么不建议用index作为key?
- 生命周期定义、作用、场景、调用顺序
- vue路由hash模式和history模式
- 路由懒加载是什么意思？如何实现路由懒加载？
- Vue-router 导航守卫
- 模版编译原理
- diff算法
- active-class 是哪个组件的属性？
- route router routes
- 区分 state 是外部直接修改，还是通过 mutation 方法修改的
- 调试时的"时空穿梭"功能是如何实现的
- 自定义指令

### 动态参

用方括号括起来的 JavaScript 表达式作为一个指令的参数，

```vue
<a v-bind:[attributeName]="url"> ... </a>
```

#### 对动态参数的值的约束

动态参数预期会求出一个字符串，异常情况下值为 `null`。这个特殊的 `null` 值可以被显性地用于移除绑定。任何其它非字符串类型的值都将会触发一个警告。

#### 对动态参数表达式的约束

动态参数表达式有一些语法约束，因为某些字符，如空格和引号，放在 HTML attribute 名里是无效的。变通的办法是使用没有空格或引号的表达式，或用计算属性替代这种复杂表达式。

在 DOM 中使用模板时 (直接在一个 HTML 文件里撰写模板)，还需要避免使用大写字符来命名键名，因为浏览器会把 attribute 名全部强制转为小写

### 过滤器

自定义过滤器，可被用于一些常见的文本格式化。过滤器可以用在两个地方：**双花括号插值和 `v-bind` 表达式** (后者从 2.1.0+ 开始支持)。过滤器应该被添加在 JavaScript 表达式的尾部，由“管道”符号指示：

```js
<!-- 在双花括号中 -->
{{ message | capitalize }}

<!-- 在 `v-bind` 中 -->
<div v-bind:id="rawId | formatId"></div>
```

你可以在一个组件的选项中定义本地的过滤器：

```js
filters: {
  capitalize: function (value) {
    if (!value) return ''
    value = value.toString()
    return value.charAt(0).toUpperCase() + value.slice(1)
  }
}
```

或者在创建 Vue 实例之前全局定义过滤器：

```js
Vue.filter('capitalize', function (value) {
  if (!value) return ''
  value = value.toString()
  return value.charAt(0).toUpperCase() + value.slice(1)
})

new Vue({
  // ...
})
```

当全局过滤器和局部过滤器重名时，会采用局部过滤器。

过滤器函数总接收表达式的值 (之前的操作链的结果) 作为第一个参数。

过滤器可以串联：

```js
{{ message | filterA | filterB }}
 //filterA 的结果传递到 filterB
```

过滤器是 JavaScript 函数，因此可以接收参数：

```js
{{ message | filterA('arg1', arg2) }}
```

这里，`filterA` 被定义为接收三个参数的过滤器函数。其中 `message` 的值作为第一个参数，普通字符串 `'arg1'` 作为第二个参数，表达式 `arg2` 的值作为第三个参数。
