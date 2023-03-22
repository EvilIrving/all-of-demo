---
title: JS AJAX
date: 2020-03-30
updated: 2020-03-31
tags:
- JavaScript
categories:
- JavaScript
---
## 基础知识

- AJAX是异步的JavaScript和XML（Asynchronous JavaScript And XML）
- 使用 `XMLHttpRequest` 对象与服务器通信
- 可以使用JSON，XML，HTML和text文本等格式发送和接收数据。

## 特性

- 在不重新加载页面的情况下发送请求给服务器。
- 接受并使用从服务器发来的数据。

## 异步和同步

同步：在发起网络请求时，系统得到服务器返回结果之前，无法继续向后执行，直到得到返回的结果，代码才会继续向下执行，会造成等待或者延迟

异步：发起请求时，系统不需要等待网络请求的结果，可以继续向后执行代码，当请求到结果时，会通过回调函数返回得到的数据，不会造成等待或者延迟

## get post 请求的区别

|          get          |    post    |
| :-------------------: | :--------: |
| 键值对格式拼接URL后面 | 存入请求体 |
| 数据大小限制1024字节  |   无限制   |
|      安全性问题       |  相对安全  |

client  -->  request  --> server

client <-- response <-- server

```js
// Old compatibility code, no longer needed.
if (window.XMLHttpRequest) { // Mozilla, Safari, IE7+ ...
    httpRequest = new XMLHttpRequest();
} else if (window.ActiveXObject) { // IE 6 and older
    httpRequest = new ActiveXObject("Microsoft.XMLHTTP");
}

if (httpRequest.readyState === XMLHttpRequest.DONE) {
    // Everything is good, the response was received.
    // 全部readyState状态值都在 XMLHTTPRequest.readyState， 如下也是：
    // 0(未初始化) or(请求还未初始化)
    // 1(正在加载) or(已建立服务器链接)
    // 2(加载成功) or(请求已接受)
    // 3(交互) or(正在处理请求)
    // 4(完成) or(请求已完成并且响应已准备好)
    if (httpRequest.status === 200) {
        // Perfect!
        //httpRequest.onreadystatechange = nameOfTheFunction;

        httpRequest.onreadystatechange = function () {
            // Process the server response here.

            // httpRequest.responseText– 服务器以文本字符的形式返回
            // httpRequest.responseXML– 以 XMLDocument 对象方式返回， 之后就可以使用JavaScript来处理
        };
    } else {
        // There was a problem with the request.
        // For example, the response may have a 404 (Not Found)
        // or 500 (Internal Server Error) response code.
    }
} else {
    // Not ready yet.
}

httpRequest.open('GET', 'http://www.example.org/some.file', true);
//如果你使用 POST 数据，那就需要设置请求的MIME类型。
//httpRequest.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');

httpRequest.send();
//- open() 的第一个参数是HTTP请求方法
//- 第二个参数是你要发送的URL。
//- 第三个参数是可选的，用于设置请求是否是异步的。如果设为 true (默认值)，即开启异步
```

### Note

- :如果你向一个代码片段发送请求，将返回XML，而不是静态XML文件，在IE浏览器上则必须要设置响应头才能正常工作。如果不设置响应头为 Content-Type:application/xml ，IE浏览器会在你访问XML元素时抛出 “Object Expected” 错误。
- 如果不设置响应头 Cache-Control: no-cache 浏览器将会把响应缓存下来而且再也无法重新提交请求。你也可以添加一个总是不同的 GET 参数，比如时间戳或者随机数 (详情见 bypassing the cache)
- 如果变量 httpRequest 在全局范围内使用，它会在 makeRequest() 函数中被相互覆盖，从而导致资源竞争。为了避免这个情况，请在包含 AJAX 函数的闭包中声明 httpRequest 变量

## JQuery AJAX

```js
//JQuery AJAX
$.ajax({
    type: "GET",  //默认get
    url: "url",  //默认当前页
    data: "data",  //格式{key:value}
    dataType: "json",
    beforeSend: function () { }, //请求发送前回调,常用验证
    success: function (response) {  //请求成功回调

    },
    error: function (e) {  //请求超时回调
        if (e.statusText == "timeout") {
            alert("请求超时")
        }
    },
    complete: function () { }, //无论请求是成功还是失败都会执行的回调，常用全局成员的释放，或者页面状态的重置
})

//POST
$.post("url", data,
    function (data, textStatus, jqXHR) {

    },
    "dataType"
)
//GET
$.get("url", data,
    function (data, textStatus, jqXHR) {

    },
    "dataType"
)

```

## 原生封装AJAX

```js
//原生方式封装ajax
function ajax(params = {}) {
    let { method, url, data } = params
    // xhr.readyState: 0初始化
    let xhr = new XMLHttpRequest()
    if (/get/gi.test(method)) {
        let temp = []
        // 传递过来的data参数格式: {a: 1, b: 2}
        // 转成form序列化字符串格式
        for (let key in data) {
            temp.push(`${key}=${data[key]}`)
        }
        url = url + '?' + temp.join('&')
    }
    // xhr.readyState: 1 开启异步请求
    xhr.open(method, url, true)
    // post请求 要设置一下请求头字段`Content-Type`
    if (/post/gi.test(method)) {
        xhr.setHeaders({
            'Content-Type': 'application/x-www-form-urlencoded',
        })
    }
    // 在send之前的操作(设置响应头、修改data数据)，都可以理解为拦截
    xhr.setHeaders({
        'X-Authorization': window.sessionStorage.getItem('token'),
    })
    // xhr.readyState: 2 发起异步请求
    xhr.send(data)
    // xhr.readyState: 3 服务器收到请求
    xhr.onreadystatechange = function () {
        // xhr.readyState: 4 客户端收到服务器的响应数据
        if (xhr.readyState === 4) {
            // 说明服务器正常响应，客户端收到数据进行处理
            if (xhr.status === 200) {
                // 客户端处理服务器返回的数据 handler是一个示意函数
                // handler(xhr.responseText)
                // 在把数据传递给success回调函数之前处理一下数据，把处理后的数据再给success
                // 这一步就是响应拦截器 obj为举例数据
                const obj = {
                    data: JSON.parse(xhr.responseText),
                    headers,
                    config,
                }
                // 把封装(拦截)后的数据交给回调函数success
                success(obj)
            }
        }
    }
}
ajax({
    method: 'get',
    url: 'http://www.abc.com/api/menu',
    data: { a: 1, b: 2 },
    success: fn,
})
ajax()
```

## 原生XHR封装成Promise Ajax

```js
var myNewAjax = function (url) {
    return new Promise(function (resolve, reject) {
        var xhr = new XMLHttpRequest();
        xhr.open('get', url);
        xhr.send(data);
        xhr.onreadystatechange = function () {
            if (xhr.status == 200 && readyState == 4) {
                var json = JSON.parse(xhr.responseText);
                resolve(json)
            } else if (xhr.readyState == 4 && xhr.status != 200) {
                reject('error');
            }
        }
    })
}
```
