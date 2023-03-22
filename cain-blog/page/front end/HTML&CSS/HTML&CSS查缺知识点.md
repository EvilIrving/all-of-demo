---
title: HTML与CSS基础
date: 2021-07-18
updated: 2021-07-18
tags:
- HTML
- CSS
- 教程
categories:
- HTML
---

## 知识点

### Font 简写

  `font-style`, `font-variant`, font-weight, font-stretch, **font-size/ line-height**,, and **font-family**.

  font: italic normal bold normal 3em/1.5 Helvetica, Arial, sans-serif;

### 网页安全字体

`Arial(Helvetica)(sans-serif)`、`Courier New(monospace)`、`Georgia(serif)`、`Times New Roman(serif)`、`Trebuchet MS(sans-serif)`、`Verdana(sans-serif)`

|     名称     | 定义                                                         |
| :----------: | :----------------------------------------------------------- |
|   `serif`    | 有衬线的字体 （衬线一词是指字体笔画尾端的小装饰，存在于某些印刷体字体中） |
| `sans-serif` | 没有衬线的字体。                                             |
| `monospace`  | 每个字符具有相同宽度的字体，通常用于代码列表。               |
|  `cursive`   | 用于模拟笔迹的字体，具有流动的连接笔画。                     |
|  `fantasy`   | 用来装饰的字体                                               |

### `text-transform`

> 允许你设置要转换的字体。值包括：

- `none`: 防止任何转型。
- `uppercase`: 将所有文本转为大写。
- `lowercase`: 将所有文本转为小写。
- `capitalize`: 转换所有单词让其首字母大写。
- `full-width`: 将所有字形转换成全角，即固定宽度的正方形，类似于等宽字体，允许拉丁字符和亚洲语言字形（如中文，日文，韩文）对齐。

### `text-decoration`

设置/取消字体上的文本装饰，可用值为：

- `none`: 取消已经存在的任何文本装饰。
- `underline`: 文本下划线.
- `overline`: 文本上划线
- `line-through`: 穿过文本的线 `strikethrough over the text`.

> `text-decoration` 可以接受多个值，如 `text-decoration: underline overline`

`letter-spacing` 和 `word-spacing` 属性允许你设置你的文本中的字母与字母之间的间距、或是单词与单词之间的间距。

### 语义化标记

- `<main>` 存放每个页面独有的内容。每个页面上只能用一次 `<main>`，且直接位于 `<body>` 中。最好不要把它嵌套进其它元素。

- `<article>` 包围的内容即一篇文章，与页面其它部分无关（比如一篇博文）。

- `<section>` 与 `<article>` 类似，但 `<section>` 更适用于组织页面使其按功能（比如迷你地图、一组文章标题和摘要）分块。一般的最佳用法是：以 [标题](https://developer.mozilla.org/en-US/Learn/HTML/Howto/Set_up_a_proper_title_hierarchy) 作为开头；也可以把一篇 `<article>` 分成若干部分并分别置于不同的 `<section>` 中，也可以把一个区段 `<section>` 分成若干部分并分别置于不同的 `<article>` 中，取决于上下文。

- `<aside>` 包含一些间接信息（术语条目、作者简介、相关链接，等等）。

- `<header>` 是简介形式的内容。如果它是 `<body>` 的子元素，那么就是网站的全局页眉。如果它是 `<article>` 或`<section>` 的子元素，那么它是这些部分特有的页眉（此 `<header>` 非彼 [标题](https://developer.mozilla.org/zh-CN/docs/learn/HTML/Introduction_to_HTML/The_head_metadata_in_HTML#%E5%A2%9E%E5%8A%A0%E4%B8%80%E4%B8%AA%E6%A0%87%E9%A2%98)）。

- `<nav>` 包含页面主导航功能。其中不应包含二级链接等内容。

- `<footer>` 包含了页面的页脚部分。

`<span>` 是一个内联的（inline）无语义元素，最好只用于无法找到更好的语义元素来包含内容时，或者不想增加特定的含义时。

`<div>` 是一个块级无语义元素，应仅用于找不到更好的块级元素时，或者不想增加特定的意义时。

`<br>` 可在段落中进行换行；`<br>` 是唯一能够生成多个短行结构（例如邮寄地址或诗歌）的元素。

`<hr>` 元素在文档中生成一条水平分割线，表示文本中主题的变化（例如话题或场景的改变）。一般就是一条水平的直线。

### 重排和重绘

- 部分渲染树（或者整个渲染树）需要重新分析并且节点尺寸需要重新计算。这被称为重排。注意这里至少会有一次重排-初始化页面布局。

- 由于节点的几何属性发生改变或者由于样式发生改变，例如改变元素背景色时，屏幕上的部分内容需要更新。这样的更新被称为重绘。

什么时候会触发重排重绘

- 添加、删除、更新 DOM 节点

- display: none 隐藏一个 DOM 节点-触发重排和重绘

- 通过 visibility: hidden 隐藏一个 DOM 节点-只触发重绘，因为没有几何变化

- 移动或者给页面中的 DOM 节点添加动画

- 添加一个样式表，调整样式属性

- 用户行为，例如调整窗口大小，改变字号，或者滚动。

### 盒模型

CSS 盒模型描述了以文档树中的元素而生成的矩形框，并根据排版模式进行布局。每个盒子都有一个内容区域（例如文本，图像等）以及周围可选的`padding`、`border`和`margin`区域。

CSS 盒模型负责计算：

- 块级元素占用多少空间。
- 边框是否重叠，边距是否合并。
- 盒子的尺寸。

盒模型有以下规则：

- 块级元素的大小由`width`、`height`、`padding`、`border`和`margin`决定。
- 如果没有指定`height`，则块级元素的高度等于其包含子元素的内容高度加上`padding`（除非有浮动元素，请参阅下文）。
- 如果没有指定`width`，则非浮动块级元素的宽度等于其父元素的宽度减去父元素的`padding`。
- 元素的`height`是由内容的`height`来计算的。
- 元素的`width`是由内容的`width`来计算的。
- 默认情况下，`padding`和`border`不是元素`width`和`height`的组成部分。

#### 参考

- <https://www.smashingmagazine.com/2010/06/the-principles-of-cross-browser-css-coding/#understand-the-css-box-model>

### 盒子模型

内容（content）、填充（padding）、边界（margin）、边框（border）

`border-box`: 属性width，height包含content、border和padding，指的是content
+padding+border

`content-box`：属性width，height只包含内容content，不包含border和padding

- `block`
  - 盒子会在内联的方向上扩展并占据父容器在该方向上的所有可用空间，在绝大数情况下意味着盒子会和父容器一样宽
  - 每个盒子都会换行
  - `width` 和 `height` 属性可以发挥作用
  - 内边距（`padding`）, 外边距（`margin`） 和 边框（`border`） 会将其他元素从当前盒子周围“推开”

- `inline`
  - 盒子不会产生换行。
  - `width` 和 `height` 属性将不起作用。
  - 垂直方向的内边距、外边距以及边框会被应用但是不会把其他处于 `inline` 状态的盒子推开。
  - 水平方向的内边距、外边距以及边框会被应用且会把其他处于 `inline` 状态的盒子推开。

用做链接的 `<a>` 元素、 `<span>`、 `<em>` 以及 `<strong>` 都是默认处于 `inline` 状态的。我们通过对盒子`display` 属性的设置，比如 `inline` 或者 `block` ，来控制盒子的外部显示类型。

### CSS字体

将字体文件引用到CSS文件中，使用`font-family`自定义字体名，src设置源文件路径  ttf格式兼容性强；eot仅IE支持；SVG用于IOS；woff用于Chrome和Firefox

```css
@font-face { 
   font-family: AuroraFont;
   src: url(fonts/sc.ttf)
}
```

### CSS 选择符

- id选择器 `#myid`
- 类选择器 `.myclassname`
- 标签选择器 `div,h1,p`
- 后代选择器 `h1 p`
- 相邻后代选择器 子选择器 `ul>li`
- 兄弟选择器 `li~a`
- 相邻兄弟选择器 `li+a`
- 属性选择器 `a[rel="external"]`
- 伪类选择器 `a:hover,li:nth-child`
- 伪元素选择器 `::before、::after`
- 通配符选择器 `*`

### `::before` 和: after 中双冒号和单冒号

单冒号`:`用于CSS3伪类，双冒号`::`用于CSS3伪元素。伪元素由双冒号和伪元素名称组成，伪类和伪元素是用来修饰不在文档树中的部分

想让插入的内容出现在其它内容前，使用`::before`，否者，使用`::after`；
在代码顺序上，`::after`生成的内容也比`::before`生成的内容靠后。
如果按堆栈视角，`::after`生成的内容会在`::before`生成的内容之上。

### CSS 中哪些属性可以继承

有继承性的属性：

- 字体系列属性
  `font、font-family、font-weight、font-size、font-style、font-variant、font-stretch、font-size-adjust`

- 文本系列属性
  `text-indent、text-align、text-shadow、line-height、word-spacing、letter-spacing、
  text-transform、direction、color`

- 表格布局属性
  `caption-side border-collapse empty-cells`

- 列表属性
  `list-style-type、list-style-image、list-style-position、list-style`

- 光标属性  `cursor`

- 元素可见性 `visibility`

- 还有一些不常用的；speak，page，设置嵌套引用的引号类型quotes等属性

>非继承属性通过将它的值设置为inherit来使它从父元素那获取同名的属性值来继承

### 优先级算法

选择器的特殊性值分为四个等级，如下：

- !important 最高
- 标签内选择符x,0,0,0
- ID选择符0,x,0,0
- class选择符/属性选择符/伪类选择符 0,0,x,0
- 元素和伪元素选择符0,0,0,x

### 伪元素

- 伪元素与`hover`不同,`hover`是一个伪类选择器并不是标签.但是伪元素就相当于一个标签存在,所以可以为它设置背景色,字号,浮动等css效果.

- `:after`和`:before`是在css2提出的. `::after`和 `::before`是在css3中提出的.

适用场景:

- 伪元素写在css代码中,如果网页里的某些内容的作用仅仅是用于装饰,没有必要用一个标签来表示,就可以考虑使用伪元素.
- 或者需要一个元素时,该元素存在且目的不是为了展示而是为了解决其他问题.此时也可以考虑使用伪元素.

### 伪类 LVHA

链接访问前、链接访问后、鼠标滑过、激活，分别对应四种伪类:link、:visited、:hover、:active；

### CSS3 有哪些新特性

- 新增各种CSS选择器 （:not(.input)：所有class不是“input”的节点）
- 圆角  （border-radius:8px）
- 多列布局 （multi-column layout）
- 阴影和反射 （Shadow\Reflect）
- 文字特效  （text-shadow）
- 文字渲染  （Text-decoration）
- 线性渐变  （gradient）
- 旋转   （transform）
- 缩放，定位，倾斜，动画，多背景
- 例如：transform:\scale(0.85,0.90)\translate(0px,-30px)\skew(-9deg,0deg)\Animation:

### CSS3 新增伪类

- `elem:nth-child(n)`选中父元素下的第n个子元素，并且这个子元素的标签名为elem，n可以接受具体的数
值，也可以接受函数。
-`elem:nth-last-child(n)`作用同上，不过是从后开始查找。
- `elem:last-child`选中最后一个子元素。
- `elem:only-child`如果elem是父元素下唯一的子元素，则选中之。
- `elem:nth-of-type(n)`选中父元素下第n个elem类型元素，n可以接受具体的数值，也可以接受函数。
- `elem:first-of-type`选中父元素下第一个elem类型元素。
- `elem:last-of-type`选中父元素下最后一个elem类型元素。
- `elem:only-of-type`如果父元素下的子元素只有一个elem类型元素，则选中该元素。
- `elem:empty`选中不包含子元素和内容的elem类型元素。
- `elem:target`选择当前活动的elem元素。
- `:not(elem)`选择非elem元素的每个元素。
- `:enabled` 控制表单控件的禁用状态。
- `:disabled` 控制表单控件的禁用状态。
- `:checked`单选框或复选框被选中。

### display 有哪些值

- block 块类型。默认宽度为父元素宽度，可设置宽高，换行显示。
- none 元素不显示，并从文档流中移除。
- inline 行内元素类型。默认宽度为内容宽度，不可设置宽高，同行显示。
- inline-block 默认宽度为内容宽度，可以设置宽高，同行显示。
- list-item 像块类型元素一样显示，并添加样式列表标记。
- table 此元素会作为块级表格来显示。
- inherit 规定应该从父元素继承display属性的值。

### position 的值 relative 和 absolute 定位原点是

- absolute：生成绝对定位的元素，相对于值不为static的第一个父元素的padding box进行定位，也可以理解为离自己这一级元素最近的一级position设置为absolute或者relative的父元素的padding box的左上角为原点的。

- fixed：生成绝对定位的元素，相对于浏览器窗口进行定位。

- relative：生成相对定位的元素，相对于其元素本身所在正常位置进行定位。

- static：默认值。没有定位，元素出现在正常的流中（忽略top,bottom,left,right,z-index声明）。

- inherit：规定从父元素继承position属性的值。

### 绝对定位元素与非绝对定位元素的百分比计算的区别

- 绝对定位元素的宽高百分比是相对于临近的position不为static的祖先元素的`padding box`来计算的。
- 非绝对定位元素的宽高百分比则是相对于父元素的`content box`来计算的。

### Flex box

```txt
flex-direction属性决定主轴的方向（即项目的排列方向）。

flex-wrap属性定义，如果一条轴线排不下，如何换行。

flex-flow属性是flex-direction属性和flex-wrap属性的简写形式，默认值为row nowrap。

justify-content属性定义了项目在主轴上的对齐方式。

align-items属性定义项目在交叉轴上如何对齐。

align-content属性定义了多根轴线的对齐方式。如果项目只有一根轴线，该属性不起作用。


以下6个属性设置在项目上。

order属性定义项目的排列顺序。数值越小，排列越靠前，默认为0。

flex-grow属性定义项目的放大比例，默认为0，即如果存在剩余空间，也不放大。

flex-shrink属性定义了项目的缩小比例，默认为1，即如果空间不足，该项目将缩小。

flex-basis属性定义了在分配多余空间之前，项目占据的主轴空间。浏览器根据这个属性，计算主轴是否有多余空间。它的默认
值为auto，即项目的本来大小。

flex属性是flex-grow，flex-shrink和flex-basis的简写，默认值为0 1 auto。

align-self属性允许单个项目有与其他项目不一样的对齐方式，可覆盖align-items属性。默认值为auto，表示继承父
元素的align-items属性，如果没有父元素，则等同于stretch。
```

### 什么是`containing block`，对于`containing block`的理解

`containing block`：元素用来计算和定位的一个框

- 根元素`<html>`被称为初始`containing block`，其尺寸等同于浏览器可视窗口的大小。

- 对于其他元素，如果该元素的`position`是`relative`或者`static`，则`containing block`由其最近的块容器祖先盒的`content box`边界形成。

- 如果元素`position:fixed`，则`containing block`是初始`containing block`。

- 如果元素`position:absolute`，则`containing block`由最近的`position`不为`static`的祖先元素建立，具体方式如下：
  - 如果该祖先元素是纯`inline`元素，则规则略复杂：
    - 假设给内联元素的前后各生成一个宽度为0的`inline box`，则这两个`inline box`的`padding box`外面的包围盒就是内联元素的`containing block`；
    - 如果该内联元素被跨行分割了，那么`containing block`是未定义的，也就是CSS2.1规范并没有明确定义，浏览器自行发挥否则，`containing block`由该祖先的padding box边界形成。
  - 如果没有符合条件的祖先元素，则`containing block`是初始`containing block`。

### CSS 里的 `visibility` 属性`collapse` 属性值

- 对于一般的元素，它的表现跟`visibility：hidden`是一样的。元素是不可见的，但此时仍占用页面空间。

- 如果这个元素是`table`相关的元素，例如`table`行，`table group`，`table`列，`table column group`，它的表现跟display:none一样，占用的空间也会释放。

不同浏览器下的区别：

- 在谷歌浏览器里，使用`collapse`值和使用`hidden`值没有什么区别。
- 火狐浏览器、Opera和IE11里，使用`collapse`值的效果就如它的字面意思：`table`的行会消失，它的下面一行会补充它的位置。

### `width:auto` 和 `width:100%` 的区别

- `width:100%`会使元素`box`的宽度等于父元素的`content box`的宽度。
- `width:auto`会使元素撑满整个父元素，`margin`、`border`、`padding`、`content`区域会自动分配水平空间。

### 图片 base64 编码

base64编码是一种图片处理格式，通过特定的算法将图片编码成一长串字符串，在页面上显示的时候，可以用该字符串来代替图片的url属性。小图标可以使用base64图片来引入

使用base64的优点是：减少一个图片的HTTP请求

使用base64的缺点是：

- 根据base64的编码原理，编码后的大小会比原文件大小大1/3
- 使用base64无法直接缓存，要缓存只能缓存包含base64的文件，比如HTML或者CSS
- 兼容性的问题，ie8以前的浏览器不支持。

### 块级格式化上下文（Block Formatting Context，BFC）

块格式化上下文（Block Formatting Context，BFC）是Web页面的可视化CSS渲染的一部分，是布局过程中生成块级盒
子的区域，也是浮动元素与其他元素的交互限定区域。

- 规则
  - 创建BFC的元素，它的高度会计算浮动元素；可以用于解决高度坍塌
  - 边框盒的不会与浮动元素重叠；可用与固定一个侧边栏，右边自适应
  - 不会和它的子元素进行外边距合并
- 创建BFC
  - 根元素或包含根元素的元素
  - `float`的值不是`none`
  - `position`的值不是`static`或`relative`
  - `display`的值是`table-cell`、`table-caption`、`inline-block`、`flex`、或`inline-flex`
  - `overflow`的值不是`visible`

### IFC 是什么？

IFC指的是行级格式化上下文，布局规则：

- 行级上下文内部的盒子会在水平方向，一个接一个地放置。
- 当一行不够的时候会自动切换到下一行。
- 行级上下文的高度由内部最高的内联盒子的高度决定。

### 请阐述`z-index`属性，并说明如何形成层叠上下文（stacking context）

CSS 中的`z-index`属性控制重叠元素的垂直叠加顺序。`z-index`只能影响`position`值不是`static`的元素。

没有定义`z-index`的值时，元素按照它们出现在 DOM 中的顺序堆叠（层级越低，出现位置越靠上）。非静态定位的元素（及其子元素）将始终覆盖静态定位（static）的元素，而不管 HTML 层次结构如何。

层叠上下文是包含一组图层的元素。 在一组层叠上下文中，其子元素的`z-index`值是相对于该父元素而不是 document root 设置的。每个层叠上下文完全独立于它的兄弟元素。如果元素 B 位于元素 A 之上，则即使元素 A 的子元素 C 具有比元素 B 更高的`z-index`值，元素 C 也永远不会在元素 B 之上.

每个层叠上下文是自包含的：当元素的内容发生层叠后，整个该元素将会在父层叠上下文中按顺序进行层叠。少数 CSS 属性会触发一个新的层叠上下文，例如`opacity`小于 1，`filter`不是`none`，`transform`不是`none`。

- <https://css-tricks.com/almanac/properties/z/z-index/>
- <https://philipwalton.com/articles/what-no-one-told-you-about-z-index/>
- <https://developer.mozilla.org/en-US/docs/Web/CSS/CSS_Positioning/Understanding_z_index/The_stacking_context>

### `display`, `position`和`float`的相互关系

1. 判断display属性是否为none，如果为none，则position和float属性的值不影响元素最后的表现。
2. 判断position的值是否为absolute或者fixed，如果是，则float属性失效，并且display的值应该被
设置为table或者block，具体转换需要看初始转换值。
3. 如果position的值不为absolute或者fixed，则判断float属性的值是否为none，如果不是，则display
的值则按上面的规则转换。注意，如果position的值为relative并且float属性的值存在，则relative相对
于浮动后的最终位置定位。
4. 如果float的值为none，则判断元素是否为根元素，如果是根元素则display属性按照上面的规则转换，如果不是，
则保持指定的display属性值不变。

总的来说，可以把它看作是一个类似优先级的机制，`position:absolute`和`position:fixed`优先级最高，有它存在
的时候，浮动不起作用，`display`的值也需要调整；其次，元素的`float`特性的值不是`none`的时候或者它是根元素
的时候，调整`display`的值；最后，非根元素，并且非浮动元素，并且非绝对定位的元素，`display`特性值同设置值。

### layout viewport、visual viewport 和 ideal viewport

- 布局视口，在移动端显示网页时，由于移动端的屏幕尺寸比较小，如果网页使用移动端的屏幕尺寸进行布局的话，那么整个页面的布局都会显示错乱。所以移动端浏览器提供了一个layout viewport布局视口的概念，使用这个视口来对页面进行布局展示，一般layout viewport的大小为980px，因此页面布局不会有太大的变化，我们可以通过拖动和缩放来查看到这个页面
- 视觉视口，visual viewport指的是移动设备上我们可见的区域的视口大小，一般为屏幕的分辨率的大小。visual viewport和layout viewport的关系，就像是我们通过窗户看外面的风景，视觉视口就是窗户，而外面的风景就是布局视口中的网页内容。
- 理想视口，由于layout viewport一般比visual viewport要大，所以想要看到整个页面必须通过拖动和缩放才能实现。所以又提出了ideal viewport的概念，ideal viewport下用户不用缩放和滚动条就能够查看到整个页面，并且页面在不同分辨率下显示的内容大小相同。ideal viewport其实就是通过修改layout viewport的大小，让它等于设备的宽度，这个宽度可以理解为是设备独立像素，因此根据ideal viewport设计的页面，在不同分辨率的屏幕下，显示应该相同。

### 图片格式

- BMP，是无损的、既支持索引色也支持直接色的、点阵图。
- JPEG是有损的，适合用来存储照片，不适合用来存储企业Logo、线框类的图。
- PNG-8是无损的、使用索引色的、点阵图。PNG是一种比较新的图片格式，PNG-8是非常好的GIF格式替代者，具有更小的文件体积。支持透明度的调节，而GIF并不支持。
- PNG-24是无损的、使用直接色的、点阵图。PNG-24的优点在于，它压缩了图片的数据，使得同样效果的图片，PNG-24格
式的文件大小要比BMP小得多。当然，PNG24的图片还是要比JPEG、GIF、PNG-8大得多。
- SVG是无损的、矢量图。SVG是矢量图。
- WebP是谷歌开发的一种新图片格式，WebP是同时支持有损和无损压缩的、使用直接色的、点阵图。
  - 在无损压缩的情况下，相同质量的WebP图片，文件大小要比PNG小26%；
  - 在有损压缩的情况下，具有相同图片精度的WebP图片，文件大小要比JPEG小25%~34%；
  - WebP图片格式支持图片透明度，一个无损压缩的WebP图片，如果要支持透明度只需要22%的格外文件大小。

### CSS 预处理器和后处理器

- CSS预处理器定义了一种新的语言，其基本思想是，用一种专门的编程语言，为CSS增加了一些编程的特性，将CSS作为目标生成文件：Less、Sass
- CSS后处理器是对CSS进行处理，并最终生成CSS的预处理器，它属于广义上的CSS预处理器：PostCSS，给CSS属性添加浏
览器私有前缀，实现跨浏览器兼容性的问题。

### 使用 CSS 预处理的优缺点分别是什么？

优点：

- 提高 CSS 可维护性。
- 易于编写嵌套选择器。
- 引入变量，增添主题功能。可以在不同的项目中共享主题文件。
- 通过混合（Mixins）生成重复的 CSS。
- 将代码分割成多个文件。不进行预处理的 CSS，虽然也可以分割成多个文件，但需要建立多个 HTTP 请求加载这些文件。

缺点：

- 需要预处理工具。
- 重新编译的时间可能会很慢。

### 对于你使用过的 CSS 预处理，说说喜欢和不喜欢的地方？

喜欢：

- 绝大部分优点上题以及提过。
- Less 用 JavaScript 实现，与 NodeJS 高度结合。

**Dislikes:**

- 我通过`node-sass`使用 Sass，它用 C ++ 编写的 LibSass 绑定。在 Node 版本切换时，我必须经常重新编译。
- Less 中，变量名称以`@`作为前缀，容易与 CSS 关键字混淆，如`@media`、`@import`和`@font-face`。

### 如何实现一个使用非标准字体的网页设计？

使用`@font-face`并为不同的`font-weight`定义`font-family`。

### 使用 rem 布局的优缺点

- 优点
在屏幕分辨率千差万别的时代，只要将rem与屏幕分辨率关联起来就可以实现页面的整体缩放，使得在设备上的展现都统一起来了。而且现在浏览器基本都已经支持rem了，兼容性也非常的好。
- 缺点
  - 在奇葩的dpr设备上表现效果不太好，比如一些华为的高端机型用rem布局会出现错乱。
  - 使用iframe引用也会出现问题。
  - rem在多屏幕尺寸适配上与当前两大平台的设计哲学不一致。即大屏的出现到底是为了看得又大又清楚，还是为了看的更多的问题。

参考：
《css3 的字体大小单位 rem 到底好在哪？》
《VW: 是时候放弃 REM 布局了》
《为什么设计稿是 750px》
《使用 Flexible 实现手淘 H5 页面的终端适配》

### transition 和 animation 的区别

- transition关注的是CSS property的变化，property值和时间的关系是一个三次贝塞尔曲线。
- animation作用于元素本身而不是样式属性，可以使用关键帧的概念，应该说可以实现更自由的动画效果。

参考：
《CSSanimation 与 CSStransition 有何区别？》
《CSS3Transition 和 Animation 区别及比较》
《CSS 动画简介》
《CSS 动画：animation、transition、transform、translate》

### 幽灵空白节点

“幽灵空白节点”是内联盒模型中非常重要的一个概念，具体指的是：在HTML5文档声明中，内联元素的所有解析和渲染表现就如同
每个行框盒子的前面有一个“空白节点”一样。这个“空白节点”永远透明，不占据任何宽度，看不见也无法通过脚本获取，就好像幽灵
一样，但又确确实实地存在，表现如同文本节点一样

### absolute 与 overflow的关系

- 如果overflow不是定位元素，同时绝对定位元素和overflow容器之间也没有定位元素，则overflow无法对absolute元素进行剪裁。
- 如果overflow的属性值不是hidden而是auto或者scroll，即使绝对定位元素高宽比overflow元素高宽还要大，也都不会出现滚动条。
- overflow元素自身transform的时候，Chrome和Opera浏览器下的overflow剪裁是无效的。

### clip 裁剪是什么

可访问性隐藏：指的是虽然内容肉眼看不见，但是其他辅助设备却能够进行识别和访问的隐藏。

### 隐藏元素的 background-image 到底加不加载

- background-image
  - 元素本身设置 `display:none`，会请求图片
  - 父级元素设置 `display:none`，不会请求图片
  - 样式没有元素使用，不会请求
  - `:hover` 样式下，触发时请求
- `img` 标签图片任何情况下都会请求图片

### 使用视网膜分辨率的图形？当中使用什么技术？

使用更高分辨率的图形（显示尺寸的两倍）来处理视网膜显示。更好的方法是使用媒体查询，像`@media only screen and (min-device-pixel-ratio: 2) { ... }`，然后改变`background-image`。

对于图标类的图形，我会尽可能使用 svg 和图标字体，因为它们在任何分辨率下，都能被渲染得十分清晰。

还有一种方法是，在检查了`window.devicePixelRatio`的值后，利用 JavaScript 将`<img>`的`src`属性修改，用更高分辨率的版本进行替换。

### 除了`screen`，你还能说出一个 @media 属性的例子吗？

- `all` 适用于所有设备。
- `print` 为了加载合适的文档到当前使用的可视窗口. 需要提前咨询 paged media（媒体屏幕尺寸）, 以满足个别设备网页尺寸不匹配等问题。
- `screen` 主要适用于彩色的电脑屏幕
- `speech` 解析 speech 这个合成器. 注意: CSS2 已经有一个相似的媒体类型叫 aural.

### 制作 SVG

使用内联 CSS、嵌入式 CSS 部分或外部 CSS 文件对形状进行着色（包括指定对象上的属性）。在网上大部分 SVG 使用的是内联 CSS，不过每个类型都有优点和缺点。

通过设置`fill`和`stroke`属性，可以完成基本着色操作。`fill`可以设置内部的颜色，`stroke`可以设置周围绘制的线条的颜色。你可以使用与`HTML`中使用的 CSS 颜色命名方案相同的 CSS 颜色命名方案：颜色名称（即`red`）、RGB 值（即`rgb(255,0,0)`）、十六进制值、RGBA 值等等。

```html
<rect
  x="10"
  y="10"
  width="100"
  height="100"
  stroke="blue"
  fill="purple"
  fill-opacity="0.5"
  stroke-opacity="0.8"
/>
```

- <https://developer.mozilla.org/en-US/docs/Web/SVG/Tutorial/Fills_and_Strokes>

### 请描述`<script>`、`<script async>`和`<script defer>`的区别

- `<script>` - HTML 解析中断，脚本被提取并立即执行。执行结束后，HTML 解析继续。
- `<script async>` - 脚本的提取、执行的过程与 HTML 解析过程并行，脚本执行完毕可能在 HTML 解析完毕之前。当脚本与页面上其他脚本独立时，可以使用`async`，比如用作页面统计分析。
- `<script defer>` - 脚本仅提取过程与 HTML 解析过程并行，脚本的执行将在 HTML 解析完毕后进行。如果有多个含`defer`的脚本，脚本的执行顺序将按照在 document 中出现的位置，从上到下顺序执行。

注意：没有`src`属性的脚本，`async`和`defer`属性会被忽略。

- [async-vs-defer](https://bitsofco.de/async-vs-defer/)

### 请描述`cookie`、`sessionStorage`和`localStorage`的区别

上面提到的技术名词，都是在客户端以键值对存储的存储机制，并且只能将值存储为字符串。

|  | `cookie` | `localStorage` | `sessionStorage` |
| --- | --- | --- | --- |
| 由谁初始化 | 客户端或服务器，服务器可以使用`Set-Cookie`请求头。 | 客户端 | 客户端 |
| 过期时间 | 手动设置 | 永不过期 | 当前页面关闭时 |
| 在当前浏览器会话（browser sessions）中是否保持不变 | 取决于是否设置了过期时间 | 是 | 否 |
| 是否随着每个 HTTP 请求发送给服务器 | 是，Cookies 会通过`Cookie`请求头，自动发送给服务器 | 否 | 否 |
| 容量（每个域名） | 4kb | 5MB | 5MB |
| 访问权限 | 任意窗口 | 任意窗口 | 当前页面窗口 |

### 如何提供包含多种语言内容的页面？

这个问题有点问得含糊其辞，我认为这是在询问最常见的情况：如何提供包含多种语言内容的页面，并保证页面内容语言的一致性。

当客户端向服务器发送 HTTP 请求时，通常会发送有关语言首选项的信息，比如使用`Accept-Language`请求头。如果替换语言存在，服务器可以利用该信息返回与之相匹配的 HTML 文档。返回的 HTML 文档还应在`<html>`标签中声明`lang`属性，比如`<html lang="en">...</html>`

在后台中，HTML 将包含`i18n`占位符和待以替换的内容，这些按照不同语言，以 YML 或 JSON 格式存储。然后，服务器将动态生成指定语言内容的 HTML 页面。整个过程通常需要借助后台框架实现。

- [language](https://www.w3.org/International/getting-started/language)

### 在设计开发多语言网站时，需要留心哪些事情？

- 在 HTML 中使用`lang`属性。
- 引导用户切换到自己的母语——让用户能够轻松地切换到自己的国家或语言，而不用麻烦。
- 在图片中展示文本会阻碍网站规模增长——把文本放在图片中展示，仍然是一种非常流行的方式。这样做可以在所有终端上，都能显示出美观的非系统字体。然而，为了翻译图片中的文本，需要为每种语言单独创建对应的图片，这种做法很容易在图片数量不断增长的过程中失控。
- 限制词语或句子的长度——网页内容在使用其他语言表述时，文字长度会发生变化。设计时，需要警惕文字长度溢出布局的问题，最好不要使用受文字长度影响较大的设计。比如标题、标签、按钮的设计，往往很受文字长度影响，这些设计中的文字与正文或评论部分不同，一般不可以自由换行。
- 注意颜色的使用——颜色在不同的语言和文化中，意义和感受是不同的。设计时应该使用恰当的颜色。
- 日期和货币的格式化——日期在不同的国家和地区，会以不同的方式显示。比如美国的日期格式是“May 31, 2012”，而在欧洲部分地区，日期格式是“31 May 2012”。
- 不要使用连接的翻译字符串——不要做类似这样的事情，比如`“今天的日期是”+具体日期`。这样做可能会打乱其他语言的语序。替代方案是，为每种语言编写带变量替换的模版字符串。请看下面两个分别用英语和中文表示的句子：`I will travel on date`和`date 我会出发`。可以看到，语言的语法规则不同，变量的位置是不同的。
- 注意语言阅读的方向——在英语中，文字是从左向右阅读的；而在传统日语中，文字是从右向左阅读的。

- [What-kind-of-things-one-should-be-wary-of-when-designing-or-developing-for-multilingual-sites](https://www.quora.com/What-kind-of-things-one-should-be-wary-of-when-designing-or-developing-for-multilingual-sites)

## 参考文章

- <https://css-tricks.com/all-about-floats/>
- <https://developer.mozilla.org/zh-CN/docs/Web/CSS/@media>
- <https://neal.codes/blog/front-end-interview-css-questions>
- <https://quizlet.com/28293152/front-end-interview-questions-css-flash-cards/>
- <http://peterdoes.it/2015/12/03/a-personal-exercise-front-end-job-interview-questions-and-my-answers-all/>
- <https://github.com/h5bp/Front-end-Developer-Interview-Questions/blob/master/src/questions/css-questions.md>
- <https://www.sitepoint.com/css-techniques-for-retina-displays/>
- <http://mediumwell.com/responsive-adaptive-mobile/>
- <https://css-tricks.com/the-difference-between-responsive-and-adaptive-design/>
- <https://philipwalton.github.io/solved-by-flexbox/>
- <https://developers.google.com/web/fundamentals/performance/rendering/>
- <https://csstriggers.com/>
