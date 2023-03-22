---
title: instanceof
date: 2023-02-22
updated: 2023-02-2
tags:
  - JavaScript
categories: JavaScript
keywords: 'instanceof'
description: instanceof
# cover: https://file.crazywong.com/gh/jerryc127/CDN/img/butterfly-docs-01-cover.png
---


## 使用

`instanceof` 操作符用于检查一个对象是否属于某个特定的 `class`

```js
 // 如果 obj 隶属于 Class 类（或 Class 类的衍生类），则返回 true。
obj instanceof Class
```

静态方法 `Symbol.hasInstance` 可以设置自定义逻辑。如下：

```
// 设置 instanceOf 检查
// 并假设具有 canEat 属性的都是 animal
class Animal {
  static [Symbol.hasInstance](obj) {
    if (obj.canEat) return true;
  }
}

let obj = { canEat: true };

console.log(obj instanceof Animal); // true：Animal[Symbol.hasInstance](obj) 被调用
```

另外：
方法 `objA.isPrototypeOf(objB)`，如果 `objA` 处在 `objB` 的原型链中，则返回 `true`。所以，可以将 `obj instanceof Class` 检查改为 `Class.prototype.isPrototypeOf(obj)`。

## 内部执行过程

* 如果有静态方法 `Symbol.hasInstance`，那就直接调用这个方法：
* 如果没有，使用 `obj instanceOf Class` 检查 `Class.prototype` 是否等于 `obj` 的原型链中的原型之一。

## 类型检查

|  | 用于 | 返回值 |
| :-----:| :----: | :----: |
| typeof | 原始数据类型 | string |
| {}.toString | 原始数据类型，内建对象，包含 Symbol.toStringTag 属性的对象 | string |
| instanceof | 对象 | true/false |
  