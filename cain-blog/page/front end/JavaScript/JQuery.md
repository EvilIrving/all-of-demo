---
title: JQuery
date: 2020-03-21
updated: 2020-03-25
tags:
- JavaScript
- JQuery
categories:
- JavaScript
---

## 版本

- JQ1.x 支持低版本IE IE678
- JQ2.x 不支持低版本IE 稳定
- JQ3.x 不支持低版本IE 最新版

## JQ获取DOM节点

`$('selector')` return JQObject;

### JQ选择器

1. `$("tagName")`：根据 标签名 选择所有该标签的元素

2. `$("#id")`：根据 id 选择元素；id应该是唯一的，如果id重复，则只会选择第一个。

3. `$(".className")`：根据 class 选择元素

4. `$("selector1 selector2")`：选择 selector1下的selector2元素 。

5. `$(selector:first)` 满足选择器条件的第一个元素

6. `$(selector:last)` 满足选择器条件的最后一个元素

7. `$(selector:odd)` 满足选择器条件的奇数元素；基0

8. `$(selector:even)` 满足选择器条件的偶数元素；基0

9. `$(selector:hidden)` 满足选择器条件的不可见的元素

10. `$(selector:visible)` 满足选择器条件的可见的元素

    > 注； `div:visible` 和`div :visible`(有空格)是不同的意思，`div:visible` 表示选中可见的div；`div :visible`(有空格) 表示选中div下可见的元素

11. 在监听函数中使用`$(this)`，即表示触发该事件的组件。

|                    选择器                                          |         类型     |
| :----------------------------------------------------------- | :----------: |
 | `[attribute]` `[attribute=value]` `[attribute!=value]` `[attribute^=value]`  `[attribute$=value]` `[attribute*=value]` |     属性     |      |
| `:input` `:button` `:radio` `:checkbox` `:text` `:file` `:submit` `:image`   |   表单对象   |
| `:enabled` `:disabled` `:checked` `:selected`                        | 表单对象属性 |

#### JQ筛选器

筛选器是在选择器基础上，做进一步选择；

- `first()` 第1个元素
- `last()` 最后一个元素
- `eq/gt/lt(index)` 找到对象序号第index/大于/小于index的对象，注: index基0
- `parent()` 选取最近的一个父元素
- `parents()` 选取所有的祖先元素
- `children()`: 筛选出儿子元素 (紧挨着的子元素)
- `find(selector)`: 筛选出后代元素；注: find() 必须使用参数 selector
- `siblings()`: 同级(同胞)元素

## JQ填充节点内容

`html()`：设置每一个匹配元素的html内容 或 获取集合中第一个匹配元素的HTML内容（保留标签）。--> innerHTML

`text()`：得到匹配元素集合中每个元素的文本内容结合，包括他们的后代（不读取标签），或设置匹配元素集合中每个元素的文本内容为指定的文本内容。--> innerText

`val()`：获取匹配的元素集合中第一个元素的当前值或设置匹配的元素集合中每个元素的值。主要用于获取表单元素的值，比如 `input`, `select` 和 `textarea` --> textContent

## JQ操作CSS样式

1. `addClass()` 增加一个样式中的class
2. `removeClass()` 删除一个样式中的class
3. 通过`toggleClass()` 切换一个样式中的class
4. 通过css函数 直接设置样式
   1. 设置JQ对象的CSS样式：`css(property,values) / css({p1:v1,p2:v2})`；如果有多个元素，则全部设置对应样式
   2. 获取JQ对象的CSS样式：`css('property') return 样式值`；如果有多个元素，获取样式时只获取第一个元素的样式值

## JQ与DOM对象的转化

JQ对象通过下标`[0]`取出的就是DOM对象；将DOM对象直接放在$()方法里，返回的是JQ对象。

## JQ操作DOM特性和属性

### 介绍与比较

在 HTML 中，标签可能拥有特性（attributes）。当浏览器解析 HTML 文本，并根据标签创建 DOM 对象时，浏览器会辨别 **标准的** 特性并以此创建 DOM 属性。非 **标准的** 特性则不会生成DOM属性，也就意味着区别。

- 特性（attribute）— 写在 HTML 中的内容。
- 属性（property）— DOM 对象中的内容。

简略的对比：

|      | 属性                                  | 特性                         |
| :--- | :------------------------------------- | :--------------------------- |
| 类型 | 任何值，标准的属性具有规范中描述的类型 | 字符串                       |
| 名字 | 名字（name）是大小写敏感的             | 名字（name）是大小写不敏感的 |

如果一个特性不是标准的，那么就没有相对应的 DOM 属性。我们有什么方法来访问这些特性吗？操作特性的方法：

- `elem.hasAttribute(name)` — 检查是否存在这个特性。
- `elem.getAttribute(name)` — 获取这个特性值。
- `elem.setAttribute(name, value)` — 设置这个特性值。
- `elem.removeAttribute(name)` — 移除这个特性。
- `elem.attributes` — 所有特性的集合，属于内建 Attr 类的对象的集合，具有 name 和 value 属性。

在大多数情况下，最好使用 DOM 属性。仅当 DOM 属性无法满足开发需求，并且我们真的需要特性时，才使用特性

在于自定义属性和选中属性的处理也有不同。选中属性指的是 `checked`, `selected`

1. 对于自定义属性 `attr`能够获取，`prop`不能获取
2. 对于选中属性` attr `只能获取初始值， 无论是否变化，`prop` 能够访问变化后的值，并且以`true|false`的布尔型返回。

### 特性和属性之间的同步

当一个标准的特性被改变，对应的属性也会自动更新，（除了几个特例）反之亦然。

特性 => 属性  属性 => 特性

特例： `input.value` 只能从特性同步到属性，反过来则不行

### 设置 修改 删除

- `attr()`、`attr(attr,value)`
- `attr({attr1:value1,attr2:value2})`
- `removeAttr(attr)`

## JQDOM操作

三步：`$('body').append($('标签').css('',''));`

### 插入

`.append/.appendTo()`(JQ对象/DOM元素/HTML字符串/元素数组)；添加到父级元素的最后面

`.prepend/.prependTo`(JQ对象/DOM元素/HTML字符串/元素数组)；添加到父元素的最前面

`after`(JQ对象/DOM元素/HTML字符串/元素数组)； 被选元素之后插入

`before`(JQ对象/DOM元素/HTML字符串/元素数组)； 被选元素之前插入

### 删除

`.remove()` 不保留事件

`.detach()`保存所有jQuery数据和被移走的元素相关联 保留事件

> 两个方法都会返回删除的对象

### 克隆

`clone(boolean)`;

> 参数决定是否克隆事件；方法返回值是克隆好的jq对象

### 查找

- `parent([expr])` 取得一个包含着所有匹配元素的唯一父元素的元素集合。

- `parents([expr])`取得一个包含着所有匹配元素的祖先元素的元素集合（不包含根元素）。可以通过一个可选的表达式进行筛选。

- `parentsUntil([expr|element] [,filter])` 查找当前元素的所有的父辈元素，直到遇到匹配的那个元素为止。

- `children([expr])` 取得一个包含匹配的元素集合中每一个元素的所有子元素的元素集合。

- `find(expr|obj|ele)` 搜索所有与指定表达式匹配的元素。这个函数是找出正在处理的元素的后代元素的好方法。

- `prev([expr])`

- `next()`

- `prevAll()`

- `nextAll([expr])` 查找当前元素之后所有的同辈元素。可以用表达式过滤

- `prevUntil([exp|ele] [,filter])` 查找当前元素之前所有的同辈元素，直到遇到匹配的那个元素为止。filter过滤匹配到的选择器。

- `nextUntil([exp|ele] [,fil])` 查找当前元素之后所有的同辈元素，直到遇到匹配的那个元素为止。

- `siblings([expr])`取得一个包含匹配的元素集合中每一个元素的所有唯一同辈元素的元素集合。可以用可选的表达式进行筛选。

  > 如果没有选择器匹配到，或者没有提供参数，那么跟在后面的所有同辈元素都会被选中。这就跟用没有提供参数的 `.nextAll()`效果一样。

### 尺寸和位置

`width()`, `height()`  块盒元素原本宽高或行盒元素内容高度（不含padding等）

`innerWidth()`, `innerHeight()` 前面基础 + padding

`outerWidth(false)`,`outerHeight(false)`  前面基础 + border; 若FALSE改为TRUE，计算margin

`offset()` 返回或设置匹配元素相对于**文档**的偏移（位置）对象，对象包含两个整型属性：top 和 left

`position()` 返回匹配元素相对于父元素的位置（偏移）。其对象包含两个整型属性：top 和 left；

> 返回元素margin距离离他最近的，设置了非static定位的祖先元素的位置，但是该元素自身的margin不参与运算

### 数组

`$.each( function(index, Element) )` 遍历一个jQuery对象，为每个匹配元素执行一个函数。

`$.unique( array )` 删除数组中重复元素。只处理删除DOM元素数组，而不能处理字符串或者数字数组。

    > 执行`unique`之前，要先调用`sort`对数组的内容进行排序。

`$.inArray( value, array [, fromIndex ] )` 在数组中查找指定值并返回它的索引（如果没有找到，则返回-1）。

## JQ动画

1. `$(*selector*).hide(*speed,callback*);`

2. `$(*selector*).show(*speed,callback*);`

3. `$(*selector*).toggle(*speed,callback*);`

4. `$(*selector*).fadeIn(*speed,callback*);`

5. `$(*selector*).fadeOut(*speed,callback*);`

6. `$(*selector*).fadeToggle(*speed,callback*);`

7. `$(*selector*).fadeTo(*speed,callback*);`

8. `$(*selector*).slideDown(*speed,callback*);`

9. `$(*selector*).slideUp(*speed,callback*);`

10. `$(*selector*).slideToggle(*speed,callback*);`
    >注：  可选的 speed 参数规定隐藏/显示的速度，可以取以下值："slow"、"fast" 或毫秒
    可选的 callback 参数是隐藏或显示完成后所执行的函数名称。
11. animate( properties [, duration ] [, easing ] [, complete ] ) easing选项 jQuery自身提供"linear" 和 "swing"
    >注： 默认情况下，html中的元素都是固定，并且无法改变的位置的。 为了使用animate()自定义动画效果，需要通过css把元素的position设置为relative、absolute或者fixed。只有数字能而字符值不能实现动画，使用 += 或 -= 创建相对动画*/

## JQ事件

- `$('selector').event(func(){});`
- `$('selector').on('event')` on方式有多种形式的事件绑定法子

```js
// 方法1
$('#div1').on('click', function () {
    $(this).css('background', randomColor());
});
//方法2
$('#div1').on('click mouseover mousemove', function () {
    $(this).css('background', randomColor());
});
//方法3
$('#div1').on({
    mouseover: function () {
        $(this.animate({
            height: '+=10'
        }, 500));
    },
    mouseleave: function () {
        $(this).animate({
            width: '+=10'
        }, 500);
    },
    click: function () {
        $(this).css('background', randomColor());
    },
}); 
```

### 自定义事件

```js
$("#div1").on("myEvent123", function (e, a) {
    console.log("myEvent123事件触发了!", e, a);
});

var timer = setInterval(function () {
    console.log("计时器触发!");
    if (Math.random() > 0.9) {
        //触发myEvent123事件
        // 触发自定义事件 trigger("触发的自定义事件的名字");
        $("#div1").trigger("myEvent123", { a: 1, b: 2 });
        clearInterval(timer);
    }
}, 1000);
```

### 文档加载事件

1. `window.onload`
2. `window.ready`

`script` 标签书写位置：

- 第三方文件写在head
- 自定义脚本
  - 如没有获取DOM元素的代码，建议写在head
  - 如有，写在body最下面，或者添加onload事件，

当DOM载入就绪可以查询及操纵时绑定一个要执行的函数。

这是事件模块中最重要的一个函数，因为它可以极大地提高web应用程序的响应速度。简单地说，这个方法纯粹是对向`window.load`事件注册事件的替代方法。通过使用这个方法，可以在DOM载入就绪能够读取并操纵时立即调用你所绑定的函数，而99.99%的JavaScript函数都需要在那一刻执行。有一个参数－－`对jQuery函数的引用`－－会传递到这个ready事件处理函数中。可以给这个参数任意起一个名字，并因此可以不再担心命名冲突而放心地使用`$`别名。请确保在 `<body>` 元素的onload事件中没有注册函数，否则不会触发`+$(document).ready()`事件。可以在同一个页面中无限次地使用`$(document).ready()`事件。其中注册的函数会按照（代码中的）先后顺序依次执行。
