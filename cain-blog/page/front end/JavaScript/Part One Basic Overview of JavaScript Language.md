---
title: 01 JavaScript语言基础上
date: 2020-03-01
updated: 2020-03-05
tags:
- JavaScript
- 基础
categories:
- JavaScript
cover: https://file.crazywong.com/gh/jerryc127/CDN@latest/cover/default_bg.png
---

## 简介

### 是什么

- [HTML](https://developer.mozilla.org/en-US/docs/Glossary/HTML)是一种标记语言
- [CSS](https://developer.mozilla.org/en-US/docs/Glossary/CSS) 是一种样式规则语言
- [JavaScript](https://developer.mozilla.org/en-US/docs/Glossary/JavaScript) 是一种事件驱动的脚本语言，解释型 （即时解释--编译）、单线程、**服务器端（server-side）**和 **客户端（client-side）**代码；动态类型：变量声明时不需指定变量的类型

浏览器中嵌入了 JavaScript 引擎，有时也称作“JavaScript 虚拟机”。

### 引擎是怎么工作的

1. 引擎（如果是浏览器，则引擎被嵌入在其中）读取（“解析”）脚本。
2. 然后，引擎将脚本转化（“编译”）为机器语言。
3. 然后，机器代码快速地执行。

## 基础知识

### 变量&常量

#### 命名

JavaScript 的变量命名有两个限制：

1. 变量名称必须仅包含字母，数字，符号 `$` 和 `_`。
2. 首字符必须非数字。

[保留字列表](https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Lexical_grammar#Keywords)

#### let 命令

for循环还有一个特别之处，就是设置循环变量的那部分是一个父作用域，而循环体内部是一个单独的子作用域。

```js
for (let i = 0; i < 3; i++) {
  let i = 'abc';
  console.log(i);
}
```

在代码块内，使用`let`命令声明变量之前，该变量都是不可用的。这在语法上，称为“暂时性死区”（temporal dead zone，简称 TDZ）。

不允许重复声明

let实际上为 JavaScript 新增了块级作用域。

块级作用域的出现，实际上使得获得广泛应用的匿名立即执行函数表达式（匿名 IIFE）不再必要了。

let只能出现在当前作用域的顶层

ES6 的块级作用域必须有大括号，如果没有大括号，JavaScript 引擎就认为不存在块级作用域。

#### 块级作用域

第一种场景，内层变量可能会覆盖外层变量。

第二种场景，用来计数的循环变量泄露为全局变量。

内层作用域可以定义外层作用域的同名变量。

const 命令

`const`声明一个只读的常量。一旦声明，常量的值就不能改变。

`const`一旦声明变量，就必须立即初始化，不能留到以后赋值。

对于简单类型的数据（数值、字符串、布尔值），值就保存在变量指向的那个内存地址，因此等同于常量。但对于复合类型的数据（主要是对象和数组），变量指向的内存地址，保存的只是一个指向实际数据的指针，const只能保证这个指针是固定的

对象冻结，应该使用`Object.freeze`方法。

`var`、`function`、`let`、`const`、`import`、`class`。

顶层对象的属性：顶层对象，在浏览器环境指的是`window`对象，在 Node 指的是`global`对象。ES5 之中，顶层对象的属性与全局变量是等价的。`var`命令和`function`命令声明的全局变量，依旧是顶层对象的属性；另一方面规定，`let`命令、`const`命令、`class`命令声明的全局变量，不属于顶层对象的属性。

引入`globalThis`作为顶层对象。也就是说，任何环境下，`globalThis`都是存在的，都可以从它拿到顶层对象，指向全局环境下的`this`。

#### var 与 let

1. 初始化一个变量之后用 `var` 声明它，它仍然可以工作

1. 当你使用 `var` 时，可以根据需要多次声明相同名称的变量，但是 `let` 不能。let可以重新赋值。

### 顺序、条件、循环结构

条件分支：`if` 语句和条件运算符 `?`（也称为“问号”运算符）来实现。

`while` 循环的语法如下：

```javascript
while (condition) {
  // 代码
  // 所谓的“循环体”
}
```

当 `condition` 为真时，执行循环体的 `code`。

使用 `do..while` 语法可以将条件检查移至循环体 **下面**：

```javascript
do {
  // 循环体
} while (condition);
```

循环首先执行循环体，然后检查条件，当条件为真时，重复执行循环体。

`for` 循环看起来就像这样：

```javascript
for (begin; condition; step) {
  // ……循环体……
}
```

`switch` 语句有至少一个 `case` 代码块和一个可选的 `default` 代码块。

```javascript
switch(x) {
  case 'value1':  // if (x === 'value1')
    ...
    [break]

  case 'value2':  // if (x === 'value2')
    ...
    [break]

  default:
    ...
    [break]
}
```

### 逻辑运算

 或运算寻找第一个真值
 与运算寻找第一个假值
与运算 `&&` 的优先级比或运算 || 要高。

### 空值合并运算符 `??`

空值合并运算符（nullish coalescing operator）的写法为两个问号 `??`。

`a ?? b` 的结果是：

- 如果 `a` 是已定义的，则结果为 `a`，
- 如果 `a` 不是已定义的，则结果为 `b`。

如果第一个参数不是 `null/undefined`，则 `??` 返回第一个参数。否则，返回第二个参数。

或 `||` 运算符 和 空值合并运算符 `??`之间重要的区别是：

- `||` 返回第一个 **真** 值。（`||` 无法区分 `false`、`0`、空字符串 `""` 和 `null/undefined`。）
- `??` 返回第一个 **已定义的** 值。

### 数据类型

- 原始数据类型
  - Undefined： `未被赋值`。
  - Null：代表“无”、“空”或“值未知”的特殊值。
  - Boolean
  - Number
  - String
  - Symbol (ES6)
- Object

```js
  typeof undefined // "undefined"

  typeof 0 // "number"

  typeof 10n // "bigint"

  typeof true // "boolean"

  typeof "foo" // "string"

  typeof Symbol("id") // "symbol"

  typeof Math // "object"  内建对象

  typeof null // "object" 遗留问题

  typeof alert // "function"  其实是对象，特殊对待
```

特殊字符：Number特殊数值（“special numeric values”）”：`Infinity`、`-Infinity` 和 `NaN`。

`typeof` 操作符的唯一目的就是检查数据类型

### 类型转换

显式转换：Number（mix）和String（mix） parseInt（string，基底）:从数字开始看，看到整数部分； parseFloat（）看到小数位一位， toString（radix）：可以转进制：null undefined不能用，Boolean（）；

隐式（内部显示）转换：数字 --> 字符串（+ ""），字符串 --> 数值（- */  : -0*1 /1）；isNaN（）

显式转换：undefined null NaN “” 0 FALSE ==> FALSE；任何类型 +字符串都是字符串；

- 对 `undefined` 进行数字型转换时，输出结果为 `NaN`，而非 `0`。
- 对 `"0"` 和只有空格的字符串（比如：`" "`）进行布尔型转换时，输出结果为 `true`。

**运算符**：算术运算符 赋值运算符 关系运算符 逻辑运算符

求幂 **； 逻辑符 场景：判断用户用邮箱、手机号等注册登录；判断音乐播放暂停；

tips : 除数为0，结果是infinite 类型是number

- `+` 应用于单个值，对数字没有任何作用。但是如果运算元不是数字，加号 `+` 则会将其转化为数字。
- `+`号：数字之间是普通运算，字符串之间拼接，数字与字符串是拼接（隐式转换）
- `()`优先级最高
- `& |`存在短路现象：如果前面是真，才执行后面语句，可用到短路现象
- 字符串比较，直接通过字符串的方式进行比较：按照字符串中字符在ASCII码表的位置比较码值，如果码值完全相同，则字符串相同。
- NaN is a Number

tips：数字 0、空字符串 ""、null、undefined 和 NaN 都会被转换成 false。因为它们被称为“假值（falsy）”值。
其他值被转换为 true，所以它们被称为“真值（truthy）”。

有三种常用的类型转换：转换为 string 类型、转换为 number 类型和转换为 boolean 类型。

**字符串转换** —— 转换发生在输出内容的时候，也可以通过 `String(value)` 进行显式转换。原始类型值的 string 类型转换通常是很明显的。

**数字型转换** —— 转换发生在进行算术操作时，也可以通过 `Number(value)` 进行显式转换。

数字型转换遵循以下规则：

| 值             | 变成……                                                       |
| :------------- | :----------------------------------------------------------- |
| `undefined`    | `NaN`                                                        |
| `null`         | `0`                                                          |
| `true / false` | `1 / 0`                                                      |
| `string`       | “按原样读取”字符串，两端的空白会被忽略。空字符串变成 `0`。转换出错则输出 `NaN`。 |

**布尔型转换** —— 转换发生在进行逻辑操作时，也可以通过 `Boolean(value)` 进行显式转换。

布尔型转换遵循以下规则：

| 值                                    | 变成……  |
| :------------------------------------ | :------ |
| `0`, `null`, `undefined`, `NaN`, `""` | `false` |
| 其他值                                | `true`  |

上述的大多数规则都容易理解和记忆。人们通常会犯错误的值得注意的例子有以下几个：

- 对 `undefined` 进行数字型转换时，输出结果为 `NaN`，而非 `0`。
- 对 `"0"` 和只有空格的字符串（比如：`" "`）进行布尔型转换时，输出结果为 `true`。

### 函数、函数表达式及箭头函数

#### 函数声明

```javascript
function showMessage() {
  alert( 'Hello everyone!' );
}
```

#### 函数表达式

```javascript
let sayHi = function() {
  alert( "Hello" );
};
```

#### 箭头函数

`let sum = (a, b) => a + b;`

1. 不带花括号：`(...args) => expression` — 右侧是一个表达式：函数计算表达式并返回其结果。
2. 带花括号：`(...args) => { body }` — 花括号允许我们在函数中编写多个语句，但是我们需要显式地 `return` 来返回一些内容。

区别：**函数表达式是在代码执行到达时被创建，并且仅从那一刻起可用。而**  在函数声明被定义之前，函数生命方式的函数就可以被调用。

**严格模式下，当一个函数声明在一个代码块内时，它在该代码块内的任何位置都是可见的。但在代码块外不可见。**

#### 箭头函数的语法

let func = (arg1, arg2, ...argN) => expression，函数 `func`，它接受参数 `arg1..argN`，然后使用参数对右侧的 `expression` 求值并返回其结果。若无参保留括号为空即可，若只有一个参数可省略括号，箭头函数对于简单的单行行为（action）来说非常方便。

其等价于

```js
let func = function(arg1, arg2, ...argN) {
  return expression;
};//花括号表示开始一个多行函数,单行不要用{}
```

#### 箭头函数的局限

- 箭头函数没有 “this”，其访问this时会从外部获取。强行使用this报错undefined。

- 不具有 `this` 自然也就意味着另一个限制：箭头函数不能用作构造器（constructor）。不能用 `new` 调用它们。

- 箭头函数也没有 `arguments` 变量。

- 也没有super。

  > 箭头函数是针对那些没有自己的“上下文”，但在当前上下文中起作用的短代码的。

### 数学运算

支持以下数学运算：

- 加法 `+`,
- 减法 `-`,
- 乘法 `*`,
- 除法 `/`,
- 取余 `%`,
- 求幂 `**`.

加号 `+` 应用于单个值，对数字没有任何作用。但是如果运算元不是数字，加号 `+` 则会将其转化为数字。

### tips

仅仅等于对方而不等于其他任何的值（只在非严格相等下成立）。
null === undefined  // false
null == undefined  // true

null/undefined 会被转化为数字：null 被转化为 0，undefined 被转化为 NaN。但是：`null >= 0 // true`

## 对象

### 介绍

它们存储属性（键值对），其中：

- 属性的键必须是字符串或者 symbol（通常是字符串）。
- 值可以是任何类型。

我们可以用下面的方法访问属性：

- 点符号: `obj.property`。
- 方括号 `obj["property"]`，方括号允许从变量中获取键，例如 `obj[varWithKey]`。

其他操作：

- 删除属性：`delete obj.prop`。
- 检查是否存在给定键的属性：`"key" in obj`。
- 遍历对象：`for(let key in obj)` 循环。

### 对象引用和赋值

**赋值了对象的变量存储的不是对象本身，而是该对象“在内存中的地址”，换句话说就是对该对象的“引用”。当一个对象变量被复制 —— 引用则被复制，而该对象并没有被复制。**

`Object.assign(dest, [src1, src2, src3...])`
第一个参数 `dest` 是指目标对象。
更后面的参数 `src1, ..., srcN`（可按需传递多个参数）是源对象。
该方法将所有源对象的属性拷贝到目标对象 `dest` 中。换句话说，从第二个开始的所有参数的属性都被拷贝到第一个参数的对象中。
调用结果返回 `dest`。

### 遍历对象

三种原生的方法用于列出或枚举对象的属性：

- for...in 循环
  该方法依次访问一个对象及其原型链中所有可枚举的属性。
- Object.keys(o)
  该方法返回对象 o 自身包含（不包括原型中）的所有可枚举属性的名称的数组。
- Object.getOwnPropertyNames(o)
  该方法返回对象 o 自身包含（不包括原型中）的所有属性(无论是否可枚举)的名称的数组。

### 垃圾回收

固有的可达值的基本集合，这些值明显不能被释放。

- 当前函数的局部变量和参数。
- 嵌套调用时，当前调用链上所有函数的变量与参数。
- 全局变量。
- （还有一些内部的）

这些值被称作 **根（roots）**。

如果一个值可以通过引用或引用链从根访问任何其他值，则认为该值是可达的。比方说，如果全局变量中有一个对象，并且该对象有一个属性引用了另一个对象，则 **该** 对象被认为是可达的。而且它引用的内容也是可达的。

垃圾回收的基本算法被称为 “mark-and-sweep”。

定期执行以下“垃圾回收”步骤：

- 垃圾收集器找到所有的根，并“标记”（记住）它们。
- 然后它遍历并“标记”来自它们的所有引用。
- 然后它遍历标记的对象并标记 **它们的** 引用。所有被遍历到的对象都会被记住，以免将来再次遍历到同一个对象。
- ……如此操作，直到所有可达的（从根部）引用都被访问到。
- 没有被标记的对象都会被删除。

一些优化建议：

- **分代收集（Generational collection）**—— 对象被分成两组：“新的”和“旧的”。许多对象出现，完成它们的工作并很快死去，它们可以很快被清理。那些长期存活的对象会变得“老旧”，而且被检查的频次也会减少。
- **增量收集（Incremental collection）**—— 如果有许多对象，并且我们试图一次遍历并标记整个对象集，则可能需要一些时间，并在执行过程中带来明显的延迟。所以引擎试图将垃圾收集工作分成几部分来做。然后将这几部分会逐一进行处理。这需要它们之间有额外的标记来追踪变化，但是这样会有许多微小的延迟而不是一个大的延迟。
- **闲时收集（Idle-time collection）**—— 垃圾收集器只会在 CPU 空闲时尝试运行，以减少可能对代码执行的影响。

### 构造器（函数）和 new

构造函数在技术上是常规函数。不过有两个约定：

1. 它们的命名以大写字母开头。
2. 它们只能由 `"new"` 操作符来执行。

当一个函数被使用 `new` 操作符执行时，它按照以下步骤：

1. 一个新的空对象被创建并分配给 `this`。this = {}
2. 函数体执行。通常它会修改 `this`，为其添加新的属性。
3. 返回 `this` 的值。return this

另一种说法：

1. 创建了一个空对象 var object = {}
2. 设置原型链（将空对象的`__proto__`设置为该构造函数的原型对象）`object.__proto__ = Func.prototype`
3. 调用Func函数，内部this重定向为这个空对象，并赋值Func.call(obj);
4. 返回创建好的对象，return object

构造器的主要目的 —— 实现可重用的对象创建代码。

构造器模式测试：new.target

用在函数内部，来判断该函数是被通过 `new` 调用的“构造器模式”，还是没被通过 `new` 调用的“常规模式”。

### 可选链 ?

可选链 `?.` 语法有三种形式：

1. `obj?.prop` —— 如果 `obj` 存在则返回 `obj.prop`，否则返回 `undefined`。
2. `obj?.[prop]` —— 如果 `obj` 存在则返回 `obj[prop]`，否则返回 `undefined`。
3. `obj.method?.()` —— 如果 `obj.method` 存在则调用 `obj.method()`，否则返回 `undefined`。

正如我们所看到的，这些语法形式用起来都很简单直接。`?.` 检查左边部分是否为 `null/undefined`，如果不是则继续运算。

`?.` 链使我们能够安全地访问嵌套属性。

### Symbol 类型

`Symbol` 是唯一标识符的基本类型

Symbol 是使用带有可选描述（name）的 `Symbol()` 调用创建的。

Symbol 总是不同的值，即使它们有相同的名字。如果我们希望同名的 Symbol 相等，那么我们应该使用全局注册表：`Symbol.for(key)` 返回（如果需要的话则创建）一个以 `key` 作为名字的全局 Symbol。使用 `Symbol.for` 多次调用 `key` 相同的 Symbol 时，返回的就是同一个 Symbol。

Symbol 有两个主要的使用场景：

1. Symbol 允许我们创建对象的“隐藏”属性，代码的任何其他部分都不能意外访问或重写这些属性。
2. 全局Symbol 从注册表中读取（不存在则创建）Symbol，请使用 `Symbol.for(key)`。该调用会检查全局注册表，如果有一个描述为 `key` 的 Symbol，则返回该 Symbol，否则将创建一个新 Symbol（`Symbol(key)`），并通过给定的 `key` 将其存储在注册表中。

JavaScript 中的大多数值都支持字符串的隐式转换。例如，我们可以 `alert` 任何值，都可以生效。Symbol 比较特殊，它不会被自动转换。

如果我们要在对象字面量 `{...}` 中使用 Symbol，则需要使用方括号把它括起来。

反向调用：`Symbol.keyFor(sym)`，它的作用完全反过来：通过全局 Symbol 返回一个名字。`Symbol.keyFor` 内部使用全局 Symbol 注册表来查找 Symbol 的键。

### 对象 — 原始值转换

**为了进行转换，JavaScript 尝试查找并调用三个对象方法：**

1. 调用 `obj[Symbol.toPrimitive](hint)` —— 带有 symbol 键 `Symbol.toPrimitive`（系统 symbol）的方法，如果这个方法存在的话，
2. 否则，如果 hint 是 `"string"` —— 尝试 `obj.toString()` 和 `obj.valueOf()`，无论哪个存在。
3. 否则，如果 hint 是 `"number"` 或 `"default"` —— 尝试 `obj.valueOf()` 和 `obj.toString()`，无论哪个存在。

普通对象具有 `toString` 和 `valueOf` 方法：

- `toString` 方法返回一个字符串 `"[object Object]"`。
- `valueOf` 方法返回对象自身。

### 如何创建对象

1. 根据语法 声明对象
2. 构造函数 创建（new）对象
3. Object（）构造函数：创建(new)空对象，使用点括号法、用{}传递对象文本到object（）小括号里作为参数加属性和方法
4. Object.create（object）方法：基于现有对象创建新的对象；new.**proto**=old;
5. let user = new Object(); // “构造函数” 的语法
6. let user = {};  // “字面量” 的语法

### 操作对象

delete删除对象属性

点语法访问单个属性 方括号添加或访问多个属性

`in` 的左边必须是 **属性名**。通常是一个带引号的字符串。省略引号，就意味着左边是一个变量，它应该包含要判断的实际属性名。

tips：

属性命名没有限制。属性名可以是任何字符串或者 symbol

键值如其他类型会被自动地转换为字符串。

“有特别的顺序”：整数属性会被进行排序，其他属性则按照创建的顺序显示

### 对象的内容

1. 一个对象由许多的成员组成，每一个成员都拥有一个名字（像上面的name、age），和一个值（如['Bob', 'Smith']、32）。每一个名字/值（name/value）对被逗号分隔开，并且名字和值之间由冒号（:）分隔。

2. 关键概念：dot notation 子命名空间 括号表示法 多态 构建函数 关联数组 object package  namespace 封装 data stores  **原型（prototype）、原型链 prototype chain、构造函数、create（）、原型对象__proto__、构造器、原型式继承**

3. **prototype-based language：**每个对象拥有一个**原型对象**，对象以其原型为模板、从原型继承方法和属性。
4. 每个实例对象都从原型中继承了一个constructor属性，该属性指向了用于构造此实例对象的构造函数。

5. 修改原型：极其常见的对象定义模式是，在构造器（函数体）中定义属性、在 `prototype` 属性上定义方法。

### OOP OOJS

**注意**：原型链中的方法和属性**没有**被复制到其他对象——它们被访问需要通过前面所说的“原型链”的方式。

tips：`this` 关键字不指向当前对象的原型对象

## 数据类型

### 数字类型

#### 转进制

方法 `num.toString(base)` 返回在给定 `base` 进制数字系统中 `num` 的字符串表示形式。`base` 的范围可以从 `2` 到 `36`。默认情况下是 `10`。

- **base=16** 用于十六进制颜色，字符编码等，数字可以是 `0..9` 或 `A..F`。
- **base=2** 主要用于调试按位操作，数字可以是 `0` 或 `1`。
- **base=36** 是最大进制，数字可以是 `0..9` 或 `A..Z`。所有拉丁字母都被用于了表示数字。短链接就是这么来的

数字直接使用需要两个点 `123456..toString(36)`

#### Math常用方法

弧度除以 `(Math.PI / 180)` 即可转换为角度，同理，角度乘以这个数则能转换为弧度。

1. Math.PI 圆周率，一个圆的周长和直径之比，约等于 3.14159。
2. Math.SQRT1_2 二分之一 ½ 的平方根，同时也是 2 的平方根的倒数 12，约等于 0.707。
3. Math.SQRT2 2 的平方根，约等于 1.414。
4. Math.abs(x) 返回一个数的绝对值。
5. Math.max([x[, y[, …]]]) 返回零到多个数值中最大值。
6. Math.min([x[, y[, …]]]) 返回零到多个数值中最小值。
7. Math.pow(x, y) 返回一个数的 y 次幂。
8. Math.random() 返回一个 0 到 1 之间的伪随机数。
9. Math.round(x) 返回四舍五入后的整数。
10. Math.sign(x) 返回一个数的符号，得知一个数是正数、负数还是 0。
11. Math.sqrt(x) 返回一个数的平方根。
12. Math.trunc(x) 返回一个数的整数部分，直接去除其小数点及之后的部分。

#### 随机数

1. [min, max) 随机数 Math.random() * (max - min) + min;
2. [min, max) 随机整数

```javascript
  function getRandomInt(min, max) {
    min = Math.ceil(min);//向上取整
    max = Math.floor(max);//向下取整

    return Math.floor(Math.random() * (max - min)) + min; //不含最大值，含最小值
  }
```

1. [min, max] 随机整数

```js
function getRandomIntInclusive(min, max) {
  min = Math.ceil(min);
  max = Math.floor(max);
  return Math.floor(Math.random() * (max - min + 1)) + min; //含最大值，含最小值 
}
```

[**猜数字游戏**](https://developer.mozilla.org/zh-CN/docs/Learn/JavaScript/First_steps/A_first_splash)

```text
Math.floor(Math.random() * 100) + 1;随机数代码
```

### 字符串

#### 特殊字符

转义字符：[点击链接](https://developer.mozilla.org/zh-CN/docs/Web/JavaScript/Reference/Global_Objects/String#parameters)

#### 常用方法

1. `charAt()`返回特定位置的字符

2. `includes()`判断一个字符串里是否包含其他字符串。

3. `endsWith()`判断一个字符串的是否以给定字符串结尾，结果返回布尔值。
4. `trim()` 方法会从一个字符串的两端删除空白字符。

5. `indexOf()`从字符串对象中返回首个被发现的给定值的索引值，如果没有找到则返回-1。

6. `lastIndexOf()`从字符串对象中返回最后一个被发现的给定值的索引值，如果没有找到则返回-1。

7. `slice(beginIndex，endIndex)`：如果值为负数，会被当做 strLength + beginIndex 看待，这里的strLength 是字符串的长度

8. `toLowerCase():`转小写

9. `toUpperCase():`转大写
10. `replace()`被用来在正则表达式和字符串直接比较，然后用新的子串来替换被匹配的子串。

11. `match()`使用正则表达式与字符串相比较。

12. `repeat()` 返回指定重复次数的由元素组成的字符串对象。

13. `substr()` 通过指定字符数返回在指定位置开始的字符串中的字符。

```javascript
if(browserType.indexOf('mozilla') !== -1) {
}
```

> 在主字符串中找不到子字符串（在本例中为“vanilla”）时将返回 -1。如上：

#### 模板字符串

使用反引号 () 来代替普通字符串中的双引号和单引号；模板字符串可以包含特定语法`（${expression}）`的占位符。占位符中的表达式和周围的文本会一起传递给一个默认函数，该函数负责将所有的部分连接起来。

使用技巧：多行字符串 插入表达式 嵌套模板 带标签的模板字符串

<link rel="stylesheet" type="text/css" href="media.css" media="screen and (max-width: 800px)">

### 数组方法

1. `push()`  `unshift()`给数组增加元素，可以同时增加多个值，同时返回新数组长度,`pop()` `shift()`给数组删除元素，一次删除一个，同时返回删掉的元素值。
2. `arr.splice(start[, deleteCount, elem1, ..., elemN])`  它从索引 start 开始修改 arr：删除 deleteCount 个元素并在当前位置插入 elem1, ..., elemN。最后返回已被删除元素的数组。允许负向索引，从数组末尾计算位置
3. `arr.slice([start], [end])` 返回一个新数组，将所有从索引 start 到 end（不包括 end）的数组项复制到一个新的数组。start 和 end 都可以是负数，在这种情况下，从末尾计算索引。
4. `arr.forEach(function(item, index, array)` {  // ... do something with item });方法允许为数组的每个元素都运行一个函数。该函数的结果（如果它有返回）会被抛弃和忽略。
5. `arr.indexOf(item, from)` 从索引 from 开始搜索 item，如果找到则返回索引，否则返回 -1。
6. `arr.lastIndexOf(item, from)` —— 和上面相同，只是从右向左搜索。
7. `arr.includes(item, from)` —— 从索引 from 开始搜索 item，如果找到则返回 true（以上三个方法都是执行严格相等 ===，但是includes可识别NaN）
8. let result = `arr.find`(function(item, index, array) {
   // 如果返回 true，则返回 item 并停止迭代
   // 对于假值（falsy）的情况，则返回 undefined
   });  `arr.findIndex` 类似，返回的是索引。找不到返回 -1；
9. let results = `arr.filter`(function(item, index, array) {  
   // 如果 true item 被 push 到 results，迭代继续  
   // 如果什么都没找到，则返回空数组 });
10. let result = `arr.map`(function(item, index, array) {
      // 返回新值而不是当前元素
    })
11. `arr.sort(fn)` 方法对数组进行 原位（in-place） 排序，更改元素的顺序。(译注：原位是指在此数组内，而非生成一个新数组。)默认是按照字符串进行排序的。arr.sort( (a, b) => a - b );
12. `arr.reverse` 方法用于颠倒 arr 中元素的顺序。
13. `str.split([separator[, limit]])`通过给定的分隔符 separator将字符串分割成一个数组 **本是字符串方法，数组也可以用**
14. `arr.join(glue)` 与 split 相反。它会在它们之间创建一串由 glue 粘合的 arr 项。即转字符串方法
15. let value = `arr.reduce`(function(accumulator, item, index, array) {
      // ...
    }, [initial]);
    该函数一个接一个地应用于所有数组元素，并将其结果“搬运（carry on）”到下一个调用。
    accumulator —— 是上一个函数调用的结果，第一次等于 initial（如果提供了 initial 的话）。
    item —— 当前的数组元素。
    index —— 当前索引。
    arr —— 数组本身。
16. `Array.prototype.entries()`返回一个数组迭代器对象，该迭代器会包含所有数组元素的键值对。
17. `Array.prototype.every()` 如果数组中的每个元素都满足测试函数，则返回 true，否则返回 false。
18. `Array.prototype.some()` 如果数组中至少有一个元素满足测试函数，则返回 true，否则返回 false。

>1. 数组下标越界时，得到`undefined`
>2. 逗号之间没有值，系统会算入数组个数里，但是其值为undefined；如果最后一个逗号后面没有值，系统则会忽略，不算入个数：  var arr4 = [1, 2, , 4, ];

[前5个搜索](https://developer.mozilla.org/zh-CN/docs/Learn/JavaScript/First_steps/Arrays#%E7%A7%AF%E6%9E%81%E5%AD%A6%E4%B9%A0%EF%BC%9A%E5%89%8D5%E4%B8%AA%E6%90%9C%E7%B4%A2 "Permalink to 积极学习：前5个搜索")

```javascript
  var list = document.querySelector('.output ul');
  var searchInput = document.querySelector('.output input');
  var searchBtn = document.querySelector('.output button');

  list.innerHTML = '';

  var myHistory= [];

  searchBtn.onclick = function() {
    if(searchInput.value !== '') {
      myHistory.unshift(searchInput.value);

      list.innerHTML = '';

      for(var i = 0; i < myHistory.length; i++) {
      var itemText = myHistory[i];
        var listItem = document.createElement('li');
        listItem.textContent = itemText;
        list.appendChild(listItem);
      }

      if(myHistory.length >= 5) {
        myHistory.pop();
      }

      searchInput.value = '';
      searchInput.focus();
    }
  }
```

### Iterable object

### Map & Set

#### Map

Map 是一个带键的数据项的集合，就像一个 Object 一样。 但是它们最大的差别是 Map 允许任何类型的键（key）。它的方法和属性如下：

- new Map([iterable])—— 创建 map，可选择带有 `[key,value]` 对的 `iterable`（例如数组）来进行初始化。
- map.set(key, value) —— 根据键存储值。
- map.get(key) —— 根据键来返回值，如果 map 中不存在对应的 key，则返回 undefined。
- map.has(key) —— 如果 key 存在则返回 true，否则返回 false。
- map.delete(key) —— 删除指定键的值。
- map.clear() —— 清空 map。
- map.size —— 返回当前元素个数。

与普通对象 `Object` 的不同点：

- 任何键、对象都可以作为键。
- 有其他的便捷方法，如 `size` 属性。

#### Map迭代（iteration）

map.keys() —— 遍历并返回所有的键
map.values() —— 遍历并返回所有的值
map.entries() —— 遍历并返回所有的实体[key, value]，for..of 在默认情况下使用的就是这个。

tips：迭代的顺序与插入值的顺序相同。与普通的 `Object` 不同，`Map` 保留了此顺序。map的系统遍历方法：

```js
// 对每个键值对 (key, value) 运行 forEach 函数
recipeMap.forEach( (value, key, map) => {
  alert(`${key}: ${value}`); // cucumber: 500 etc
});
```

**Object.entries:当创建一个 `Map` 后，我们可以传入一个带有键值对的数组（或其它可迭代对象）来进行初始化**

> `Object.entries` 返回键/值对数组
>
> *let map = new Map(Object.entries(obj));*

**Object.fromEntries:给定一个具有 `[key, value]` 键值对的数组，它会根据给定数组创建一个对象**

> `Object.fromEntries` 从 `Map` 得到一个普通对象（plain object）
>
> *let obj = Object.fromEntries(map.entries()); // 创建一个普通对象（plain object）*

#### Set

Set 是一个特殊的类型集合 —— “值的集合”（没有键），它的每一个值只能出现一次。它的主要方法如下：

- new Set(iterable) —— 创建一个 set，如果提供了一个 iterable 对象（通常是数组），将会从数组里面复制值到 set 中。
- set.add(value) —— 添加一个值，返回 set 本身
- set.delete(value) —— 删除值，如果 value 在这个方法调用的时候存在则返回 true ，否则返回 false。
- set.has(value) —— 如果 value 在 set 中，返回 true，否则返回 false。
- set.clear() —— 清空 set。
- set.size —— 返回元素个数。

它的主要特点是，重复使用同一个值调用 set.add(value) 并不会发生什么改变。这就是 Set 里面的每一个值只出现一次的原因。

#### Set 迭代（iteration）

set.keys() / set.values() —— 遍历并返回所有的值
set.entries() —— 遍历并返回所有的实体[value, value]，它的存在也是为了兼容 Map。

**tips**：在 `Map` 和 `Set` 中迭代总是按照值插入的顺序进行的，所以我们不能说这些集合是无序的，但是我们不能对元素进行重新排序，也不能直接按其编号来获取元素。

### 对象转换

#### 方法

- Object.keys(obj) —— 返回一个包含该对象所有的键的数组。
- Object.values(obj) —— 返回一个包含该对象所有的值的数组。
- Object.entries(obj) —— 返回一个包含该对象所有 [key, value] 键值对的数组。

#### 应用数组方法到对象

1. 使用 `Object.entries(obj)` 从 `obj` 获取由键/值对组成的数组。
2. 对该数组使用数组方法，例如 `map`。
3. 对结果数组使用 `Object.fromEntries(array)` 方法，将结果转回成对象。

### 解构

### 日期和时间

#### 概念：时间戳 getTime()

```js
var dateObj =  new Date(year, month, date, hours, minutes, seconds, ms)
alert(+dateObj); // 以毫秒为单位的数值，与使用 date.getTime() 的结果相同
console.log(dateObj.getFullYear());//年份
console.log(dateObj.getMonth());//获取月份 取值范围 0-11
console.log(dateObj.getDay());//获取周几，周日到周六 0-6
console.log(dateObj.getDate());//1-31
console.log(dateObj.getHours());//0-23
console.log(dateObj.getMinutes());//0-59
console.log(dateObj.getSeconds());//0-59
console.log(dateObj.getTime());//从1970.1.1日返回的毫秒数，时间戳
```

**相应的年月日时分秒 时间戳等有一个set方法；**

tips：当 `Date` 对象被转化为数字时，得到的是对应的时间戳

## 函数进阶

### Rest参数和Spread语法

Spread 语法内部使用了迭代器来收集元素，与 `for..of` 的方式相同。

当我们在代码中看到 `"..."` 时，它要么是 rest 参数，要么就是 spread 语法。

有一个简单的方法可以区分它们：

- 若 `...` 出现在函数参数列表的最后，那么它就是 rest 参数，它会把参数列表中剩余的参数收集到一个数组中。
- 若 `...` 出现在函数调用或类似的表达式中，那它就是 spread 语法，它会把一个数组展开为列表。

使用场景：

- Rest 参数用于创建可接受任意数量参数的函数。
- Spread 语法用于将数组传递给通常需要含有许多参数的列表的函数。

它们俩的出现帮助我们轻松地在列表和参数数组之间来回转换。

“旧式”的 `arguments`（类数组且可迭代的对象）也依然能够帮助我们获取函数调用中的所有参数。

### 函数对象，NFE

函数就是对象：

- `name` —— 函数的名字。通常取自函数定义，但如果函数定义时没设定函数名，JavaScript 会尝试通过函数的上下文猜一个函数名（例如把赋值的变量名取为函数名）。
- `length` —— 函数定义时的入参的个数。Rest 参数不参与计数。

如果函数是通过函数表达式的形式被声明的（不是在主代码流里），并且附带了名字，那么它被称为命名函数表达式（Named Function Expression）。这个名字可以用于在该函数内部进行自调用，例如递归调用等。

此外，函数可以带有额外的属性。很多知名的 JavaScript 库都充分利用了这个功能。

### new Function

创建函数的语法

let func = new Function ([arg1, arg2, ...argN], functionBody);

该函数是通过使用参数 `arg1...argN` 和给定的 `functionBody` 创建的。

### 装饰器模式和转发，call/apply

 **装饰器（decorator）**：一个特殊的函数，它接受另一个函数并改变它的行为。

JavaScript 在处理函数时提供了非凡的灵活性。它们可以被传递，用作对象，现在我们将看到如何在它们之间 转发（forward） 调用并 装饰（decorate） 它们。

**装饰器（decorator）**：一个特殊的函数，它接受另一个函数并改变它的行为。

使用分离的装饰器函数而不改变另一个函数代码的好处:

- `cachingDecorator` 是可重用的。我们可以将它应用于另一个函数。
- 缓存逻辑是独立的，它没有增加 `slow` 本身的复杂性（如果有的话）。
- 如果需要，我们可以组合多个装饰器（其他装饰器将遵循同样的逻辑）。

但缓存装饰器不适用于对象方法。

func.call(context, arg1, arg2, ...)  func.call(修改成的this对象，实参列表...);

> 修改this指向后，立即调用该函数。Spread 语法 ... 允许将 可迭代对象 args 作为列表传递给 call。

apply(): func.apply(修改成的this对象，实参列表数组...);

> 实参写在一个数组里面，apply 仅接受 类数组对象 args。

bind(): func.bind(修改成的this对象，实参列表...);返回值是修改后的新函数，需自行调用

- 谁
- this 关键字：判断其指代哪个元素，通过this所在函数，以及触发this效果的是谁，即指代谁。
- arguments[].callee = 函数的引用  （立即执行函数的自身调用）
- 关键字"this"指向了当前代码运行时的对象

链式调用：每次调用后返回这个对象本身（return this）。

## 原型

原型：在 JavaScript 中，对象有一个特殊的隐藏属性 [[Prototype]]（如规范中所命名的），它要么为 null，要么就是对另一个对象的引用。该对象被称为“原型”

原型继承：当我们从 object 中读取一个缺失的属性时，JavaScript 会自动从原型中获取该属性。在编程中，这种行为被称为“原型继承”

设置原型：**proto**

查找：（自下而上）

无论在哪里找到方法：在一个对象还是在原型中。在一个方法调用中，this 始终是点符号 . 前面的对象。

### 两个限制

引用不能形成闭环。如果我们试图在一个闭环中分配`__proto__`，JavaScript 会抛出错误。
`__proto__` 的值可以是对象，也可以是 null。而其他的类型都会被忽略。

只能有一个 `[[Prototype]]`。一个对象不能从其他两个对象获得继承。

`__proto__` **是** `[[Prototype]]` **的因历史原因而留下来的 getter/setter**：Object.getPrototypeOf/Object.setPrototypeOf

原型仅用于读取属性。对于写入/删除操作可以直接在对象上进行。

默认的 `"prototype"` 是一个只有属性 `constructor` 的对象，属性 `constructor` 指向函数自身。

```js
function Rabbit() {}

/* default prototype
Rabbit.prototype = { constructor: Rabbit };
*/
```

### 原型继承

当我们从 `object` 中读取一个缺失的属性时，JavaScript 会自动从原型中获取该属性。在编程中，这种行为被称为“原型继承”。

object [[prototype]] --> prototype object

设置原型

```js
rabbit.__proto__ = animal; // 设置 rabbit.[[Prototype]] = animal
```

原型引用限制

1. 引用不能形成闭环。如果我们试图在一个闭环中分配 `__proto__`，JavaScript 会抛出错误。
2. `__proto__` 的值可以是对象，也可以是 `null`。而其他的类型都会被忽略。

`__proto__` 是 `[[Prototype]]` 的 getter/setter(Object.getPrototypeOf/Object.setPrototypeOf)

**无论在哪里找到方法：在一个对象还是在原型中。在一个方法调用中，`this` 始终是点符号 `.` 前面的对象。**

在现代引擎中，从性能的角度来看，我们是从对象还是从原型链获取属性都是没区别的。它们（引擎）会记住在哪里找到的该属性，并在下一次请求中重用它。

### 原型方法

Object.create(proto, [descriptors]) —— 利用给定的 proto 作为 [[Prototype]] 和可选的属性描述来创建一个空对象。

Object.getPrototypeOf(obj) —— 返回对象 obj 的 [[Prototype]]。

Object.setPrototypeOf(obj, proto) —— 将对象 obj 的 [[Prototype]] 设置为 proto。

## Promise async/await

Promise 对象的构造器（constructor）语法如下：

```javascript
let promise = new Promise(function(resolve, reject) {
  // executor（生产者代码，“歌手”）
});
```

传递给 `new Promise` 的函数被称为 **executor**。当 `new Promise` 被创建，executor 会自动运行并尝试执行一项工作。尝试结束后，如果成功则调用 resolve，如果出现 error 则调用 reject。：

- `resolve(value)` — 如果任务成功，并返回结果 `value`。
- `reject(error)` — 如果出现了 error，调用reject，返回error

由 `new Promise` 构造器返回的 `promise` 对象具有以下内部属性：

- `state` — 最初是 `"pending"`，然后在 `resolve` 被调用时变为 `"fulfilled"`，或者在 `reject` 被调用时变为 `"rejected"`。
- `result` — 最初是 `undefined`，然后在 `resolve(value)` 被调用时变为 `value`，或者在 `reject(error)` 被调用时变为 `error`。

**Promise 对象的 `state` 和 `result` 属性都是内部的。我们无法直接访问它们。**

### 消费者：then，catch，finally

Promise 对象充当的是 executor（“生产者代码”或“歌手”）和消费函数（“粉丝”）之间的连接，后者将接收结果或 error。可以通过使用 `.then`、`.catch` 和 `.finally` 方法为消费函数进行注册。

then

```js
promise.then(
  function(result) { /* handle a successful result */ },
  function(error) { /* handle an error */ }
);
.then 的第一个参数是一个函数，该函数将在 promise resolved 后运行并接收结果。
.then 的第二个参数也是一个函数，该函数将在 promise rejected 后运行并接收 error。
```

catch

```js
let promise = new Promise((resolve, reject) => {
  setTimeout(() => reject(new Error("Whoops!")), 1000);
});

// .catch(f) 与 promise.then(null, f) 一样
promise.catch(alert); // 1 秒后显示 "Error: Whoops!"
```

finally

`.finally(f)` 调用与 `.then(f, f)` 类似，在某种意义上，`f` 总是在 promise 被 settled 时运行：即 promise 被 resolve 或 reject。

不同于老式的回调函数，Promise约定：

- 在本轮 事件循环 运行完成之前，回调函数是不会被调用的。
- 即使异步操作已经完成（成功或失败），在这之后通过 then() 添加的回调函数也会被调用。
- 通过多次调用 then() 可以添加多个回调函数，它们会按照插入顺序进行执行 --> **链式调用**（**chaining**）

### Async&Await

函数前面的关键字 `async` 有两个作用：

1. 让这个函数总是返回一个 promise。
2. 允许在该函数内使用 `await`。

Promise 前的关键字 `await` 使 JavaScript 引擎等待该 promise settle，然后：

1. 如果有 error，就会抛出异常 — 就像那里调用了 `throw error` 一样。
2. 否则，就返回结果。

这两个关键字一起提供了一个很好的用来编写异步代码的框架，这种代码易于阅读也易于编写。

### 事件循环：微任务和宏任务

浏览器中 JavaScript 的执行流程和 Node.js 中的流程都是基于 **事件循环** 的。

**事件循环** 的概念非常简单。它是一个在 JavaScript 引擎等待任务，执行任务和进入休眠状态等待更多任务这几个状态之间转换的无限循环。

```js
while (queue.waitForMessage()) {
  queue.processNextMessage();
}
```

一个任务到来时，引擎可能正处于繁忙状态，那么这个任务就会被排入队列。多个任务组成了一个队列，即所谓的“宏任务队列”（v8 术语）

**每个宏任务之后，引擎会立即执行微任务队列中的所有任务，然后再执行其他的宏任务，或渲染，或进行其他任何操作。**

事件循环算法：

1. 从 **宏任务** 队列（例如 “script”）中出队（dequeue）并执行最早的任务。
2. 执行所有微任务：
   - 当微任务队列非空时：
     - 出队（dequeue）并执行最早的微任务。
3. 执行渲染，如果有。
4. 如果宏任务队列为空，则休眠直到出现宏任务。
5. 转到步骤 1。

安排（schedule）一个新的 **宏任务**：

- 使用零延迟的 `setTimeout(f)`。

它可被用于将繁重的计算任务拆分成多个部分，以使浏览器能够对用户事件作出反应，并在任务的各部分之间显示任务进度。

此外，也被用于在事件处理程序中，将一个行为（action）安排（schedule）在事件被完全处理（冒泡完成）后。

安排一个新的 **微任务**：

- 使用 `queueMicrotask(f)`。
- promise 处理程序也会通过微任务队列。

在微任务之间没有 UI 或网络事件的处理：它们一个立即接一个地执行。

所以，我们可以使用 `queueMicrotask` 来在保持环境状态一致的情况下，异步地执行一个函数。

用例1

假设我们有一个 CPU 过载任务。通过将大任务拆分成多个小任务来避免这个问题。

用例2

对浏览器脚本中的过载型任务进行拆分的另一个好处是，我们可以显示进度指示。

用例3

在事件之后做一些事情

在事件处理程序中，我们可能会决定推迟某些行为，直到事件冒泡并在所有级别上得到处理后。我们可以通过将该代码包装到零延迟的 `setTimeout` 中来做到这一点。

## Modules

### 什么是模块

一个模块（module）就是一个文件。一个脚本就是一个模块。模块可以相互加载，并可以使用特殊的指令 `export` 和 `import` 来交换功能，从另一个模块调用一个模块的函数：

- `export` 关键字标记了可以从当前模块外部访问的变量和函数。
- `import` 关键字允许从其他模块导入功能。

### 核心功能

1. 始终使用 `use strict`
2. 模块级作用域：每个模块都有自己的顶级作用域（top-level scope）。换句话说，一个模块中的顶级作用域变量和函数在其他脚本中是不可见的。
3. 模块解析：如果同一个模块被导入到多个其他位置（或多个文件），那么它的代码仅会在第一次导入时执行，然后将导出（export）的内容提供给所有的导入（importer）。即**模块只被执行一次**
4. import.meta：包含关于当前模块的信息。
5. 副作用（side-effect）：函数副作用指当调用函数时，除了返回函数值之外，还对主调用函数产生附加的影响。例如修改全局变量（函数外的变量），修改参数或改变外部存储。

### 模块特性

1. 模块与 defer 特性对外部脚本和内联脚本（inline script）的影响相同：
   1. 下载外部模块脚本 `<script type="module" src="...">` 不会阻塞 HTML 的处理，它们会与其他资源并行加载。
   2. 模块脚本会等到 HTML 文档完全准备就绪（即使它们很小并且比 HTML 加载速度更快），然后才会运行。
   3. 保持脚本的相对顺序：在文档中排在前面的脚本先执行。
2. Async 适用于内联脚本（inline script）异步脚本会在准备好后立即运行，独立于其他脚本或 HTML 文档。适用于例如计数器，广告，文档级事件监听器的场景
3. 兼容性，在 script 标签中使用 nomodule 属性，可以确保向后兼容。
4. 不允许裸模块（“bare” module）：`import` 必须给出相对或绝对的 URL 路径。没有任何路径的模块被称为“裸（bare）”模块。
5. 要从另一个源（域/协议/端口）加载外部脚本，需要 CORS header。

### 构建工具（如Webpack）

它们可以更好地控制模块的解析方式，允许我们使用裸模块和更多的功能，例如 CSS/HTML 模块等。

构建工具做以下这些事儿：

1. 从一个打算放在 HTML 中的 `<script type="module">` “主”模块开始。
2. 分析它的依赖：它的导入，以及它的导入的导入等。
3. 使用所有模块构建一个文件（或者多个文件，这是可调的），并用打包函数（bundler function）替代原生的 `import` 调用，以使其正常工作。还支持像 HTML/CSS 模块等“特殊”的模块类型。
4. 在处理过程中，可能会应用其他转换和优化：
   - 删除无法访问的代码。
   - 删除未使用的导出（“tree-shaking”）。
   - 删除特定于开发的像 `console` 和 `debugger` 这样的语句。
   - 可以使用 [Babel](https://babeljs.io/) 将前沿的现代的 JavaScript 语法转换为具有类似功能的旧的 JavaScript 语法。
   - 压缩生成的文件（删除空格，用短的名字替换变量等）。

如果我们使用打包工具，那么脚本会被打包进一个单一文件（或者几个文件），在这些脚本中的 `import/export` 语句会被替换成特殊的打包函数（bundler function）。因此，最终打包好的脚本中不包含任何 `import/export`，它也不需要 `type="module"`，我们可以将其放入常规的 `<script>`

### 导出export

`export`语句用于从模块中导出实时绑定的函数、对象或原始值；这些导出的内容如有修改，将实时更新。但在导入时只能被读取，不能被导入模块修改。

### 命名导出（每个模块包含任意数量）

> 在导出多个值时，命名导出非常有用。在导入期间，必须使用相应对象的相同名称。

```js
// 导出事先定义的特性
export { myFunction，myVariable };

// 导出单个特性（可以导出var，let，
//const,function,class）
export let myVariable = Math.sqrt(2);
export function myFunction() { ... };
```

### 默认导出（每个模块包含一个）

> 可以使用任何名称导入默认导出；要导出一个值或要的模块中的返回值

```js
// 导出事先定义的特性作为默认值
export { myFunction as default };

// 导出单个特性作为默认值
export default function () { ... }
export default class { .. }

// 每个导出都覆盖前一个导出
```

```js
// 导出单个特性
export let name1, name2, …, nameN; // also var, const
export let name1 = …, name2 = …, …, nameN; // also var, const
export function FunctionName(){...}
export class ClassName {...}

// 导出列表
export { name1, name2, …, nameN };

// 重命名导出
export { variable1 as name1, variable2 as name2, …, nameN };

// 解构导出并重命名
export const { name1, name2: bar } = o;

// 默认导出
export default expression;
export default function (…) { … } // also class, function*
export default function name1(…) { … } // also class, function*
export { name1 as default, … };

// 导出模块合集
export * from …; // does not set the default export
export * as name1 from …; // Draft ECMAScript® 2O21
export { name1, name2, …, nameN } from …;
export { import1 as name1, import2 as name2, …, nameN } from …;
export { default } from …;    
                               
//模块重定向(合并模块)
//将两个模块的导出，统一导入在一个父模块中，再通过一个顶层模块将两个不同模块的内容导入到顶层模块。
```

### 导入import

- 静态`import` 语句用于导入由另一个模块导出的绑定。`import` 语句只能在声明了 `type="module"` 的 `script` 的标签中使用。
- 动态 `import()`，它不需要依赖 `type="module"` 的script标签。

```js
//导入默认值（也可与上述形式结合，“，” 隔开）
import defaultExport from "module-name";
import defaultExport, { export [ , [...] ] } from "module-name";
//导入整个模块的内容
import * as name from "module-name";
//导入单个接口
import { export } from "module-name";
//导入带有别名的接口
import { export as alias } from "module-name";
//导入多个接口
import { export1 , export2 } from "module-name";
//导入多个接口
import { foo , bar } from "module-name/path/to/specific/un-exported/file";
//导入时重命名多个接口
import { export1 , export2 as alias2 , [...] } from "module-name";
//仅为副作用导入一个模块
import defaultExport, * as name from "module-name";
import "module-name";

//defaultExport 导入模块的默认导出接口的引用名。
//module-name 要导入的模块。通常是包含目标模块的.js文件的相对或绝对路径名，可以不包括.js扩展名。某些特定的打包工具可能允许或需要使用扩展或依赖文件，它会检查比对你的运行环境。只允许单引号和双引号的字符串。
//name 导入模块对象整体的别名，在引用导入模块时，它将作为一个命名空间来使用。
//export, exportN 被导入模块的导出接口的名称。
//alias, aliasN 将引用指定的导入的名称。
```

### 动态导入

标准用法的import导入的模块是静态的，会使所有被导入的模块，在加载时就被编译（无法做到按需编译，降低首页加载速度）。而动态导入可以像调用函数一样动态导入模块，以这种方式调用，将返回一个 `promise`。常用场景有：

- 当静态导入的模块很明显的降低了代码的加载速度且被使用的可能性很低，或者并不需要马上使用它。
- 当静态导入的模块很明显的占用了大量系统内存且被使用的可能性很低。
- 当被导入的模块，在加载时并不存在，需要异步获取
- 当导入模块的说明符，需要动态构建。（静态导入只能使用静态说明符）
- 当被导入的模块有副作用（这里说的副作用，可以理解为模块中会直接运行的代码），这些副作用只有在触发了某些条件才被需要时。（原则上来说，模块不能有副作用，但是很多时候，你无法控制你所依赖的模块的内容）

```js
//这种方式调用，将返回一个 promise
import('/modules/my-module.js')
  .then((module) => {
    // Do something with the module.
  });
//支持 await 关键字
let module = await import('/modules/my-module.js');
```

### 模块的特点

1. 一个模块就是一个文件。浏览器需要使用 `<script type="module">` 以使 `import/export` 可以工作。
2. 模块只通过 HTTP(s) 工作，在本地文件则不行，即：通过 `file://` 协议在本地打开一个网页时 `import/export` 指令不起作用，需要使用本地 Web 服务器。
3. 模块始终默认使用 `use strict`，严格模式下的一切限制均有效
4. 每个模块都有自己的顶级作用域（top-level scope）。换句话说，一个模块中的顶级作用域变量和函数在其他脚本中是不可见的。
5. 模块代码只执行一次。导出仅创建一次，然后会在导入之间共享。
6. 在一个模块中，顶级 `this` 是 undefined，非模块脚本的顶级 `this` 是全局对象。
7. 模块脚本 总是 被延迟的，与 defer 特性 对外部脚本和内联脚本（inline script）的影响相同。
   1. 下载外部模块脚本 `<script type="module" src="...">` 不会阻塞 HTML 的处理，它们会与其他资源并行加载。
   2. 模块脚本会等到 HTML 文档完全准备就绪（即使它们很小并且比 HTML 加载速度更快），然后才会运行。
   3. 保持脚本的相对顺序：在文档中排在前面的脚本先执行。
8. Async 适用于内联脚本（inline script）
9. 具有 `type="module"` 的外部脚本（external script）且相同 `src` 的外部脚本仅运行一次；从另一个源（例如另一个网站）获取的外部脚本需要 CORS header(不能使用JSONP)
10. 在浏览器中，`import` 必须给出相对或绝对的 URL 路径。没有任何路径的模块被称为“裸（bare）”模块。在 `import` 中不允许这种模块。
11. 旧时的浏览器不理解 `type="module"`。未知类型的脚本会被忽略。对此，我们可以使用 `nomodule` 特性来提供一个后备

函数进阶内容

对象属性配置

Generator，高级 iteration

杂项

浏览器内核

IE --> trident  

chrome --> webkit/blink  

firefox --> gecko

opera --> presto

Safari --> webkit

脚本调用策略

- 如果脚本无需等待页面解析，且无依赖独立运行，那么应使用 `async`。

- 如果脚本需要等待页面解析，且依赖于其它脚本，调用这些脚本时应使用 `defer`，将关联的脚本按所需顺序置于 HTML 中。

## JS交互与输出

1. alert()：只能输出一个内容
1. console.log()：字符串默认颜色是黑色，数字颜色是蓝色；可以输出多个内容，中间逗号隔开；
1. document.write()：可识别HMTL标签
1. innerHTML 往标签里面写入文字
1. 修改HTML元素属性和样式
1. prompt
1. confirm

alert弹出的窗口称为‘ **模态窗**’，用户不能与其它部分交互直到用户点击确定按钮。

### prompt

result = prompt(title, [default]); 浏览器会显示一个带有文本消息的模态窗口，还有 input 框和确定/取消按钮。

- title 显示给用户的文本

- default 可选的第二个参数，指定 input 框的初始值。

  > 语法中的方括号 `[...]`周围的方括号表示该参数是可选的，不是必需的。

result 是prompt函数的返回值，用户输入一些内容，确定后result获取该文本，或者按取消键，此时函数返回null；如用户不输入内容，并且第二参数没有给默认值，则内容为undefined

### confirm

result = confirm(question); `confirm` 函数显示一个带有 `question` 以及确定和取消两个按钮的模态窗口。点击确定返回 `true`，点击取消返回 `false`。

alert prompt confirm 都是 模态的：它们暂停脚本的执行，并且不允许用户与该页面的其余部分进行交互，直到窗口被解除。

#### 交互窗口的限制

1. 模态窗口的确切位置由浏览器决定。通常在页面中心。
2. 窗口的确切外观也取决于浏览器。我们不能修改它。

## 变量提升

“hoisting” 把所有的变量声明移动到函数或者全局代码的开头位置。实际上变量和函数声明在代码里的位置是不会动的，而是在编译阶段被放入内存中。

**const**常量是块级范围的，非常类似用 [let](https://developer.mozilla.org/zh-CN/docs/Web/JavaScript/Reference/Statements/let) 语句定义的变量。但常量的值是无法（通过重新赋值）改变的，也不能被重新声明。

**let** 语句声明一个块级作用域的本地变量，并且可选的将其初始化为一个值。

前置时--  ++和后置时-- ++单独运算时没有区别，参与到其他运算时，前置是先算自己再参与运算，后置是先参与运算再算自己。

1 变量提升 其赋值不会被提升

2 函数提升，函数体会被提升，但函数表达式在执行时，只提升变量，不提升函数体，此时发起调用，会报错。

3 正常顺序下，函数与变量重名，变量优先级高于函数；触发变量提升时，函数优先级高。优先级高被提升到更靠前的位置。

4 函数如果重名，两种情况下均是后面覆盖前面。

## 表达式和运算符

## yield

yield 关键字用来暂停和恢复一个生成器函数（(function* 或遗留的生成器函数）

[rv] = yield [expression];

expression
定义通过迭代器协议从生成器函数返回的值。如果省略，则返回undefined。

rv
返回传递给生成器的next()方法的可选值，以恢复其执行。

yield关键字实际返回一个IteratorResult对象，它有两个属性，value和done。value属性是对yield表达式求值的结果，而done是false，表示生成器函数尚未完全完成。

## [This]('https://juejin.cn/post/6844903680446038023')

全局作用域、普通函数、定时器、行间事件触发函数、系统自带函数及其调用传入的回调函数里 this --> Window

绑定给标签事件的函数里的this，代指的该标签

方法调用中，谁调用这个方法，这个方法的this指向谁 obj.func() this --> obj

构造函数中this指向这个构造函数的实例

在一个模块中，顶级 `this` 是 undefined。

全局上下文

全局执行环境中（在任何函数体外部）`this` 都指向全局对象。

函数上下文

`this`的值取决于函数被调用的方式。

浏览器中，全局对象是window

严格模式下，如果进入执行环境时没有设置 `this` 的值，`this` 会保持为 `undefined`

## 作用域

### 预编译

函数执行前 ：1. 创建Activation Object对象（执行期上下文）2. 找形参和变量声明作为AO属性名（默认undefined） 3. 实参 --> 形参 4. 函数体内找函数声明  值赋予函数体

### 作用域链

作用域  --> 执行期上下文   集合 -- 作用域链

函数域是指函数声明时的所在的地方，或者函数在顶层被声明时指整个程序。

**匿名函数** — 通常将匿名函数与事件处理程序一起使用；还可以将匿名函数分配为变量的值（用变量名+( ) 就可以调用匿名函数。）

函数提升仅适用于函数声明，而不适用于函数表达式。

作用域规则不适用于循环（for（）{...}）和条件块（if（）{...}） ，他们不是函数；

**难点：自定义函数时什么时候要参，加多少参，什么时候要返回值，返回值是什么。**

函数无参时可设定默认值

函数名后面的这个括号叫做**“函数调用运算符”**（function invocation operator）。你只有在想直接调用函数的地方才这么写。 同样要重视的是, **匿名函数里面的代码也不是直接运行的,** 只要代码在函数作用域内。**函数名已经是函数本身；**

没有被赋值的参数 其值为undefined；

如果一个函数有返回值，必须要接收或直接使用，不然此次函数运行无意义；

return [[expression]]; 当在函数体中使用`return`语句时，函数将会停止执行。如果指定一个值，则这个值返回给函数调用者。如果省略该值，则返回undefined。

tips：JavaScript中的函数表达式没有提升,不像函数声明,你在定义函数表达式之前不能使用函数表达式

### 构造函数 vs 函数声明 vs 函数表达式

一个用Function构造函数定义的函数,被赋值给变量multiply：

```text
var multiply = new Function('x', 'y', 'return x * y');
```

一个名为`multiply`的函数声明：

```text
function multiply(x, y) {
   return x * y;
} // 没有分号

```

一个匿名函数的函数表达式，被赋值给变量`multiply`：

```text
 var multiply = function(x, y) {
   return x * y;
 };

```

一个命名为`func_named`的函数的函数表达式，被赋值给变量`multiply`：

```text
var multiply = function func_name(x, y) {
   return x * y;
};
```

函数名不能被改变，但函数的变量却能够被再分配。

函数名只能在函数体内使用。倘若在函数体外使用函数名将会导致错误（如果函数之前是通过一个var语句声明的则是undefined）。

函数内部默认有一个arguments数组的形参，如需不固定形参个数时则函数一个参数都不要写。

### IIFE（立即调用函数表达式）

```text
(function () {
    statements
})();
```

这是一个被称为 自执行匿名函数 的设计模式，主要包含两部分。第一部分是包围在 `圆括号运算符` `()` 里的一个匿名函数，这个匿名函数拥有独立的词法作用域。这不仅避免了外界访问此 IIFE 中的变量，而且又不会污染全局作用域。第二部分再一次使用 `()` 创建了一个立即执行函数表达式，JavaScript 引擎到此将直接执行函数。当函数变成立即执行的函数表达式时，表达式中的变量不能从外部访问。

### 闭包（[**Closures**](https://developer.mozilla.org/zh-CN/docs/Web/JavaScript/Closures)）

*闭包*是由函数以及声明该函数的词法环境组合而成的。该环境包含了这个闭包创建时作用域内的任何局部变量。也就是说，闭包让你可以在一个内层函数中访问到其外层函数的作用域。在 JavaScript 中，每当创建一个函数，闭包就会在函数创建的同时被创建出来。

作用：模拟私有方法--> [模块模式（module pattern）](http://www.google.com/search?q=javascript+module+pattern "http://www.google.com/search?q=javascript+module+pattern") 数据隐藏和封装

### 回调函数

被作为实参传入另一函数，并在该外部函数内被调用，用以来完成某些任务的函数，称为回调函数。
