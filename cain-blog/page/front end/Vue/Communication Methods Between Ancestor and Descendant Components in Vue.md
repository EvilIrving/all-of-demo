---
title: vue中祖孙组件通信方法
date: 2023-02-10
tags:
  - Vue
  - Echarts
categories: Vue
keywords: 'Vue'
description: vue中祖孙组件通信方法
# cover: https://file.crazywong.com/gh/jerryc127/CDN/img/butterfly-docs-01-cover.png
---
## 祖孙通信

### [`Vuex`](https://segmentfault.com/a/1190000040050182)

### [`Events Bus`](https://segmentfault.com/a/1190000039860587)

### [`attrs` && `$listens`](https://segmentfault.com/a/1190000040317732)

#### 定义

官方定义：[`$attr`](https://cn.vuejs.org/guide/components/attrs.html) && [`$listeners`](https://v2.cn.vuejs.org/v2/api/#vm-listeners)

#### 例子

`$attr`:`$attrs`就是一个容器对象，这个容器对象会存放:父组件传过来的且子组件未使用`props`声明接收的数据

#### 逻辑

爷组件传递给孙组件的逻辑流程就是，通过爷组件首先传递给父组件，当然父组件不在`props`中接收，那么爷组件传递给父组件的数据就会存放到父组件的`$attrs`对象中里面了，然后，再通过`v-bind="$attrs"`，再把这个`$attr`传递给孙组件，在孙组件中使用`props`就能接收到`$attrs`中的数据了，这样就实现了，祖孙之间的数据传递

// $attrs一般搭配interitAttrs 一块使用
  inheritAttrs: false, // 默认会继承在html标签上传递过来的数据，类似href属性的继承

## [`Provide` && `Inject`](https://segmentfault.com/a/1190000020954324)
