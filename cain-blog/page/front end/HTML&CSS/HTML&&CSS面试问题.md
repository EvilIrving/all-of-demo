---
title: HTML&&CSS面试问题
date: 2021-07-22
updated: 2021-07-22
tags:
- HTML
- CSS
- 教程
categories:
- HTML

---

## 题目：无答案

- CSS 盒模型：标准盒模型（Content-box）、怪异盒模型（Border-box）
- HTML 5、CSS3、ES6新增内容
- DocType 的作用
- Drag API
- 常见的浏览器内核
- 渐进增强和优雅降级
- HTML5 的离线储存和工作原理
- attribute 和 property 的区别
- 伪类 LVHA 的解释
- 块级格式化上下文（BFC）、外边距折叠解决方式
- rem和其他单位之间的区别
- 清除浮动方式及其特点
- 元素水平和垂直居中
- position 和 display 的取值和各自的意思和用法
- 简述选择器种类及优先级、伪类和伪元素，抽离样式模块怎么写
- chrome显示小于12px的文字
- 比较 opacity: 0、visibility: hidden、 display: none 优劣和适用场景
- 移动端视口配置
- 页面导入样式时，使用 link 和 @import 有什么区别？
- 常用块元素和行内元素
- 简述Flex布局及其属性
- media媒体查询
- 回流与重绘

## 题目：含答案

### 为什么 height:100% 会无效

对于普通文档流中的元素，百分比高度值要想起作用，其父级必须有一个可以生效的高度值。使用绝对定位的元素会有计算值，即使祖先元素的height计算为auto也是如此。

原因：如果包含块的高度没有显式指定（即高度由内容决定），并且该元素不是绝对定位，则计算值为auto，因为解释成了auto，
所以无法参与计算。

### `min-width/max-width 和 min-height/max-height` 属性间的覆盖规则？

- max-width会覆盖width，即使width是行类样式或者设置了!important。
- min-width会覆盖max-width，此规则发生在min-width和max-width冲突的时候。

### 居中

水平居中对齐一个元素如`div`，可以使用`margin：auto`（设置宽度后外边距均分）；

文本：`text-align：center`

图片居中：`margin: auto`，并将图片放在块元素（`display：block`）中；

右对齐：`position：absolute；right：0px`(左对齐同理)，可以交叠元素；或者使用`float`左右对齐，如果子元素溢出，使用overflow解决。

垂直居中对齐：`margin-top`使用`padding`设置为块宽度。垂直居中还可以使用`line-height`设置为块高度；`padding`方式联合文本居中可以水平垂直均居中


### CSS 多列等高如何实现

- 利用`table-cell`所有单元格高度都相等
- flex布局中项目`align-items`属性不设置高度或设为`auto`

### 浏览器的兼容性有哪些

- png24位的图片在iE6浏览器上出现背景
  >解决方案：做成PNG8，也可以引用一段脚本处理。

- 浏览器默认的margin和padding不同
  >解决方案：加一个全局的*{margin:0;padding:0;}来统一。

- IE6双边距bug：在IE6下，如果对元素设置了浮动，同时又设置了margin-left或
margin-right，margin值会加倍。
  > `#box{float:left;width:10px;margin:0 0 0 10px;}` 这种情况之下IE会产生20px的距离
  > 解决方案：在float的标签样式控制中加入`_display:inline`;将其转化为行内属性。(_这个符号只有ie6会识别)

- 渐进识别的方式，从总体中逐渐排除局部。
  >首先，巧妙的使用"\9"这一标记，将IE游览器从所有情况中分离出来。
  >接着，再次使用"+"将IE8和IE7、IE6分离开来，这样IE8已经独立识别。

  ```css
  .bb{
  background-color:#f1ee18;/*所有识别*/
  .background-color:#00deff\9;/*IE6、7、8识别*/
  +background-color:#a200ff;/*IE6、7识别*/
  _background-color:#1e0bd1;/*IE6识别*/
  }
  ```

- IE下，可以使用获取常规属性的方法来获取自定义属性，也可以使用getAttribute()获取自定义属性；Firefox下，只能使用getAttribute()获取自定义属性
  > 解决方法：统一通过getAttribute()获取自定义属性。

- IE下，event对象有x、y属性，但是没有pageX、pageY属性;Firefox下，event对象有
pageX、pageY属性，但是没有x、y属性。
  >解决方法：（条件注释）缺点是在IE浏览器下可能会增加额外的HTTP请求数。

- Chrome中文界面下默认会将小于12px的文本强制按照12px显示
  > 解决方法：
  >
  >  1. 可通过加入CSS属性`-webkit-text-size-adjust:none;`解决。但是，在chrome更新到27版本之后就不可用了。
   > 2. 还可以使用`-webkit-transform:scale(0.5);`其收缩的是整个span的大小，这时候，必须要将span转换成块元素，可以使用`display：block/inline-block`；

### li 与 li 之间有看不见的空白间隔

浏览器会把inline元素间的空白字符（空格、换行、Tab等）渲染成一个空格。而为了美观。我们通常是一个``<li>``放在一行，这导致``<li>``换行后产生换行字符，它变成一个空格，占用了一个字符的宽度。

- 为`<li>`设置`float:left`。不足：有些容器是不能设置浮动，如左右切换的焦点图等。-
- 将所有`<li>`写在同一行。不足：代码不美观。
- 将`<ul>`内的字符尺寸直接设为0，即`font-size:0`。不足：`<ul>`中的其他字符尺寸也被设为0，需要额外重新设定其他字符尺寸，且在Safari浏览器依然会出现空白间隔。
- 消除`<ul>`的字符间隔`letter-spacing:-8px`，不足：这也设置了`<li>`内的字符间隔，因此需要将`<li>`内的字符间隔设为默认`letter-spacing:normal`。

### margin 重叠问题

主要是掌握盒模型的概念和实际操作中盒模型如何变化，`margin`是外边框，在盒子外面。`padding`是内边框，`border`是`padding`外面的一个边，但是处于盒子里面。

必备条件：margin必须是邻接的

W3C规范条件：

- 必须是处于常规文档流（非float和绝对定位）的块级盒子，并且处于同一个BFC当中。
- 没有线盒，没有空隙，没有padding和border将他们分隔开
- 都属于垂直方向上相邻的外边距，可以是下面任意一种情况
- 元素的margin-top与其第一个常规文档流的子元素的margin-top
- 元素的margin-bottom与其下一个常规文档流的兄弟元素的margin-top
- height为auto的元素的margin-bottom与其最后一个常规文档流的子元素的margin-bottom
- 高度为0并且最小高度也为0，不包含常规文档流的子元素，并且自身没有建立新的BFC的元素的margin-top
和margin-bottom

合并的3种场景

- 相邻兄弟元素margin合并
  - 设置块状格式化上下文元素（BFC）
- 父级和第一个/最后一个子元素的margin合并
  - 对于margin-top合并，可以进行如下操作
    - 父元素设置为块状格式化上下文元素；
    - 父元素设置border-top值；
    - 父元素设置padding-top值；
    - 父元素和第一个子元素之间添加内联元素进行分隔。
  - 对于margin-bottom合并，可以进行如下操作
    - 父元素设置为块状格式化上下文元素；
    - 父元素设置border-bottom值；
    - 父元素设置padding-bottom值；
    - 父元素和最后一个子元素之间添加内联元素进行分隔；
    - 父元素设置height、min-height或max-height。
- 空块级元素的margin合并
  - 设置垂直方向的border；
  - 设置垂直方向的padding；
  - 里面添加内联元素，直接Space键空格是没用的；
  - 设置height或者min-height。

- 上下标签间隔最大值

- 左右标签间隔取相加

### 隐藏或显示标签

- `opacity: 0;`
- `visibility: hidden;`
- `display:none;`
- 绝对定位将元素移除可视区域内
- z-index 负值
- clip/clip-path 元素裁剪
- transform:scale(0,0) 来将元素缩放为 0
  - 后两种占据位置，不响应绑定的监听事件

### 有什么不同的方式可以隐藏内容（使其仅适用于屏幕阅读器）？

这些方法与可访问性（a11y）有关。

- `width: 0; height: 0`：使元素不占用屏幕上的任何空间，导致不显示它。
- `position: absolute; left: -99999px`： 将它置于屏幕之外。
- `text-indent: -9999px`：这只适用于`block`元素中的文本。

- <https://www.w3.org/TR/wai-aria-1.1/>
- <https://developer.mozilla.org/en-US/docs/Web/Accessibility/ARIA>
- <http://a11yproject.com/>

### 清除浮动

浮动元素可以左右移动，直到遇到另一个浮动元素或者遇到它外边缘的包含框。浮动框不属于文档流中的普通流，当元素浮动之后，不会影响块级元素的布局，只会影响内联元素布局。此时文档流中的普通流就会表现得该浮动框不存在一样的布局模式。当包含框的高度小于浮动框的时候，此时就会出现“高度塌陷”。

清除浮动是为了清除使用浮动元素产生的影响。浮动的元素，高度会塌陷，而高度的塌陷使我们页面后面的布局不能正常显示。

- 使用clear属性清除浮动
  - clear属性只有块级元素才有效的，而::after等伪元素默认都是内联水平，这就是借助伪元素清除浮动影响时需要设置display属性值的原因。

  ```css
  <!-- 也可以是'block'，或者是'list-item' -->
  .clear::after{
      content:'';
      display:table;
      clear:both;
   }
  ```

- 使用BFC块级格式化上下文来清除浮动

### 全屏滚动的原理是什么

`overflow：hidden；transition：all 1000 ms ease；`

### 视差滚动效果，如何给每页做不同的动画？（回到顶部，向下滑动要再次出现，和只出现一次分别怎么做？）

视差滚动是指多层背景以不同的速度移动，形成立体的运动效果，带来非常出色的视觉体验。

question：如何实现视差滚动效果的网页

### 修改 chrome 记住密码后自动填充表单的黄色背景

### 让页面里的字体变清晰，变细用 CSS 怎么做

- webkit内核的私有属性：`-webkit-font-smoothing`，用于字体抗锯齿
- MacOS：`-webkit-font-smoothing:antialiased;`

### italic 和 oblique 的区别

italic是使用当前字体的斜体字体，而oblique只是单纯地让文字倾斜

### `position:fixed;`在`android`下无效怎么处理

因为移动端浏览器默认的viewport叫做layout viewport。在移动端显示时，因为layout viewport的宽度大于移动端屏幕的宽度，所以页面会出现滚动条左右移动，fixed的元素是相对layout viewport来固定位置的，而不是移动端屏幕来固定位置的，所以会出现感觉fixed无效的情况。

如果想实现fixed相对于屏幕的固定效果，我们需要改变的是viewport的大小为ideal viewport，可以如下设置：

```txt
<metawidth=device-width,initial-scale=1.0,maximum-scale=1.0,minimum-sca
le=1.0,user-sca/>
```

### 去除 inline-block 元素间间距

移除空格、使用margin负值、使用font-size:0、letter-spacing、word-spacing

### overflow:scroll 时不能平滑滚动

启用硬件加速特性：`-webkit-overflow-scrolling:touch;`

### img图片下面有间隙的5种解决方案

1、将图片转换为块级对像

即设置img为：`display：block；`

在本例中添加一组CSS代码：`sub img {display:block;}`

2、设置图片的垂直对齐方

即设置图片的`vertical-align`属性为`「top，text-top，bottom，text-bottom」`也可以解决。如本例中增加一组CSS代码： `sub img {vertical-align:top;}`

3、设置父对象的文字大小为`0px`

即，在`#sub`中添加一行： `font-size:0;`

可以解决问题。但这也引发了新的问题，在父对像中的文字都无法显示。就算文字部分被子对像括起来，设置子对像文字大小依然可以显示，但在CSS效验的时候会提示文字过小的错误。（不建议使用）

4、改变父对象的属性

如果父对象的宽、高固定，图片大小随父对像而定，那麽可以设置： overflow:hidden; 来解决。

如本例中可以向#sub中添加以下代码：`width:88px;height:31px;overflow:hidden;`

5、设置图片的浮动属性

即在本例中增加一行CSS代码： `#sub img {float:left;}`

如果要实现图文混排，这种方法是很好的选择。


### 弹性布局

#### 布局属性及设置

不依赖于盒模型，Flex可以完成响应式的布局。

容器 flex container：设置display：（flex或inline-flex）的元素；（假如你想设置行内元素为 flexible box，也可以置 `display` 属性的值为 `inline-flex。`）

子元素 flex item

主轴main axis 和 侧轴 cross axis：不真实存在，仅用作描述方便

main axis默认水平（row），左开始右结束，cross axis默认垂直，上开始下结束；

flex-direction: column;可以更改默认方向；flex-wrap当子元素溢出时换行；存在着 `flex-direction` 和 `flex-wrap` — 的缩写 `flex-flow，按顺序。`

使用 row-reverse 和 column-reverse 值反向排列 flex 项目。

#### 容器样式和子元素样式

`align-items` 控制 flex 项在交叉轴上的位置。

- 默认的值是 `stretch`，其会使所有 flex 项沿着交叉轴的方向拉伸以填充父容器。如果父容器在交叉轴方向上没有固定宽度（即高度），则所有 flex 项将变得与最长的 flex 项一样长（即高度保持一致）。**就是说不设置子元素高度时才有用。**

- `baseline`基线对齐：侧轴起点到元素基线距离最大的元素将会于侧轴起点对齐以确定基线。

- `center` 在交叉轴居中

- `flex-start` 或 `flex-end` 使 flex 项在交叉轴的开始或结束处对齐所有的值。

- 可以用 `align-self` 属性覆盖 `align-items` 的行为。

`justify-content`控制 flex 项在主轴上的位置。

- 默认值是 `flex-start`，这会使所有 flex 项都位于主轴的开始处。

- 你也可以用 `flex-end` 来让 flex 项到结尾处。

- `center` 在 `justify-content` 里也是可用的，可以让 flex 项在主轴居中。

- 而我们上面用到的值 `space-around` 是很有用的——它会使所有 flex 项沿着主轴均匀地分布，在任意一端都会留有一点空间。

- 还有一个值是 `space-between`，它和 `space-around` 非常相似，只是它不会在两端留下任何空间。

flex-grow：默认（值为0）不分配多余空间，此值允许子元素分配多余空间

flex-shrink：子元素当空间不足时的收缩比例，默认是1；容器中子元素溢出时应该如何计算收缩值：原始宽度和收缩比例是影响因素

- step1：权重：（子元素的原始宽度*收缩比例）之和

- step2：溢出量：子元素原始宽度之和-权重

- step3：收缩比例：单个子元素的原始宽度*收缩比例/权重*溢出量

**flex-basis指定了 flex 元素在主轴方向上的初始大小。如果不使用**`**box-sizing**`**改变盒模型的话，那么这个属性就决定了 flex 元素的内容盒（content-box）的尺寸。**可以理解为子元素放进容器之前的大小，是我们假设或理想的尺寸大小，但是不一定是真实的大小，弹性子元素最终实际大小取决于容器；

**和width区别：**

content < width < flex-basis < ( limited max | min-width )

flex-basis只在弹性布局中的子元素有效；在绝对定位中对元素无效

**Note:** 当一个元素同时被设置了 flex-basis (除值为 auto 外) 和 width (**或者在 flex-direction: column 情况下设置了height 即根据主轴方向改变影响维度**) , `flex-basis` 具有更高的优先级.

**基线**是指欧洲和西亚文字排版中，用于在上面放置字符的一条假象的基准线。

缩写flex可以指定以上三个值`flex：flex-grow flex-shrink flex-basis；默认`

`flex：0 1 auto；特别的：`

`flex：auto == 1 1 auto`  

`flex：none == 0 0 auto`

`flex：1 == 1 1 0%`

[**常用布局cookbook**](https://developer.mozilla.org/zh-CN/docs/Web/CSS/Layout_cookbook)

### 表单类型有哪些

```html
<!-- 表单类型 -->
<form action="" method="get">
    <p>邮箱标签: <input type="email"></p>
    <p>数字标签: <input type="number"></p>
    <p>滑动条标签: <input type="range"></p>
    <p>搜索框标签: <input type="search"></p>
    <p>日期框: <input type="date"></p>
    <p>星期框: <input type="week"></p>
    <p>月份框: <input type="month"></p>
    <p>颜色框: <input type="color"></p>
    <p>网址框: <input type="url"></p>
    <div>
      <input type="submit">
      <input type="reset">
    </div>
</form>
<!--  都是 input 标签，但是渲染出来的是不同类型的页面元素。除此之外，HTML5 对 form 表单还进行了内置属性的增强。比如通用属性 placeholder、autofocus，再比如如果是 number 类型，则可以设置 min 和 max 属性，如果是 password 类型，则可以设置 minLength 和 maxLength 属性。 -->
```

链接点击后 hover 失效：a 标签的四种伪元素有着前后规则，其中 hover 需要放在 visited 后，所以将两者调换位置就可以。

### 设备像素、css像素、设备独立像素、dpr、ppi

- 设备像素指的是设备的物理像素，一般等于屏幕的分辨率，且不可变；
- css 像素是相对像素，大小取决于屏幕的缩放和 dpr 的大小。
- 设备独立像素与 css 像素是等价的。
- dpr 是像素比，一般设备的 dpr 是 1，苹果推出的 retina 屏幕的 dpr 为 2
- ppi 是每英寸的物理像素的密度，当前手机 ppi 最高可达到 1000 以上

### 样式优化有哪些

- 加载性能
  - css 压缩
  - 减少使用 @import，它导入的样式需要页面加载完毕后才加载，推荐使用 link
- 选择器性能
  - 不使用通配符选择器，消耗性能
  - 选好关键选择器，减少层级
  - ID 作为关键选择器，不需要在左边添加额外的层级
  - 用类选择器代替元素选择器
- 渲染性能
  - 减少使用 浮动、定位
  - 去除空规则
  - 使用雪碧图减少请求次数
  - 继承属性不要重复指定
  - 选择器嵌套不要超过三层
- 样式抽离
  - 将相同属性的样式抽离，整合到同一个 class 中，提高维护性
  - 样式与结构分离，采用外部引入

### 编写高效的 CSS 应该注意什么？

首先，浏览器从最右边的选择器，即关键选择器（key selector），向左依次匹配。根据关键选择器，浏览器从 DOM 中筛选出元素，然后向上遍历被选元素的父元素，判断是否匹配。选择器匹配语句链越短，浏览器的匹配速度越快。避免使用标签和通用选择器作为关键选择器，因为它们会匹配大量的元素，浏览器必须要进行大量的工作，去判断这些元素的父元素们是否匹配。

[BEM (Block Element Modifier)](https://bem.info/)原则上建议为独立的 CSS 类命名，并且在需要层级关系时，将关系也体现在命名中，这自然会使选择器高效且易于覆盖。

搞清楚哪些 CSS 属性会触发重新布局（reflow）、重绘（repaint）和合成（compositing）。在写样式时，避免触发重新布局的可能。

### 什么情况下，用`translate()`而不用绝对定位？什么时候，情况相反

`translate()`是`transform`的一个值。改变`transform`或`opacity`不会触发浏览器重新布局（reflow）或重绘（repaint），只会触发复合（compositions）。而改变绝对定位会触发重新布局，进而触发重绘和复合。`transform`使浏览器为元素创建一个 GPU 图层，但改变绝对定位会使用到 CPU。 因此`translate()`更高效，可以缩短平滑动画的绘制时间。

当使用`translate()`时，元素仍然占据其原始空间（有点像`position：relative`），这与改变绝对定位不同。

#### 参考

- <https://www.paulirish.com/2012/why-moving-elements-with-translate-is-better-than-posabs-topleft/>
