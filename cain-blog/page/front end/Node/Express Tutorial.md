---
title: Express 教程
date: 2020-05-31
update: 2020-05-31
tags: 
- Node
- Express
categories: 
---

## Express.js
>
> Express.js 或简称 Express，是针对 Node.js 的 web 应用框架，在 MIT 许可证下作为自由及开放源代码软件发行。设计用来建造 web 应用和 API。它已经被称为针对 Node.js 的服务器框架的事实标准。

最初作者`TJ Holowaychuk`，将它描述为受 Sinatra 启发的服务器，意味着它是相对极小化的，带有以插件形式获得的很多特征。Express 是 MEAN 软件栈的后端部件，其他部件是 MongoDB 数据库软件和 AngularJS 前端框架。

## 历史

Express.js 由 TJ Holowaychuk 创立。首次发行的版本 0.12.0，依据 Express.js 的 GitHub 仓库，是在 2010 年 5 月 22 日。

在 2014 年 6 月，StrongLoop 获得了项目的管理权 。StrongLoop 在 2015 年 9 月被 IBM 并购。在 2016 年 1 月，IBM 宣布将 Express.js 置于 Node.js 基金会孵化器的管理之下。

## 流行度

Express.js 被用于 Fox Sports、PayPal、Uber 和 IBM，Node 框架名副其实的一哥。

## Express 4.x API

`express()`用来创建一个 Express 的程序。`express()`方法是 _**express**_ 模块导出的顶层方法。

```plain
var express = require('express');
var app = express();

```

### Methods

#### express.static(root, [options])

`express.static`是 Express 中唯一的内建中间件。它以 [server-static](https://github.com/expressjs/serve-static) 模块为基础开发，负责托管 Express 应用内的静态资源。 参数`root`为静态资源的所在的根目录。 参数`options`是可选的，支持以下的属性：

![express](./../../../assets/express.png)

如果你想获得更多关于使用中间件的细节，你可以查阅 [Serving static files in Express](http://expressjs.com/starter/static-files.html)。

### Application()

`app`对象一般用来表示 Express 程序。通过调用 Express 模块导出的顶层的`express()`方法来创建它:

```js
var express = require('express');
var app = express();
app.get('/', function(req, res) {
    res.send('hello world!');
});
app.listen(3000);
```

`app`对象具有以下的方法：

* 路由 HTTP 请求；具体可以看 [app.METHOD](http://expressjs.com/4x/api.html#app.METHOD) 和 [app.param](http://expressjs.com/4x/api.html#app.param) 这两个例子。
* 配置中间件；具体请看 [app.route](http://expressjs.com/4x/api.html#app.route)。
* 渲染 HTML 视图；具体请看 [app.render](http://expressjs.com/4x/api.html#app.render)。
* 注册模板引擎；具体请看 [app.engine](http://expressjs.com/4x/api.html#app.engine)。

它还有一些属性设置，这些属性可以改变程序的行为。获得更多的信息，可以查阅 [Application settings](http://expressjs.com/4x/api.html#app.settings.table)。

### Properties

#### app.locals

`app.locals`对象是一个 javascript 对象，它的属性就是程序本地的变量。

```js
app.locals.title
// => 'My App'
app.locals.email
// => 'me@myapp.com'

```

一旦设定，`app.locals`的各属性值将贯穿程序的整个生命周期，与其相反的是`res.locals`，它只在这次请求的生命周期中有效。

在程序中，你可以在渲染模板时使用这些本地变量。它们是非常有用的，可以为模板提供一些有用的方法，以及`app`级别的数据。通过`req.app.locals`(具体查看 [req.app](http://expressjs.com/4x/api.html#req.app))，Locals 可以在中间件中使用。

```hs
app.locals.title = 'My App';
app.locals.strftime = require('strftime');
app.locals.email = 'me@myapp.com';

```

#### app.mountpath

`app.mountpath`属性是子程序挂载的路径模式。

> 一个子程序是一个`express`的实例，其可以被用来作为路由句柄来处理请求。

```js
var express = require('express');
var app = express(); // the main app
var admin = express(); // the sub app
admin.get('/', function(req, res) {
    console.log(admin.mountpath); // /admin
    res.send('Admin Homepage');
});
app.use('/admin', admin); // mount the sub app

```

它和 req 对象的 [baseUrl](http://expressjs.com/4x/api.html#req.baseUrl) 属性比较相似，除了`req.baseUrl`是匹配的 URL 路径，而不是匹配的模式。如果一个子程序被挂载在多条路径模式，`app.mountpath`就是一个关于挂载路径模式项的列表，如下面例子所示。

```js
var admin = express();
admin.get('/', function(req, res) {
    console.log(admin.mountpath); // ['adm*n', '/manager'] 
    res.send('Admin Homepage');
});
var secret = express();
secret.get('/', function(req, res) {
    console.log(secret.mountpath); // /secr*t
    res.send('Admin secret');
});
admin.use('secr*t', secret); // load the 'secret' router on '/secr*t', on the 'admin' sub app
app.use(['/adm*n', '/manager'], admin); // load the 'admin' router on '/adm*n' and  '/manager' , on the parent app
```

### Events

#### app.on(‘mount’, callback(parent))

当子程序被挂载到父程序时，`mount`事件被发射。父程序对象作为参数，传递给回调方法。

```js
var admin = express();
admin.on('mount', function(parent) {
    console.log('Admin Mounted');
    console.log(parent); // refers to the parent app
});
admin.get('/', function(req, res) {
    res.send('Admin Homepage');
});
app.use('/admin', admin);
```

### Methods

#### `app.all(path, callback[, callback …]`

`app.all`方法和标准的`app.METHOD()`方法相似，除了它匹配所有的 HTTP 动词。 对于给一个特殊前缀映射一个全局的逻辑处理，或者无条件匹配，它是很有效的。例如，如果你把下面内容放在所有其他的路由定义的前面，它要求所有从这个点开始的路由需要认证和自动加载一个用户。记住这些回调并不是一定是终点:`loadUser`可以在完成了一个任务后，调用`next()`方法来继续匹配随后的路由。

```js
app.all('*', requireAuthentication, loadUser);
```

或者这种相等的形式:

```js
    app.all('*', requireAuthentication);
    app.all('*', loadUser);
```

另一个例子是全局的白名单方法。这个例子和前面的很像，然而它只是限制以`/api`开头的路径。

```js
app.all('/api/*', requireAuthentication);
```

#### `app.delete(path, callback[, callback …])`

路由`HTTP DELETE`请求到有特殊回调方法的特殊的路径。获取更多的信息，可以查阅 [routing guide](http://expressjs.com/guide/routing.html)。 你可以提供多个回调函数，它们的行为和中间件一样，除了这些回调可以通过调用`next('router')`来绕过剩余的路由回调。你可以使用这个机制来为一个路由设置一些前提条件，如果不能满足当前路由的处理条件，那么你可以传递控制到随后的路由。

```js
app.delete('/', function(req, res) {
    res.send('DELETE request to homepage');
});
```

#### app.disable(name)

设置类型为布尔的设置名为`name`的值为`false`，此处的`name`是 [app settings table](http://expressjs.com/4x/api.html#app.settings.table) 中各属性的一个。调用`app.set('foo', false)`和调用`app.disable('foo')`是等价的。 比如:

```js
app.disable('trust proxy');
app.get('trust proxy');
// => false
```

#### `app.disabled(name)`

返回`true`如果布尔类型的设置值`name`被禁用为`false`，此处的`name`是 [app settings table](http://expressjs.com/4x/api.html#app.settings.table) 中各属性的一个。

```js
app.disabled('trust proxy');
// => true
app.enable('trust proxy');
app.disabled('trust proxy');
// => false
```

#### app.enable(name)

设置布尔类型的设置值`name`为`true`，此处的`name`是 [app settings table](http://expressjs.com/4x/api.html#app.settings.table) 中各属性的一个。调用`app.set('foo', true)`和调用`app.enable('foo')`是等价的。

```js
app.enable('trust proxy');
app.get('trust proxy');
// => true
```

#### app.enabled(name)

返回`true`如果布尔类型的设置值`name`被启动为`true`，此处的`name`是 [app settings table](http://expressjs.com/4x/api.html#app.settings.table) 中各属性的一个。

```js
app.enabled('trust proxy');
// => false
app.enable('trust proxy');
app.enabled('trust proxy');
// => true
```

#### `app.engine(ext, callback)`

注册给定引擎的回调，用来渲染处理 ext 文件。 默认情况下，Express 需要使用`require()`来加载基于文件扩展的引擎。例如，如果你尝试渲染一个`foo.jade`文件，Express 在内部调用下面的内容，同时缓存`require()`结果供随后的调用，来加速性能。

```js
app.engine('jade', require('jade').__express);
```

使用下面的方法对于那些没有提供开箱即用的`.__express`方法的模板，或者你希望使用不同的模板引擎扩展。 比如，使用 EJS 模板引擎来渲染`.html`文件：

```js
app.engine('html', require('ejs').renderFile);
```

在这个例子中，EJS 提供了一个`.renderFile`方法，这个方法满足了 Express 规定的签名规则：`(path, options, callback)`，然而记住在内部它只是`ejs.__express`的一个别名，所以你可以在不做任何事的情况下直接使用`.ejs`扩展。 一些模板引擎没有遵循这种规范，[consolidate.js](https://github.com/tj/consolidate.js) 库映射模板引擎以下面的使用方式，所以他们可以无缝的和 Express 工作。

```js
var engines = require('consolidate');
app.engine('haml', engines.haml);
app.engine('html', engines.hogan);

```

#### `app.get(name)`

获得设置名为`name`的 app 设置的值，此处的`name`是 [app settings table](http://expressjs.com/4x/api.html#app.settings.table) 中各属性的一个。 如下：

```js
app.get('title');
// => undefined
app.set('title', 'My Site');
app.get('title');
// => 'My Site'
```

#### `app.get(path, callback [, callback …])`

路由`HTTP GET`请求到有特殊回调的特殊路径。获取更多的信息，可以查阅 [routing guide](http://expressjs.com/guide/routing.html)。 你可以提供多个回调函数，它们的行为和中间件一样，除了这些回调可以通过调用`next('router')`来绕过剩余的路由回调。你可以使用这个机制来为一个路由设置一些前提条件，如果请求没能满足当前路由的处理条件，那么传递控制到随后的路由。

```js
app.get('/', function(req, res) {
    res.send('GET request to homepage');
});
```

#### `app.listen(port, [hostname], [backlog], [callback])`

绑定程序监听端口到指定的主机和端口号。这个方法和`Node`中的 [http.Server.listen()](http://nodejs.org/api/http.html#http_server_listen_port_hostname_backlog_callback) 是一样的。

```js
var express = require('express');
var app = express();
app.listen(3000);
```

通过调用`express()`返回得到的`app`实际上是一个 JavaScript 的`Function`，被设计用来作为一个回调传递给`Node HTTP servers`来处理请求。这样，其就可以很简便的基于同一份代码提供 http 和 https 版本，所以 app 没有从这些继承 (它只是一个简单的回调)。

```js
var express = require('express');
var https = require('https');
var http = require('http');
http.createServer(app).listen(80);
https.createServer(options, app).listen(443);
```

`app.listen()`方法是下面所示的一个便利的方法 (只针对 HTTP 协议):

```js
app.listen = function() {
    var server = http.createServer(this);
    return server.listen.apply(server, arguments);
};
```

#### `app.METHOD(path, callback [, callback …])`

路由一个 HTTP 请求，`METHOD`是这个请求的 HTTP 方法，比如`GET`，`PUT`，`POST`等等，注意是小写的。所以，实际的方法是`app.get()`，`app.post()`，`app.put()`等等。下面有关于方法的完整的表。 获取更多信息，请看 [routing guide](http://expressjs.com/guide/routing.html)。 Express 支持下面的路由方法，对应与同名的 HTTP 方法：

![http](./../../../assets/http.png)

> 如果使用上述方法时，导致了无效的 javascript 的变量名，可以使用中括号符号，比如,`app['m-search']('/', function ...`

你可以提供多个回调函数，它们的行为和中间件一样，除了这些回调可以通过调用`next('router')`来绕过剩余的路由回调。你可以使用这个机制来为一个路由设置一些前提条件，如果请求没有满足当前路由的处理条件，那么传递控制到随后的路由。

> 本 API 文档把使用比较多的 HTTP 方法`app.get()`，`app.post`，`app.put()`，`app.delete()`作为一个个单独的项进行说明。然而，其他上述列出的方法以完全相同的方式工作。

`app.all()`是一个特殊的路由方法，它不属于 HTTP 协议中的规定的方法。它为一个路径加载中间件，其对所有的请求方法都有效。

```js
app.all('/secret', function (req, res) {
    console.log('Accessing the secret section...');
    next(); // pass control to the next handler
});
```

#### app.param([name], callback)

给路由参数添加回调触发器，这里的`name`是参数名或者参数数组，`function`是回调方法。回调方法的参数按序是`请求对象`，`响应对象`，`下个中间件`，`参数值`和`参数名`。 如果`name`是数组，会按照各个参数在数组中被声明的顺序将回调触发器注册下来。还有，对于除了最后一个参数的其他参数，在他们的回调中调用`next()`来调用下个声明参数的回调。对于最后一个参数，在回调中调用`next()`将调用位于当前处理路由中的下一个中间件，如果`name`只是一个`string`那就和它是一样的 (就是说只有一个参数，那么就是最后一个参数，和数组中最后一个参数是一样的)。 例如，当`:user`出现在路由路径中，你可以映射用户加载的逻辑处理来自动提供`req.user`给这个路由，或者对输入的参数进行验证。

```js
app.param('user', function(req, res, next, id) {
    User.find(id, function(error, user) {
        if (err) {
            next(err);
        }
        else if (user){
            req.user = user;
        } else {
            next(new Error('failed to load user'));
        }
    });
});
```

对于`Param`的回调定义的路由来说，他们是局部的。它们不会被挂载的 app 或者路由继承。所以，定义在`app`上的`Param`回调只有是在`app`上的路由具有这个路由参数时才起作用。 在定义`param`的路由上，`param`回调都是第一个被调用的，它们在一个请求 - 响应循环中都会被调用一次并且只有一次，即使多个路由都匹配，如下面的例子：

```js
app.param('id', function(req, res, next, id) {
    console.log('CALLED ONLY ONCE');
    next();
});
app.get('/user/:id', function(req, res, next) {
    console.log('although this matches');
    next();
});
app.get('/user/:id', function(req, res) {
    console.log('and this mathces too');
    res.end();
});
```

当`GET /user/42`，得到下面的结果:

```js
    CALLED ONLY ONCE
    although this matches
    and this matches too
```

```js
app.param(['id', 'page'], function(req, res, next, value) {
    console.log('CALLED ONLY ONCE with', value);
    next();
});
app.get('/user/:id/:page', function(req. res, next) {
    console.log('although this matches');
    next();
});
app.get('/user/:id/:page', function (req, res, next) {
    console.log('and this matches too');
    res.end();
});
```

当执行`GET /user/42/3`，结果如下：

```plain
CALLED ONLY ONCE with 42
CALLED ONLY ONCE with 3
although this matches
and this mathes too
```

#### app.path()

通过这个方法可以得到`app`典型的路径，其是一个`string`。

```js
var app = express()
  , blog = express()
  , blogAdmin = express();
app.use('/blog', blog);
app.use('/admin', blogAdmin);
console.log(app.path()); // ''
console.log(blog.path()); // '/blog'
console.log(blogAdmin.path()); // '/blog/admin'
```

如果`app`挂载很复杂下，那么这个方法的行为也会很复杂：一种更好用的方式是使用`req.baseUrl`来获得这个 app 的典型路径。

#### app.post(path, callback, [callback …])

路由`HTTP POST`请求到有特殊回调的特殊路径。获取更多的信息，可以查阅 [routing guide](http://expressjs.com/guide/routing.html)。 你可以提供多个回调函数，它们的行为和中间件一样，除了这些回调可以通过调用`next('router')`来绕过剩余的路由回调。你可以使用这个机制来为一个路由设置一些前提条件，如果请求没能满足当前路由的处理条件，那么传递控制到随后的路由。

```js
app.post('/', function(req, res) {
    res.send('POST request to homepage')
});
```

#### app.put(path, callback, [callback …])

路由`HTTP PUT`请求到有特殊回调的特殊路径。获取更多的信息，可以查阅 [routing guide](http://expressjs.com/guide/routing.html)。 你可以提供多个回调函数，它们的行为和中间件一样，除了这些回调可以通过调用`next('router')`来绕过剩余的路由回调。你可以使用这个机制来为一个路由设置一些前提条件，如果请求没能满足当前路由的处理条件，那么传递控制到随后的路由。

```js
app.put('/', function(req, res) {
    res.send('PUT request to homepage');
});
```

#### app.render(view, [locals], callback)

通过`callback`回调返回一个`view`渲染之后得到的 HTML 文本。它可以接受一个可选的参数，可选参数包含了这个`view`需要用到的本地数据。这个方法类似于`res.render()`，**除了它不能把渲染得到的 HTML 文本发送给客户端**。

> 将`app.render()`当作是可以生成渲染视图字符串的工具方法。在`res.render()`内部，就是使用的`app.render()`来渲染视图。

> 如果使能了视图缓存，那么本地变量缓存就会保留。如果你想在开发的过程中缓存视图，设置它为`true`。在生产环境中，视图缓存默认是打开的。

```js
app.render('email', function(err, html) {
// ...
});
app.render('email', {name:'Tobi'}, function(err, html) {
// ...
});
```

#### app.route(path)

返回一个单例模式的路由的实例，之后你可以在其上施加各种 HTTP 动作的中间件。使用`app.route()`来避免重复路由名字 (例如错字错误)–说的意思应该是使用`app.router()`这个单例方法来避免同一个路径多个路由实例。

```js
var app = express();
app.route('/events')
.all(function(req, res, next) {
  // runs for all HTTP verbs first
  // think of it as route specific middleware!
})
.get(function(req, res, next) {
  res.json(...);
})
.post(function(req, res, next) {
  // maybe add a new event...
})
```

#### app.set(name, value)

给`name`设置项赋`value`值，`name`是 [app settings table](http://expressjs.com/4x/api.html#app.settings.table) 中属性的一项。 对于一个类型是布尔型的属性调用`app.set('foo', ture)`等价于调用`app.enable('foo')`。同样的，调用`app.set('foo', false)`等价于调用`app.disable('foo')`。 可以使用`app.get()`来取得设置的值：

```js
app.set('title', 'My Site');
app.get('title'); // 'My Site'
```

**Application Settings** 如果`name`是程序设置之一，它将影响到程序的行为。下边列出了程序中的设置。

![option](./../../../assets/option.png)

**Options for `trust proxy` settings**

查阅 [Express behind proxies](http://expressjs.com/guide/behind-proxies.html) 来获取更多信息。

```js
app.set('trust proxy', function (ip) {
    if (ip === '127.0.0.1' || ip === '123.123.123.123') return true; 
    else return false;
  })
```

**Options for `etag` settings**

`ETag`功能的实现使用了 [etag](https://www.npmjs.org/package/etag) 包。如果你需要获得更多的信息，你可以查阅它的文档。

#### `app.use([path,], function [, function…])`

挂载[中间件](http://expressjs.com/guide/using-middleware.html)方法到路径上。如果路径未指定，那么默认为 "/"。

> 一个路由将匹配任何路径如果这个路径以这个路由设置路径后紧跟着 "/"。比如：`app.use('/appale', ...)`将匹配 "/apple"，"/apple/images"，"/apple/images/news" 等。

中间件中的`req.originalUrl`是`req.baseUrl`和`req.path`的组合，如下面的例子所示。

```js
app.use('/admin', function(req, res, next) {
  // GET 'http://www.example.com/admin/new'
  console.log(req.originalUrl); // '/admin/new'
  console.log(req.baseUrl); // '/admin'
  console.log(req.path);// '/new'
});

```

在一个路径上挂载一个中间件之后，每当请求的路径的前缀部分匹配了这个路由路径，那么这个中间件就会被执行。 由于默认的路径为`/`，中间件挂载没有指定路径，那么对于每个请求，这个中间件都会被执行。

```js
// this middleware will be executed for every request to the app.
app.use(function(req, res, next) {
    console.log('Time: %d', Date.now());
    next();
});

```

中间件方法是顺序处理的，所以中间件包含的顺序是很重要的。

```js
// this middleware will not allow the request to  go beyond it
app.use(function(req, res, next) {
    res.send('Hello World');
});
// this middleware will never reach this route
app.use('/', function(req, res) {
    res.send('Welcome');
});

```

路径可以是代表路径的一串字符，一个路径模式，一个匹配路径的正则表达式，或者他们的一组集合。

下面是一些例子，在`Express`程序中使用`express.static`中间件。 为程序托管位于程序目录下的`public`目录下的静态资源：

```js
// GET /style.css etc
app.use(express.static(__dirname + '/public'));

```

在`/static`路径下挂载中间件来提供静态资源托管服务，只当请求是以`/static`为前缀的时候。

```js
// GET /static/style.css etc.
app.use('/static', express.static(express.__dirname + '/public'));

```

通过在设置静态资源中间件之后加载日志中间件来关闭静态资源请求的日志。

```js
    app.use(express.static(__dirname + '/public'));
    app.use(logger());

```

托管静态资源从不同的路径，但`./public`路径比其他更容易被匹配：

```js
app.use(express.static(__dirname + '/public'));
app.use(express.static(__dirname + '/files'));
app.use(express.static(__dirname + '/uploads'));

```

## Request

`req`对象代表了一个 HTTP 请求，其具有一些属性来保存请求中的一些数据，比如`query string`，`parameters`，`body`，`HTTP headers`等等。在本文档中，按照惯例，这个对象总是简称为`req`(http 响应简称为`res`)，但是它们实际的名字由这个回调方法在那里使用时的参数决定。 如下例子:

```
    app.get('/user/:id', function(req, res) {
        res.send('user' + req.params.id);
    });

```

其实你也可以这样写：

```js
    app.get('/user/:id', function(request, response) {
        response.send('user' + request.params.id);
    });

```

### Properties

在`Express 4`中，`req.files`默认在`req`对象中不再是可用的。为了通过`req.files`对象来获得上传的文件，你可以使用一个`multipart-handling`(多种处理的工具集) 中间件，比如`busboy`，`multer`，`formidable`，`multipraty`，`connect-multiparty`或者`pez`。

#### req.app

这个属性持有`express`程序实例的一个引用，其可以作为中间件使用。 如果你按照这个模式，你创建一个模块导出一个中间件，这个中间件只在你的主文件中`require()`它，那么这个中间件可以通过`req.app`来获取 express 的实例。 例如:

```js
    // index.js
    app.get("/viewdirectory", require('./mymiddleware.js'));

```

```js
// mymiddleware.js
module.exports = function(req, res) {
    res.send('The views directory is ' + req.app.get('views'));
};

```

#### req.baseUrl

一个路由实例挂载的 Url 路径。

```js
    var greet = express.Router();
    greet.get('/jp', function(req, res) {
        console.log(req.baseUrl); // greet
        res.send('Konichiwa!');
    });
    app.use('/greet', greet);

```

即使你使用的路径模式或者一系列路径模式来加载路由，`baseUrl`属性返回匹配的字符串，而不是路由模式。下面的例子，`greet`路由被加载在两个路径模式上。

```js
app.use(['/gre+t', 'hel{2}o'], greet); // load the on router on '/gre+t' and '/hel{2}o'
```

当一个请求路径是`/greet/jp`，`baseUrl`是`/greet`，当一个请求路径是`/hello/jp`，`req.baseUrl`是`/hello`。 `req.baseUrl`和`app`对象的 [mountpath](http://expressjs.com/4x/api.html#app.mountpath) 属性相似，除了`app.mountpath`返回的是路径匹配模式。

#### req.body

在请求的 body 中保存的是提交的一对对键值数据。默认情况下，它是`undefined`，当你使用比如`body-parser`和`multer`这类解析`body`数据的中间件时，它是填充的。 下面的例子，给你展示了怎么使用`body-parser`中间件来填充`req.body`。

```js
    var app = require('express');
    var bodyParser = require('body-parser');
    var multer = require('multer')；// v1.0.5
    var upload = multer(); // for parsing multipart/form-data
    app.use(bodyParser.json()); // for parsing application/json
    app.use(bodyParser.urlencoded({extended:true})); // for parsing application/x-www-form-urlencoded
    app.post('/profile', upload.array(), function(req, res, next) {
        console.log(req.body);
        res.json(req.body);
    });

```

#### req.cookies

当使用`cookie-parser`中间件的时候，这个属性是一个对象，其包含了请求发送过来的`cookies`。如果请求没有带`cookies`，那么其值为`{}`。

```js
    // Cookie: name=tj
    req.cookies.name
    // => "tj"

```

获取更多信息，问题，或者关注，可以查阅 [cookie-parser](https://github.com/expressjs/cookie-parser)。

#### req.fresh

指示这个请求是否是新鲜的。其和`req.stale`是相反的。 当`cache-control`请求头没有`no-cache`指示和下面中的任一一个条件为`true`，那么其就为`true`：

* `if-modified-since`请求头被指定，和`last-modified`请求头等于或者早于`modified`响应头。
* `if-none-match`请求头是`*`。
* `if-none-match`请求头在被解析进它的指令之后，和`etag`响应头的值不相等

> ps:If-None-Match 作用: If-None-Match 和 ETag 一起工作，工作原理是在 HTTP Response 中添加 ETag 信息。 当用户再次请求该资源时，将在 HTTP Request 中加入 If-None-Match 信息 (ETag 的值)。如果服务器验证资源的 ETag 没有改变（该资源没有更新），将返回一个 304 状态告诉客户端使用本地缓存文件。否则将返回 200 状态和新的资源和 Etag. 使用这样的机制将提高网站的性能

#### req.hostname

包含了源自`Host`HTTP 头部的`hostname`。 当`trust proxy`设置项被设置为启用值，`X-Forwarded-Host`头部被使用来代替`Host`。这个头部可以被客户端或者代理设置。

```js
// Host: "example.com"
req.hostname
// => "example.com"

```

#### req.ips

当`trust proxy`设置项被设置为启用值，这个属性包含了一组在`X-Forwarded-For`请求头中指定的 IP 地址。不然，其就包含一个空的数组。这个头部可以被客户端或者代理设置。 例如，如果`X-Forwarded-For`是`client`，`proxy1`，`proxy2`，`req.ips`就是`["clinet", "proxy1", "proxy2"]`，这里`proxy2`就是最远的下游。

#### req.originalUrl

> `req.url`不是一个原生的`Express`属性，它继承自 [Node’s http module](https://nodejs.org/api/http.html#http_message_url)。

这个属性很像`req.url`；然而，其保留了原版的请求链接，允许你自由地重定向`req.url`到内部路由。比如，`app.use()`的`mounting`特点可以重定向`req.url`跳转到挂载点。

```js
// GET /search?q=something
req.originalUrl
// => "/search?q=something"

```

#### req.params

一个对象，其包含了一系列的属性，这些属性和在路由中命名的参数名是一一对应的。例如，如果你有`/user/:name`路由，`name`属性可作为`req.params.name`。这个对象默认值为`{}`。

```js
// GET /user/tj
req.params.name
// => "tj"

```

当你使用正则表达式来定义路由规则，捕获组的组合一般使用`req.params[n]`，这里的`n`是第几个捕获租。这个规则被施加在无名通配符匹配，比如`/file/*`的路由：

```js
// GET /file/javascripts/jquery.js
req.params[0]
// => "javascripts/jquery.js"

```

#### req.path

包含请求 URL 的部分路径。

```js
// example.com/users?sort=desc
req.path
// => "/users"

```

> 当在一个中间件中被调用，挂载点不包含在`req.path`中。你可以查阅 [app.use()](http://expressjs.com/4x/api.html#app.use) 获得跟多的信息。

#### req.protocol

请求的协议，一般为`http`，当启用 TLS 加密，则为`https`。 当`trust proxy`设置一个启用的参数，如果存在`X-Forwarded-Proto`头部的话，其将被信赖和使用。这个头部可以被客户端或者代理设置。

```js
req.ptotocol
// => "http"

```

#### req.query

一个对象，为每一个路由中的`query string`参数都分配一个属性。如果没有`query string`，它就是一个空对象，`{}`。

```js
// GET /search?q=tobi+ferret
req.query.q
// => "tobi ferret"
// GET /shoes?order=desc&shoe[color]=blue&shoe[type]=converse
req.query.order
// => "desc"
req.query.shoe.color
// => "blue"
req.query.shoe.type
// => "converse"

```

#### req.route

当前匹配的路由，其为一串字符。比如：

```js
app.get('/user/:id?', function userIdHandler(req, res) {
    console.log(req.route);
    res.send('GET')
})

```

前面片段的输出为:

```js
{ path:"/user/:id?"
    stack:
    [
        { handle:[Function:userIdHandler],
          name:"userIdHandler",
          params:undefined,
          path:undefined,
          keys:[],
          regexp:/^\/?$/i,
          method:'get'
        }
    ]
    methods:{get:true}
}

```

#### req.secure

一个布尔值，如果建立的是 TLS 的连接，那么就为`true`。等价与：

```js
    'https' == req.protocol;
```

#### req.signedCookies

当使用`cookie-parser`中间件的时候，这个属性包含的是请求发过来的签名`cookies`，这个属性取得的是不含签名，可以直接使用的值。签名的`cookies`保存在不同的对象中来体现开发者的意图；不然，一个恶意攻击可以被施加在`req.cookie`值上 (它是很容易被欺骗的)。记住，签名一个`cookie`不是把它藏起来或者加密；而是简单的防止篡改 (因为签名使用的加密是私人的)。如果没有发送签名的`cookie`，那么这个属性默认为`{}`。

```js
// Cookie： user=tobi.CP7AWaXDfAKIRfH49dQzKJx7sKzzSoPq7/AcBBRVwlI3
req.signedCookies.user
// => "tobi"

```

为了获取更多的信息，问题或者关注，可以参阅 [cookie-parser](https://github.com/expressjs/cookie-parser)。

#### req.stale

指示这个请求是否是`stale`(陈旧的)，它与`req.fresh`是相反的。更多信息，可以查看 [req.fresh](http://expressjs.com/4x/api.html#req.fresh)。

```js
req.stale
// => true

```

#### req.subdomains

请求中域名的子域名数组。

```js
// Host: "tobi.ferrets.example.com"
req.subdomains
// => ["ferrets", "tobi"]

```

#### req.xhr

一个布尔值，如果`X-Requested-With`的值为`XMLHttpRequest`，那么其为`true`，其指示这个请求是被一个客服端库发送，比如`jQuery`。

```js
req.xhr
// => true

```

### Methods

#### req.accepts(types)

检查这个指定的内容类型是否被接受，基于请求的`Accept` HTTP 头部。这个方法返回最佳匹配，如果没有一个匹配，那么其返回`undefined`(在这个 case 下，服务器端应该返回 406 和 "Not Acceptable")。 `type`值可以是一个单的`MIME type`字符串 (比如`application/json`)，一个扩展名比如`json`，一个逗号分隔的列表，或者一个数组。对于一个列表或者数组，这个方法返回最佳项 (如果有的话)。

```js
// Accept: text/html
req.accepts('html');
// => "html"
// Accept: text/*, application/json
req.accepts('html');
// => "html"
req.accepts('text/html');
// => "text/html"
req.accepts(['json', 'text']);
// => "json"
req.accepts('application/json');
// => "application/json"
// Accept: text/*, application/json
req.accepts('image/png');
req.accepts('png');
// => undefined
// Accept: text/*;q=.5, application/json
req.accepts(['html', 'json']);
// => "json"

```

获取更多信息，或者如果你有问题或关注，可以参阅 [accepts](https://github.com/expressjs/accepts)。

#### req.acceptsCharsets(charset[, …])

返回指定的字符集集合中第一个的配置的字符集，基于请求的`Accept-Charset`HTTP 头。如果指定的字符集没有匹配的，那么就返回 false。 获取更多信息，或者如果你有问题或关注，可以参阅 [accepts](https://github.com/expressjs/accepts)。

#### req.acceptsEncodings(encoding[, …])

返回指定的编码集合中第一个的配置的编码，基于请求的`Accept-Encoding`HTTP 头。如果指定的编码集没有匹配的，那么就返回 false。 获取更多信息，或者如果你有问题或关注，可以参阅 [accepts](https://github.com/expressjs/accepts)。

#### req.acceptsLanguages(lang [, …])

返回指定的语言集合中第一个的配置的语言，基于请求的`Accept-Language`HTTP 头。如果指定的语言集没有匹配的，那么就返回 false。 获取更多信息，或者如果你有问题或关注，可以参阅 [accepts](https://github.com/expressjs/accepts)。

#### req.get(field)

返回指定的请求 HTTP 头部的域内容 (不区分大小写)。`Referrer`和`Referer`的域内容可互换。

```
req.get('Content-type');
// => "text/plain"
req.get('content-type');
// => "text/plain"
req.get('Something')
// => undefined

```

其是`req.header(field)`的别名。

#### req.is(type)

如果进来的请求的`Content-type`头部域匹配参数`type`给定的`MIME type`，那么其返回`true`。否则返回`false`。

```
// With Content-Type: text/html; charset=utf-8
req.is('html');
req.is('text/html');
req.is('text/*');
// => true
// When Content-Type is application/json
req.is('json');
req.is('application/json');
req.is('application/*');
// => true
req.is('html');
// => false

```

获取更多信息，或者如果你有问题或关注，可以参阅 [type-is](https://github.com/expressjs/type-is)。

#### req.param(naem, [, defaultValue])

> 过时的。可以在适合的情况下，使用`req.params`，`req.body`或者`req.query`。

返回当前参数`name`的值。

```js
// ?name=tobi
req.param('name')
// => "tobi"
// POST name=tobi
req.param('name')
// => "tobi"
// /user/tobi for /user/:name
req.param('name')
// => "tobi"

```

按下面给出的顺序查找：

* req.params
* req.body
* req.query

可选的，你可以指定一个`defaultValue`来设置一个默认值，如果这个参数在任何一个请求的对象中都不能找到。

> 直接通过`req.params`，`req.body`，`req.query`取得应该更加的清晰 - 除非你确定每一个对象的输入。 `Body-parser`中间件必须加载，如果你使用`req.param()`。详细请看 [req.body](http://expressjs.com/4x/api.html#req.body)。

## Response

`res`对象代表了当一个 HTTP 请求到来时，`Express`程序返回的 HTTP 响应。在本文档中，按照惯例，这个对象总是简称为`res`(http 请求简称为`req`)，但是它们实际的名字由这个回调方法在那里使用时的参数决定。 例如：

```
app.get('/user/:id', function(req, res) {
    res.send('user' + req.params.id);
});

```

这样写也是一样的：

```
app.get('/user/:id', function(request, response) {
    response.send('user' + request.params.id);
});

```

### Properties

#### res.app

这个属性持有`express`程序实例的一个引用，其可以在中间件中使用。 `res.app`和请求对象中的`req.app`属性是相同的。

布尔类型的属性，指示这个响应是否已经发送 HTTP 头部。

```js
app.get('/', function(req, res) {
    console.log(res.headersSent); // false
    res.send('OK'); // send之后就发送了头部
    console.log(res.headersSent); // true
});

```

#### res.locals

一个对象，其包含了本次请求的响应中的变量和因此它的变量只提供给本次请求响应的周期内视图渲染里使用 (如果有视图的话)。 其他方面，其和`app.locals`是一样的。 这个参数在导出请求级别的信息是很有效的，这些信息比如请求路径，已认证的用户，用户设置等等。

```
app.use(function(req, res, next) {
    res.locals.user = req.user;
    res.locals.authenticated = !req.user.anonymous;
    next();
});

```

### Methods

#### res.append(field [, value])

> res.append() 方法在`Expresxs`4.11.0 以上版本才支持。

在指定的`field`的 HTTP 头部追加特殊的值`value`。如果这个头部没有被设置，那么将用`value`新建这个头部。`value`可以是一个字符串或者数组。 注意：在`res.append()`之后调用`app.set()`函数将重置前面设置的值。

```
res.append('Lind', ['<http://localhost>', '<http://localhost:3000>']);
res.append('Set-Cookie', 'foo=bar;Path=/;HttpOnly');
res.append('Warning', '199 Miscellaneous warning');

```

#### res.attachment([filename])

设置 HTTP 响应的`Content-Disposition`头内容为 "attachment"。如果提供了`filename`，那么将通过`res.type()`获得扩展名来设置`Content-Type`，并且设置`Content-Disposition`内容为 "filename=“parameter。

```
res.attachment();
// Content-Disposition: attachment
res.attachment('path/to/logo.png');
// Content-Disposition: attachment; file
// Content-Type: image/png

```

#### res.cookie(name, value [,options])

设置`name`和`value`的`cookie`，`value`参数可以是一串字符或者是转化为 json 字符串的对象。
> res.cookie() 所作的都是基于提供的`options`参数来设置`Set-Cookie`头部。没有指定任何的`options`，那么默认值在`RFC6265`中指定。

使用实例：

```
res.cookie('name', 'tobi', {'domain':'.example.com', 'path':'/admin', 'secure':true});
res.cookie('remenberme', '1', {'expires':new Date(Date.now() + 90000), 'httpOnly':true});

```

`maxAge`是一个方便设置过期时间的方便的选项，其以当前时间开始的毫秒数来计算。下面的示例和上面的第二条功效一样。

```
res.cookie('rememberme', '1', {'maxAge':90000}, "httpOnly":true);
```

你可以设置传递一个对象作为`value`的参数。然后其将被序列化为 Json 字符串，被`bodyParser()`中间件解析。

```
res.cookie('cart', {'items':[1, 2, 3]});
res.cookie('cart', {'items':[1, 2, 3]}, {'maxAge':90000});

```

当我们使用`cookie-parser`中间件的时候，这个方法也支持签名的 cookie。简单地，在设置`options`时包含`signed`选项为`true`。然后`res.cookie()`将使用传递给`cookieParser(secret)`的密钥来签名这个值。

```
res.cookie('name', 'tobi', {'signed':true});
```

#### res.clearCookie(name [,options])

根据指定的`name`清除对应的 cookie。更多关于`options`对象可以查阅`res.cookie()`。

```
res.cookie('name', 'tobi', {'path':'/admin'});
res.clearCookie('name', {'path':'admin'});

```

#### res.download(path, [,filename], [,fn])

传输`path`指定文件作为一个附件。通常，浏览器提示用户下载。默认情况下，`Content-Disposition`头部 "filename=“的参数为`path`(通常会出现在浏览器的对话框中)。通过指定`filename`参数来覆盖默认值。 当一个错误发生时或者传输完成，这个方法将调用`fn`指定的回调方法。这个方法使用`res.sendFile()`来传输文件。

```js
res.download('/report-12345.pdf');
res.download('/report-12345.pdf', 'report.pdf');
res.download('report-12345.pdf', 'report.pdf', function(err) {
// Handle error, but keep in mind the response may be partially-sent
// so check res.headersSent
if (err) {
} else {
// decrement a download credit, etc.
}
});

```

#### res.end([data] [, encoding])

结束本响应的过程。这个方法实际上来自`Node`核心模块，具体的是 [response.end() method of http.ServerResponse](https://nodejs.org/api/http.html#http_response_end_data_encoding_callback)。 用来快速结束请求，没有任何的数据。如果你需要发送数据，可以使用 [res.send()](http://expressjs.com/4x/api.html#res.send) 和 [res.json()](http://expressjs.com/4x/api.html#res.json) 这类的方法。

```
res.end();
res.status(404).end();

```

#### res.format(object)

进行内容协商，根据请求的对象中`Accept`HTTP 头部指定的接受内容。它使用 [req.accepts()](http://expressjs.com/4x/api.html#res.accepts) 来选择一个句柄来为请求服务，这些句柄按质量值进行排序。如果这个头部没有指定，那么第一个方法默认被调用。当不匹配时，服务器将返回`406`“Not Acceptable”，或者调用`default`回调。 `Content-Type`请求头被设置，当一个回调方法被选择。然而你可以改变他，在这个方法中使用这些方法，比如`res.set()`或者`res.type()`。 下面的例子，将回复`{"message":"hey"}`，当请求的对象中`Accept`头部设置成 "application/json" 或者”*/json”(不过如果是`*/*`，然后这个回复就是 "hey")。

```js
res.format({
    'text/plain':function() {
        res.send('hey');
    },
    'text/html':function() {
        res.send('<p>hey</p>');
    },
    'application/json':function() {
        res.send({message:'hey'});
    },
    'default':function() {
        res.status(406).send('Not Acceptable');
    }
})

```

除了规范化的 MIME 类型之外，你也可以使用拓展名来映射这些类型来避免冗长的实现：

```js
res.format({
    text:function() {
        res.send('hey');
    },
    html:function() {
        res.send('<p>hey</p>');
    },
    json:function() {
        res.send({message:'hey'});
    }
})

```

#### res.get(field)

返回`field`指定的 HTTP 响应的头部。匹配是区分大小写。

```js
res.get('Content-Type');
// => "text/plain"

```

#### res.json([body])

发送一个 json 的响应。这个方法和将一个对象或者一个数组作为参数传递给`res.send()`方法的效果相同。不过，你可以使用这个方法来转换其他的值到 json，例如`null`，`undefined`。(虽然这些都是技术上无效的 JSON)。

```
res.json(null);
res.json({user:'tobi'});
res.status(500).json({error:'message'});

```

#### res.jsonp([body])

发送一个 json 的响应，并且支持 JSONP。这个方法和`res.json()`效果相同，除了其在选项中支持 JSONP 回调。

```
res.jsonp(null)
// => null
res.jsonp({user:'tobi'})
// => {"user" : "tobi"}
res.status(500).jsonp({error:'message'})
 // => {"error" : "message"}

```

默认情况下，jsonp 的回调方法简单写作`callback`。可以通过 [jsonp callback name](http://expressjs.com/4x/api.html#app.settings.table) 设置来重写它。 下面是一些例子使用 JSONP 响应，使用相同的代码:

```
// ?callback=foo
res.jsonp({user:'tobo'})
// => foo({"user":"tobi"})
app.set('jsonp callback name', 'cb')
// ?cb=foo
res.status(500).jsonp({error:'message'})
// => foo({"error":"message"})

```

#### res.links(links)

连接这些`links`，`links`是以传入参数的属性形式提供，连接之后的内容用来填充响应的 Link HTTP 头部。

```
res.links({
    next:'http://api.example.com/users?page=2',
    last:'http://api.example.com/user?page=5'
});

```

效果：

```
Link:<http://api.example.com/users?page=2>;rel="next",
<http://api.example.com/users?page=5>;rel="last"

```

#### res.location(path)

设置响应的`Location`HTTP 头部为指定的`path`参数。

```
    res.location('/foo/bar');
    res.location('http://example.com');
    res.location('back');

```

当`path`参数为`back`时，其具有特殊的意义，其指定 URL 为请求对象的`Referer`头部指定的 URL。如果请求中没有指定，那么其即为 "/"。

> Express 传递指定的 URL 字符串作为回复给浏览器响应中的`Location`头部的值，不检测和操作，除了`back`这个参数。浏览器会将用户重定向到`location`设置的 url 或者`Referer`的 url（`back`参数的情况）

#### res.redirect([status,] path)

重定向来源于指定`path`的 URL，以及指定的 [HTTP status code](http://www.w3.org/Protocols/rfc2616/rfc2616-sec10.html)`status`。如果你没有指定`status`，status code 默认为 "302 Found"。

```
res.redirect('/foo/bar');
res.redirect('http://example.com');
res.redirect(301, 'http://example.com');
res.redirect('../login');

```

重定向也可以是完整的 URL，来重定向到不同的站点。

```
res.redirect('http://google.com');
```

重定向也可以相对于主机的根路径。比如，如果程序的路径为`http://example.com/admin/post/new`，那么下面将重定向到`http://example.com/admim`:

```
res.redirect('/admin');
```

重定向也可以相对于当前的 URL。比如，来之于`http://example.com/blog/admin/`(注意结尾的`/`)，下面将重定向到`http://example.com/blog/admin/post/new`。

```
res.redirect('post/new');
```

如果来至于`http://example.com/blog/admin`（没有尾部`/`），重定向`post/new`，将重定向到`http://example.com/blog/post/new`。如果你觉得上面很混乱，可以把路径段认为目录 (有'/') 或者文件，这样是可以的。相对路径的重定向也是可以的。如果你当前的路径为`http://example.com/admin/post/new`，下面的操作将重定向到`http://example.com/admin/post`：

```
res.redirect('..');
```

`back`将重定向请求到 [referer](http://en.wikipedia.org/wiki/HTTP_referer)，当没有`referer`的时候，默认为`/`。

```
res.redirect('back');
```

#### res.render(view [, locals] [, callback])

渲染一个视图，然后将渲染得到的 HTML 文档发送给客户端。可选的参数为:

* `locals`，定义了视图本地参数属性的一个对象。
* `callback`，一个回调方法。如果提供了这个参数，`render`方法将返回错误和渲染之后的模板，并且不自动发送响应。当有错误发生时，可以在这个回调内部，调用`next(err)`方法。

> 本地变量缓存使能视图缓存。在开发环境中缓存视图，需要手动设置为 true；视图缓存在生产环境中默认开启。

```
// send the rendered view to the client
res.render('index');
// if a callback is specified, the render HTML string has to be sent explicitly
res.render('index', function(err, html) {
    res.send(html);
});
// pass a local variable to  the view
res.render('user', {name:'Tobi'}, function(err, html) {
    // ...
});

```

#### res.send([body])

发送 HTTP 响应。 `body`参数可以是一个`Buffer`对象，一个字符串，一个对象，或者一个数组。比如：

```
res.send(new Buffer('whoop'));
res.send({some:'json'});
res.send('<p>some html</p>');
res.status(404).send('Sorry, we cannot find that!');
res.status(500).send({ error: 'something blew up' });

```

对于一般的非流请求，这个方法可以执行许多有用的的任务：比如，它自动给`Content-Length`HTTP 响应头赋值 (除非先前定义)，也支持自动的 HEAD 和 HTTP 缓存更新。 当参数是一个`Buffer`对象，这个方法设置`Content-Type`响应头为`application/octet-stream`，除非事先提供，如下所示:

```
res.set('Content-Type', 'text/html');
res.send(new Buffer('<p>some html</p>'));

```

当参数是一个字符串，这个方法设置`Content-Type`响应头为`text/html`：

```
res.send('<p>some html</p>');
```

当参数是一个对象或者数组，Express 使用 JSON 格式来表示：

```
res.send({user:'tobi'});
res.send([1, 2, 3]);

```

#### res.sendFile(path [, options] [, fn])

> `res.sendFile()`从`Express v4.8.0`开始支持。

传输`path`指定的文件。根据文件的扩展名设置`Content-Type`HTTP 头部。除非在`options`中有关于`root`的设置，`path`一定是关于文件的绝对路径。

```
app.get('/file/:name', function(req, res, next) {
    var options = {
        root:__dirname + '/public',
        dotfile:'deny',
        headers:{
            'x-timestamp':Date.now(),
            'x-sent':true
        }
    };
    var fileName = req.params.name;
    res.sendFile(fileName, options, function(err) {
        if (err) {
            console.log(err);
            res.status(err.status).end();
        }
        else {
            console.log('sent', fileName);
        }
    });
});

```

`res.sendFile`提供了文件服务的细粒度支持，如下例子说明：

```
app.get('/user/:uid/photos/:file', function(req, res) {
    var uid = req.params.uid
        , file = req.params.file;
    req.user.mayViewFilesFrom(uid, function(yes) {
        if (yes) {
            res.sendFile('/upload/' + uid + '/' + file);
        }
        else {
            res.status(403).send('Sorry! you cant see that.');
        }
    });
})

```

获取更多信息，或者你有问题或者关注，可以查阅 [send](https://github.com/pillarjs/send)。

#### res.sendStatus(statusCode)

设置响应对象的`HTTP status code`为`statusCode`并且发送`statusCode`的相应的字符串形式作为响应的 Body。

```
    res.sendStatus(200); // equivalent to res.status(200).send('OK');
    res.sendStatus(403); // equivalent to res.status(403).send('Forbidden');
    res.sendStatus(404); // equivalent to res.status(404).send('Not Found');
    res.sendStatus(500); // equivalent to res.status(500).send('Internal Server Error')

```

如果一个不支持的状态被指定，这个 HTTP status 依然被设置为`statusCode`并且用这个 code 的字符串作为 Body。

```
res.sendStatus(2000); // equivalent to res.status(2000).send('2000');
```

[More about HTTP Status Codes](http://en.wikipedia.org/wiki/List_of_HTTP_status_codes)

#### res.set(field [, value])

设置响应对象的 HTTP 头部`field`为`value`。为了一次设置多个值，那么可以传递一个对象为参数。

```
res.set('Content-Type', 'text/plain');
res.set({
    'Content-Type':'text/plain',
    'Content-Length':'123',
    'ETag':'123456'
})

```

其和`res.header(field [,value])`效果一致。

#### res.status(code)

使用这个方法来设置响应对象的 HTTP status。其是 Node 中 [response.statusCode](http://nodejs.org/api/http.html#http_response_statuscode) 的一个连贯性的别名。

```
res.status(403).end();
res.status(400).send('Bad Request');
res.status(404).sendFile('/absolute/path/to/404.png');

```

#### res.type(type)

程序将设置`Content-Type`HTTP 头部的 MIME type，如果这个设置的`type`能够被 [mime.lookup](https://github.com/broofa/node-mime) 解析成正确的`Content-Type`。如果`type`中包含了`/`字符，那么程序会直接设置`Content-Type`为`type`。

```
res.type('.html');              // => 'text/html'
res.type('html');               // => 'text/html'
res.type('json');               // => 'application/json'
res.type('application/json');   // => 'application/json'
res.type('png');                // => image/png:

```

#### res.vary(field)

在没有 Vary 应答头部时增加 Vary 应答头部。

> ps：vary 的意义在于告诉代理服务器 / 缓存 / CDN，如何判断请求是否一样，vary 中的组合就是服务器 / 缓存 / CDN 判断的依据，比如 Vary 中有 User-Agent，那么即使相同的请求，如果用户使用 IE 打开了一个页面，再用 Firefox 打开这个页面的时候，CDN / 代理会认为是不同的页面，如果 Vary 中没有 User-Agent，那么 CDN / 代理会认为是相同的页面，直接给用户返回缓存的页面，而不会再去 web 服务器请求相应的页面。通俗的说就相当于`field`作为了一个缓存的 key 来判断是否命中缓存

```
res.vary('User-Agent').render('docs');
```

Router
------

一个`router`对象是一个单独的实例关于中间件和路由。你可以认为其是一个 "mini-application"（迷你程序），其具有操作中间件和路由方法的能力。每个`Express`程序有一个内建的 app 路由。 路由自身表现为一个中间件，所以你可以使用它作为`app.use()`方法的一个参数或者作为另一个路由的`use()`的参数。 顶层的`express`对象有一个`Router()`方法，你可以使用`Router()`来创建一个新的`router`对象。

### Router([options])

如下，可以创建一个路由：

```
var router = express.Router([options]);
```

你可以将`router`当作一个程序，可以在其上添加中间件和 HTTP 路由方法 (例如`get`，`put`，`post`等等)。

```
// invoked for any requests passed to this router
router.use(function(req, res, next) {
  // .. some logic here .. like any other middleware
  next();
});
// will handle any request that ends in /events
// depends on where the router is "use()'d"
router.get('/events', function(req, res, next) {
  // ..
});

```

你可以在一个特别的根 URL 上挂载一个路由，这样你就以将你的各个路由放到不同的文件中或者甚至是 mini 的程序。

```
// only requests to /calendar/* will be sent to our "router"
app.use('/calendar', router);

```

### Methods

#### router.all(path, [callback, …] callback)

这个方法和`router.METHOD()`方法一样，除了这个方法会匹配所有的 HTTP 动作。 这个方法对想映射全局的逻辑处理到特殊的路径前缀或者任意匹配是十分有用的。比如，如果你放置下面所示的这个路由在其他路由的前面，那么其将要求从这个点开始的所有的路由进行验证操作和自动加载用户信息。记住，这些全局的逻辑操作，不需要结束请求响应周期：`loaduser`可以执行一个任务，然后调用`next()`来将执行流程移交到随后的路由。

```
router.all('*', requireAuthentication, loadUser);
```

相等的形式:

```
router.all('*', requireAuthentication)
router.all('*', loadUser);

```

这是一个白名单全局功能的例子。这个例子很像前面的，不过其仅仅作用于以`/api`开头的路径:

```
router.all('/api/*', requireAuthentication);
```

#### router.METHOD(path, [callback, …] callback)

`router.METHOD()`方法提供了路由方法在`Express`中，这里的`METHOD`是 HTTP 方法中的一个，比如`GET`，`PUT`，`POST`等等，但`router`中的 METHOD 是小写的。所以，实际的方法是`router.get()`，`router.put()`，`router.post()`等等。 你可以提供多个回调函数，它们的行为和中间件一样，除了这些回调可以通过调用`next('router')`来绕过剩余的路由回调。你可以使用这个机制来为一个路由设置一些前提条件，如果请求没有满足当前路由的处理条件，那么传递控制到随后的路由。 下面的片段可能说明了最简单的路由定义。Experss 转换 path 字符串为正则表达式，用于内部匹配传入的请求。在匹配的时候，是不考虑`Query strings`，例如，“GET /“将匹配下面的路由，“GET /? 也是一样的。

```
router.get('/', function(req, res) {
    res.send('Hello World');
});

```

如果你对匹配的 path 有特殊的限制，你可以使用正则表达式，例如，下面的可以匹配 "GET /commits/71dbb9c" 和 "GET /commits/71bb92..4c084f9”。

```
router.get(/^\/commits\/(\w+)(?:\.\.(\w+))?$/, function(req, res) {
    var from = req.params[0];
    var to = req.params[1];
    res.send('commit range ' + from + '..' + to);
});

```

#### router.param(name, callback)

给路由参数添加回调触发器，这里的`name`是参数名，`function`是回调方法。回调方法的参数按序是请求对象，响应对象，下个中间件，参数值和参数名。虽然`name`在技术上是可选的，但是自 Express V4.11.0 之后版本不推荐使用 (见下面)。

> 不像`app.param()`，`router.param()`不接受一个数组作为路由参数。

例如，当`:user`出现在路由路径中，你可以映射用户加载的逻辑处理来自动提供`req.user`给这个路由，或者对输入的参数进行验证。

```
    router.param('user', function(req, res, next, id) {
        User.find(id, function(error, user) {
            if (err) {
                next(err);
            }
            else if (user){
                req.user = user;
            } else {
                next(new Error('failed to load user'));
            }
        });
    });

```

对于`Param`的回调定义的路由来说，他们是局部的。它们不会被挂载的 app 或者路由继承。所以，定义在`router`上的`param`回调只有是在`router`上的路由具有这个路由参数时才起作用。 在定义`param`的路由上，`param`回调都是第一个被调用的，它们在一个请求 - 响应循环中都会被调用一次并且只有一次，即使多个路由都匹配，如下面的例子：

```
router.param('id', function(req, res, next, id) {
    console.log('CALLED ONLY ONCE');
    next();
});
router.get('/user/:id', function(req, res, next) {
    console.log('although this matches');
    next();
});
router.get('/user/:id', function(req, res) {
    console.log('and this mathces too');
    res.end();
});

```

当`GET /user/42`，得到下面的结果:

```
    CALLED ONLY ONCE
    although this matches
    and this matches too

```

`

> 下面章节描述的`router.param(callback)`在 v4.11.0 之后被弃用。

通过只传递一个回调参数给`router.param(name, callback)`方法，`router.param(naem, callback)`方法的行为将被完全改变。这个回调参数是关于`router.param(name, callback)`该具有怎样的行为的一个自定义方法，这个方法必须接受两个参数并且返回一个中间件。 这个回调的第一个参数就是需要捕获的 url 的参数名，第二个参数可以是任一的 JavaScript 对象，其可能在实现返回一个中间件时被使用。 这个回调方法返回的中间件决定了当 URL 中包含这个参数时所采取的行为。 在下面的例子中，`router.param(name, callback)`参数签名被修改成了`router.param(name, accessId)`。替换接受一个参数名和回调，`router.param()`现在接受一个参数名和一个数字。

```
var express = require('express');
var app = express();
var router = express.Router();
router.param(function(param, option){
    return function(req, res, next, val) {
        if (val == option) {
            next();
        }
        else {
            res.sendStatus(403);
        }
    }
});
router.param('id', 1337);
router.get('/user/:id', function(req, res) {
    res.send('Ok');
});
app.use(router);
app.listen(3000, function() {
    console.log('Ready');
}); 

```

在这个例子中，`router.param(name. callback)`参数签名保持和原来一样，但是替换成了一个中间件，定义了一个自定义的数据类型检测方法来检测`user id`的类型正确性。

```
router.param(function(param, validator) {
    return function(req, res, next, val) {
        if (validator(val)) {
            next();
        }
        else {
            res.sendStatus(403);
        }
    }
});
router.param('id', function(candidate) {
    return !isNaN(parseFloat(candidate)) && isFinite(candidate);
});

```

#### router.route(path)

返回一个单例模式的路由的实例，之后你可以在其上施加各种 HTTP 动作的中间件。使用`router.route()`来避免重复路由名字 (例如错字错误)–说的意思应该是使用`router.route()`这个单例方法来避免同一个路径多个路由实例。

构建在上面的`router.param()`例子之上，下面的代码展示了怎么使用`router.route()`来指定各种 HTTP 方法的处理句柄。

```
var router = express.Router();
router.param('user_id', function(req, res, next, id) {
    // sample user, would actually fetch from DB, etc...
    req.user = {
        id:id,
        name:"TJ"
    };
    next();
});
router.route('/users/:user_id')
    .all(function(req, res, next) {
        // runs for all HTTP verbs first
        // think of it as route specific middleware!
        next();
    })
    .get(function(req, res, next) {
        res.json(req.user);
    })
    .put(function(req, res, next) {
        // just an example of maybe updating the user
        req.user.name = req.params.name;
        // save user ... etc
        res.json(req.user);
    })
    .post(function(req, res, next) {
        next(new Error('not implemented'));
    })
    .delete(function(req, res, next) {
        next(new Error('not implemented'));
    })

```

这种方法重复使用单个`/usrs/:user_id`路径来添加了各种的 HTTP 方法。

#### router.use([path], [function, …] function)

给可选的`path`参数指定的路径挂载给定的中间件方法，未指定`path`参数，默认值为`/`。 这个方法类似于`app.use()`。一个简单的例子和用例在下面描述。查阅 [app.use()](http://expressjs.com/en/4x/api.html#app.use) 获得更多的信息。 中间件就像一个水暖管道，请求在你定义的第一个中间件处开始，顺着中间件堆栈一路往下，如果路径匹配则处理这个请求。

```
var express = require('express');
var app = express();
var router = express.Router();
// simple logger for this router`s requests
// all requests to this router will first hit this middleware
router.use(function(req, res, next) {
    console.log('%s %s %s', req.method, req.url, req.path);
    next();
})
// this will only be invoked if the path starts with /bar form the mount ponit
router.use('/bar', function(req, res, next) {
    // ... maybe some additional /bar logging ...
    next();
})
// always be invoked
router.use(function(req, res, next) {
    res.send('hello world')；
})
app.use('/foo', router);
app.listen(3000);

```

对于中间件`function`，挂载的路径是被剥离的和不可见的。关于这个特性主要的影响是对于不同的路径，挂载相同的中间件可能对代码不做改动，尽管其前缀已经改变。 你使用`router.use()`定义中间件的顺序很重要。中间们是按序被调用的，所以顺序决定了中间件的优先级。例如，通常日志是你将使用的第一个中间件，以便每一个请求都被记录。

```
var logger = require('morgan');
router.use(logger());
router.use(express.static(__dirname + '/public'));
router.use(function(req, res) {
    res.send('Hello');
});

```

现在为了支持你不希望记录静态文件请求，但为了继续记录那些定义在`logger()`之后的路由和中间件。你可以简单的将`static()`移动到前面来解决：

```
router.use(express.static(__dirname + '/public'));
router.use(logger());
router.use(function(req, res){
  res.send('Hello');
});

```

另外一个确凿的例子是从不同的路径托管静态文件，你可以将`./public`放到前面来获得更高的优先级:

```
app.use(express.static(__dirname + '/public'));
app.use(express.static(__dirname + '/files'));
app.use(express.static(__dirname + '/uploads'));

```

`router.use()`方法也支持命名参数，以便你的挂载点对于其他的路由而言，可以使用命名参数来进行预加载，这样做是很有益的。
