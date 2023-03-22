---
title: JavaScript语言基础下
date: 2020-03-06
updated: 2020-03-10
tags:
- JavaScript
- 基础
categories:
- JavaScript
---
## 存储数据

### Cookie

### 属性

`document.cookie` 的值由 `name=value` 对组成，以 `;` 分隔。每一个都是独立的 cookie。从技术上讲，cookie 的名称和值可以是任何字符，为了保持有效的格式， 它们应该使用内建的 `encodeURIComponent` 函数对其进行转义；编码后的 `name=value` 对，大小不能超过 4KB。每个域的 cookie 总数不得超过 20+ 左右，具体限制取决于浏览器。**Cookie生成在服务器，保存在浏览器；**

```js
document.cookie = "user=John; path=/; expires=Tue, 19 Jan 2038 03:14:07 GMT"
```

**`path=/mypath`**

url 路径前缀，该路径下的页面可以访问该 cookie。必须是绝对路径。默认为当前路径。将 `path` 设置为根目录：`path=/`，以使 cookie 对此网站的所有页面可见。

**`domain=site.com`**

可访问 cookie 的域。如需访问其子域：domain`选项显式地设置为根域：`domain=site.com

**`expires，max-age`**
默认情况下，如果一个 cookie 没有设置这两个参数中的任何一个，那么在关闭浏览器之后，它就会消失。此类 cookie 被称为 "session cookie”。为了让 cookie 在浏览器关闭后仍然存在，我们可以设置 `expires` 或 `max-age` 选项中的一个。

```
expires=Tue, 19 Jan 2038 03:14:07 GMT
max-age=3600//expires 的替代选项，具指明 cookie 的过期时间距离当前时间的秒数。
```

**`secure`**
设置 cookie secure（只在 HTTPS 环境下可访问）；默认情况下，如果我们在 <http://site.com> 上设置了 cookie，那么该 cookie 也会出现在 <https://site.com> 上，反之亦然。也就是说，cookie 是基于域的，它们不区分协议。

**`samesite`**
旨在防止 XSRF（“跨网站请求伪造（Cross-Site Request Forgery，简称 XSRF）”）攻击。特殊字段“XSRF 保护 token”进行校验避免攻击。其值可以为**`strict`**和**`lax`**，lax模式，和 `strict` 模式类似，当从外部来到网站，则禁止浏览器发送 cookie，但是增加了一个例外。如果以下两个条件均成立，则会发送 `samesite=lax` cookie：

- HTTP 方法是“安全的”（例如 GET 方法，而不是 POST）。
- 该操作执行顶级导航（更改浏览器地址栏中的 URL）。

`samesite` 会被到 2017 年左右的旧版本浏览器忽略（不兼容）。

**`httpOnly`**
这个选项和 JavaScript 没有关系，但是我们必须为了完整性也提一下它。Web 服务器使用 Set-Cookie header 来设置 cookie。并且，它可以设置 httpOnly 选项。这个选项禁止任何 JavaScript 访问 cookie。我们使用 document.cookie 看不到此类 cookie，也无法对此类 cookie 进行操作。

### Cookie方法

getCookie(name) 函数返回具有给定 name 的 cookie，没找到，则返回 undefined;

```js
// 返回具有给定 name 的 cookie，
// 如果没找到，则返回 undefined
function getCookie(name) {
  let matches = document.cookie.match(new RegExp(
    "(?:^|; )" + name.replace(/([\.$?*|{}\(\)\[\]\\\/\+^])/g, '\\$1') + "=([^;]*)"
  ));
  return matches ? decodeURIComponent(matches[1]) : undefined;
}
```

setCookie(name, value, options)//使用范例： setCookie('user', 'John', {secure: true, 'max-age': 3600});

```js
将 cookie name 设置为具有默认值 path=/（可以修改以添加其他默认值）和给定值 value：
function setCookie(name, value, options = {}) {

  options = {
    path: '/',
    // 如果需要，可以在这里添加其他默认值
    ...options
  };

  if (options.expires instanceof Date) {
    options.expires = options.expires.toUTCString();
  }

  let updatedCookie = encodeURIComponent(name) + "=" + encodeURIComponent(value);

  for (let optionKey in options) {
    updatedCookie += "; " + optionKey;
    let optionValue = options[optionKey];
    if (optionValue !== true) {
      updatedCookie += "=" + optionValue;
    }
  }

  document.cookie = updatedCookie;
}

```

deleteCookie()

```js
function deleteCookie(name) {
  setCookie(name, "", {
    'max-age': -1
  })
}
```

### localStorage 和 sessionStorage

优势：5MB 增删改查 生命周期默认永久（在页面刷新后（对于 `sessionStorage`）甚至浏览器完全重启（对于 `localStorage`）后，数据仍然保留在浏览器中。）

局限：不兼容低版本IE 不会跟请求发送到后台服务端 隐私模式下无法被读取 无法被爬虫爬到 数据若过大可能卡顿

两个存储对象都提供相同的方法和属性：

- `setItem(key, value)` —— 存储键/值对。
- `getItem(key)` —— 按照键获取值。
- `removeItem(key)` —— 删除键及其对应的值。
- `clear()` —— 删除所有数据。
- `key(index)` —— 获取该索引下的键名。
- `length` —— 存储的内容的长度。

## 二进制数据、文件

JS中存在有很多种二进制数据格式

- `ArrayBuffer`，`Uint8Array`，`DataView`，`Blob`，`File` 及其他。

**基本的二进制对象是 `ArrayBuffer` —— 对固定长度的连续内存空间的引用。**

**`ArrayBuffer` 不是某种东西的数组**

`ArrayBuffer` 与 `Array` 没有任何共同之处：

- 它的长度是固定的，我们无法增加或减少它的长度。
- 它正好占用了内存中的那么多空间。
- 要访问单个字节，需要另一个“视图”对象，而不是 `buffer[index]`。

视图对象本身并不存储任何东西。它是一副“眼镜”，透过它来解释存储在 `ArrayBuffer` 中的字节。

- **`Uint8Array`** —— 将 `ArrayBuffer` 中的每个字节视为 0 到 255 之间的单个数字（每个字节是 8 位，因此只能容纳那么多）。这称为 “8 位无符号整数”。
- **`Uint16Array`** —— 将每 2 个字节视为一个 0 到 65535 之间的整数。这称为 “16 位无符号整数”。
- **`Uint32Array`** —— 将每 4 个字节视为一个 0 到 4294967295 之间的整数。这称为 “32 位无符号整数”。
- **`Float64Array`** —— 将每 8 个字节视为一个 `5.0x10-324` 到 `1.8x10308` 之间的浮点数。

`ArrayBuffer` 是核心对象，是所有的基础，是原始的二进制数据。

所有这些视图（Uint8Array，Uint32Array 等）的通用术语是 TypedArray。它们都享有同一组方法和属性。

> 没有名为 `TypedArray` 的构造器，它只是表示 `ArrayBuffer` 上的视图之一的通用总称术语

访问 `ArrayBuffer`，可以用以下属性：

- `arr.buffer` —— 引用 `ArrayBuffer`。
- `arr.byteLength` —— `ArrayBuffer` 的长度。

### 越界行为

如果我们尝试将越界值写入类型化数组会出现什么情况？不会报错。但是多余的位被切除。

### TypedArray 方法

我们可以遍历（iterate），`map`，`slice`，`find` 和 `reduce` 等。

但有几件事我们做不了：

- 没有 `splice` —— 我们无法“删除”一个值，因为类型化数组是缓冲区（buffer）上的视图，并且缓冲区（buffer）是固定的、连续的内存区域。我们所能做的就是分配一个零值。
- 无 `concat` 方法。

还有两种其他方法：

- `arr.set(fromArr, [offset])` 从 `offset`（默认为 0）开始，将 `fromArr` 中的所有元素复制到 `arr`。
- `arr.subarray([begin, end])` 创建一个从 `begin` 到 `end`（不包括）相同类型的新视图。这类似于 `slice` 方法（同样也支持），但不复制任何内容 —— 只是创建一个新视图，以对给定片段的数据进行操作。

### DataView

DataView 是在 ArrayBuffer 上的一种特殊的超灵活“未类型化”视图。它允许以任何格式访问任何偏移量（offset）的数据。

- 对于类型化的数组，构造器决定了其格式。整个数组应该是统一的。第 i 个数字是 arr[i]。
- 通过 DataView，我们可以使用 .getUint8(i) 或 .getUint16(i) 之类的方法访问数据。我们在调用方法时选择格式，而不是在构造的时候。

```js
//语法
new DataView(buffer, [byteOffset], [byteLength])
```

- **`buffer`** —— 底层的 `ArrayBuffer`。与类型化数组不同，`DataView` 不会自行创建缓冲区（buffer）。我们需要事先准备好。
- **`byteOffset`** —— 视图的起始字节位置（默认为 0）。
- **`byteLength`** —— 视图的字节长度（默认至 `buffer` 的末尾）。

## TextDecoder 和 TextEncoder

如果二进制数据实际上是一个字符串，内建的 TextDecoder 对象在给定缓冲区（buffer）和编码格式（encoding）的情况下，能够将值读取到实际的 JavaScript 字符串中。首先我们需要创建：

```js
let decoder = new TextDecoder([label], [options]);

label —— 编码格式，默认为 utf-8，但同时也支持 big5，windows-1251 等许多其他编码格式。
options —— 可选对象：
 fatal —— 布尔值，如果为 true 则为无效（不可解码）字符抛出异常，否则（默认）用字符 \uFFFD 替换无效字符。
 ignoreBOM —— 布尔值，如果为 true 则 BOM（可选的字节顺序 unicode 标记），很少需要使用。
```

然后解码：

```js
let str = decoder.decode([input], [options]);

input —— 要被解码的 BufferSource。
options —— 可选对象：
 stream —— 对于解码流，为 true，则将传入的数据块（chunk）作为参数重复调用 decoder。在这种情况下，多字节的字符可能偶尔会在块与块之间被分割。这个选项告诉 TextDecoder 记住“未完成”的字符，并在下一个数据块来的时候进行解码。
```

TextEncoder 做相反的事情 —— 将字符串转换为字节。

```js
let encoder = new TextEncoder();

只支持 utf-8 编码。它有两种方法：
encode(str) —— 从字符串返回 Uint8Array。
encodeInto(str, destination) —— 将 str 编码到 destination 中，该目标必须为 Uint8Array。
```

## Blob

`Blob` 由一个可选的字符串 `type`（通常是 MIME 类型）和 `blobParts` 组成 —— 一系列其他 `Blob` 对象，字符串和 `BufferSource`。或者说，ArrayBuffer，Uint8Array 及其他 BufferSource 是“二进制数据”，而 Blob 则表示“具有类型的二进制数据”。

![image-20210426162523200](C:\Users\lanou\AppData\Roaming\Typora\typora-user-images\image-20210426162523200.png)

```js
new Blob(blobParts, options);

//blobParts 是 Blob/BufferSource/String 类型的值的数组。
//options 可选对象：
 //type —— Blob 类型，通常是 MIME 类型，例如 image/png，
 //endings —— 是否转换换行符，使 Blob 对应于当前操作系统的换行符（\r\n 或 \n）。默认为 "transparent"（啥也不做），不过也可以是 "native"（转换）。
```

**TIPS:**

- `Blob` **对象是不可改变的**
- 媒体类型（通常称为 Multipurpose Internet Mail Extensions 或 MIME 类型 ）是一种标准，用来表示文档、文件或字节流的性质和格式。
- blobParts参数必须是一个数组 [...]

Blob 用作 URL

`URL.createObjectURL` 取一个 `Blob`，并为其创建一个唯一的 URL，形式为 `blob:<origin>/<uuid>`。即 link.href ：

blob:<https://javascript.info/1e67e00e-860d-40a5-89ae-6ab0cbee6273>

浏览器内部为每个通过 `URL.createObjectURL` 生成的 URL 存储了一个 URL → `Blob` 映射。因此，此类 URL 很短，但可以访问 `Blob`。`URL.revokeObjectURL(url)` 从内部映射中移除引用，因此允许 `Blob` 被删除（如果没有其他引用的话），并释放内存。

Blob 转换为 base64

这种编码将二进制数据表示为一个由 0 到 64 的 ASCII 码组成的字符串，非常安全且“可读“。更重要的是 —— 我们可以在 “data-url” 中使用此编码。

`“data-url”` 的形式为 `data:[<mediatype>][;base64],<data>`。我们可以在任何地方使用这种 ` url `，和使用“常规”` url ` 一样。

这两种从 `Blob` 创建 URL 的方法都可以用。但通常 `URL.createObjectURL(blob)` 更简单快捷。

URL.createObjectURL(blob)

- 如果介意内存，我们需要撤销（revoke）它们
- 直接访问 `Blob`，无需“编码/解码”

Blob 转换为 data url

- 无需撤销（revoke）任何操作。
- 对大的 `Blob` 进行编码时，性能和内存会有损耗。

Image 转换为 blob，图像操作是通过 <canvas> 元素来实现的：

- 使用 canvas.drawImage 在 canvas 上绘制图像（或图像的一部分）。
- 调用 canvas 方法 .toBlob(callback, format, quality) 创建一个 Blob，并在创建完成后使用其运行 callback。

页面截屏库：<https://github.com/niklasvh/html2canvas>

Blob 转换为 ArrayBuffer

如果我们需要执行低级别的操作的话，则可以使用 `FileReader` 从 blob 中获取最低级别的 `ArrayBuffer`

## File和Filereader

File 对象继承自 Blob，并扩展了与文件系统相关的功能。有两种方式可以获取它。

new File(fileParts, fileName, [options])

> fileParts —— Blob/BufferSource/String 类型值的数组。
> fileName —— 文件名字符串。
> options —— 可选对象：
> lastModified —— 最后一次修改的时间戳（整数日期）

更常见的是，我们从 `<input type="file">` 或拖放或其他浏览器接口来获取文件。在这种情况下，file 将从操作系统（OS）获得 this 信息。<input>元素上选择文件后返回的FileList对象，拖放操作生成的 DataTransfer对象

FileReader 是一个对象，其唯一目的是从 Blob（因此也从 File）对象中读取数据。它使用事件来传递数据，因为从磁盘读取数据可能比较费时间。

let reader = new FileReader(); // 没有参数

主要方法:

- **`readAsArrayBuffer(blob)`** —— 将数据读取为二进制格式的 `ArrayBuffer`。
- **`readAsText(blob, [encoding])`** —— 将数据读取为给定编码（默认为 `utf-8` 编码）的文本字符串。
- **`readAsDataURL(blob)`** —— 读取二进制数据，并将其编码为 base64 的 data url。
- **`abort()`** —— 取消操作。

`read*` 方法的选择，取决于我们喜欢哪种格式，以及如何使用数据。

- `readAsArrayBuffer` —— 用于二进制文件，执行低级别的二进制操作。对于诸如切片（slicing）之类的高级别的操作，`File` 是继承自 `Blob` 的，所以我们可以直接调用它们，而无需读取。
- `readAsText` —— 用于文本文件，当我们想要获取字符串时。
- `readAsDataURL` —— 当我们想在 `src` 中使用此数据，并将其用于 `img` 或其他标签时。替代方案：`URL.createObjectURL(file)`。

读取过程中，有以下事件：

- `loadstart` —— 开始加载。
- `progress` —— 在读取过程中出现。
- `load` —— 读取完成，没有 error。
- `abort` —— 调用了 `abort()`。
- `error` —— 出现 error。
- `loadend` —— 读取完成，无论成功还是失败。

读取完成后，我们可以通过以下方式访问读取结果：

- `reader.result` 是结果（如果成功）
- `reader.error` 是 error（如果失败）。

使用最广泛的事件无疑是 `load` 和 `error`。

## Web Worker

对于不应该阻塞事件循环的耗时长的繁重计算任务，我们可以使用 Web Workers。

这是在另一个并行线程中运行代码的方式。

Web Workers 可以与主线程交换消息，但是它们具有自己的变量和事件循环。

Web Workers 没有访问 DOM 的权限，因此，它们对于同时使用多个 CPU 内核的计算非常有用。

一个worker是使用一个构造函数创建的一个对象(e.g. Worker()) 运行一个命名的JavaScript文件 - 这个文件包含将在工作线程中运行的代码; workers 运行在另一个全局上下文中,不同于当前的window. 因此，在 Worker 内通过 window获取全局作用域 (而不是self) 将返回错误。

在worker线程中你可以运行任何你喜欢的代码，不过有一些例外情况。比如：在worker内，不能直接操作DOM节点，也不能使用window对象的默认方法和属性。

## 网络请求

### Fetch

#### 基本语法

```js
let promise = fetch(url,[options])
//url —— 要访问的 URL。
//options —— 可选参数：method，header 等。
//没有 options，那就是一个简单的 GET 请求，下载 url 的内容。
```

获取响应通常需要经过两个阶段。

**第一阶段，当服务器发送了响应头（response header），`fetch` 返回的 `promise` 就使用内建的 [Response](https://fetch.spec.whatwg.org/#response-class) class 对象来对响应头进行解析。**

response 的属性中看到 HTTP 状态：

- **`status`** —— HTTP 状态码，例如 200。
- **`ok`** —— 布尔值，如果 HTTP 状态码为 200-299，则为 `true`。

```js
let response = await fetch(url);

if (response.ok) { // 如果 HTTP 状态码为 200-299
  // 获取 response body（此方法会在下面解释）
  let json = await response.json();
} else {
  alert("HTTP-Error: " + response.status);
}
```

**第二阶段，为了获取 response body，我们需要使用一个其他的方法调用。**

`Response` 提供了多种基于 promise 的方法，来以不同的格式访问 body：

- **`response.text()`** —— 读取 response，并以文本形式返回 response，
- **`response.json()`** —— 将 response 解析为 JSON，
- **`response.formData()`** —— 以 `FormData` 对象的形式返回 response，
- **`response.blob()`** —— 以 Blob （具有类型的二进制数据）形式返回 response，
- **`response.arrayBuffer()`** —— 以ArrayBuffer（低级别的二进制数据）形式返回 response，
- 另外，`response.body` 是 ReadableStream对象，它允许你逐块读取 body

```js
fetch('https://api.github.com/repos/javascript-tutorial/en.javascript.info/commits')
  .then(response => response.json())
  .then(commits => alert(commits[0].author.login));
```

#### Response header

Response header 位于 `response.headers` 中的一个类似于 Map 的 header 对象。可以按名称（name）获取各个 header，或迭代它们

```js
let response = fetch(protectedUrl, {
  headers: {
    Authentication: 'secret'
  }
});
```
