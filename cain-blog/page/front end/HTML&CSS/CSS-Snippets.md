---
title: CSS Snippets
date: 2022-05-19 13:15:29
updated: 2023-01-13
tags:
- CSS
- Snippets
categories:
- CSS
---

## prettierrc配置

```json
{
  "printWidth": 500,
  "tabWidth": 2,
  "useTabs": false,
  "semi": false,
  "singleQuote": false,
  "TrailingCooma": "all",
  "bracketSpacing": true,
  "arrowParens": "avoid",
  "bracketSameLine": true,
  "jsxBracketSameLine": true,
  "trailingComma": "es5",
  "quoteProps": "as-needed",
  "jsxSingleQuote": false,
  "requirePragma": false,
  "insertPragma": false,
  "proseWrap": "preserve",
  "htmlWhitespaceSensitivity": "ignore",
  "vueIndentScriptAndStyle": true,
  "endOfLine": "cr"
}
```

## IE6-7 图片缩放锯齿问题

```CSS
  img {
    -ms-interpolation-mode: bicubic;
  }
```

## 修改占位符`palceholder`样式

```CSS
input::-webkit-input-placeholder {
  color: #babbc1;
  font-size: 12px;
}
```

## 选择器相关

```CSS
 /*   除了最后一个元素之外的所有元素都需要一些样式，如：列表最后一个元素不需要下划线  */
li:not(:last-child) {
  border-bottom: 1px solid #ebedf0;
}
/*   选择前几个元素，如：【负方向范围】选择第1个到第6个  */
:nth-child(-n+6){}

/*  从第几个开始选择，如：【正方向范围】选择从第6个开始的 */
:nth-child(n+6){}

/*   两者结合使用，可以限制选择某一个范围，如：【限制范围】选择第6个到第9个，取两者的交集 */
:nth-child(-n+9):nth-child(n+6){}
```

## 使用 `caret-color` 修改光标颜色

```CSS
/* 关键样式 */
caret-color: #ffd476;

 .caret-color::-webkit-input-placeholder {
  color: #4f4c5f;
  font-size: 14px;
}
```

## flex相关

```CSS
/*  最后一个div加上margin-left；auto 实现flex布局最后一个元素 最靠右 */

/*  `flex` 布局将元素智能地固定在底部 */

.container {
  /* 关键样式 */
  display: flex;
  flex-direction: column;
  justify-content: space-between;
}

.main {
  /* 关键样式 */
  flex: 1;
}
.footer {
  text-align: center;
}
```

## 去掉 `type="number"` 末尾的箭头

```CSS
/* 关键样式 */
input::-webkit-outer-spin-button,
input::-webkit-inner-spin-button {
  -webkit-appearance: none;
}
```

## 使用 `outline:none` 删除输入状态行

## 解决iOS滚动条卡住的问题

```CSS
body,html{
  -webkit-overflow-scrolling: touch;
}


```

## 修复ios设备上滑动时候闪屏问题

```CSS
@mixin ios-fix {
  -webkit-backface-visibility: hidden;
  -webkit-transform-style: preserve-3d;
}
```

## 自定义选定的文本样式

```CSS
::selection {
  color: #ffffff;
  background-color: #ff4c9f;
}
```

## 使用 `filter:grayscale(1)` 使页面处于灰色模式

```CSS
html{
    -webkit-filter:grayscale(100%);
    -moz-filter:grayscale(100%);
    -ms-filter:grayscale(100%);
    -o-filter:grayscale(100%);
    filter:grayscale(100%);
    filter:url("data:image/svg+xml;utf8,<svg xmlns=\'http://www.w3.org/2000/svg\'><filter id=\'grayscale\'><feColorMatrix type=\'matrix\' values=\'0.3333 0.3333 0.3333 0 0 0.3333 0.3333 0.3333 0 0 0.3333 0.3333 0.3333 0 0 0 0 0 1 0\'/></filter></svg>#grayscale");
    filter:progid:DXImageTransform.Microsoft.BasicImage(grayscale=1)
}
```

## 文字多行显示

```CSS
.ellipsis {
    overflow: hidden;            // 溢出隐藏
    text-overflow: ellipsis;     // 溢出用省略号显示
    display:-webkit-box;         // 作为弹性伸缩盒子模型显示。
    -webkit-box-orient:vertical; // 设置伸缩盒子的子元素排列方式：从上到下垂直排列
    -webkit-line-clamp:3;        // 显示的行数
}
```

## 文字单行溢出

```CSS
p {
  overflow: hideen;
  text-overflow: ellipsis;
  white-space: nowrap;
}
p {
  position: relative;
  overflow: hidden;
  height: 3em;
  line-height: 1.5rem;
}
p::after {
  content: '...';
  position: absolute;
  bottom: 0;
  right: 0;
  background-color: #fff;
}

/* 溢出处理：`overflow；`
断词规则：`word-break`在什么时候截断
normal：cjk字符 文字位置截断  非cjk字符 字母位置截断
break-all：截断所有，所有字符在文字处截断；
keep-all：保持所有，所有文字在单词之间截断； */
```

## 清除浮动clear

1. 给父元素设置高度
1. 使用overflow：hidden
1. 使用额外标签法：在子元素末尾添加一个标签，给标签添加一个样式，样式定义clear:both闭合浮动
1. 伪元素清除浮动

```css
.clearfix:after{
  content: '';
  display: block;
  clear: both;
  visibility: hidden;
}
```

```CSS
.clearfix::after{
  content : "";
  display: block;
  clear:both
}
```

## CSS变量

```CSS
/* CSS变量又称CSS自定义属性，通过在css中自定义属性--var与函数var()组成，var()用于引用自定义属性。谈到为何会在CSS中使用变量，以下使用一个示例讲述。*/

:root {
 --c-color: orange;
}
.title {
 background-color: var(--c-color);
}
```

## 滚动条

```CSS
::-webkit-scrollbar 滚动条整体部分
::-webkit-scrollbar-thumb  滚动条里面的小方块，能向上向下移动（或往左往右移动，取决于是垂直滚动条还是水平滚动条）
::-webkit-scrollbar-track  滚动条的轨道（里面装有Thumb）
::-webkit-scrollbar-button 滚动条的轨道的两端按钮，允许通过点击微调小方块的位置。
::-webkit-scrollbar-track-piece 内层轨道，滚动条中间部分（除去）
::-webkit-scrollbar-corner 边角，即两个滚动条的交汇处
::-webkit-resizer 两个滚动条的交汇处上用于通过拖动调整元素大小的小控件

```

## 渐变

```CSS
/*  渐变分为线性渐变、径向渐变，这里笔者直接记录的使用方式，也是为了使用的时候更加的直观，这里说下，在使用线性渐变的时候，使用角度以及百分比去控制渐变，会更加的灵活  */

/*  渐变(方向) */
background: linear-gradient(to right, rgba(255, 255, 255, 0),#3FB6F7,rgba(255,255,255,0));

/*  渐变(角度) */
background: linear-gradient(88deg, #4DF7BF 0%, rgba(77, 247, 191, 0.26) 12%, rgba(77, 247, 191, 0) 100%);
```

## 渐变文字

```css
span{
    background: linear-gradient(to right, red, blue);
    background-clip: text;
    -webkit-background-clip: text;
    color: transparent;  
}


 h1{
        position: relative;
        color: yellow;
    }
    h1:before{
        content: attr(text);
        position: absolute;
        z-index: 10;
        color:pink;
        -webkit-mask:linear-gradient(to left, red, transparent );
    }
```

## 画一条 0.5px 的线

- `meta viewport`的方式
- `border-image`的方式
- `transform:scale()`的方式

## 居中div

- 水平居中：给 div 设置一个宽度，然后添加 margin:0 auto 属性

```css
div {
  width: 200px;
  margin: 0 auto;
}
```

- 水平居中，利用 text-align:center 实现

```css
.container {
  background: rgba(0, 0, 0, 0.5);
  text-align: center;
  font-size: 0;
}

.box {
  display: inline-block;
  width: 500px;
  height: 400px;
  background-color: pink;
}
```

- 让绝对定位的 div 居中

```css
div {
  position: absolute;
  width: 300px;
  height: 300px;
  margin: auto;
  top: 0;
  left: 0;
  bottom: 0;
  right: 0;
  background-color: pink; /*方便看效果*/
}
```

- 水平垂直居中一

```CSS
/*确定容器的宽高宽500高300的层设置层的外边距div{*/
position: absolute;/*绝对定位*/
width: 500px;
height: 300px;
top: 50%;
left: 50%;
margin: -150px -250px;/*外边距为自身宽高的一半*/
background-color: pink;/*方便看效果*/
}
```

- 水平垂直居中二

```CSS
/*未知容器的宽高，利用`transform`属性*/
div {
  position: absolute; /*相对定位或绝对定位均可*/
  width: 500px;
  height: 300px;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  background-color: pink; /*方便看效果*/
}
```

- 水平垂直居中三

```css
/*利用flex布局实际使用时应考虑兼容性*/
.container {
  display: flex;
  align-items: center; /*垂直居中*/
  justify-content: center; /*水平居中*/
}
.containerdiv {
  width: 100px;
  height: 100px;
  background-color: pink; /*方便看效果*/
}
```

- 水平垂直居中四

```CSS
/*利用text-align:center和vertical-align:middle属性*/
.container {
  position: fixed;
  top: 0;
  right: 0;
  bottom: 0;
  left: 0;
  background: rgba(0, 0, 0, 0.5);
  text-align: center;
  font-size: 0;
  white-space: nowrap;
  overflow: auto;
}

.container::after {
  content: '';
  display: inline-block;
  height: 100%;
  vertical-align: middle;
}

.box {
  display: inline-block;
  width: 500px;
  height: 400px;
  background-color: pink;
  white-space: normal;
  vertical-align: middle;
}
```

## 两栏布局

- 浮动

  ```css
  .outer {
    height: 100px;
  }

  .left {
    width: 200px;
    height: 100px;
    float: left;
    background: tomato;
  }

  .right {
    width: auto;
    height: 100px;
    margin-left: 200px;
    background: gold;
  }
  ```

- flex

  ```css
  .outer {
    display: flex;
    height: 100px;
  }

  .left {
    flex-shrink: 0;
    flex-grow: 0;
    flex-basis: 200px;
    background: tomato;
  }

  .right {
    flex: auto;
    /*11auto*/
    background: gold;
  }
  ```

- 绝对定位

```css
.outer {
  position: relative;
  height: 100px;
}

.left {
  position: absolute;
  width: 200px;
  height: 100px;
  background: tomato;
}

.right {
  height: 100px;
  margin-left: 200px;
  background: gold;
}
```

```css
.outer {
  position: relative;
  height: 100px;
}

.left {
  width: 200px;
  height: 100px;
  background: tomato;
}

.right {
  position: absolute;
  top: 0;
  right: 0;
  bottom: 0;
  left: 200px;
  background: gold;
}
```

## 三栏布局

- 绝对定位

  ```css
  .outer {
    position: relative;
    height: 100px;
  }

  .left {
    position: absolute;

    width: 100px;
    height: 100px;
    background: tomato;
  }

  .right {
    position: absolute;
    top: 0;
    right: 0;

    width: 200px;
    height: 100px;
    background: gold;
  }

  .center {
    margin-left: 100px;
    margin-right: 200px;
    height: 100px;
    background: lightgreen;
  }
  ```

- flex布局

  ```css
  .outer {
    display: flex;
    height: 100px;
  }

  .left {
    flex: 00100px;
    background: tomato;
  }

  .right {
    flex: 00200px;
    background: gold;
  }

  .center {
    flex: auto;
    background: lightgreen;
  }
  ```

- 浮动

  ```css
  .outer {
    height: 100px;
  }

  .left {
    float: left;
    width: 100px;
    height: 100px;
    background: tomato;
  }

  .right {
    float: right;
    width: 200px;
    height: 100px;
    background: gold;
  }

  .center {
    height: 100px;
    margin-left: 100px;
    margin-right: 200px;
    background: lightgreen;
  }
  ```

- 圣杯布局

  ```css
  .outer {
    height: 100px;
    padding-left: 100px;
    padding-right: 200px;
  }

  .left {
    position: relative;
    left: -100px;
    float: left;
    margin-left: -100%;
    width: 100px;
    height: 100px;
    background: tomato;
  }

  .right {
    position: relative;
    left: 200px;
    float: right;
    margin-left: -200px;
    width: 200px;
    height: 100px;
    background: gold;
  }

  .center {
    float: left;
    width: 100%;
    height: 100px;
    background: lightgreen;
  }
  ```

- 双飞翼布局

  ```css
  .outer {
    height: 100px;
  }

  .left {
    width: 100px;
    height: 100px;
    float: left;
    margin-left: -100%;
    background: tomato;
  }

  .right {
    width: 200px;
    height: 100px;
    float: left;
    margin-left: -200px;
    background: gold;
  }

  .wrapper {
    float: left;
    width: 100%;
    height: 100px;
    background: lightgreen;
  }

  .center {
    margin-left: 100px;
    margin-right: 200px;
    height: 100px;
  }
  ```

## 宽高自适应的正方形

- vw

  ```css
  .square {
    width: 10%;
    height: 10vw;
    background: tomato;
  }
  ```

- 元素的margin/padding百分比是相对父元素width的性质来实现

  ```css
  .square {
    width: 20%;
    height: 0;
    padding-top: 20%;
    background: orange;
  }
  ```

- 利用子元素的margin-top的值来实现的

  ```css
  .square {
    width: 30%;
    overflow: hidden;
    background: yellow;
  }

  .square::after {
    content: '';
    display: block;
    margin-top: 100%;
  }
  ```

## 自适应矩形水平垂直居中，且宽高比为 2:1

```css
/*实现原理参考自适应正方形和水平居中方式*/
.box {
  position: absolute;
  top: 0;
  right: 0;
  left: 0;
  bottom: 0;
  margin: auto;

  width: 10%;
  height: 0;
  padding-top: 20%;
  background: tomato;
}
```

## 修改Element Table样式

- 修改表格头部背景

  ```css
  ::v-deep .el-table th{
      background: orange;
  }
  ```

- 修改表格行背景

  ```css
  ::v-deep .el-table tr{
    background: #eee;
  }
  ```

- 修改斑马线表格的背景

  ```css
  ::v-deep .el-table--striped .el-table__body tr.el-table__row--striped td {
      background: #ccc;
  }
  ```

- 修改行内线的颜色

  ```css
  ::v-deep .el-table td,.building-top .el-table th.is-leaf {
      border-bottom:  2px solid #eee;
    }
  ```

- 修改表格最底部边框颜色和高度

  ```css
  ::v-deep .el-table::before{
    border-bottom:  1px solid #ccc;
      height: 3px
    }
  ```

- 修改表头字体颜色

  ```css
  ::v-deep .el-table thead {
      color: #ccc;
      font-weight: 700;
  }
  ```

- 修改表格内容字体颜色和字体大小

  ```css
  ::v-deep .el-table{
      color: #6B91CE;
      font-size: 14px;
    }
  ```

- 修改表格无数据的时候背景，字体颜色

  ```css
  ::v-deep .el-table__empty-block{
      background: #ccc;
    }
  ::v-deep .el-table__empty-text{
    color: #fff
  }
  ```

- 修改表格鼠标悬浮hover背景色

  ```css
  ::v-deep .el-table--enable-row-hover .el-table__body tr:hover>td {
      background-color: pink;
  }
  ```
