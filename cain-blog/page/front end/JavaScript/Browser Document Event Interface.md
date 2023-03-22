---
title: 浏览器 文档 事件 接口
date: 2020-03-11
updated: 2020-03-15
tags:
- JavaScript
- 基础
categories:
- JavaScript
---
## 事件

### 概念

**事件处理器（事件监听器）：**也就是事件触发时会运行的代码块

**注册了一个事件处理器：**定义了一个用来回应事件被激发的代码块的时候

### 事件类型

#### 鼠标事件

1. 左键单击 onclick
2. 左键双击 ondblclick
3. 鼠标移入移出 onmouseover onmouseout；onmouseleave onmouseenter 效率高
4. 鼠标移动 onmousemove
5. 鼠标按下 onmousedown 其button属性判断鼠标按键
6. 鼠标按键松开 onmouseup;与onclick的区别在其是鼠标所有按键都松开
7. 鼠标右键单击 oncontextmenu
8. 鼠标滚轮滚动  onmousewheel：wheeldelta > 0 滚轮向上 ；wheeldelta < 0 滚轮向下；火狐浏览器不支持onmousewheel，兼容火狐通过事件监听形式绑定DOMMouseScroll事件；

#### 鼠标位置的常用属性

1. screenX 、screenY ：只读，提供鼠标在全局（屏幕）中的水平坐标（偏移量）。
2. clientX 、clientY：只读，事件发生时的应用客户端区域的水平坐标
3. pageX 、 pageY ：只读，相对于整个文档的坐标以像素为单位的

### 键盘事件

1. onkeydown 其属性key keycode 与code

2. onkeyup

3. onkeypress

### 表单事件

1. onchange
2. onfocus
3. onblur
4. onsubmit

**事件对象：**自动传递给事件处理函数，以提供额外的功能和信息；事件对象 `e` 的`target`属性始终是事件刚刚发生的元素的引用。开发人员最常使用 e / evt / event；当您要在多个元素上设置相同的事件处理程序时，`e.target`非常有用，并且在发生事件时对所有元素执行某些操作。大多数事件处理器的事件对象都有可用的标准属性和函数（方法）

### 事件监听方式

1. ele.onxxx = function(){}; 兼容性好 但是一个元素的同一个事件上只能绑定一个处理程序
2. addEventListener("click", function(){},flase); IE9 以下不兼容，可以为一个事件绑定多个处理程序，**FALSE改为TRUE关闭事件冒泡，开启捕获**
3. attachEvent('on' + type,function(){}) IE专用

tips：如果for循环中用到事件，要考虑闭包 的问题：**函数需要立即执行，可以在外面加 （）；**

兼容性方法如下：

tips：this.onclick = null; 在onclick事件处理函数中添加此行代码，执行一次后不再生效；

### 解除事件监听

ele.onclick = false/''/null;

ele.removeEventListener(type,fn,false);

ele.detachEvent('on' + type,fn);

tips：若绑定匿名函数，则无法解除；fn通常声明一个函数，不能匿名，函数在外面实现；

### 事件冒泡及捕获

事件流模型表示的是一个事件触发的完整步骤，该步骤一共有三步：

1. 事件捕获：第一阶段，某个节点触发事件后，系统从根节点开始，一直向下寻找，直至真正触发该事件的节点，沿途在直系继承树上绑定了相同事件的节点会按照顺序依次触发；即同一事件，自父元素捕获至子元素（事件源元素）。自顶向下；
2. 事件触发：真正触发事件的节点，触发事件；
3. 事件冒泡：第三阶段，真正触发事件的节点触发完毕，会沿着直系继承树一直向上传递该事件，一直传递到根节点，沿途绑定了相同事件的节点会依次触发。即同一事件，自子元素冒泡向父元素。自底向上。

触发顺序：捕获 >> 触发 >> 冒泡

tips ：focus blur change submit reset select 等事件不冒泡

### 取消冒泡或捕获

event.stopPropagation(); 阻止捕获和冒泡阶段中当前事件的进一步传播，不支持IE9以下；

IE独有：event.cancelBubble = TRUE

封装兼容性函数：stopBubble(event);

### 阻止默认事件

默认事件：表单提交，a标签跳转，右键菜单等

return FALSE 以对象属性的方式注册的事件才生效

event.preventDefault(); IE9以下不兼容

event.returnValue = false; 兼容IE

封装函数：cancelHandler(event);

### 事件对象

event 存储事件触发后与该事件有关的详细数据；低版本火狐不支持；

event || window.event 用于IE

事件源对象：俩Chrome都有

event.target 火狐只有这个

event.srcElement IE 只有这个

兼容性写法：

### 事件委托

利用事件冒泡和事件源对象进行处理；这个概念依赖于这样一个事实,如果你想要在大量子元素中单击任何一个都可以运行一段代码，您可以将事件监听器设置在其父节点上，并让子节点上发生的事件冒泡到父节点上，而不是每个子节点单独设置事件监听器。

优点：

- 性能 不需要循环所有的元素一个个绑定事件

- 灵活 当有新的子元素时不需要重新绑定事件

tips：

- 按 Ctrl + Alt + F12 进入Intel管理界面：点击选项和支持，在以下界面点击禁用快捷键。应用退出。

- a 标签自带一个刷新页面或返回顶部的功能；

- 鼠标事件：click mousedown mousemove mouseup contextmenu mouseover mouseout mouseenter mouseleave ;

- button 用来区分mouse的

## Document

DOM 定义了表示和修改文档所需的方法 DOM对象即为宿主对象，用来操作HTML和XML的一类对象的集合，HTML文档加载时，浏览器根据代码创建DOM树，反映文档间所有内容的嵌套关系。document代表整个文档

API (web 或 XML 页面) = DOM + JS (脚本语言)

### 查看元素节点

- document.getElementById("ID"); 得到的是一个元素， IE8以下不区分大小写：定义在Document.prototype上，即Element节点上不能使用；

- document.getElementsByTagName("标签名"); 得到一个数组：定义在Document.prototype和Element.prototype上；

- document.getElementsByName("标签名"); 部分标签生效（表单及其元素 img iframe）；定义在HTMLDocument.prototype上，即document不能使用

- document.getElementsByClassName("class名"); 得到一个数组，IE8及以下无

- document.querySelector("选择器名"); 得到的是一个元素，IE7及以下无，不是实时的

- document.querySelectorAll("选择器名");得到一个数组，不是实时的

### 遍历节点树

1. parentNode --> 父节点  最顶端的parentNode是#document

1. childNodes --> 子节点们

1. firstChild --> 第一个

1. lastChild  --> 最后一个

1. **nextSibling --> 后一个兄弟**

1. **previousSibling    --> 前一个兄弟**

### 基于元素节点树的遍历（除children外，其它属性IE均不兼容）

1. parentElement

1. **children**

1. node.childElementCount === node.children.length  //当前元素节点的子元素节点个数

1. lastElementChild

1. nextElementSibling

1. previousElementSibling

### 节点类型

- 元素节点 ——1

- 属性节点 ——2

- 文本节点 ——3

- 注释节点 ——8

- document ——9

- DocumentFragment ——1

### 特殊节点的获取方式

1. 文档节点 document

1. HTML节点 document.documentElement

1. body节点 document.body

1. head节点 document.head

1. 文档声明节点 document.docType

### 获取节点类型（NodeType）：四个属性

- nodeName -- 元素的标签名 大写形式表示 只读

- nodeValue -- Text节点或Comment节点的文本内容，可读写

- **nodeType -- 该节点的类型**只读

- attributes -- Elements节点的属性集合

- 节点的一个方法 ：Node.hasChildNodes();

### DOM结构树

- document.getElementById：定义在Document.prototype上，即Element节点上不能使用；

- document.getElementsByTagName定义在Document.prototype和Element.prototype上；

- document.getElementsByName；定义在HTMLDocument.prototype上，即非HTML中的document不能使用（XML document ，Element）

- HTMLDocument.prototype定义了一些常用的属性，body、head分别指代HTML文档中的<body>,<head>标签；还可以获取文档节点（document）、html节点（document.documentElement）、文档声明节点（document.doctype）

- getElementsByClassName，querySelector，querySelectorAll在Document.prototype和Element.prototype类中均有定义

- Document.prototype上定义了documentElement属性，指代文档的根元素HTML文档，它总是指代<html>元素

### 练习题

```javascript
function retParent(elem, n) {
    while (elem && n) {
        elem = elem.parentElement;
        n--;
    }
    return elem;
}

Element.prototype.myChild = function () {
    var child = this.childNodes;
    var len = child.length;
    var arr = [];
    for (let i = 0; i < len; i++) {
        if (child[i].nodeType == 1) {
            arr.push(child[i]);
            // return true;
        }
    }
    return arr;
    // return false;  //这两个return TRUE 和 FALSE  是判断自定义hasChild方法
}
function retSibling(e, n) {
    while (e && n) {
        if (n > 0) {
            if (e = e.nextElementSibling) {
                e = e.nextElementSibling;
            } else {                
                for (e = e.nextSibling; e && e.NodeType != 1; e = e.nextSibling);
            }
            n--;
        } else {
            for (e = e.previousSibling; e && e.NodeType != 1; e = e.previousSibling);                
            }         
            n++;
        } 
    return e;
}
```

### 创建元素节点的步骤

- 创建一个空的元素节点

- 添加所需属性和样式

- 将其添加到当前文档应该的位置

### 增插删替

- document.createElement();

- document.createTextNode();

- document.createComment();

- document.createDocumentFragment();

- parentNode.appendChild();  // 剪切操作

- parentNode.insertBefore(a,b); //insert a, before b;

- parent.removeChild();

- child.remove();

- parent.replaceChild(a,b); // new a , replace  b;

### Element节点的一些属性和方法

1. **innerHTML  2.**innerText（火狐不兼容） 3. textContent （老版本IE不好使

innerHTML

- 在设置内容时, 如果内容有html标签字符串, 会被直接解析. 如果有空格, 只会识别一个, 忽略其余的空格
- 在获取内容时, 能够获取到节点里的所有内容, 包括文字, 标签, 空格, 注释

innerText

- 设置：如果内容存在标签，只会原样输出，如果有空格，解析为一个

- 获取：只能获取文本，不能获取多余空格

- 无法获取隐藏元素；visibility：hidden

textContent

- 设置：如果内容存在标签，只会原样输出，如果有空格，也会原样 输出

- 获取：只能获取文本，也能获取多余的空格

操作元素节点的属性

- 通过点语法直接操作元素的属性；点表示法只能接受字面量的成员的名字，不接受变量作为名字。

- set  get remove has方法

tips：点语法不适用于操作属性不确定时，或无法直接写出对应属性，需要间接获取的场景，此时只能使用set get方法；get方法获取的属性值是原样的。

- 三个属性之间的方法

- ele.setAttribute('class','classname');

- ele.getAttribute('class');

```text
Element.prototype.insertAfter = function (targetNode,afterNode) {
    var beforeNode = afterNode.nextElementSibling;
    if(beforeNode == null){
         this.appendChild(targetNode);
    }else{
        this.insertBefore(targetNode,beforeNode)
    }
}
```

### **查看滚动条的滚动距离**

window.pageXoffset/pageYoffset //IE 8及以下不兼容；

document.body/documentElement .scrollLeft/scrollTop //兼容性比较混乱 用时取两个值相加 因为不可能两个同时有值；以下是封装兼容性方法

```js
function getScrollOffset() {
    if (window.pageXOffset) {
        return{
            x : window.pageXOffset,
            y : window.pageYOffset
        }
    } else {
        return {
            x : document.body.scrollLeft + document.documentElement.scrollLeft,
            y : document.body.scrollTop + document.documentElement.scrollTop;
        }
    }
}
```

### 查看元素尺寸

1. **查看可视区域尺寸**

    >window.innerWidth/innerHeight  浏览器视口的高度
    >
    >document.documentElement.clientWidth/clientHeight IE8及以下不兼容  标准模式下任意浏览器都兼容
    >
    >document.body.clientWidht/clientHeight 适用于怪异模式下的浏览器

1. **clientHeight**

    只读，对于没有定义CSS或者内联布局盒子的元素为0，否则，它是元素内部的高度(单位像素)，包含内边距，但不包括水平滚动条、边框和外边距。

1. **clientWidth**

    只读，内联元素以及没有 CSS 样式的元素的属性值为 0。表示元素的内部宽度，以像素计。包括内边距 padding，但不包括边框 border、外边距 margin 和垂直滚动条（如果有的话）。

1. **clientLeft**

    只读，表示一个元素的左边框的宽度，以像素表示。 不包括左外边距和左内边距。

1. **clientTop**

    只读，一个元素顶部边框的宽度（以像素表示）。不包括顶部外边距或内边距。

1. **scrollWidth**

    只读，包括由于overflow溢出而在屏幕上不可见的内容。

    宽度的测量方式与clientWidth相同； 它还可以包括伪元素的宽度，例如::before或::after。 如果元素的内容可以适合而不需要水平滚动条，则其scrollWidth等于clientWidth

1. **scrollHeight**

    只读，包括由于溢出导致的视图中不可见内容。 没有垂直滚动条的情况下，scrollHeight值与元素视图填充所有内容所需要的最小值clientHeight相同。scrollHeight也包括 ::before 和 ::after这样的伪元素。

1. **scrollTop**

    获取或设置一个元素的内容垂直滚动的像素数。一个元素的 scrollTop 值是这个元素的**内容顶部**（卷起来的）到它的视口可见内容（的顶部）的距离的度量。当一个元素的内容没有产生垂直方向的滚动条，那么它的 scrollTop 值为0

1. **scrollLeft**

    读取或设置元素滚动条到元素左边的距离。注意如果这个元素的内容排列方向（direction） 是rtl (right-to-left) ，那么滚动条会位于最右侧（内容开始处），并且scrollLeft值为0。此时，当你从右到左拖动滚动条时，scrollLeft会从0变为负数。

1. **offsetWidth**

    只读，一个典型的（译者注：各浏览器的offsetWidth可能有所不同）offsetWidth是测量包含元素的边框(border)、水平线上的内边距(padding)、竖直方向滚动条(scrollbar)、以及CSS设置的宽度(width)的值。

1. **offsetHeight**

    只读，高度包含该元素的垂直内边距和边框，且是一个整数。

    通常，元素的offsetHeight是一种元素CSS高度的衡量标准，包括元素的边框、内边距和元素的水平滚动条，不包含:before或:after等伪类元素的高度。

1. **offsetLeft**

    只读，返回当前元素*左上角*相对于  HTMLElement.offsetParent 节点的左边界偏移的像素值。

1. **offsetTop**

    只读，它返回当前元素相对于其 offsetParent 元素的顶部内边距的距离。

    dom.offsetLeft dom.offsetTop 对于无定位父级的元素 返回相对文档的坐标，有定位父级的元素，返回相对最近的有定位的父级的坐标

1. **offsetParent**

    返回最近的有定位的父级 如无 返回body，body.offsetParent 返回null

#### **块级元素**

offsetTop、offsetLeft、offsetWidth 及 offsetHeight 描述了元素相对于 offsetParent 的边界框。

#### **行内元素**（*可被截断到下一行的***）**（如 **span**）

使用 Element.getClientRects() 来获取其宽度和高度，

使用 Element.getBoundingClientRect 来获取其位置。

#### **让滚动条滚动**

window.scroll(x,y), scrollto(x,y), scrollBy(x,y)  用法是传入xy坐标，实现让滚动条滚动到当前位置

区别：scrollby()会在之前的数据基础上做累加。

#### **应用：利用scrollby（）快速阅读的功能**

### **标准模式与怪异模式**

<!DOCTYPE html>不写这一行就是怪异（混乱）模式（向后兼容）
查看元素几何尺寸：getBoundingClientRect()
兼容性很好，返回一个对象，对象里有left top right bottom 等属性，left和top 代表该元素左上角的X 和 Y 坐标，right 和 bottom 代表元素右下角的 X 和 Y 坐标；height 和 width 属性老版本IE并未实现 返回的结果并不是实时的。

### 脚本化CSS

dom.style.porp

- 可读写行间样式，没有兼容性问题，碰到float这样的保留字属性，前面加css

- 复合属性必须拆解，小驼峰写法

- 写入的值必须是字符串格式

查询计算样式

- window.getComputedstyle(ele,null); //null 可以用来选伪元素

- 计算样式 只读

- 返回的计算样式的值都是绝对值 没有相对单位 IE8及以下不兼容

查询样式

- ele.currentStyle

- 返回的计算样式的值不是进过转换的绝对值

- IE 独有属性

兼容性方法

## BOM

### 介绍

- BOM用来处理与浏览器窗口有关的操作；
- BOM是由一系列的对象组成的，每个对象都有自己的属性，它们各自负责一类功能；
- **window对象是BOM的核心对象，也是顶层对象**，其余的对象从window里延伸出来的，可以理解为window的子对象；
- window可以理解成浏览器窗口，而HTML文档由浏览器运行，所以document是window的子对象

### window的子对象

1. document
2. history
3. location
4. navigator
5. screen

### window有关方法

window.open打开一个弹窗的语法是 window.open(url, name, params)：

- url 要在新窗口中加载的 URL。
- name 新窗口的名称。每个窗口都有一个 window.name，在这里我们可以指定哪个窗口用于弹窗。如果已经有一个这样名字的窗口 —— 将在该窗口打开给定的 URL，否则会打开一个新窗口。
- params 新窗口的配置字符串。它包括设置，用逗号分隔。参数之间不能有空
- 返回值：打开的新窗口的window对象

`Window.close()` 方法关闭当前窗口或某个指定的窗口。该方法只能由 Window.open() 方法打开的窗口的 window 对象来调用。

### 移动弹窗

- win.moveBy(x,y) 将窗口相对于当前位置向右移动 x 像素，并向下移动 y 像素。允许负值（向上/向左移动）。
- win.moveTo(x,y) 将窗口移动到屏幕上的坐标 (x,y) 处。
- win.resizeBy(width,height) 根据给定的相对于当前大小的 width/height 调整窗口大小。允许负值。
- win.resizeTo(width,height) 将窗口调整为给定的大小。

检查一个窗口是否被关闭：`win.closed`。

### 滚动弹窗

- win.scrollBy(x,y) 相对于当前位置，将窗口向右滚动 x 像素，并向下滚动 y 像素。允许负值。
- win.scrollTo(x,y) 将窗口滚动到给定坐标 (x,y)。
- elem.scrollIntoView(top = true) 滚动窗口，使 elem 显示在 elem.scrollIntoView(false) 的顶部（默认）或底部。
- window.onscroll 事件。

### history对象

该对象存储的是浏览器的浏览历史（页面的访问顺序，仅存储最近访问的，有限条目的url信息）；H5 之前为了保护个人隐私，history是禁止通过JS访问的

常用方法

`window.history.back()` 在会话历史记录中向后移动一页。如果没有上一页，则此方法调用不执行任何操作。

`window.history.forward()`  在会话历史中向前移动一页。它与使用delta参数为1时调用 history.go(delta)的效果相同。

`window.history.go(delta)` 方法从会话历史记录中加载特定页面。你可以使用它在历史记录中前后移动，具体取决于`delta`参数的值。负值表示向后移动，正值表示向前移动。如果未向该函数传参或delta相等于0（**宽松相等**），则该函数与调用location.reload()具有相同的效果。

### Location

接口表示其链接到的对象的位置（URL）。所做的修改反映在与之相关的对象上。

- Location.href 包含整个URL的一个DOMString
- Location.protocol 包含URL对应协议的一个DOMString，**最后有一个":"。**
- Location.host 包含了域名的一个DOMString，**可能在该串最后带有一个":"**并跟上URL的端口号。
- Location.hostname 包含URL域名的一个DOMString。
- Location.port 包含端口号的一个DOMString。
- Location.pathname  包含URL中路径部分的一个DOMString，**开头有一个“/"。**
- Location.search  包含URL参数的一个DOMString，**开头有一个“?”。**
- Location.hash 包含块标识符的DOMString，**开头有一个“#”。**
- Location.username  包含URL中域名前的用户名的一个DOMString。
- Location.password  包含URL域名前的密码的一个 DOMString。
- Location.origin 只读 包含页面来源的域名的标准形式DOMString。
- Location.assign() 加载给定URL的内容资源到这个Location对象所关联的对象上。
- Location.reload() 重新加载来自当前 URL的资源。他有一个特殊的可选参数，类型为 Boolean (en-US)，该参数为true时会导致该方法引发的刷新一定会从服务器上加载数据。如果是 false或没有制定这个参数，浏览器可能从缓存当中加载页面。
- Location.replace() 用给定的URL替换掉当前的资源。

### Navigator

接口表示用户代理的状态和标识。 它允许脚本查询它和注册自己进行一些活动。可以使用只读的 window.navigator 属性检索navigator对象。

- NavigatorID.appCodeName 只读 返回当前浏览器的内部“开发代号”名称。 不能保证此属性返回的值是正确的。
- NavigatorID.appName 只读 以 DOMString 的形式返回浏览器官方名称。 不能保证此属性返回的值是正确的。
- NavigatorID.appVersion 只读 以 DOMString 的形式返回浏览器版本。不能保证此属性返回的值是正确的。
- Navigator.battery 只读 返回一个 BatteryManager 对象，你可以用它来获取一些电池充电状态的信息。
- Navigator.connection 只读 提供一个NetworkInformation对象来获取设备的网络连接信息。
- Navigator.cookieEnabled 只读 当忽略 cookie 时返回 false，否则返回 true
- Navigator.geolocation 只读 返回一个 Geolocation 对象，据之可访问设备的地理位位置信息。
- NavigatorOnLine.onLine 只读 返回Boolean (en-US)来表明浏览器是否联网。
- Navigator.oscpu 返回当前操作系统名。
- NavigatorID.userAgent 只读返回当前浏览器的用户代理。

### screen对象

存储了客户端的屏幕信息，这些信息可以用来探测客户端的屏幕的配置；

- width/height 屏幕宽高
- availWidth/availHeight  屏幕可用宽高
- Screen.availTop Screen.availLeft 返回屏幕上边/左边边界的第一个像素点

移动端常用事件

- TouchEvent 表示位于表面上的一个触摸点的某个状态发生改变时产生的事件。
- Touch 表示用户与触摸表面间的一个单独的接触点。
- TouchList 表示一组 Touch，用于多点触控的情况。

### 事件

1. ontouchstart
2. ontouchmove
3. ontouchend
4. ontouchcancel 被动触摸 这是被动触发的事件，因为移动设备中有一些优先级较高的任务，如来电、闹钟、事件提醒等，如果当前有优先级高的任务触发，系统会丢弃原来的事件，触发cancel事件。适合做一些保护性的操作，如暂停游戏、音视频的播放等。

### 跟踪触摸的属性

- touches：包含了所有当前接触触摸平面的触点的 Touch 对象，无论它们的起始于哪个 element 上，也无论它们状态是否发生了变化。
- targetTouches：触摸起始于当前事件的目标 element 上，并且仍然没有离开触摸平面的触点。
- changedTouches：包含了代表所有从上一次触摸事件到此次事件过程中，状态发生了改变的触点的 Touch 对象。

### Touch 对象表示在触控设备上的触摸点,其基本属性

- Touch.screenX/Y
- Touch.clientX/Y
- Touch.pageX/Y
- Touch.radiusX只读 返回能够包围接触区域的最小椭圆的水平轴(X轴)半径。这个值的单位和 screenX 相同。
- Touch.radiusY只读 返回能够包围接触区域的最小椭圆的垂直轴(Y轴)半径。这个值的单位和 screenY 相同。
- Touch.rotationAngle只读 返回一个角度值，表示上述由radiusX 和 radiusY 描述的椭圆为了尽可能精确地覆盖用户与平面之间的接触区域而需要顺时针旋转的角度。
- Touch.force只读 返回用户对触摸平面的压力大小，是一个从0.0(没有压力)到1.0(最大压力)的浮点数。

## 文档加载和其它资源

### 文档加载

HTML 页面的生命周期包含三个重要事件：

- `DOMContentLoaded` —— 浏览器已完全加载 HTML，并构建了 DOM 树，但像 `<img>` 和样式表之类的外部资源可能尚未加载完成。
- `load` —— 浏览器不仅加载完成了 HTML，还加载完成了所有外部资源：图片，样式等。
- `beforeunload/unload` —— 当用户正在离开页面时。

每个事件都是有用的：

- `DOMContentLoaded` 事件 —— DOM 已经就绪，因此处理程序可以查找 DOM 节点，并初始化接口。
- `load` 事件 —— 外部资源已加载完成，样式已被应用，图片大小也已知了。
- `beforeunload` 事件 —— 用户正在离开：我们可以检查用户是否保存了更改，并询问他是否真的要离开。
- `unload` 事件 —— 用户几乎已经离开了，但是我们仍然可以启动一些操作，例如发送统计数据。

```js
DOMContentLoaded 事件发生在 document 对象上。使用事件监听捕获
document.addEventListener("DOMContentLoaded", ready); 
// 不是 "document.onDOMContentLoaded = ..."
```

### DOMContentLoaded

当浏览器处理一个 HTML 文档，并在文档中遇到 <script> 标签时，就会在继续构建 DOM 之前运行它。这是一种防范措施，因为脚本可能想要修改 DOM，甚至对其执行 document.write 操作，所以 DOMContentLoaded 必须等待脚本执行结束。

- 诸如 `<script>...</script>` 或 `<script src="..."></script>` 之类的脚本会阻塞 `DOMContentLoaded`，浏览器将等待它们执行结束。
- 图片和其他资源仍然可以继续被加载。

此规则有两个例外：

1. 具有 `async` 特性（attribute）的脚本不会阻塞 `DOMContentLoaded`，[稍后](https://zh.javascript.info/script-async-defer) 我们会讲到。
2. 使用 `document.createElement('script')` 动态生成并添加到网页的脚本也不会阻塞 `DOMContentLoaded`。

**外部样式表不会影响 DOM**，因此 `DOMContentLoaded` 不会等待它们。但这里有一个陷阱。如果在样式后面有一个脚本，那么该脚本必须等待样式表加载完成：

```js
<link type="text/css" rel="stylesheet" href="style.css">
<script>
  // 在样式表加载完成之前，脚本都不会执行
  alert(getComputedStyle(document.body).marginTop);
</script>
```

原因是，脚本可能想要获取元素的坐标和其他与样式相关的属性，如上例所示。因此，它必须等待样式加载完成。

### window.onload

当整个页面，包括样式、图片和其他资源被加载完成时，会触发 window 对象上的 load 事件。可以通过 onload 属性获取此事件。

### window.onbeforeunload

如果访问者触发了离开页面的导航（navigation）或试图关闭窗口，beforeunload 处理程序将要求进行更多确认。

```js
window.onbeforeunload = function() {
  return false;
};
```

### window.onunload

当访问者离开页面时，window 对象上的 unload 事件就会被触发。我们可以在那里做一些**不涉及延迟**的操作，例如关闭相关的弹出窗口。

navigator.sendBeacon(url, data) ，发送分析数据，详见规范 <https://w3c.github.io/beacon/。它在后台发送数据，转换到另外一个页面不会有延迟：浏览器离开页面，但仍然在执行> sendBeacon。

```js
let analyticsData = { /* 带有收集的数据的对象 */ };

window.addEventListener("unload", function() {
  navigator.sendBeacon("/analytics", JSON.stringify(analyticsData));
});
//请求以 POST 方式发送。
//我们不仅能发送字符串，还能发送表单以及其他格式的数据，通常它是一个字符串化的对象。
//数据大小限制在 64kb。
```

`document.readyState` 是文档的当前状态，可以在 `readystatechange` 事件中跟踪状态更改：

- `loading` —— 文档正在被加载。
- `interactive` —— 文档已被解析完成，与 `DOMContentLoaded` 几乎同时发生，但是在 `DOMContentLoaded` 之前发生。
- `complete` —— 文档和资源均已加载完成，与 `window.onload` 几乎同时发生，但是在 `window.onload` 之前发生。

```js
<script>
  log('initial readyState:' + document.readyState);

  document.addEventListener('readystatechange', () => log('readyState:' + document.readyState));
  document.addEventListener('DOMContentLoaded', () => log('DOMContentLoaded'));

  window.onload = () => log('window onload');
</script>

<iframe src="iframe.html" onload="log('iframe onload')"></iframe>

<img src="http://en.js.cx/clipart/train.gif" id="img">
<script>
  img.onload = () => log('img onload');
</script>

/*
output:标有相同数字的事件几乎是同时发生的（± 几毫秒）。
[1] initial readyState:loading
[2] readyState:interactive
[2] DOMContentLoaded
[3] iframe onload
[4] img onload
[4] readyState:complete
[4] window onload
在 DOMContentLoaded 之前，document.readyState 会立即变成 interactive。它们俩的意义实际上是相同的。
当所有资源（iframe 和 img）都加载完成后，document.readyState 变成 complete。这里我们可以发现，它与 img.onload（img 是最后一个资源）和 window.onload 几乎同时发生。转换到 complete 状态的意义与 window.onload 相同。区别在于 window.onload 始终在所有其他 load 处理程序之后运行。
*/
```

### 脚本：async，defer

| 顺序    | `DOMContentLoaded`                                           |                                                              |
| :------ | :----------------------------------------------------------- | ------------------------------------------------------------ |
| `async` | **加载优先顺序**。脚本在文档中的顺序不重要 —— 先加载完成的先执行 | 不相关。可能在文档加载完成前加载并执行完毕。如果脚本很小或者来自于缓存，同时文档足够长，就会发生这种情况。 |
| `defer` | **文档顺序**（它们在文档中的顺序）                           | 在文档加载和解析完成之后（如果需要，则会等待），即在 `DOMContentLoaded` 之前执行。 |

加载这样的脚本都不会阻塞页面的渲染。因此，用户可以立即阅读并了解页面内容。

```js
let script = document.createElement('script');
script.src = "/article/script-async-defer/long.js";
document.body.append(script); // (*)
```

默认情况下，动态脚本的行为是“异步”的。

如果我们显式地设置了 `script.async=false`，则可以改变这个规则。然后脚本将按照脚本在文档中的顺序执行，就像 `defer` 那样。

### 加载脚本

script.onload

script.onerror

onload/onerror 事件仅跟踪加载本身。

> 在脚本处理和执行期间可能发生的 error 超出了这些事件跟踪的范围。
>
> 大多数资源在被添加到文档中后，便开始加载。但是 <img> 是个例外。它要等到获得 src (*) 后才开始加载。
>
> 对于 <iframe> 来说，iframe 加载完成时会触发 iframe.onload 事件，无论是成功加载还是出现 error。
> 这是出于历史原因。

### 跨源策略（CORS）

要允许跨源访问，<script> 标签需要具有 crossorigin 特性（attribute），并且远程服务器必须提供特殊的 header。

这里有三个级别的跨源访问：

- 无 crossorigin 特性 —— 禁止访问。
- crossorigin="anonymous" —— 如果服务器的响应带有包含 * 或我们的源（origin）的 header Access-Control-Allow-Origin，则允许访问。浏览器不会将授权信息和 cookie 发送到远程服务器。
- crossorigin="use-credentials" —— 如果服务器发送回带有我们的源的 header Access-Control-Allow-Origin 和 Access-Control-Allow-Credentials: true，则允许访问。浏览器会将授权信息和 cookie 发送到远程服务器。
